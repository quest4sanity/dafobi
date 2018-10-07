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
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

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
import q4s.dafobi.variables.functions.MiscFn;

public class MiscFnTest {

	static final BigDecimal VALUE1 = null;
	static final BigDecimal VALUE2 = new BigDecimal("2");
	static final BigDecimal VALUE3 = new BigDecimal("3");

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
				case "VALUE1":
					return factory.createValueExpression(VALUE1, Object.class);
				case "VALUE2":
					return factory.createValueExpression(VALUE2, BigDecimal.class);
				case "VALUE3":
					return factory.createValueExpression(VALUE3, BigDecimal.class);
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
		Object result = new Expression(context, "#{value1}").get();
		assertEquals(VALUE1, result);

		result = (BigDecimal) new Expression(context, "#{value2}").get();
		assertEquals(VALUE2, result);

		result = (BigDecimal) new Expression(context, "#{value3}").get();
		assertEquals(VALUE3, result);
	}

	/**
	 * Проверим работу функции округления числа.
	 */
	@Test
	public final void testNvl() {
		// С одним параметром должна возникнуть ошибка.
		boolean error = false;
		try {
			MiscFn.nvl(VALUE1);
		} catch (Exception e) {
			error = true;
		}
		assertTrue(error);
		
		// С двумя параметрами
		Object result = MiscFn.nvl(VALUE1, VALUE2);
		BigDecimal expected = VALUE2;
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//
		result = (BigDecimal) new Expression(context, "#{misc:nvl(value1, value2)}").get();
		assertEquals(expected, result);

		// Проверка произвольного числа параметров
		result = (BigDecimal) new Expression(context, "#{misc:nvl(value1, value1, value2)}").get();
		assertEquals(expected, result);

		result = (BigDecimal) new Expression(context, "#{misc:nvl(value1, value1,  value1,  value1, value2)}").get();
		assertEquals(expected, result);
	}

}
