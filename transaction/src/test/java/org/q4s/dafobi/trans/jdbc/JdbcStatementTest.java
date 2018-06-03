/**
 * 
 */
package org.q4s.dafobi.trans.jdbc;

import static org.junit.Assert.*;

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
import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IRow;
import org.q4s.dafobi.trans.IStatement;

/**
 * Тестировани методов взаимодействия с базой данных класса
 * {@link JdbcStatement}.
 * 
 * @author Q4S
 * 
 */
public class JdbcStatementTest {

	private static Connection connection = null;

	JdbcTransaction transaction;

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
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcStatementTest_create.sql");
				PreparedStatement stmt = connection.prepareStatement(IOUtils.toString(createTable));) {
			stmt.execute();
		}

		// Creating the procedure
		try (InputStream createTable = HsqldbTest.class.getResourceAsStream("JdbcStatementTest_proc.sql");
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
		String dropTable = "DROP TABLE TEST";
		// Dropping the table
		try (PreparedStatement stmt = connection.prepareStatement(dropTable);) {
			stmt.execute();
		}
	}

	PreparedStatement insertStatement;

	/**
	 * Метод добавляет начальные строки в тестовую таблицу.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Adding rows into the table
		insertStatement = connection.prepareStatement("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(?, ?, ?)");

		insertStatement.setInt(1, new Integer(1));
		insertStatement.setString(2, "Str1");
		insertStatement.setDate(3, new Date(0));
		insertStatement.executeUpdate();

		insertStatement.setInt(1, new Integer(2));
		insertStatement.setString(2, "Str2");
		insertStatement.setDate(3, new Date(1000));
		insertStatement.executeUpdate();

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

		insertStatement.close();
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
		try (IStatement statement = transaction.prepare(query);) {
			IResultTable result = statement.query();
			for (IRow row : result) {
				return row;
			}
		}
		throw new RuntimeException("Запрос не вернул ни одной строки");
	}

	private Long getRowCount() {
		IRow row = getOneRow("SELECT count(*) as c FROM TEST");
		return row.getInteger("c");
	}

	/**
	 * Самый простой вариант запроса - получение числа строк в таблице.
	 */
	@Test
	public void testSimpleQuery() {
		assertEquals(new Long(2), getRowCount());
	}

	/**
	 * Test method for {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#query()}.
	 */
	@Test
	public void testQuery1() {
		try (IStatement statement = transaction.prepare("SELECT * FROM TEST WHERE ID = :id");) {
			statement.setParam("id", DataType.LONG.param(2l));
			try (IResultTable result = statement.query();) {
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
	}

	/**
	 * Test method for {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#query()}.
	 */
	@Test
	public void testQuery2() {
		try (IStatement statement = transaction.prepare("SELECT * FROM TEST");
				IResultTable result = statement.query();) {

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
	 * Test method for {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#execute()}
	 * <p>
	 * Проверяется вызов процедуры и возвращение ею параметров.
	 */
	@Test
	public void testExecute() {
		try (IStatement statement = transaction.prepare("{call test_out_param( &outp, :inp)}")) {

			statement.setParam("inp", DataType.INTEGER.param(13));
			statement.execute();
			Integer outp = (Integer) statement.getParam("outp", DataType.INTEGER);

			assertEquals(1311, outp.intValue());
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#executeUpdate()}.
	 */
	@Test
	public void testExecuteUpdate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#executeBatch()}.
	 */
	@Test
	public void testExecuteBatch() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#query(java.util.Map)}.
	 */
	@Test
	public void testQueryMapOfStringObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#execute(java.util.Map)}.
	 */
	@Test
	public void testExecuteMapOfStringObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#executeUpdate(java.util.Map)}
	 * .
	 */
	@Test
	public void testExecuteUpdateMapOfStringObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#addBatch(java.util.Map)}.
	 */
	@Test
	public void testAddBatchMapOfStringObject() {
		fail("Not yet implemented"); // TODO
	}

}
