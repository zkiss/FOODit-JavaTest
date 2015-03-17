package com.foodit.test.sample.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

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
	public void test() {
		// TODO
		fail("Not yet implemented");

	}

}
