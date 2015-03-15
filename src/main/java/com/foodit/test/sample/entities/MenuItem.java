package com.foodit.test.sample.entities;

import com.foodit.test.sample.controller.RestaurantData;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class MenuItem {

	@Parent
	private Key<RestaurantData> restaurant;

	@Id
	private String id;

	private String name;
	private String category;

	public Key<RestaurantData> getRestaurant() {
		return this.restaurant;
	}

	public void setRestaurant(Key<RestaurantData> restaurant) {
		this.restaurant = restaurant;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
