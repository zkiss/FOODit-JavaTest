package com.foodit.test.sample.controller.message;

import java.util.List;

public class TopCategories {

	private final List<Category> categories;

	public TopCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

}
