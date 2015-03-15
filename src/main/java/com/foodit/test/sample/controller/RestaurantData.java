package com.foodit.test.sample.controller;

import java.math.BigDecimal;
import java.math.MathContext;

import com.foodit.test.sample.entities.OfyBigDecimal;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class RestaurantData {

	@Id
	private String restaurant;
	private OfyBigDecimal sales;

	public RestaurantData() {
	}

	public RestaurantData(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public BigDecimal getSales() {
		return this.sales.getValue();
	}

	public void setSales(BigDecimal sales) {
		this.sales = new OfyBigDecimal(sales);
	}

	public void incSales(BigDecimal increment, MathContext mc) {
		if (sales == null) {
			sales = new OfyBigDecimal(increment);
		} else {
			sales = new OfyBigDecimal(sales.getValue().add(increment, mc));
		}
	}

}
