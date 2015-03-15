package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.Collection;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.Order.Item;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.MenuItemDao;
import com.foodit.test.sample.persistence.OrderDao;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.threewks.thundr.logger.Logger;

@Named
@Singleton
public class OrderImportService {

	@Inject
	private OrderDao orderDao;
	@Inject
	private MenuItemDao menuDao;
	@Inject
	private RestaurantDataDao restaurantDataDao;

	public void saveOrders(RestaurantData restaurant, Collection<Order> orders) {
		/*
		 * This has a downside: double looping on the data.
		 * 1. when processing the json
		 * 2. when processing the resulting entities
		 * Upside is separation of concerns - business logic (data integrity) and json parsing
		 * 
		 * Visitor pattern could solve this but that would have put the parsing logic in control of this business logic
		 * (relying on a callback).
		 */

		for (Order order : orders) {
			for (Item item : order.getItems()) {
				MenuItem menuItem = menuDao.load(item.getId());
				if (menuItem == null) {
					Logger.warn("Data integrity error: MenuItem not found: " + item.getId());
					/*
					 * Possibly a menu item which has been deleted after the order was placed. I'll ignore these as a
					 * report like this is probably more interested in items which are still live. If we wanted to
					 * present this data on the GUI we wouldn't be able to get any data about the deleted menu item
					 * anyways.
					 */
					continue;
				}
				menuItem.incOrderCount();
				// imported Objectify version (4.x) has no defer() so we need to save or
				// manually collect modified menu items in a collection ourselves
				// chose not to collect them manually
				menuDao.saveAsync(menuItem);
			}
			restaurant.incSales(order.getTotalValue());
		}
		orderDao.saveAsync(orders);
		restaurantDataDao.save(restaurant);
	}

}
