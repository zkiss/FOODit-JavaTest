package com.foodit.test.sample.controller;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.foodit.test.sample.controller.message.Category;
import com.foodit.test.sample.controller.message.Meal;
import com.foodit.test.sample.controller.message.OrderCount;
import com.foodit.test.sample.controller.message.SalesAmount;
import com.foodit.test.sample.controller.message.TopCategories;
import com.foodit.test.sample.controller.message.TopMeals;
import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.services.MenuStatsService;
import com.foodit.test.sample.services.RestaurantStatsService;
import com.threewks.thundr.view.json.JsonView;

public class ReportsController {

	/*
	 * A general approach I've taken here is that I do part of these calculations at data insertion time. I have assumed
	 * that these endpoints are called frequently enough so that it is worth it. An other approach could be to calculate
	 * everything on the fly based on the order and menu item records, but with millions of records that would probably
	 * take a very long time. And requests would time out.
	 * 
	 * Long running tasks could probably be executed using the tasks api. To be honest I don't know how to use them
	 * exactly and with all the research this task required me to do I have prioritised this lower. Practically I am
	 * unfamiliar with every API here.
	 */

	@Inject
	private MenuStatsService menuStatsService;
	@Inject
	private RestaurantStatsService restaurantStatsService;

	public JsonView orders() {
		Map<String, Integer> count = restaurantStatsService.getOrdersForAll();
		return new JsonView(count);
	}

	public JsonView ordersRestaurant(String restaurant) {
		int count = restaurantStatsService.getOrders(restaurant);
		return new JsonView(new OrderCount(count));
	}

	public JsonView sales() {
		Map<String, BigDecimal> sales = restaurantStatsService.getSalesForAll();
		return new JsonView(sales);
	}

	public JsonView salesRestaurant(String restaurant) {
		BigDecimal sales = restaurantStatsService.getSales(restaurant);
		return new JsonView(new SalesAmount(sales));
	}

	public JsonView topMeals() {
		/*
		 * Leaderboard problem. NoSQL is not really suited for this. Depending on how accurate results we want and how
		 * frequently we need this there are multiple ways to go about this.
		 *
		 * 1. export stats to a relational db
		 * 2. maintain leaderboard periodically in a background task and query that
		 * 3. build the most accurate leaderboard on the fly and accept the query could run for a while - for this I
		 * probably should use the task api and have a separate endpoint for querying task execution result. I won't do
		 * this now, but I recognize the fact that this solution can easily time out.
		 *
		 * My expectation is that there are significantly less menu item records than orders and that it is not too bad
		 * to even iterate them through in a report request.
		 *
		 * If menu items don't all fit in the memory then a merge sort can be used by sorting chunks of the data and
		 * saving the sorted chunks on the disk. Then these chunks can be merged without having to load all the data in
		 * the memory using a merge sort unti we have a file which contains all the data in order. Then we just have to
		 * read up the top x record. In this case though it's definitely worth saving the result somewhere and
		 * maintaining it.
		 */

		// let's do top 10
		List<MenuItem> items = menuStatsService.getTopMenuItemsGlobal(10);

		ArrayList<Meal> list = new ArrayList<>(items.size());
		for (MenuItem item : items) {
			// I could also do the mapping from entity to API message with mapstruct. Nice lib.
			list.add(new Meal(item.getRestaurant().getName(), item.getId(), item.getOrderCount()));
		}
		return new JsonView(new TopMeals(list));
	}

	public JsonView topCategories() {
		Map<String, Category> topCategories = restaurantStatsService.getTopCategories();
		return new JsonView(topCategories);
	}

	public JsonView topCategoriesRestaurant(String restaurant) {
		List<Category> topCategories = menuStatsService.getTopCategoriesForRestaurant(restaurant, 10);
		return new JsonView(new TopCategories(topCategories));
	}
}
