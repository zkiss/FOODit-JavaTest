package com.foodit.test.sample.config;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyConfig {

	public void configure() {
		ObjectifyService.register(RestaurantData.class);
		ObjectifyService.register(MenuItem.class);
		ObjectifyService.register(Order.class);
	}

}
