package com.foodit.test.sample.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class MenuItem {

	@Parent
	private Key<RestaurantData> restaurant;

	@Id
	// reason for sloppyness: bad data
	// String because there are menu items with the value 0 which is rejected by objectify
	private String id;

	private String name;
	private String category;

	private int orderCount;

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

	/**
	 * Number of times this item was ordered during it's lifetime
	 * 
	 * @return order count
	 */
	public int getOrderCount() {
		return this.orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public void incOrderCount() {
		this.orderCount++;
	}

}
