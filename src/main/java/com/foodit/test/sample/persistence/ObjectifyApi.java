package com.foodit.test.sample.persistence;

import javax.inject.Named;
import javax.inject.Singleton;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@Named
@Singleton
public class ObjectifyApi {

	public Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public <E> Key<E> save(E entity) {
		return ofy().save().entity(entity).now();
	}

	public <E> E load(Class<E> type, String id) {
		return ofy().load().type(type).id(id).now();
	}

	public <E> E load(Class<E> type, long id) {
		return ofy().load().type(type).id(id).now();
	}

}
