package org.q4s.dafobi.trans;

import static org.junit.Assert.*;

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
		param = DataType.LONGSTRING.param("test");
		param = DataType.INTEGER.param(123);
		param = DataType.LONG.param(123l);
		param = DataType.DATE.param(new Date(0));
		param = DataType.TIME.param(new Time(0));
		param = DataType.DATETIME.param(new Timestamp(0));
	}

}
