package com.foodit.test.sample.controller.message;

public class Meal {

	private final String restaurant;
	private final long menuItem;
	private final int orderCount;

	public Meal(String restaurant, long menuItem, int orderCount) {
		this.restaurant = restaurant;
		this.menuItem = menuItem;
		this.orderCount = orderCount;
	}

	public String getRestaurant() {
		return this.restaurant;
	}

	public long getMenuItem() {
		return this.menuItem;
	}

	public int getOrderCount() {
		return this.orderCount;
	}

}
