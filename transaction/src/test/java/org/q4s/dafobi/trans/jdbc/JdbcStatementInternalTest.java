package org.q4s.dafobi.trans.jdbc;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
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
import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IRow;
import org.q4s.dafobi.trans.IStatement;

public class JdbcStatementInternalTest {

	private static Connection connection = null;

	JdbcTransaction transaction;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Creating database server instance
		// Driver: "org.hsqldb.jdbcDriver",
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:"
				+ UUID.randomUUID().toString(), "sa", "");

		// Creating the table
		try (InputStream createTable = HsqldbTest.class
				.getResourceAsStream("JdbcStatementTest_create.sql");

				PreparedStatement stmt = connection.prepareStatement(IOUtils
						.toString(createTable));) {
			stmt.execute();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		String dropTable = "DROP TABLE TEST";
		// Dropping the table
		try (PreparedStatement stmt = connection.prepareStatement(dropTable);) {
			stmt.execute();
		}
	}

	@Before
	public void setUp() throws Exception {
		transaction = new JdbcTransaction(connection);
	}

	@After
	public void tearDown() throws Exception {
		transaction.close();
	}

	/**
	 * Вначале тестируется самый простой SELECT запрос (однострочный и только с
	 * одним параметром)
	 */
	@Test
	public void testSimpleSelectQuery() {
		try (JdbcStatement statement = (JdbcStatement) transaction
				.prepare("SELECT * FROM TEST WHERE ID = :id");) {

			assertEquals("SELECT * FROM TEST WHERE ID = ?",
					statement.getParsedQuery());
			assertEquals("SELECT * FROM TEST WHERE ID = ?",
					statement.getCleanQuery());
			assertArrayEquals(new String[] { "ID" }, statement.getParamNames());
			assertArrayEquals(new String[] {}, statement.getOutParamNames());
		}
	}

	/**
	 * Тестирум многострочный SELECT запрос с несколькими праметрами и шапкой
	 * комментариев.
	 */
	@Test
	public void testMediumSelectQuery() {
		try (JdbcStatement statement = (JdbcStatement) transaction
				.prepare("/* Шапка запроса*/\r\n"//
						+ "SELECT * FROM TEST\r\n"
						+ "WHERE ID = :id1 OR ID = :id2 OR ID = :id3");) {

			assertEquals("/* Шапка запроса*/\r\n"//
					+ "SELECT * FROM TEST\r\n"
					+ "WHERE ID = ? OR ID = ? OR ID = ?",
					statement.getParsedQuery());
			assertEquals("SELECT * FROM TEST WHERE ID = ? OR ID = ? OR ID = ?",
					statement.getCleanQuery());

			String[] params = statement.getParamNames();
			Arrays.sort(params);
			assertArrayEquals(new String[] { "ID1", "ID2", "ID3" }, params);
			assertArrayEquals(new String[] {}, statement.getOutParamNames());
		}
	}
	
	// TODO Надо протестировать вызовы процедур.

}
