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
package org.q4s.dafobi.variables;

import javax.el.ValueExpression;

import org.apache.el.ExpressionFactoryImpl;
import org.q4s.dafobi.jaxb.common.DataType;
import org.q4s.dafobi.variables.context.Context;

/**
 * 
 * <pre>
 * • Ссылка на переменную приложения:
 *  #{APP.<имя параметра>}
 * • Ссылка на переменную сессии:
 *  #{SESS.<имя параметра>}
 * • Ссылка на параметр:
 *  #{SECTION|PANE}.PARAM.<имя параметра>[.NULL]#
 * • Ссылка на параметр:
 *  #{SECTION|PANE}.PARAM.<имя параметра>[.NULL]#
 * • Ссылка на атрибут:
 *  #{SECTION|PANE}.ATTR.<имя атрибута>[.NULL]#
 * • Ссылка на переменную ENVIRONMENT:
 *  #ENV.<имя переменной>#
 * 
 * </pre>
 * 
 * @author Q4S
 * 
 */
public class Expression extends AbstractVar {

	private static final long serialVersionUID = 1L;

	/**
	 * Контекст, в котором происходит компиляция выражения. Часто (но не всегда)
	 * этот же самый контекст используется для выполнения выражения.
	 */
	private Context elContext;

	/**
	 * Итоговое скомпилированное выражение.
	 */
	private ValueExpression expression;

	/**
	 * <p>
	 * Создание выражения, работающего в контексте панели.
	 * </p>
	 * <p>
	 * См. так же: {@link Expression}.
	 * </p>
	 * 
	 * @param elContext
	 *            Контекст, в котором происходит вычисление выражения.
	 * 
	 * @param expression
	 *            Выражение, с которым предстоит работать.
	 */
	public Expression(final Context elContext, final String expression) {
		this(elContext, expression, null);
	}

	/**
	 * См. {@link #Expression(Context, String)}
	 * 
	 * @param elContext
	 *            Контекст, в котором происходит вычисление выражения.
	 * 
	 * @param type
	 *            Тип данных, к которому должно приводится значение выражения.
	 * 
	 * @param expression
	 *            Выражение, с которым предстоит работать.
	 */
	public Expression(final Context elContext, final String expression, final DataType type) {

		setType(type);
		this.elContext = elContext;

		// Отсутствие контекста - это особый случай, который может возникать
		// в отдельных случаях тестирования (там, где DataStore тестируется
		// без панели). Пустое же выражение вообще является нормальным явлением.
		if (elContext == null || expression == null) {
			this.expression = null;

		} else {
			ExpressionFactoryImpl elFactory = new ExpressionFactoryImpl();

			Class<?> expectedType = type == null ? Object.class : type.typeClass();
			this.expression = elFactory.createValueExpression(elContext, expression, expectedType);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#isReadOnly()
	 */
	@Override
	public final boolean isReadOnly() {
		return isReadOnly(this.elContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#get()
	 */
	@Override
	public final Object get() {
		return get(this.elContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#set(java.lang.Object)
	 */
	@Override
	public final void set(Object value) {
		set(this.elContext, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#isReadOnly(org.q4s.dafobi.variables.
	 * IContext)
	 */
	@Override
	public boolean isReadOnly(IContext context) {
		if (expression == null) {
			return true;

		} else {
			boolean rc = expression.isReadOnly((Context) elContext);
			return rc;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#get(org.q4s.dafobi.variables.IContext)
	 */
	@Override
	public Object get(final IContext elContext) {
		if (expression == null) {
			return null;

		} else {
			Object value = expression.getValue((Context) elContext);
			if (value == null) {
				return null;

			} else {
				return convert(value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#set(org.q4s.dafobi.variables.IContext,
	 * java.lang.Object)
	 */
	@Override
	public void set(final IContext elContext, Object value) {
		if (expression != null) {
			expression.setValue((Context) elContext, convert(value));
		}
	}

}
