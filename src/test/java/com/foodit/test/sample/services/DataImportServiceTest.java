package com.foodit.test.sample.services;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.ObjectifyTestBase;
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

		ofy.clear();
		int menuCount = ofy.load().type(MenuItem.class).count();
		assertThat(menuCount).isEqualTo(0);
		int orderCount = ofy.load().type(Order.class).count();
		assertThat(orderCount).isEqualTo(0);
	}

	@Test
	public void givenOnlyMenu_whenImportFromFile_thenMenuIsSaved() {
		service.importFromFile("onlymenu");

		ofy.clear();
		List<MenuItem> menu = ofy.load().type(MenuItem.class).list();
		assertThat(menu).hasSize(2);
		assertThat(menu).areExactly(1, new Condition<MenuItem>() {
			@Override
			public boolean matches(MenuItem value) {
				return value.getCategory().equals("Item Category")
						&& value.getId().equals("1")
						&& value.getName().equals("Item Name")
						&& (value.getOrderCount() == 0)
						&& value.getRestaurant().equals(Key.create(new RestaurantData("onlymenu")));
			}
		});
		assertThat(menu).areExactly(1, new Condition<MenuItem>() {
			@Override
			public boolean matches(MenuItem value) {
				return value.getCategory().equals("Second Category")
						&& value.getId().equals("2")
						&& value.getName().equals("Second Item")
						&& (value.getOrderCount() == 0)
						&& value.getRestaurant().equals(Key.create(new RestaurantData("onlymenu")));
			}
		});
		int orderCount = ofy.load().type(Order.class).count();
		assertThat(orderCount).isEqualTo(0);
	}
}
