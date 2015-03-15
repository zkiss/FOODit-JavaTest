package com.foodit.test.sample.config;

import com.foodit.test.sample.persistence.ObjectifyApi;
import com.foodit.test.sample.persistence.OrderDao;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ApplicationBeans {

	public void injectBeans(UpdatableInjectionContext injectionContext) {
		inject(injectionContext, ObjectifyApi.class);
		inject(injectionContext, RestaurantDataDao.class);
		inject(injectionContext, OrderDao.class);
	}

	private <T> void inject(UpdatableInjectionContext injectionContext, Class<T> type) {
		// why can't I simply say
		// injectionContext.inject(Whatever.class) ???
		injectionContext.inject(type).as(type);
	}

}
