import com.foodit.test.sample.config.ApplicationRoutes;
import com.foodit.test.sample.entities.MenuItem;
import com.foodit.test.sample.entities.Order;
import com.foodit.test.sample.entities.RestaurantData;
import com.foodit.test.sample.persistence.ObjectifyApi;
import com.foodit.test.sample.persistence.RestaurantDataDao;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.thundr.gae.GaeModule;
import com.threewks.thundr.gae.objectify.ObjectifyModule;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.route.Routes;

public class ApplicationModule extends BaseModule {

	/*
	 * thundr desing ENCOURAGES BAD PRACTICE - module in default package by default
	 */

	private ApplicationRoutes applicationRoutes = new ApplicationRoutes();

	@Override
	public void requires(DependencyRegistry dependencyRegistry) {
		super.requires(dependencyRegistry);
		dependencyRegistry.addDependency(GaeModule.class);
		dependencyRegistry.addDependency(ObjectifyModule.class);
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);
		configureObjectify();
		// why can't I simply say
		// injectionContext.inject(ObjectifyApi.class) ???
		injectionContext.inject(ObjectifyApi.class).as(ObjectifyApi.class);
		injectionContext.inject(RestaurantDataDao.class).as(RestaurantDataDao.class);
	}

	@Override
	public void start(UpdatableInjectionContext injectionContext) {
		super.start(injectionContext);
		Routes routes = injectionContext.get(Routes.class);
		applicationRoutes.addRoutes(routes);
	}

	private void configureObjectify() {
		ObjectifyService.register(RestaurantData.class);
		ObjectifyService.register(MenuItem.class);
		ObjectifyService.register(Order.class);
	}
}
