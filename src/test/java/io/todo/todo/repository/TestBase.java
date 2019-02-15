package io.todo.todo.repository;

import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

@SpringBootTest
public abstract class TestBase {

	protected Sql2o sql2o;
	protected Connection connection;
	protected Query query;
	protected Executor executor;

	@Before
	public void setup() {
		executor = Executors.newSingleThreadExecutor();
		sql2o = mock(Sql2o.class);
		connection = mock(Connection.class);
		query = mock(Query.class);
	}

}
