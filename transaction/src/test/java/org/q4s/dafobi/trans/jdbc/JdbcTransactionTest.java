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
package org.q4s.dafobi.trans.jdbc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.q4s.dafobi.trans.DataParam;
import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IRow;

/**
 * Тестирование методов класса {@link JdbcTransaction}, работающих
 * непосредственно с базой данных.
 * 
 * @author Q4S
 * 
 */
public class JdbcTransactionTest {

	private static Connection connection = null;

	private JdbcTransaction transaction;

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Creating database server instance
		// Driver: "org.hsqldb.jdbcDriver",
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:" + UUID.randomUUID().toString(), "sa", "");

		// Creating the table
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcTransactionTest_create.sql");
				PreparedStatement stmt = connection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}

		// Creating the procedure
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcTransactionTest_proc.sql");
				PreparedStatement stmt = connection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}

		// Creating second the procedure
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcTransactionTest_proc_query.sql");
				PreparedStatement stmt = connection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}
	}

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Dropping procedures
		String dropProc = "DROP PROCEDURE test_out_param";
		try (PreparedStatement stmt = connection.prepareStatement(dropProc);) {
			stmt.execute();
		}

		dropProc = "DROP PROCEDURE test_cursor";
		try (PreparedStatement stmt = connection.prepareStatement(dropProc);) {
			stmt.execute();
		}

		// Dropping the table
		String dropTable = "DROP TABLE TEST";
		try (PreparedStatement stmt = connection.prepareStatement(dropTable);) {
			stmt.execute();
		}
	}

	/**
	 * Метод добавляет начальные строки в тестовую таблицу.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Adding rows into the table
		try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(?, ?, ?)")) {

			insertStatement.setInt(1, new Integer(1));
			insertStatement.setString(2, "Str1");
			insertStatement.setDate(3, new Date(0));
			insertStatement.addBatch();

			insertStatement.setInt(1, new Integer(2));
			insertStatement.setString(2, "Str2");
			insertStatement.setDate(3, new Date(1000));
			insertStatement.addBatch();

			insertStatement.executeBatch();
		}
		transaction = new JdbcTransaction(connection);
	}

	/**
	 * Метод опустошает тестовую таблицу.
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Cleaning the table
		try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM TEST");) {
			stmt.executeUpdate();
		}

		transaction.close();
	}

	/**
	 * Метод выполняет запрос в базу данных и возвращает первую строку.
	 * 
	 * @param query
	 *            Текст запроса.
	 * 
	 * @return Первая строка данных, возвращенная запросом.
	 */
	private IRow getOneRow(String query) {
		IRow row = transaction.queryRow(query, new TreeMap<String, DataParam>());
		if (row == null) {
			throw new RuntimeException("Запрос не вернул ни одной строки");
		} else {
			return row;
		}
	}

	private long getRowCount() {
		IRow row = getOneRow("SELECT count(*) as c FROM TEST");
		return row.getInteger("c");
	}

	/**
	 * Самый простой вариант запроса - получение числа строк в таблице.
	 */
	@Test
	public void testSimpleQuery() {
		assertEquals(2l, getRowCount());
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#query(String, Map)}
	 * <p>
	 * Пример получения одной строки (по ид.).
	 */
	@Test
	public void testQueryById() {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("id", DataType.LONG.param(2l));
		try (IResultTable result = transaction.query("SELECT * FROM TEST WHERE ID = :id", parameters)) {
			int i = 0;
			for (IRow row : result) {
				assertEquals(new Long(2), row.getInteger(0));
				assertEquals("Str2", row.getString(1));
				assertEquals(new Date(1000), row.getTimestamp(2));
				i++;
			}
			assertEquals(1, i);
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#query(String, Map)}
	 * <p>
	 * Пример получения одной строики (по ид.) с помощью процедуры.
	 */
	@Test
	public void testQueryByIdProc() {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("id", DataType.LONG.param(2l));
		try (IResultTable result = transaction.query("{call test_cursor(:id)}", parameters)) {
			int i = 0;
			for (IRow row : result) {
				assertEquals(new Long(2), row.getInteger(0));
				assertEquals("Str2", row.getString(1));
				assertEquals(new Date(1000), row.getTimestamp(2));
				i++;
			}
			assertEquals(1, i);
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#query(String, Map)}
	 * <p>
	 * Пример получения нескольких строк.
	 */
	@Test
	public void testQueryManyRows() {
		try (IResultTable result = transaction.query("SELECT * FROM TEST", new TreeMap<String, DataParam>())) {
			int i = 0;
			for (IRow row : result) {
				if (i == 0) {
					assertEquals(new Long(1), row.getInteger(0));
					assertEquals("Str1", row.getString(1));
					assertEquals(new Date(0), row.getTimestamp(2));
				} else {
					assertEquals(new Long(2), row.getInteger(0));
					assertEquals("Str2", row.getString(1));
					assertEquals(new Date(1000), row.getTimestamp(2));
				}
				i++;
			}
			assertEquals(2, i);
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#execute(String, Map)}
	 * <p>
	 * Проверяется вызов процедуры и возвращение ею параметров.
	 */
	@Test
	public void testExecute() {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("inp", DataType.INTEGER.param(13));
		parameters.put("outp", DataType.INTEGER.param(0));
		int rc = transaction.execute("{call test_out_param( &outp, :inp)}", parameters);

		assertEquals(0, rc);
		assertEquals(1311, ((DataParam) parameters.get("outp")).getValue());
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#execute(String, Map)}
	 */
	@Test
	public void testExecuteUpdate() {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("id", DataType.INTEGER.param(10));
		parameters.put("str", DataType.STRING.param("Str 10"));
		parameters.put("dt", DataType.DATE.param(new Date(2000)));
		int rc = transaction.execute("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(:id, :str, :dt)", parameters);

		assertEquals(1, rc);
		assertEquals(3l, getRowCount());
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#executeScript(String, Map)}
	 * 
	 * @throws IOException
	 */
	@Test
	public void testExecuteScript() throws IOException {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("id", DataType.INTEGER.param(10));
		try (InputStream stream = HsqldbTest.class.getResourceAsStream("JdbcTransactionTest_script.sql")) {
			String script = IOUtils.toString(stream);
			int[] rc = transaction.executeScript(script, parameters);

			Assert.assertArrayEquals(new int[] { 2, 4, 1 }, rc);
			assertEquals(7l, getRowCount());
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcTransaction#executeScript(String, Map)}
	 * 
	 * @throws IOException
	 */
	@Test
	public void testExecuteScriptCascadeCall() throws IOException {
		Map<String, DataParam> parameters = new TreeMap<>();
		parameters.put("inp", DataType.INTEGER.param(13));
		parameters.put("outp", DataType.INTEGER.param(0));
		try (InputStream stream = HsqldbTest.class.getResourceAsStream("JdbcTransactionTest_script_proc.sql")) {
			String script = IOUtils.toString(stream);
			int[] rc = transaction.executeScript(script, parameters);

			Assert.assertArrayEquals(new int[] { 0, 0, 0 }, rc);

			Integer expected = (13 * 100 + 11) * 3;
			assertEquals(expected, ((DataParam) parameters.get("outp")).getValue());
		}
	}
}
