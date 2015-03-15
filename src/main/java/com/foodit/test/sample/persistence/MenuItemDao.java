package com.foodit.test.sample.persistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;
import java.util.Map;

import com.foodit.test.sample.entities.MenuItem;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

@Named
@Singleton
public class MenuItemDao {

	@Inject
	private ObjectifyApi api;

	public MenuItem load(String id) {
		return api.load(MenuItem.class, id);
	}

	public MenuItem load(Key<MenuItem> key) {
		return api.ofy().load().key(key).now();
	}

	public Result<Map<Key<MenuItem>, MenuItem>> saveAsync(Iterable<MenuItem> items) {
		return api.ofy().save().entities(items);
	}

	public Result<Key<MenuItem>> saveAsync(MenuItem item) {
		return api.ofy().save().entity(item);
	}

	public List<MenuItem> loadAll() {
		return api.ofy().load().type(MenuItem.class).list();
	}

}
