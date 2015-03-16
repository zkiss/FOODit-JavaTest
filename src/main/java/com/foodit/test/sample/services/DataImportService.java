package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.Order.Item;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.objectify.Key;
import com.threewks.thundr.logger.Logger;
import org.apache.commons.io.IOUtils;

@Named
@Singleton
public class DataImportService {

	@Inject
	private RestaurantDataDao restaurantDao;
	@Inject
	private MenuImportService menuSvc;
	@Inject
	private OrderImportService orderSvc;

	public void importFromFile(String restaurantId) {
		Logger.info("Importing data for restaurant %s", restaurantId);
		RestaurantData restaurantLoadData = new RestaurantData(restaurantId);
		restaurantDao.save(restaurantLoadData);

		List<MenuItem> menuItems = parseMenu(restaurantLoadData, readFile(String.format("menu-%s.json", restaurantId)));
		menuSvc.saveMenu(menuItems);

		List<Order> orders = parseOrders(restaurantLoadData, readFile(String.format("orders-%s.json", restaurantId)));
		orderSvc.saveOrders(restaurantLoadData, orders);
	}

	private List<MenuItem> parseMenu(RestaurantData restaurantData, String json) {
		LinkedList<MenuItem> menu = new LinkedList<>();
		JsonObject root = new JsonParser().parse(json).getAsJsonObject();
		Set<Entry<String, JsonElement>> categories = root.getAsJsonObject("menu").entrySet();
		for (Entry<String, JsonElement> catEntry : categories) {
			JsonArray itemsInCat = catEntry.getValue().getAsJsonArray();
			for (JsonElement itemElement : itemsInCat) {
				JsonObject itemJson = itemElement.getAsJsonObject();
				MenuItem item = new MenuItem();
				item.setRestaurant(Key.create(restaurantData));
				item.setId(itemJson.getAsJsonPrimitive("id").getAsLong());
				item.setName(itemJson.getAsJsonPrimitive("name").getAsString());
				item.setCategory(itemJson.getAsJsonPrimitive("category").getAsString());
				menu.add(item);
			}
		}
		return menu;
	}

	private List<Order> parseOrders(RestaurantData restaurantData, String json) {
		LinkedList<Order> orders = new LinkedList<>();
		JsonArray root = new JsonParser().parse(json).getAsJsonArray();
		for (JsonElement orderElement : root) {
			JsonObject orderJson = orderElement.getAsJsonObject();

			Order order = new Order();
			order.setRestaurant(restaurantData.getId());
			order.setId(orderJson.getAsJsonPrimitive("orderId").getAsLong());
			order.setTotalValue(orderJson.getAsJsonPrimitive("totalValue").getAsBigDecimal());
			order.setItems(new LinkedList<Item>());
			for (JsonElement itemElement : orderJson.get("lineItems").getAsJsonArray()) {
				JsonObject itemJson = itemElement.getAsJsonObject();
				if (!itemJson.has("id")) {
					// skip delivery charges
					Logger.debug("Skipping order item: " + itemJson.toString());
					continue;
				}
				Item item = new Item();
				item.setId(Key.create(
						Key.create(restaurantData),
						MenuItem.class,
						String.valueOf(itemJson.getAsJsonPrimitive("id").getAsLong())));
				order.getItems().add(item);
			}
			orders.add(order);
		}
		return orders;
	}

	private String readFile(String resourceName) {
		URL url = Resources.getResource(resourceName);
		try {
			return IOUtils.toString(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			Logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
