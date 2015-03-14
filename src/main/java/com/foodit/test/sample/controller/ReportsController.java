package com.foodit.test.sample.controller;

import com.foodit.test.sample.controller.message.OrderCount;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.json.JsonView;

public class ReportsController {

	public JsonView orders(String restaurant) {
		// TODO
		Logger.info("Counting orders for restaurant: %s", restaurant);
		return new JsonView(new OrderCount(0));
	}

	public void sales(String storeId) {
		// TODO
	}

	public void topMeals() {
		// TODO
	}

	public void topCategories() {
		// TODO
	}

}
