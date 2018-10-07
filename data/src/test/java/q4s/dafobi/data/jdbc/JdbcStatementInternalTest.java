/*
 * (C) Copyright 2018 - Vladimir Bogdanov | Data Form Builder
 *
 * https://github.com/quest4sanity/dafobi
 *
 * Licensed under the LGPL, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License in LGPL.txt file in 
 * the root directory or at https://www.gnu.org/licenses/lgpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Vladimir Bogdanov - quest4sanity@gmail.com
 */
package q4s.dafobi.data.jdbc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import q4s.dafobi.data.jdbc.JdbcConnection;
import q4s.dafobi.data.jdbc.JdbcStatement;

/**
 * Тестировани внутренних методов класса {@link JdbcStatement}:
 * <p>
 * {@link JdbcStatement#getParsedQuery()},
 * {@link JdbcStatement#getProcessedQuery()},
 * {@link JdbcStatement#getParamNames()},
 * {@link JdbcStatement#getOutParamNames()}
 * 
 * @author Q4S
 *
 */
public class JdbcStatementInternalTest {

	private static Connection jdbcConnection = null;

	JdbcConnection connection;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Creating database server instance
		// Driver: "org.hsqldb.jdbcDriver",
		jdbcConnection = DriverManager.getConnection("jdbc:hsqldb:mem:" + UUID.randomUUID().toString(), "sa", "");

		// Creating the table
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcStatementTest_create.sql");

				PreparedStatement stmt = jdbcConnection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		String dropTable = "DROP TABLE TEST";
		// Dropping the table
		try (PreparedStatement stmt = jdbcConnection.prepareStatement(dropTable);) {
			stmt.execute();
		}
	}

	@Before
	public void setUp() throws Exception {
		connection = new JdbcConnection(jdbcConnection);
	}

	@After
	public void tearDown() throws Exception {
		connection.close();
	}

	/**
	 * Вначале тестируется самый простой SELECT запрос (однострочный и только с
	 * одним параметром).
	 */
	@Test
	public void testSimpleSelectQuery() {
		try (JdbcStatement statement = (JdbcStatement) connection.prepare("SELECT * FROM TEST WHERE ID = :id");) {

			assertEquals("SELECT * FROM TEST WHERE ID = ?", statement.getParsedQuery());
			assertEquals("SELECT * FROM TEST WHERE ID = ?", statement.getProcessedQuery());
			assertArrayEquals(new String[] { "id" }, statement.getParamNames());
			assertArrayEquals(new String[] {}, statement.getOutParamNames());
		}
	}

	/**
	 * Тестирум многострочный SELECT запрос с несколькими праметрами и шапкой
	 * комментариев.
	 */
	@Test
	public void testMediumSelectQuery() {
		try (JdbcStatement statement = (JdbcStatement) connection.prepare("/* Шапка запроса*/\r\n"//
				+ "SELECT * FROM TEST\r\n" //
				+ "WHERE ID = :id1 OR ID = :id2 OR ID = :id3");) {

			assertEquals("/* Шапка запроса*/\r\n"//
					+ "SELECT * FROM TEST\r\n" //
					+ "WHERE ID = ? OR ID = ? OR ID = ?", statement.getParsedQuery());
			assertEquals("SELECT * FROM TEST WHERE ID = ? OR ID = ? OR ID = ?", statement.getProcessedQuery());

			String[] params = statement.getParamNames();
			Arrays.sort(params);
			assertArrayEquals(new String[] { "id1", "id2", "id3" }, params);
			assertArrayEquals(new String[] {}, statement.getOutParamNames());
		}
	}
}
