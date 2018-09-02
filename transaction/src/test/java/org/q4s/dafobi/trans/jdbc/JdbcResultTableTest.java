package org.q4s.dafobi.trans.jdbc;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.q4s.dafobi.jaxb.common.DataType;
import org.q4s.dafobi.trans.AbstractResultTable;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IRow;
import org.q4s.dafobi.trans.IStatement;

public class JdbcResultTableTest {

	private static Connection jdbcConnection = null;

	private JdbcConnection connection;

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Creating database server instance
		// Driver: "org.hsqldb.jdbcDriver",
		jdbcConnection = DriverManager.getConnection("jdbc:hsqldb:mem:" + UUID.randomUUID().toString(), "sa", "");

		// Creating the table
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcResultTest_create.sql");
				PreparedStatement stmt = jdbcConnection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}
	}

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Dropping the table
		String dropTable = "DROP TABLE TEST";
		try (PreparedStatement stmt = jdbcConnection.prepareStatement(dropTable);) {
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
		try (PreparedStatement insertStatement = jdbcConnection.prepareStatement("INSERT INTO TEST(ID, STR, DT)" //
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
		connection = new JdbcConnection(jdbcConnection);
	}

	/**
	 * Метод опустошает тестовую таблицу.
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Cleaning the table
		try (PreparedStatement stmt = jdbcConnection.prepareStatement("DELETE FROM TEST");) {
			stmt.executeUpdate();
		}

		connection.close();
	}

	/**
	 * Проверка работы класса {@link AbstractResultTable}
	 */
	@Test
	public void testResultTable() {
		String query = "SELECT * as c FROM TEST";
		try (IStatement statement = connection.prepare(query);) {
			IResultTable result = statement.query();

			assertEquals(3, result.count());
			assertEquals(DataType.INTEGER, result.getColumnType(0));
			assertEquals(DataType.STRING, result.getColumnType(1));
			assertEquals(DataType.TIMESTAMP, result.getColumnType(2));

			assertEquals("id", result.getColumnName(0));
			assertEquals("str", result.getColumnName(1));
			assertEquals("dt", result.getColumnName(2));

			assertEquals(0, result.getColumnIndex("id"));
			assertEquals(1, result.getColumnIndex("str"));
			assertEquals(2, result.getColumnIndex("dt"));

			int i = 0;
			for (IRow row : result) {
				assertEquals(new Long(++i), row.getInteger(0));
			}
			assertEquals(2, i);
		}
	}

}
