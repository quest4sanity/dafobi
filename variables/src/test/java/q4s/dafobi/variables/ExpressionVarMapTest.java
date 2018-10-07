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
package q4s.dafobi.variables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.apache.el.ExpressionFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import q4s.dafobi.jaxb.common.DataType;
import q4s.dafobi.variables.Expression;
import q4s.dafobi.variables.Var;
import q4s.dafobi.variables.VarMap;
import q4s.dafobi.variables.context.Context;

public class ExpressionVarMapTest {

	VarMap map = new VarMap();
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
		map.put("n0", new Var(null));
		map.put("n1", new Var(new BigDecimal("123.3"), DataType.DECIMAL));
		map.put("n2", new Var(new BigDecimal("312.2"), DataType.DECIMAL));
		map.put("n3", new Var(new BigDecimal("321.1"), DataType.DECIMAL));

		factory = new ExpressionFactoryImpl();
		context = new Context() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ValueExpression resolveVariable(String variable) {
				switch (variable.toUpperCase()) {
				case "M":
					return factory.createValueExpression(map, VarMap.class);
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
	 * Проверка возможности получения значений из карты значений.
	 *
	 * @see Expression#get()
	 * @see Expression#getType()
	 * @see Expression#setType(DataType)
	 */
	@Test
	public void testGetType() {
		// Null-овое значение.
		Expression expr = new Expression(context, "#{m.n0}");
		assertNull(expr.getType());
		assertNull(expr.get());

		// Реальное значение.
		expr = new Expression(context, "#{m.n1}");
		assertNull(expr.getType());
		assertEquals(new BigDecimal("123.3"), expr.get());

		// Попробуем приведение типа. Здесь должно сработать.
		expr.setType(DataType.DECIMAL);
		assertEquals(DataType.DECIMAL, expr.getType());
		assertEquals(new BigDecimal("123.3"), expr.get());
	}

	/**
	 * Проверка возможности установки значений в карте.
	 *
	 * @see Expression#get()
	 * @see Expression#set(Object)
	 */
	@Test
	public void testSetType() {
		// Получение несуществующего значения из карты. 
		Expression expr = new Expression(context, "#{m.n10}");
		boolean error = false;
		try {
			expr.get();
		} catch (PropertyNotFoundException e) {
			error = true;
		}
		assertTrue(error);

		// Установка в несуществующее значение тоже невозможна,
		// так как непонятно, что должен быть за контейнер для него.
		error = false;
		try {
			expr.set(new BigDecimal("100.12"));
		} catch (PropertyNotFoundException e) {
			error = true;
		}
		assertTrue(error);

		// Получение существующего значения.
		expr = new Expression(context, "#{m.n1}", DataType.DECIMAL);
		assertEquals(DataType.DECIMAL, expr.getType());
		BigDecimal expected = new BigDecimal("123.3");
		assertEquals(new BigDecimal("123.3"), expr.get());
		assertEquals(expected, map.get("n1").get());

		// Установка значения
		expr.set(new BigDecimal("100.12"));
		expected = new BigDecimal("100.12");
		assertEquals(expected, expr.get());
		assertEquals(expected, map.get("n1").get());
	}

}
