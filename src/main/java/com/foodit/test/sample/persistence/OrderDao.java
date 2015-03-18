package com.foodit.test.sample.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.Collection;
import java.util.Map;

import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

@Named
@Singleton
public class OrderDao {

	@Inject
	private ObjectifyApi api;

	public int countByRestaurant(String storeId) {
		return api.ofy().load().type(Order.class)
				.ancestor(new RestaurantData(storeId))
				.count();
	}

	public Result<Map<Key<Order>, Order>> saveAsync(Collection<Order> orders) {
		return api.ofy().save().entities(orders);
	}

}
