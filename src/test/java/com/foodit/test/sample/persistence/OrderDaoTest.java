package com.foodit.test.sample.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import com.foodit.test.sample.entities.Order;
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
		List<Order> saved = Arrays.asList(create("aa", 1), create("aa", 2), create("cc", 3));
		ofy.save().entities(saved).now();
		ofy.clear();

		int count = dao.countByRestaurant("aa");

		assertThat(count).isEqualTo(2);
	}

	@Test
	public void whenSaveAsync_thenSavedEntitiesAreFound() {
		List<Order> saved = Arrays.asList(create("aa", 1), create("aa", 2), create("cc", 3));

		dao.saveAsync(saved).now();

		ofy.clear();
		List<Order> loaded = ofy.load().type(Order.class).list();
		assertThat(loaded).containsOnly(saved.toArray(new Order[saved.size()]));
	}

	private Order create(String restaurant, long id) {
		Order order = new Order();
		order.setRestaurant(restaurant);
		order.setId(id);
		return order;
	}

}
