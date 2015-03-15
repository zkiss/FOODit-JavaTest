package com.foodit.test.sample.controller;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.servlet.http.HttpServletResponse;

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
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.objectify.Key;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.jsp.JspView;
import com.threewks.thundr.view.string.StringView;
import org.apache.commons.io.IOUtils;

public class DataLoadController {

	public JspView instructions() {
		return new JspView("instructions.jsp");
	}

	public StringView load() {
		Logger.info("Loading data");
		List<String> restaurants = Lists.newArrayList("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");
		for (String restaurant : restaurants) {
			loadData(restaurant);
		}
		return new StringView("Data loaded.");
	}

	private RestaurantData loadData(String restaurant) {
		Logger.info("Processing data for restaurant %s", restaurant);
		RestaurantData restaurantLoadData = new RestaurantData(restaurant);
		Key<RestaurantData> key = ofy().save().entity(restaurantLoadData).now();

		String menuJson = readFile(String.format("menu-%s.json", restaurant));
		saveMenu(key, menuJson);

		String ordersJson = readFile(String.format("orders-%s.json", restaurant));
		saveOrders(key, ordersJson);

		return restaurantLoadData;
	}

	private void saveMenu(Key<RestaurantData> key, String json) {
		LinkedList<MenuItem> menu = new LinkedList<>();
		JsonObject root = new JsonParser().parse(json).getAsJsonObject();
		Set<Entry<String, JsonElement>> categories = root.getAsJsonObject("menu").entrySet();
		for (Entry<String, JsonElement> catEntry : categories) {
			JsonArray itemsInCat = catEntry.getValue().getAsJsonArray();
			for (JsonElement itemElement : itemsInCat) {
				JsonObject itemJson = itemElement.getAsJsonObject();
				MenuItem item = new MenuItem();
				item.setRestaurant(key);
				item.setId("" + itemJson.getAsJsonPrimitive("id").getAsLong());
				item.setName(itemJson.getAsJsonPrimitive("name").getAsString());
				item.setCategory(itemJson.getAsJsonPrimitive("category").getAsString());
				menu.add(item);
			}
		}
		ofy().save().entities(menu).now();
	}

	private void saveOrders(Key<RestaurantData> key, String json) {
		LinkedList<Order> orders = new LinkedList<>();
		JsonArray root = new JsonParser().parse(json).getAsJsonArray();
		for (JsonElement orderElement : root) {
			JsonObject orderJson = orderElement.getAsJsonObject();

			Order order = new Order();
			order.setRestaurant(key.getName());
			order.setId(orderJson.getAsJsonPrimitive("orderId").getAsLong());
			order.setTotalValue(orderJson.getAsJsonPrimitive("totalValue").getAsBigDecimal());
			order.setItems(new LinkedList<Item>());
			for (JsonElement itemElement : orderJson.get("lineItems").getAsJsonArray()) {
				JsonObject itemJson = itemElement.getAsJsonObject();
				if (!itemJson.has("id")) {
					Logger.debug("Skipping order item: " + itemJson.toString());
					continue;
				}
				Item item = new Item();
				item.setId(Key.create(key, MenuItem.class, itemJson.getAsJsonPrimitive("id").getAsLong() + ""));
				item.setTotal(itemJson.getAsJsonPrimitive("total").getAsBigDecimal());
				order.getItems().add(item);
			}
			orders.add(order);
		}
		ofy().save().entities(orders).now();
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

	public void viewData(String restaurant, HttpServletResponse response) throws IOException {
		response.addHeader(HttpSupport.Header.ContentType, ContentType.ApplicationJson.value());
		RestaurantData restaurantLoadData = ofy().load().key(Key.create(RestaurantData.class, restaurant)).now();

		// not much to do here
		String data = restaurantLoadData.getRestaurant();
		response.getWriter().write(data);
		response.setContentLength(data.getBytes().length);
	}

}
