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
import q4s.dafobi.variables.functions.MathFn;

public class MathFnTest {

	static final BigDecimal SOURCE_NUM = new BigDecimal("456.6739");

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
				case "TEST_NUM":
					return factory.createValueExpression(SOURCE_NUM, BigDecimal.class);
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
		BigDecimal result = (BigDecimal) new Expression(context, "#{test_num}").get();
		assertEquals(SOURCE_NUM, result);
	}

	/**
	 * Проверим работу функции округления числа.
	 */
	@Test
	public final void testMathRound() {
		BigDecimal srcNum = new BigDecimal("456.6739");

		// В сторону уменьшения
		BigDecimal result = MathFn.round(srcNum, 2);
		BigDecimal expected = new BigDecimal("456.67");
		assertEquals(expected, result);

		// В сторону увеличения
		result = MathFn.round(srcNum, 1);
		expected = new BigDecimal("456.7");
		assertEquals(expected, result);

		// Целочисленное округление
		result = MathFn.round(srcNum, 0);
		expected = new BigDecimal("457");
		assertEquals(expected, result);

		// --------------------------------------------------------------------
		// А теперь проверим использование их с помощью вычислимых выражений
		//

		// В сторону уменьшения
		result = (BigDecimal) new Expression(context, "#{math:round(test_num, 2)}").get();
		expected = new BigDecimal("456.67");
		assertEquals(expected, result);

		// В сторону увеличения
		result = (BigDecimal) new Expression(context, "#{math:round(test_num, 1)}").get();
		expected = new BigDecimal("456.7");
		assertEquals(expected, result);

		// Целочисленное округление
		result = (BigDecimal) new Expression(context, "#{math:round(test_num, 0)}").get();
		expected = new BigDecimal("457");
		assertEquals(expected, result);
	}

}
