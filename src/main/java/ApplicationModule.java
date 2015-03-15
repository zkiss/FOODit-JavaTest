import com.foodit.test.sample.config.ApplicationBeans;
import com.foodit.test.sample.config.ApplicationRoutes;
import com.foodit.test.sample.config.ObjectifyConfig;
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

	private final ApplicationRoutes applicationRoutes = new ApplicationRoutes();
	private final ApplicationBeans beans = new ApplicationBeans();
	private final ObjectifyConfig objectifyConfig = new ObjectifyConfig();

	@Override
	public void requires(DependencyRegistry dependencyRegistry) {
		super.requires(dependencyRegistry);
		dependencyRegistry.addDependency(GaeModule.class);
		dependencyRegistry.addDependency(ObjectifyModule.class);
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		super.configure(injectionContext);
		objectifyConfig.configure();
		beans.injectBeans(injectionContext);
	}

	@Override
	public void start(UpdatableInjectionContext injectionContext) {
		super.start(injectionContext);
		applicationRoutes.addRoutes(injectionContext.get(Routes.class));
	}
}
