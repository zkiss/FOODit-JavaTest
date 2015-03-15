package com.foodit.test.sample.persistence;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;

public class ObjectifyTestBase {

	protected final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	protected Objectify ofy;

	@Before
	public final void setUpBase() {
		helper.setUp();
		this.ofy = ObjectifyService.ofy();
	}

	@After
	public final void tearDownBase() {
		helper.tearDown();
	}

}
