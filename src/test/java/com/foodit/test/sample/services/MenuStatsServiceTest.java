package com.foodit.test.sample.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import com.foodit.test.sample.controller.message.Category;
import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.persistence.MenuItemDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuStatsServiceTest {

	private static final String STORE_ID = "restaurantId";

	@Mock
	private MenuItemDao dao;

	@InjectMocks
	private MenuStatsService service;

	/**
	 * Returned from {@link MenuItemDao#loadForRestaurant(String)}
	 */
	private List<MenuItem> menu;

	/**
	 * Returned from {@link MenuItemDao#loadAll()}
	 */
	private List<MenuItem> globalMenu;

	@Before
	public void setUp() {
		menu = new LinkedList<>();
		globalMenu = new LinkedList<>();
		when(dao.loadForRestaurant(STORE_ID)).thenReturn(menu);
		when(dao.loadAll()).thenReturn(globalMenu);
	}

	@Test
	public void givenNoItems_whenGetTopMenuItemsGlobal_thenReturnsEmptyList() {
		List<MenuItem> top = service.getTopMenuItemsGlobal(3);

		assertThat(top).isEmpty();
	}

	@Test
	public void givenManyItems_whenGetTopMenuItemsGlobal_thenLimitsList() {
		globalMenu.add(item(1, "a"));
		globalMenu.add(item(2, "a"));
		globalMenu.add(item(3, "a"));
		globalMenu.add(item(4, "a"));
		globalMenu.add(item(5, "a"));
		globalMenu.add(item(6, "a"));

		List<MenuItem> top = service.getTopMenuItemsGlobal(3);

		assertThat(top).hasSize(3);
		assertThat(top.get(0).getOrderCount()).isEqualTo(6);
		assertThat(top.get(1).getOrderCount()).isEqualTo(5);
		assertThat(top.get(2).getOrderCount()).isEqualTo(4);
	}

	@Test
	public void givenSomeItems_whenGetTopMenuItemsGlobal_thenReturnsAll() {
		globalMenu.add(item(1, "a"));
		globalMenu.add(item(2, "a"));
		globalMenu.add(item(3, "a"));

		List<MenuItem> top = service.getTopMenuItemsGlobal(6);

		assertThat(top).hasSize(3);
		assertThat(top.get(0).getOrderCount()).isEqualTo(3);
		assertThat(top.get(1).getOrderCount()).isEqualTo(2);
		assertThat(top.get(2).getOrderCount()).isEqualTo(1);
	}

	@Test
	public void givenEmptyMenu_whenGetTopCategoriesForRestaurant_thenReturnsEmptyList() {
		List<Category> top = service.getTopCategoriesForRestaurant(STORE_ID, 3);

		assertThat(top).isEmpty();
	}

	@Test
	public void givenMultipleItemsInCat_whenGetTopCategoriesForRestaurant_thenSumsCorrectly() {
		menu.add(item(6, "C7"));
		menu.add(item(5, "C5"));
		menu.add(item(3, "C3"));
		menu.add(item(1, "C4"));
		menu.add(item(1, "C4"));
		menu.add(item(1, "C4"));
		menu.add(item(1, "C4"));
		menu.add(item(1, "C7"));

		List<Category> top = service.getTopCategoriesForRestaurant(STORE_ID, 3);

		assertThat(top).hasSize(3);
		assertThat(top.get(0).getOrderCount()).isEqualTo(7);
		assertThat(top.get(1).getOrderCount()).isEqualTo(5);
		assertThat(top.get(2).getOrderCount()).isEqualTo(4);
	}

	private MenuItem item(int orderCount, String category) {
		MenuItem mi = new MenuItem();
		mi.setOrderCount(orderCount);
		mi.setCategory(category);
		mi.setName("Item with order count " + orderCount);
		return mi;
	}

}
