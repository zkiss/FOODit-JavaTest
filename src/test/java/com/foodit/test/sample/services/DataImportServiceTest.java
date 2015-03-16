package com.foodit.test.sample.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.ObjectifyTestBase;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import org.fest.assertions.core.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class DataImportServiceTest extends ObjectifyTestBase {

	/*
	 * I need a good DI framework for testing implementation from end to end. Using Spring.
	 *
	 * Chose to test it this way because this is not sensitive to internal refactoring and movement of logic
	 */

	private DataImportService service;

	@Before
	public void setUp() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.foodit.test.sample");
		this.service = ctx.getBean(DataImportService.class);
	}

	@Test
	public void givenEmptyMenu_whenImportFromFile_thenNoDataSaved() {
		service.importFromFile("emptymenu");

		LocalServiceTestHelper.endRequest();
		ofy.clear();
		int menuCount = ofy.load().type(MenuItem.class).count();
		assertThat(menuCount).isEqualTo(0);
		int orderCount = ofy.load().type(Order.class).count();
		assertThat(orderCount).isEqualTo(0);
	}

	@Test
	public void givenOnlyMenu_whenImportFromFile_thenMenuIsSaved() {
		service.importFromFile("onlymenu");

		LocalServiceTestHelper.endRequest();
		ofy.clear();
		List<MenuItem> menu = ofy.load().type(MenuItem.class).list();
		assertThat(menu).hasSize(2);
		assertThat(menu).areExactly(1, new Condition<MenuItem>() {
			@Override
			public boolean matches(MenuItem value) {
				return value.getCategory().equals("Item Category")
						&& (value.getId() == 1)
						&& value.getName().equals("Item Name")
						&& (value.getOrderCount() == 0)
						&& value.getRestaurant().equals(Key.create(new RestaurantData("onlymenu")));
			}
		});
		assertThat(menu).areExactly(1, new Condition<MenuItem>() {
			@Override
			public boolean matches(MenuItem value) {
				return value.getCategory().equals("Second Category")
						&& (value.getId() == 2)
						&& value.getName().equals("Second Item")
						&& (value.getOrderCount() == 0)
						&& value.getRestaurant().equals(Key.create(new RestaurantData("onlymenu")));
			}
		});
		int orderCount = ofy.load().type(Order.class).count();
		assertThat(orderCount).isEqualTo(0);
	}

	@Test
	public void givenMenuItemWithOrders_whenImportFromFile_thenSetsNumbers() throws InterruptedException {
		service.importFromFile("simpleorder");

		LocalServiceTestHelper.endRequest();
		ofy.clear();
		MenuItem item = loadMenu("simpleorder", "1");
		assertThat(item.getOrderCount()).isEqualTo(2);

		RestaurantData restaurantData = loadRestaurant("simpleorder");
		assertThat(restaurantData.getSales()).isEqualByComparingTo("21");
	}

	@Test
	public void givenMissingMenuItem_whenImportFromFile_thenDoesNotFail() {
		service.importFromFile("missingmenuitem");

		LocalServiceTestHelper.endRequest();
		ofy.clear();
		int menuCount = ofy.load().type(MenuItem.class).count();
		assertThat(menuCount).isEqualTo(0);
		int orderCount = ofy.load().type(Order.class).count();
		assertThat(orderCount).isEqualTo(2);
		RestaurantData data = loadRestaurant("missingmenuitem");
		assertThat(data.getSales()).isEqualByComparingTo("21.051");
	}

	private MenuItem loadMenu(String restaurant, String id) {
		return ofy.load().key(Key.create(
				Key.create(new RestaurantData(restaurant)),
				MenuItem.class,
				id)).now();
	}

	private RestaurantData loadRestaurant(String id) {
		return ofy.load().type(RestaurantData.class).id(id).now();
	}
}
