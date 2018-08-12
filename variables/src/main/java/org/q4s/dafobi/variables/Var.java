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

import org.q4s.dafobi.jaxb.common.DataType;

/**
 * Обычная переменная, изменение значения которой может потребоваться
 * отслеживать.
 * 
 * @author Q4S
 * 
 */
public class Var extends AbstractVar {

	private static final long serialVersionUID = 1L;

	private boolean readOnly = false;

	/**
	 * Значение переменной.
	 */
	private Object value;

	/**
	 * @see Var
	 */
	public Var() {
		this(null, null);
	}

	/**
	 * @param value
	 *            Начальное значение переменной.
	 * 
	 * @see Var
	 */
	public Var(Object value) {
		this(value, null);
	}

	/**
	 * @param value
	 *            Начальное значение переменной.
	 * 
	 * @param valueClass
	 *            Класс, к которому надо приводить значение переменной.
	 * 
	 * @see Var
	 */
	public Var(Object value, DataType type) {
		super();

		setType(type);
		set(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Метод позволяет защитить значение переменной от изменений, фактически
	 * сделав его константой (вплоть до отмены защиты).
	 * 
	 * @param readOnly
	 *            при значении true изменить значение станет невозможно.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#get()
	 */
	@Override
	public Object get() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#set(java.lang.Object)
	 */
	@Override
	public void set(final Object value) {
		if (readOnly) {
			return;

		} else if (value == null) {
			this.value = null;

		} else {
			this.value = convert(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#isReadOnly(org.q4s.dafobi.variables.
	 * IContext)
	 */
	@Override
	public final boolean isReadOnly(IContext context) {
		return isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#get(org.q4s.dafobi.variables.IContext)
	 */
	@Override
	public final Object get(IContext context) {
		return get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.variables.IVar#set(org.q4s.dafobi.variables.IContext,
	 * java.lang.Object)
	 */
	@Override
	public final void set(IContext context, Object value) {
		set(value);
	}
}
