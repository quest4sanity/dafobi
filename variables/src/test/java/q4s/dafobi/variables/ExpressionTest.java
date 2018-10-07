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
import javax.el.ValueExpression;

import org.apache.el.ExpressionFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import q4s.dafobi.jaxb.common.DataType;
import q4s.dafobi.variables.Expression;
import q4s.dafobi.variables.IContext;
import q4s.dafobi.variables.context.Context;

public class ExpressionTest {

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
				case "N0":
					return factory.createValueExpression(null, Object.class);
				case "N1":
					return factory.createValueExpression(new BigDecimal("123.3"), BigDecimal.class);
				case "N2":
					return factory.createValueExpression(new BigDecimal("312.2"), BigDecimal.class);
				case "N3":
					return factory.createValueExpression(new BigDecimal("321.1"), BigDecimal.class);
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
	 * Проверка работа связанного с контейнером типа данных. В данном случае не
	 * производится проверка приведение типа у уже готового выражения, так как
	 * она подразумевает установку значения для выражения, что затрудненно с
	 * силу специфики используемых тестовых данных.
	 * 
	 * @see Expression#getType()
	 */
	@Test
	public void testExpressionType() {
		// При работе с константой
		Expression expr = new Expression(context, "123");
		assertEquals(null, expr.getType());
		assertEquals("123", expr.get());

		// Код ниже не работает, так как мы имеем дело с константой, а
		// приведение типа подразумевает обновление значения в контейнере.
		// expr.setType(DataType.NUMBER);
		// assertEquals(DataType.NUMBER, expr.getType());
		// assertEquals(new BigDecimal("123"), expr.get());

		expr = new Expression(context, "123", DataType.DECIMAL);
		assertEquals(DataType.DECIMAL, expr.getType());
		assertEquals(new BigDecimal("123"), expr.get());

		// При работе с переменной, определенной в контексте
		expr = new Expression(context, "#{n1}");
		assertEquals(new BigDecimal("123.3"), expr.get(context));
		assertNull(expr.getType());

		expr = new Expression(context, "#{n1}", DataType.DECIMAL);
		assertEquals(new BigDecimal("123.3"), expr.get());
		assertEquals(DataType.DECIMAL, expr.getType());
	}

	/**
	 * Тест на работу простых вычислимых выражений. В рабоет используются
	 * переменные контекста. К сожалению, с ними невозможно протестировать
	 * операцию изменения значения переменной.
	 * 
	 * @see Expression#get()
	 * @see Expression#get(IContext)
	 */
	@Test
	public void testExpressionGet() {
		// При работе с константой
		Expression expr = new Expression(context, "123");
		assertTrue(expr.isReadOnly());
		assertEquals("123", expr.get(context));

		// При работе с переменной, определенной в контексте
		expr = new Expression(context, "#{n0}");
		assertTrue(expr.isReadOnly());
		assertNull(expr.get(context));

		expr = new Expression(context, "#{n1}");
		assertEquals(new BigDecimal("123.3"), expr.get(context));

		// Простое вычислимое выражение
		expr = new Expression(context, "#{n1 + n2}");
		assertTrue(expr.isReadOnly());
		BigDecimal expected = new BigDecimal("123.3").add(new BigDecimal("312.2"));
		assertEquals(expected, expr.get(context));
	}
}
