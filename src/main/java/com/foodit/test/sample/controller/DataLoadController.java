package com.foodit.test.sample.controller;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.services.DataImportService;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.jsp.JspView;
import com.threewks.thundr.view.string.StringView;

public class DataLoadController {

	@Inject
	private DataImportService importService;

	public JspView instructions() {
		return new JspView("instructions.jsp");
	}

	public StringView loadAll() {
		Logger.info("Loading data");
		List<String> restaurants = Lists.newArrayList("bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress");
		for (String restaurant : restaurants) {
			importService.importFromFile(restaurant);
		}
		return new StringView("Data loaded.");
	}

	public StringView load(String restaurant) {
		importService.importFromFile(restaurant);
		return new StringView("Data loaded for restaurant: " + restaurant);
	}

	public void viewData(String restaurant, HttpServletResponse response) throws IOException {
		response.addHeader(HttpSupport.Header.ContentType, ContentType.ApplicationJson.value());
		RestaurantData restaurantLoadData = ofy().load().key(Key.create(RestaurantData.class, restaurant)).now();

		// not much to do here
		String data = restaurantLoadData.getId();
		response.getWriter().write(data);
		response.setContentLength(data.getBytes().length);
	}

}
