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
package org.q4s.dafobi.trans;

import java.text.MessageFormat;

import org.q4s.dafobi.jaxb.trans.DataType;

/**
 * Данный класс связывает воедино значение и его тип данных {@link DataType}. В
 * таком виде значение используется в методах {@link IStatement}.
 * <p>
 * Для получения объекта данного типа проще всего воспользоваться типом данных
 * {@link DataType}. Например так:
 * 
 * <pre>
 * DataParam param;
 * param = DataType.INTEGER.param(123);
 * </pre>
 * 
 * @author Q4S
 *
 */
public class DataParam {
	/**
	 * Тип параметра.
	 */
	private final DataType type;

	/**
	 * Значение параметра.
	 */
	private final Object value;

	/**
	 * Создание типированного значения парметра.
	 * 
	 * @param type
	 *            Тип параметра.
	 * 
	 * @param value
	 *            Значение параметра.
	 */
	public DataParam(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return Тип параметра.
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * @return Значение параметра.
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return MessageFormat.format("DataParam: {0} [{1}]", type, value);
	}
}
