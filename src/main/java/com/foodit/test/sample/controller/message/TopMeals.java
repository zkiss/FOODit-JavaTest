package com.foodit.test.sample.controller.message;

import java.util.List;

public class TopMeals {

	private final List<Meal> topMeals;

	public TopMeals(List<Meal> topMeals) {
		this.topMeals = topMeals;
	}

	public List<Meal> getTopMeals() {
		return this.topMeals;
	}

}
