package com.foodit.test.sample.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import com.foodit.test.sample.entities.RestaurantData;
import com.googlecode.objectify.Key;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantDataDaoTest extends ObjectifyTestBase {

	@Spy
	private ObjectifyApi api;

	@InjectMocks
	private RestaurantDataDao dao;

	@Test
	public void whenSave_thenFindsData() {
		RestaurantData data = new RestaurantData("rrr");

		Key<RestaurantData> key = dao.save(data);

		ofy.clear();
		RestaurantData found = ofy.load().key(key).now();
		assertThat(found).isNotNull();
	}

	public void whenLoad_thenFindsSavedData() {
		ofy.save().entity(new RestaurantData("rr2")).now();
		ofy.clear();

		RestaurantData found = dao.load("rr2");

		assertThat(found).isNotNull();
	}

}
