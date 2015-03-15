package com.foodit.test.sample.persistence;

import static org.fest.assertions.api.Assertions.assertThat;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.junit.Before;
import org.junit.Test;

public class ObjectifyApiTest extends ObjectifyTestBase {

	private ObjectifyApi api;

	@Before
	public void setUp() {
		api = new ObjectifyApi();
		ObjectifyService.register(TestEntity.class);
		ObjectifyService.register(TestEntityStringKey.class);
	}

	@Test
	public void whenOfy_thenReturnsOfy() {
		assertThat(api.ofy()).isSameAs(ObjectifyService.ofy());
	}

	@Test
	public void whenSave_thenSaves() {
		Key<TestEntity> key = api.save(new TestEntity("data"));
		ofy.clear();

		TestEntity saved = ofy.load().key(key).now();

		assertThat(saved).isNotNull();
		assertThat(saved.data).isEqualTo("data");
	}

	@Test
	public void givenNoEntity_whenLoad_thenReturnsNull() {
		TestEntity entity = api.load(TestEntity.class, 1);

		assertThat(entity).isNull();
	}

	@Test
	public void givenEntityExists_whenLoad_thenReturnsEntity() {
		TestEntity entity = new TestEntity("data");
		entity.id = 1l;
		api.save(entity);
		ofy.clear();

		entity = api.load(TestEntity.class, 1);

		assertThat(entity).isNotNull();
	}

	@Test
	public void givenEntityStringKeyExists_whenLoad_thenReturnsEntity() {
		TestEntityStringKey entity = new TestEntityStringKey("data");
		entity.id = "key";
		api.save(entity);
		ofy.clear();

		entity = api.load(TestEntityStringKey.class, "key");

		assertThat(entity).isNotNull();
	}

	@Entity
	private static class TestEntity {
		@Id
		Long id;
		String data;

		public TestEntity() {
			// ofy constructor
		}

		public TestEntity(String data) {
			this.data = data;
		}
	}

	@Entity
	private static class TestEntityStringKey {
		@Id
		String id;
		String data;

		public TestEntityStringKey() {
			// ofy constructor
		}

		public TestEntityStringKey(String data) {
			this.data = data;
		}
	}

}
