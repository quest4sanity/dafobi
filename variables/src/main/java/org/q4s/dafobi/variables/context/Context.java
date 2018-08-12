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
package org.q4s.dafobi.variables.context;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.atteo.classindex.ClassIndex;
import org.q4s.dafobi.variables.IContext;
import org.q4s.dafobi.variables.annotations.ELFunction;
import org.q4s.dafobi.variables.annotations.ELFunctionGroup;

//import com.borlas.bus.kernel.application.Application;
//import com.borlas.bus.kernel.annotations.expressions.BusFunction;
//import com.borlas.bus.kernel.annotations.expressions.BusFunctionGroup;
//import com.borlas.bus.kernel.session.Session;
//import com.borlas.bus.kernel.tools.FacesTools;

/**
 * <p>
 * Система Dafobi при настройке элементов использует EL-выражения. Эти выражения
 * отличаются от стандартных, построенных на основе бинов сервера приложений. И
 * в первую очередь они отличаются EL-контекстом и ресолверами. В выражениях
 * Dafobi возможно использовать только: переменные среды, атрибуты и параметры,
 * привязанные к разделам и панелям, а так же поля и колонки строк форм данных.
 * </p>
 * <p>
 * В системе Dafobi набор доступных источников переменных может варьироваться в
 * зависимости от того, где именно вычисляется выражение. Одно дело, если оно
 * вычисляется на кнопке действия, другое, если на строке DataStore и третье,
 * если это DataStore находится внутри поля данных. Данный объект определяет, в
 * каком именно месте выполняется выражение и делится этой информацией с
 * ресолверами.
 * </p>
 * <p>
 * Кроме того в системе есть возможность создания кастомных функций. За
 * описанием и примерами можно обратиться к пакету
 * {@link org.q4s.dafobi.variables.functions}.
 * </p>
 * 
 * @author Q4S
 * 
 */
public abstract class Context extends ELContext implements IContext, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Обработчик переменных, используемых в вычислимых выражениях.
	 */
	private final VariableMapperImpl variableMapper;

	/**
	 * Обработчик функций, используемых в вычислимых выражениях.
	 */
	private final FunctionMapperImpl functionMapper;

	private final ELResolver dispatcher;
	/**
	 * @see Context
	 */
	public Context() {
		variableMapper = new VariableMapperImpl();
		functionMapper = new FunctionMapperImpl();
		dispatcher =  new ELResolverDispatcher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getELResolver()
	 */
	@Override
	public ELResolver getELResolver() {
		return dispatcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getFunctionMapper()
	 */
	@Override
	public FunctionMapper getFunctionMapper() {
		return functionMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELContext#getVariableMapper()
	 */
	@Override
	public VariableMapper getVariableMapper() {
		return variableMapper;
	}

	/**
	 * Данный метод возвращает выражение
	 * 
	 * @param variable
	 * @return
	 */
	protected ValueExpression resolveVariable(String variable) {
		switch (variable.toUpperCase()) {
		// case "SESS":
		// return FacesTools.getExpressionFactory().createValueExpression(
		// Session.getInstance(), Session.class);
		//
		// case "APP":
		// return FacesTools.getExpressionFactory().createValueExpression(
		// Application.getInstance(), Application.class);

		default:
			return null;
		}
	}

	/**
	 * Реализация хранилища системных переменных, используемых в выражениях.
	 * 
	 * @author Q4S
	 * 
	 */
	private class VariableMapperImpl extends VariableMapper implements Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * @see VariableMapperImpl
		 */
		public VariableMapperImpl() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
		 */
		@Override
		public ValueExpression resolveVariable(String variable) {
			return Context.this.resolveVariable(variable);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.el.VariableMapper#setVariable(java.lang.String,
		 * javax.el.ValueExpression)
		 */
		@Override
		public ValueExpression setVariable(String variable, ValueExpression expression) {
			// TODO Надо реализовать грамотную масштабируемую архитектуру для EL
			return null;
		}

	}

	/**
	 * Реализация хранилища системных функций, используемых в выражениях.
	 * 
	 * @author Q4S
	 * 
	 */
	private static class FunctionMapperImpl extends FunctionMapper implements Serializable {

		private static final long serialVersionUID = 1L;

		private final Map<String, Method> functions = new TreeMap<String, Method>();

		/**
		 * 
		 */
		public FunctionMapperImpl() {
			super();

			// Загрузим список всех функций, зарегистрированных в системе.
			// Такая инициализация нужна только один раз, ибо данные функции
			// могут быть использованы во всех контекстах.
			for (Class<?> clazz : ClassIndex.getAnnotated(ELFunctionGroup.class)) {
				String prefix = clazz.getAnnotation(ELFunctionGroup.class).prefix();
				for (Method method : clazz.getDeclaredMethods()) {
					String name = method.getAnnotation(ELFunction.class).name();
					mapFunction(prefix, name, method);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.el.FunctionMapper#resolveFunction(java.lang.String,
		 * java.lang.String)
		 */
		@Override
		public Method resolveFunction(String prefix, String localName) {
			String key = prefix.toUpperCase() + ":" + localName.toUpperCase();
			return functions.get(key);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.el.FunctionMapper#mapFunction(java.lang.String,
		 * java.lang.String, java.lang.reflect.Method)
		 */
		@Override
		public void mapFunction(String prefix, String localName, Method method) {
			String key = localName.toUpperCase();

			// При добавлении функции с префиксом делается так же попытка
			// добавить ее без префикса. Такой подход полезен, так как
			// подавляющее большинство функций имеют уникальные имена. Однако, в
			// случае конфликта без префикса может быть только одна функция
			// (причем, заранее негарантировано, какая). В этом случае для ее
			// использования префикс обязателен.
			if (!functions.containsKey(key)) {
				functions.put(key, method);
			}
			key = prefix.toUpperCase() + ":" + key;
			functions.put(key, method);
		}
	}
}
