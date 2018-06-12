/**
 * 
 */
package org.q4s.dafobi.trans.jdbc;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.q4s.dafobi.trans.DataParam;
import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IRow;
import org.q4s.dafobi.trans.IStatement;

/**
 * Тестирование методов класса {@link JdbcStatement}, работающих непосредственно
 * с базой данных.
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
			statement.setParam("outp", DataType.INTEGER.param(0));
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
		try (IStatement statement = transaction.prepare("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(:id, :str, :dt)")) {

			statement.setParam("id", DataType.INTEGER.param(10));
			statement.setParam("str", DataType.STRING.param("Str 10"));
			statement.setParam("dt", DataType.DATE.param(new Date(2000)));
			statement.executeUpdate();

			assertEquals(new Long(3), getRowCount());
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.jdbc.JdbcStatement#executeBatch()}.
	 */
	@Test
	public void testExecuteBatch() {
		try (IStatement statement = transaction.prepare("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(:id, :str, :dt)");) {

			statement.setParam("id", DataType.INTEGER.param(10));
			statement.setParam("str", DataType.STRING.param("Str 10"));
			statement.setParam("dt", DataType.DATE.param(new Date(2000)));
			statement.addBatch();

			statement.setParam("id", DataType.INTEGER.param(20));
			statement.setParam("str", DataType.STRING.param("Str 20"));
			statement.setParam("dt", DataType.DATE.param(new Date(4000)));
			statement.addBatch();

			statement.executeBatch();

			assertEquals(new Long(4), getRowCount());

		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#query(java.util.Map)}.
	 */
	@Test
	public void testQueryMapOfStringObject() {
		try (IStatement statement = transaction.prepare("SELECT * FROM TEST WHERE ID = :id");) {
			Map<String, DataParam> params = new TreeMap<String, DataParam>();
			params.put("id", DataType.INTEGER.param(2));

			try (IResultTable result = statement.query(params);) {
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
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#execute(java.util.Map)}.
	 */
	@Test
	public void testExecuteMapOfStringObject() {
		try (IStatement statement = transaction.prepare("{call test_out_param( &outp, :inp)}")) {
			Map<String, DataParam> params = new TreeMap<String, DataParam>();
			params.put("inp", DataType.INTEGER.param(13));
			params.put("outp", DataType.INTEGER.param(0));

			statement.execute(params);
			Integer outp = (Integer) DataType.INTEGER.convert(params.get("outp"));

			assertEquals(1311, outp.intValue());
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#executeUpdate(java.util.Map)}
	 * .
	 */
	@Test
	public void testExecuteUpdateMapOfStringObject() {
		try (IStatement statement = transaction.prepare("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(:id, :str, :dt)")) {
			Map<String, DataParam> params = new TreeMap<String, DataParam>();
			params.put("id", DataType.INTEGER.param(10));
			params.put("str", DataType.STRING.param("Str 10"));
			params.put("dt", DataType.DATE.param(new Date(2000)));

			statement.executeUpdate(params);

			assertEquals(new Long(3), getRowCount());
		}
	}

	/**
	 * Test method for
	 * {@link org.q4s.dafobi.trans.AbstractStatement#addBatch(java.util.Map)}.
	 */
	@Test
	public void testAddBatchMapOfStringObject() {
		try (IStatement statement = transaction.prepare("INSERT INTO TEST(ID, STR, DT)" //
				+ "VALUES(:id, :str, :dt)");) {
			Map<String, DataParam> params = new TreeMap<String, DataParam>();
			params.put("id", DataType.INTEGER.param(10));
			params.put("str", DataType.STRING.param("Str 10"));
			params.put("dt", DataType.DATE.param(new Date(2000)));
			statement.addBatch(params);

			params.put("id", DataType.INTEGER.param(20));
			params.put("str", DataType.STRING.param("Str 20"));
			params.put("dt", DataType.DATE.param(new Date(4000)));
			statement.addBatch(params);

			statement.executeBatch();

			assertEquals(new Long(4), getRowCount());
		}
	}

}
