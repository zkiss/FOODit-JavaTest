package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.persistence.MenuItemDao;

@Named
@Singleton
public class MenuStatsService {

	private static final Comparator<MenuItem> TOP_COMPARATOR = new Comparator<MenuItem>() {
		@Override
		public int compare(MenuItem o1, MenuItem o2) {
			// higher first
			return Integer.compare(o2.getOrderCount(), o1.getOrderCount());
		}
	};

	@Inject
	private MenuItemDao dao;

	public List<MenuItem> getGlobalTopN(int n) {
		List<MenuItem> items = dao.loadAll();
		Collections.sort(items, TOP_COMPARATOR);
		if (items.size() > n) {
			items = items.subList(0, n);
			// subList still keeps the whole backing list in the memory, let's get rid of the data we don't need
			items = new ArrayList<>(items);
		}

		return items;
	}

}
