package org.q4s.dafobi.trans;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Тестируем, как проходит проверка соответствия типа данных и связываемого
	 * с ним значения.
	 */
	@Test
	public void testParam() {
		DataParam param;
		param = DataType.STRING.param("test");
		assertEquals(DataType.STRING, param.getType());
		assertEquals("test", param.getValue());

		param = DataType.LONGSTRING.param("test");
		assertEquals(DataType.LONGSTRING, param.getType());
		assertEquals("test", param.getValue());

		param = DataType.INTEGER.param(123);
		assertEquals(DataType.INTEGER, param.getType());
		assertEquals(123, param.getValue());

		param = DataType.LONG.param(123l);
		assertEquals(DataType.LONG, param.getType());
		assertEquals(123l, param.getValue());

		param = DataType.DATE.param(Date.valueOf("2018-06-12"));
		assertEquals(DataType.DATE, param.getType());
		assertEquals(Date.valueOf("2018-06-12"), param.getValue());

		param = DataType.TIME.param(Time.valueOf("15:20:34"));
		assertEquals(DataType.TIME, param.getType());
		assertEquals(Time.valueOf("15:20:34"), param.getValue());

		param = DataType.DATETIME.param(Timestamp.valueOf("2018-06-12 15:20:34"));
		assertEquals(DataType.DATETIME, param.getType());
		assertEquals(Timestamp.valueOf("2018-06-12 15:20:34"), param.getValue());

	}

	/**
	 * Тестируем, как проходит приведение значения к типу данных.
	 */
	@Test
	public void testConvert() {
		assertEquals("123.0", DataType.STRING.convert(123.0));
		assertEquals("-123", DataType.LONGSTRING.convert(-123));
		assertEquals(-123, DataType.INTEGER.convert("-123"));
		assertEquals(-123l, DataType.LONG.convert("-123"));
		assertEquals(Date.valueOf("2018-06-12"), DataType.DATE.convert("2018-06-12"));
		assertEquals(Time.valueOf("15:20:34"), DataType.TIME.convert("15:20:34"));
		assertEquals(Timestamp.valueOf("2018-06-12 15:20:34"), DataType.DATETIME.convert("2018-06-12 15:20:34"));
	}

}
