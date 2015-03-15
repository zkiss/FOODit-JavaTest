package com.foodit.test.sample.controller;

import com.foodit.test.sample.controller.message.OrderCount;
import com.foodit.test.sample.controller.message.SalesAmount;
import com.foodit.test.sample.entities.Order;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.json.JsonView;

public class ReportsController {

	public JsonView orders(String restaurant) {
		Logger.info("Counting orders for restaurant: %s", restaurant);

		int count = ObjectifyService.ofy().load().type(Order.class).filter("restaurant", restaurant).count();

		return new JsonView(new OrderCount(count));
	}

	public JsonView sales(String restaurant) {
		Logger.info("Counting sales for restaurant: %s", restaurant);

		double sales = 0;
		for (Order order : ObjectifyService.ofy().load().type(Order.class).filter("restaurant", restaurant)) {
			sales += order.getTotalValue();
		}

		return new JsonView(new SalesAmount(sales));
	}

	public void topMeals() {
		// TODO
	}

	public void topCategories() {
		// TODO
	}

}
