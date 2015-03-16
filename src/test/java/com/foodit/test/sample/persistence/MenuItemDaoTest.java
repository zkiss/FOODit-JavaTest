package com.foodit.test.sample.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.Key;
import org.fest.assertions.core.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuItemDaoTest extends ObjectifyTestBase {

	@Spy
	private ObjectifyApi api;

	@InjectMocks
	private MenuItemDao dao;

	@Test
	public void whenSaveAsync_thenDataIsFouncWhenLoaded() {
		MenuItem item = create("rest", 2);

		dao.saveAsync(item).now();

		ofy.clear();
		MenuItem found = ofy.load().key(Key.create(item)).now();
		assertThat(found).isNotNull();
	}

	@Test
	public void whenLoad_thenReturnsExisting() {
		MenuItem item = create("rrr", 22);
		ofy.save().entity(item).now();
		ofy.clear();

		MenuItem found = dao.load(Key.create(item));
		assertThat(found).isNotNull();
	}

	@Test
	public void whenSaveAsyncIterable_thenAllAreFound() {
		List<MenuItem> saved = Arrays.asList(create("aa", 11), create("bb", 22));

		dao.saveAsync(saved).now();

		ofy.clear();
		List<MenuItem> loaded = ofy.load().type(MenuItem.class).list();
		assertThat(loaded).areExactly(1, id("aa", 11));
		assertThat(loaded).areExactly(1, id("bb", 22));
	}

	private Condition<MenuItem> id(final String restaurant, final long id) {
		return new Condition<MenuItem>() {
			@Override
			public boolean matches(MenuItem value) {
				return (value.getId() == id)
						&& value.getRestaurant().equivalent(Key.create(RestaurantData.class, restaurant));
			}
		};
	}

	@Test
	public void whenLoadAll_thenAllAreFound() {
		List<MenuItem> saved = Arrays.asList(create("aa", 11), create("bb", 22));
		ofy.save().entities(saved).now();
		ofy.clear();

		List<MenuItem> loaded = dao.loadAll();

		assertThat(loaded).areExactly(1, id("aa", 11));
		assertThat(loaded).areExactly(1, id("bb", 22));
	}

	private MenuItem create(String restaurantId, long itemId) {
		MenuItem item = new MenuItem();
		item.setRestaurant(Key.create(new RestaurantData(restaurantId)));
		item.setId(itemId);
		return item;
	}
}
