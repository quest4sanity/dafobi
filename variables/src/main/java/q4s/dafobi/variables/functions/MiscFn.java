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

import q4s.dafobi.variables.annotations.ELFunction;
import q4s.dafobi.variables.annotations.ELFunctionGroup;

/**
 * Функции, оперирующие с произвольными объектами (для вычислимых выражений).
 * 
 * @author Q4S
 * 
 */
@ELFunctionGroup(prefix = "misc")
public class MiscFn {

	private MiscFn() {
	}

	/**
	 * <p>
	 * Функция возвращает первое значение не null из параметров.
	 * </p>
	 * 
	 * @param values
	 *            Список значений
	 * 
	 * @return Первое значение не null из списка.
	 */
	@ELFunction(name = "nvl")
	public static Object nvl(final Object... values) {
		if (values.length <= 1) {
			throw new IllegalArgumentException("Функция nvl() требует как минимум 2 параметра");
		}
		Object value = values[0];
		for (int i = 0; value == null && i < values.length; i++) {
			value = (Object) values[i];
		}
		return value;
	}

	/**
	 * <p>
	 * Функция взависимости от условия в первом параметре возвращает значение
	 * второго или третьего параметра.
	 * </p>
	 * 
	 * @param value1
	 *            Первое значение
	 * 
	 * @param value2
	 *            Второе значение
	 * 
	 * @return value1, если оно не null или value2.
	 */
	@ELFunction(name = "if")
	public static Object ifFunction(boolean condition, final Object value1, final Object value2) {
		return condition ? value1 : value2;
	}

}
