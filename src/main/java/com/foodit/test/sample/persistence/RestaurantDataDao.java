package com.foodit.test.sample.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.Key;

@Named
@Singleton
public class RestaurantDataDao {

	@Inject
	private ObjectifyApi api;

	public Key<RestaurantData> save(RestaurantData restaurantData) {
		return api.save(restaurantData);
	}

	public RestaurantData load(String id) {
		return api.load(RestaurantData.class, id);
	}

}
