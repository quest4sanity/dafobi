package org.q4s.dafobi.trans.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Простейшие тесты на проверку работы JDBC драйвера с базой HSQLDB. Именно эта
 * база будет использоваться для тестирования проекта.
 * 
 * @author Q4S
 */
public class HsqldbTest {

	private static Connection connection = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Creating database server instance
		// Driver: "org.hsqldb.jdbcDriver",
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:"
				+ UUID.randomUUID().toString(), "sa", "");

		// Creating the table
		try (InputStream createTable = HsqldbTest.class
				.getResourceAsStream("HsqldbTest_create.sql");

				PreparedStatement stmt = connection.prepareStatement(IOUtils
						.toString(createTable));) {
			stmt.execute();
		}
	}

	/**
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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Adding rows into the table
		try (PreparedStatement stmt = connection
				.prepareStatement("INSERT INTO TEST(ID, STR, DT)" //
						+ "VALUES(?, ?, ?)");) {
			stmt.setInt(1, new Integer(1));
			stmt.setString(2, "Str1");
			stmt.setDate(3, new Date(0));
			stmt.executeUpdate();

			stmt.setInt(1, new Integer(2));
			stmt.setString(2, "Str2");
			stmt.setDate(3, new Date(0));
			stmt.executeUpdate();

		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Cleaning the table
		try (PreparedStatement stmt = connection
				.prepareStatement("DELETE FROM TEST");) {
			stmt.executeUpdate();
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		try (Statement statement = connection.createStatement();
				ResultSet result1 = statement
						.executeQuery("SELECT * FROM TEST");) {

			// Массив эталонных значений
			int[] ids = { 1, 2 };
			String[] strs = { "Str1", "Str2" };

			while (result1.next()) {
				int r = result1.getRow() - 1;
				int id = result1.getInt("id");
				String str = result1.getString("str");

				Assert.assertEquals(ids[r], id);
				Assert.assertEquals(strs[r], str);
			}
		}
	}

}
