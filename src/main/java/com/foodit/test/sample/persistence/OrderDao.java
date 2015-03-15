package com.foodit.test.sample.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.foodit.test.sample.entities.Order;

@Named
@Singleton
public class OrderDao {

	@Inject
	private ObjectifyApi api;

	public int countByRestaurant(String restaurantId) {
		return api.ofy().load().type(Order.class)
				.filter("restaurant", restaurantId)
				.count();
	}

}
