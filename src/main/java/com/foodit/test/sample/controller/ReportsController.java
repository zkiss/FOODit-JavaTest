package com.foodit.test.sample.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import com.foodit.test.sample.controller.message.Meal;
import com.foodit.test.sample.controller.message.OrderCount;
import com.foodit.test.sample.controller.message.SalesAmount;
import com.foodit.test.sample.controller.message.TopMeals;
import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.Order.Item;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.json.JsonView;

public class ReportsController {

	/*
	 * Really don't like the static access to the data access api.
	 */

	public JsonView orders(String restaurant) {
		Logger.info("Counting orders for restaurant: %s", restaurant);

		int count = ObjectifyService.ofy().load().type(Order.class).filter("restaurant", restaurant).count();

		return new JsonView(new OrderCount(count));
	}

	public JsonView sales(String restaurant) {
		Logger.info("Counting sales for restaurant: %s", restaurant);

		RestaurantData data = ObjectifyService.ofy().load().type(RestaurantData.class).id(restaurant).now();

		return new JsonView(new SalesAmount(data.getSales()));
	}

	public JsonView topMeals() {
		Logger.info("Calculating top meals");

		HashMap<String, MealOrderCount> orders = new HashMap<>();
		for (Order order : ObjectifyService.ofy().load().type(Order.class)) {
			for (Item item : order.getItems()) {
				String key = order.getRestaurant() + "_" + item.getId().getName();
				MealOrderCount cnt = orders.get(key);
				if (cnt == null) {
					cnt = new MealOrderCount(order.getRestaurant(), item.getId());
					orders.put(key, cnt);
				}
				cnt.orderCount++;
			}
		}

		ArrayList<Meal> list = new ArrayList<>(orders.size());
		for (Entry<String, MealOrderCount> entry : orders.entrySet()) {
			list.add(new Meal(entry.getValue().restaurant, entry.getValue().id.getName(), entry.getValue().orderCount));
		}
		Collections.sort(list, new Comparator<Meal>() {
			@Override
			public int compare(Meal o1, Meal o2) {
				// higher first
				return Integer.compare(o2.getOrderCount(), o1.getOrderCount());
			}
		});

		return new JsonView(new TopMeals(list.subList(0, 10)));
	}

	private static class MealOrderCount {
		String restaurant;
		Key<MenuItem> id;
		int orderCount;

		public MealOrderCount(String restaurant, Key<MenuItem> id) {
			this.restaurant = restaurant;
			this.id = id;
		}

	}

	public void topCategories() {
		// TODO
	}

}
