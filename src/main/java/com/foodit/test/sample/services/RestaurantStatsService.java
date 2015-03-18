package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.foodit.test.sample.controller.message.Category;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.OrderDao;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.googlecode.objectify.Key;
import com.threewks.thundr.logger.Logger;

@Named
@Singleton
public class RestaurantStatsService {

	@Inject
	private RestaurantDataDao restaurantDataDao;
	@Inject
	private OrderDao orderDao;
	@Inject
	private MenuStatsService menuStatsService;

	public int getOrders(String storeId) {
		Logger.info("Counting orders for restaurant: %s", storeId);
		/*
		 * This could also be saved to RestaurantData same way as sales but I thought count should be fast enough
		 */
		return orderDao.countByRestaurant(storeId);
	}

	public Map<String, Integer> getOrdersForAll() {
		Logger.info("Counting orders for all restaurant");

		Map<String, Integer> orders = new HashMap<>();
		for (Key<RestaurantData> key : restaurantDataDao.getKeys()) {
			String storeId = key.getName();
			orders.put(storeId, orderDao.countByRestaurant(storeId));
		}
		return orders;
	}

	public BigDecimal getSales(String storeId) {
		Logger.info("Counting sales for restaurant: %s", storeId);

		RestaurantData data = restaurantDataDao.load(storeId);

		return data.getSales();
	}

	public Map<String, BigDecimal> getSalesForAll() {
		Map<String, BigDecimal> orders = new HashMap<>();
		for (Key<RestaurantData> key : restaurantDataDao.getKeys()) {
			String storeId = key.getName();
			orders.put(storeId, getSales(storeId));
		}
		return orders;
	}

	public Map<String, Category> getTopCategories() {
		Map<String, Category> top = new HashMap<>();
		for (Key<RestaurantData> key : restaurantDataDao.getKeys()) {
			String storeId = key.getName();
			Category c = menuStatsService.getTopCategoryForRestaurant(storeId);
			top.put(storeId, c);
		}
		return top;
	}

}
