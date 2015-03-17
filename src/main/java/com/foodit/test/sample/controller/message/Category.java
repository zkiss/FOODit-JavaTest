package com.foodit.test.sample.controller.message;

public class Category {

	private final String name;
	private final int orderCount;

	public Category(String name, int orderCount) {
		this.name = name;
		this.orderCount = orderCount;
	}

	public String getName() {
		return this.name;
	}

	public int getOrderCount() {
		return this.orderCount;
	}

}
