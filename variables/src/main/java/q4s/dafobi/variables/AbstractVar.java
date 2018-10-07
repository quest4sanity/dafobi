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

import java.io.Serializable;

import q4s.dafobi.jaxb.common.DataType;

/**
 * <p>
 * Данный класс является базовой заготовкой для всех видов контейнеров
 * переменных. Он предоставляет функционал по работе с типом данных контейнера.
 * </p>
 * 
 * @author Q4S
 * 
 */
public abstract class AbstractVar implements Serializable, IVar {

	private static final long serialVersionUID = 1L;

	/**
	 * Здесь хранится тип данных, к которому должно приводится значение,
	 * хранящееся в контейнере.
	 */
	private DataType type;

	/**
	 * 
	 */
	public AbstractVar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.variables.IVar#getType()
	 */
	@Override
	public final DataType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.variables.IVar#setType(q4s.dafobi.jaxb.common.
	 * DataType)
	 */
	@Override
	public final void setType(DataType type) {
		this.type = type;
		set(get());
	}

	/**
	 * Приведение значения, к установленному в контейнере типу данных.
	 * 
	 * @param value
	 *            Исходное значение.
	 * 
	 * @return Значение, приведенное к установленному в контейнере типу данных.
	 *         Если тип данных не установлен, то исходное значение.
	 */
	protected final Object convert(Object value) {
		if (type == null) {
			return value;
		} else {
			return type.convert(value);
		}
	}

	/**
	 * Функция вовзвращает true, если строковое выражение имеет признаки
	 * EL-выражения. Или, говоря иначе, если в строке вычислимое выражение, а не
	 * константа.
	 * 
	 * @param text
	 *            Текстовое выражение (константа или EL-формула).
	 * 
	 * @return <b>true</b> - если в тексте EL-выражение.
	 */
	public final static boolean isElExpression(final String text) {
		return text.contains("#" + "{") || text.contains("$" + "{");
	}
}
