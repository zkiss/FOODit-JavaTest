package com.foodit.test.sample.controller;

import javax.inject.Inject;

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
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.OrderDao;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.json.JsonView;

public class ReportsController {

	@Inject
	private OrderDao orderDao;
	@Inject
	private RestaurantDataDao restaurantDataDao;

	public JsonView orders(String restaurant) {
		Logger.info("Counting orders for restaurant: %s", restaurant);

		/*
		 * This could also be saved to RestaurantData same way as sales but I thought count should be fast enough
		 */
		int count = orderDao.countByRestaurant(restaurant);

		return new JsonView(new OrderCount(count));
	}

	public JsonView sales(String restaurant) {
		Logger.info("Counting sales for restaurant: %s", restaurant);

		RestaurantData data = restaurantDataDao.load(restaurant);

		return new JsonView(new SalesAmount(data.getSales()));
	}

	public JsonView topMeals() {
		Logger.info("Calculating top meals");

		/*
		 * Leaderboard problem. NoSQL is not really suited for this. Depending on how accurate results we want and how
		 * frequently we need this there are multiple ways to go about this.
		 * 
		 * 1. export stats to a relational db
		 * 2. maintain leaderboard periodically in a background task and query that
		 * 3. build the most accurate leaderboard on the fly and accept the query could run for a while - for this I
		 * probably should use the task api and have a separate endpoint for querying task execution result. I won't do
		 * this now, but I recognize the fact that this solution can easily time out.
		 */

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
