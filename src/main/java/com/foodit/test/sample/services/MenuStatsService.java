package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.foodit.test.sample.controller.message.Category;
import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.persistence.MenuItemDao;
import com.threewks.thundr.logger.Logger;

@Named
@Singleton
public class MenuStatsService {

	private static final Comparator<MenuItem> MENU_COMPARATOR = new Comparator<MenuItem>() {
		@Override
		public int compare(MenuItem o1, MenuItem o2) {
			// higher first
			return Integer.compare(o2.getOrderCount(), o1.getOrderCount());
		}
	};

	private static final Comparator<Entry<String, Integer>> CATEGORY_COMPARATOR = new Comparator<Map.Entry<String, Integer>>() {
		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			// higher first
			return Integer.compare(o2.getValue(), o1.getValue());
		}
	};

	@Inject
	private MenuItemDao dao;

	public List<MenuItem> getTopMenuItemsGlobal(int limit) {
		Logger.info("Calculating top meals");

		List<MenuItem> items = dao.loadAll();
		Collections.sort(items, MENU_COMPARATOR);
		if (items.size() > limit) {
			// subList still keeps the whole backing list in the memory, let's get rid of the data we don't need
			items = new ArrayList<>(items.subList(0, limit));
		}

		return items;
	}

	public List<Category> getTopCategoriesForRestaurant(String storeId, int limit) {
		Logger.info("Calculating top categories for restaurant %s", storeId);

		/*
		 * Listing the whole menu for a restaurant really should not be an expensive operation.
		 */
		List<MenuItem> menu = dao.loadForRestaurant(storeId);

		Map<String, Integer> categoryCount = getOrdersPerCategory(menu);

		// order by popularity, throw away excessive data
		ArrayList<Entry<String, Integer>> categories = new ArrayList<>(categoryCount.entrySet());
		Collections.sort(categories, CATEGORY_COMPARATOR);
		if (categories.size() > limit) {
			categories = new ArrayList<>(categories.subList(0, limit));
		}

		// convert data to the required format
		List<Category> ret = new ArrayList<>(categories.size());
		for (Entry<String, Integer> entry : categories) {
			ret.add(new Category(entry.getKey(), entry.getValue()));
		}
		return ret;
	}

	public Category getTopCategoryForRestaurant(String storeId) {
		Logger.info("Finding top category for restaurant %s", storeId);

		List<MenuItem> menu = dao.loadForRestaurant(storeId);
		Map<String, Integer> ordersPerCategory = getOrdersPerCategory(menu);

		Entry<String, Integer> top = null;
		// max search - cheaper than sort
		for (Entry<String, Integer> entry : ordersPerCategory.entrySet()) {
			if ((top == null) || (CATEGORY_COMPARATOR.compare(top, entry) > 0)) {
				top = entry;
			}
		}

		if (top == null) {
			return null;
		}
		return new Category(top.getKey(), top.getValue());
	}

	private Map<String, Integer> getOrdersPerCategory(List<MenuItem> menu) {
		// count popularity
		Map<String, Integer> categoryCount = new HashMap<>();
		for (MenuItem item : menu) {
			Integer count = categoryCount.get(item.getCategory());
			if (count == null) {
				categoryCount.put(item.getCategory(), item.getOrderCount());
			} else {
				categoryCount.put(item.getCategory(), count + item.getOrderCount());
			}
		}
		return categoryCount;
	}

}
