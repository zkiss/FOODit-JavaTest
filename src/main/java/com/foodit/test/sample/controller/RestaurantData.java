package com.foodit.test.sample.controller;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class RestaurantData {

	@Id
	String restaurant;

	public RestaurantData(String restaurant) {
		this.restaurant = restaurant;
	}

	public RestaurantData() {
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

}
