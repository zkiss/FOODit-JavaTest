package com.foodit.test.sample.config;
import static com.threewks.thundr.route.RouteType.GET;

import com.foodit.test.sample.controller.DataLoadController;
import com.foodit.test.sample.controller.ReportsController;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.route.Route;
import com.threewks.thundr.route.Routes;

public class ApplicationRoutes {
	public static class Names {
		public static final String ListTasks = "list";
		public static final String CreateTask = "create-task";
		public static final String ViewTask = "view-task";
		public static final String UpdateTask = "update-task";
		public static final String StartTask = "start-task";
		public static final String StopTask = "stop-task";
		public static final String FinishedTask = "finished-task";
		public static final String ArchiveTask = "archive-task";

		public static final String LoadData = "load-data";

		public static final String ViewInstructions = "view-instructions";
		public static final String ViewData = "view-data";
	}

	public void addRoutes(Routes routes) {
		/*
		 * Thundr seems to be sensitive to renaming controller methods and I did not find a way around this.
		 */

		// Loader
		routes.addRoute(new Route(GET, "/load/", Names.LoadData), new MethodAction(DataLoadController.class, "load"));

		// Instructions
		routes.addRoute(new Route(GET, "/", Names.ViewInstructions), new MethodAction(DataLoadController.class, "instructions"));
		routes.addRoute(new Route(GET, "/restaurant/{restaurant}/download", Names.ViewData), new MethodAction(DataLoadController.class, "viewData"));

		// Solution

		/*
		 * renaming variable breaks the parameter passing. parameter name is only available if compiled with debug info.
		 * how does it work if it's not compiled like that???
		 */
		routes.addRoute(new Route(GET, "/reports/orders/{restaurant}", "orders-restaurant"),
				new MethodAction(ReportsController.class, "orders"));

		routes.addRoute(new Route(GET, "/reports/sales/{restaurant}", "sales-restaurant"),
				new MethodAction(ReportsController.class, "sales"));

		routes.addRoute(new Route(GET, "/reports/topmeals", "topmeals"),
				new MethodAction(ReportsController.class, "topMeals"));

		routes.addRoute(new Route(GET, "/reports/topcategories", "count-orders"),
				new MethodAction(ReportsController.class, "topCategories"));
	}
}
