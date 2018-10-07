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
package q4s.dafobi.variables.functions;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.apache.el.ExpressionFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import q4s.dafobi.variables.Expression;
import q4s.dafobi.variables.context.Context;
import q4s.dafobi.variables.functions.DateFn;

public class DateFnTest {

	static final Timestamp SOURCE_DATE = Timestamp.valueOf("2015-09-04 00:00:00");

	ExpressionFactory factory;
	Context context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		factory = new ExpressionFactoryImpl();
		context = new Context() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ValueExpression resolveVariable(String variable) {
				switch (variable.toUpperCase()) {
				case "TEST_DATE":
					return factory.createValueExpression(SOURCE_DATE, Timestamp.class);
				default:
					return super.resolveVariable(variable);
				}
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Проверим, что мы правильно настроили кастомный контекст.
	 */
	@Test
	public final void testContext() {
		Date result = (Date) new Expression(context, "#{test_date}").get();
		assertEquals(SOURCE_DATE, result);
	}

	/**
	 * Проверим работу функции добавления месяца.
	 */
	@Test
	public final void testDateAddMonth() {
		Timestamp srcDate = Timestamp.valueOf("2015-01-07 00:00:00");

		Date result = DateFn.addMonth(srcDate, 4);
		Date expected = new Date(Timestamp.valueOf("2015-05-07 00:00:00").getTime());
		assertEquals(expected, result);

		result = DateFn.addMonth(srcDate, -7);
		expected = new Date(Timestamp.valueOf("2014-06-07 00:00:00").getTime());
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		// Увеличение месяца
		result = (Date) new Expression(context, "#{date:add_month(test_date, 1)}").get();
		expected = new Date(Timestamp.valueOf("2015-10-04 00:00:00").getTime());
		assertEquals(expected, result);

		// Уменьшение месяца
		result = (Date) new Expression(context, "#{date:add_month(test_date, -10)}").get();
		expected = new Date(Timestamp.valueOf("2014-11-04 00:00:00").getTime());
		assertEquals(expected, result);
	}

	/**
	 * Проверим работу функции обрезания даты на границе месяца.
	 */
	@Test
	public final void testDateTruncMonth() {
		Timestamp srcDate = Timestamp.valueOf("2015-01-07 00:00:00");

		Date result = DateFn.truncMonth(srcDate);
		Date expected = new Date(Timestamp.valueOf("2015-01-01 00:00:00").getTime());
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		// Увеличение месяца
		result = (Date) new Expression(context, "#{date:trunc_month(test_date)}").get();
		expected = new Date(Timestamp.valueOf("2015-09-01 00:00:00").getTime());
		assertEquals(expected, result);
	}

	/**
	 * Проверим работу функции получения последнего дня месяца.
	 */
	@Test
	public final void testDateLastDay() {
		Date result = DateFn.lastDay(Timestamp.valueOf("2015-01-07 00:00:00"));
		Date expected = new Date(Timestamp.valueOf("2015-01-31 00:00:00").getTime());
		assertEquals(expected, result);

		// Проверим февраль
		result = DateFn.lastDay(Timestamp.valueOf("2015-02-07 00:00:00"));
		expected = new Date(Timestamp.valueOf("2015-02-28 00:00:00").getTime());
		assertEquals(expected, result);

		// Проверим високосный февраль
		result = DateFn.lastDay(Timestamp.valueOf("2012-02-07 00:00:00"));
		expected = new Date(Timestamp.valueOf("2012-02-29 00:00:00").getTime());
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		// Увеличение месяца
		result = (Date) new Expression(context, "#{date:last_day(test_date)}").get();
		expected = new Date(Timestamp.valueOf("2015-09-30 00:00:00").getTime());
		assertEquals(expected, result);
	}

	/**
	 * Проверим работу функции форматирования строкового представления.
	 */
	@Test
	public final void testDateFormat() {
		// Проверим стандартный полный формат
		String result = DateFn.format("yyyy-MM-dd HH:mm:ss", Timestamp.valueOf("2015-01-07 00:00:00"));
		String expected = "2015-01-07 00:00:00";
		assertEquals(expected, result);

		// Проверим формат даты
		result = DateFn.format("yyyy-MM-dd", Timestamp.valueOf("2015-01-07 00:00:00"));
		expected = "2015-01-07";
		assertEquals(expected, result);

		// Проверим формат времени
		result = DateFn.format("HH:mm:ss", Timestamp.valueOf("2012-01-07 00:00:00"));
		expected = "00:00:00";
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		result = (String) new Expression(context, "#{date:format('yyyy-MM-dd', test_date)}").get();
		expected = "2015-09-04";
		assertEquals(expected, result);
	}

	/**
	 * Проверим работу функции разбора строкового представления даты.
	 */
	@Test
	public final void testDateParse() {
		// Проверим стандартный полный формат
		Date result = DateFn.parse("yyyy-MM-dd HH:mm:ss", "2015-01-07 00:00:00");
		Date expected = new Date(Timestamp.valueOf("2015-01-07 00:00:00").getTime());
		assertEquals(expected, result);

		// Проверим формат даты
		result = DateFn.parse("yyyy-MM-dd", "2015-01-07");
		expected = new Date(java.sql.Date.valueOf("2015-01-07").getTime());
		assertEquals(expected, result);

		// Проверим формат времени
		result = DateFn.parse("HH:mm:ss", "00:00:00");
		expected = new Date(Time.valueOf("00:00:00").getTime());
		assertEquals(expected, result);

		// Альтернативный формат с точками в качестве разделителей.
		result = DateFn.parse("dd.MM.yyyy", "08.03.2015");
		expected = new Date(java.sql.Date.valueOf("2015-03-08").getTime());
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		result = (Date) new Expression(context, "#{date:parse('yyyy-MM-dd', test_date)}").get();
		expected = new Date(Timestamp.valueOf("2015-09-04 00:00:00").getTime());
		assertEquals(expected, result);
	}

}
