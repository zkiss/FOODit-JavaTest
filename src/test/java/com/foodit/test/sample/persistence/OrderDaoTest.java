package com.foodit.test.sample.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.Key;
import org.fest.assertions.core.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderDaoTest extends ObjectifyTestBase {

	@Spy
	private ObjectifyApi api;

	@InjectMocks
	private OrderDao dao;

	@Test
	public void whenCountByRestaurant_thenCountsOnlyRelevantRestaurants() {
		List<Order> saved = Arrays.asList(
				create("aa", 1),
				create("aa", 2),
				create("cc", 3));
		ofy.save().entities(saved).now();
		ofy.clear();

		int count = dao.countByRestaurant("aa");

		assertThat(count).isEqualTo(2);
	}

	@Test
	public void whenSaveAsync_thenSavedEntitiesAreFound() {
		List<Order> saved = Arrays.asList(
				create("aa", 1),
				create("aa", 2),
				create("cc", 3));

		dao.saveAsync(saved).now();

		ofy.clear();
		List<Order> loaded = ofy.load().type(Order.class).list();
		assertThat(loaded).hasSize(3);
		assertThat(loaded).areExactly(1, id("aa", 1));
		assertThat(loaded).areExactly(1, id("aa", 2));
		assertThat(loaded).areExactly(1, id("cc", 3));
	}

	private Condition<Order> id(final String restaurant, final long id) {
		return new Condition<Order>() {
			@Override
			public boolean matches(Order value) {
				return (value.getId() == id)
						&& value.getRestaurant().equals(Key.create(new RestaurantData(restaurant)));
			}
		};
	}

	private Order create(String restaurant, long id) {
		Order order = new Order();
		order.setRestaurant(Key.create(new RestaurantData(restaurant)));
		order.setId(id);
		return order;
	}

}
