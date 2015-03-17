package com.foodit.test.sample.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;

import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.persistence.MenuItemDao;

@Named
@Singleton
public class MenuImportService {

	@Inject
	private MenuItemDao menuDao;

	public void saveMenu(List<MenuItem> menuItems) {
		/*
		 * Waiting for the save because menu items are going to be loaded later. Maybe the Objectify cache would ensure
		 * they are found, but didn't want to take the chance.
		 */
		menuDao.saveAsync(menuItems).now();
	}

}
