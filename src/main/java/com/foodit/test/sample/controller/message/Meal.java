package com.foodit.test.sample.controller.message;

public class Meal {

	private final String restaurant;
	private final String menuItem;
	private final int orderCount;

	public Meal(String restaurant, String menuItem, int orderCount) {
		this.restaurant = restaurant;
		this.menuItem = menuItem;
		this.orderCount = orderCount;
	}

	public String getRestaurant() {
		return this.restaurant;
	}

	public String getMenuItem() {
		return this.menuItem;
	}

	public int getOrderCount() {
		return this.orderCount;
	}

}
