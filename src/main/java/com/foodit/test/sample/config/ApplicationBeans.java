package com.foodit.test.sample.config;

import com.foodit.test.sample.persistence.MenuItemDao;
import com.foodit.test.sample.persistence.ObjectifyApi;
import com.foodit.test.sample.persistence.OrderDao;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.foodit.test.sample.services.DataImportService;
import com.foodit.test.sample.services.MenuImportService;
import com.foodit.test.sample.services.MenuStatsService;
import com.foodit.test.sample.services.OrderImportService;
import com.foodit.test.sample.services.RestaurantStatsService;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ApplicationBeans {

	public void injectBeans(UpdatableInjectionContext injectionContext) {
		inject(injectionContext, ObjectifyApi.class);
		inject(injectionContext, RestaurantDataDao.class);
		inject(injectionContext, OrderDao.class);
		inject(injectionContext, MenuItemDao.class);
		inject(injectionContext, MenuStatsService.class);
		inject(injectionContext, RestaurantStatsService.class);
		inject(injectionContext, MenuImportService.class);
		inject(injectionContext, OrderImportService.class);
		inject(injectionContext, DataImportService.class);
	}

	private <T> void inject(UpdatableInjectionContext injectionContext, Class<T> type) {
		// why can't I simply say
		// injectionContext.inject(Whatever.class) ???
		injectionContext.inject(type).as(type);
	}

}
