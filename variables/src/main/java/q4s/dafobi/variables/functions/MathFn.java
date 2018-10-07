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

import java.math.BigDecimal;
import java.math.RoundingMode;

import q4s.dafobi.variables.annotations.ELFunction;
import q4s.dafobi.variables.annotations.ELFunctionGroup;

/**
 * Функции, оперирующие с числами (для вычислимых выражений).
 * 
 * @author Q4S
 * 
 */
@ELFunctionGroup(prefix = "math")
public class MathFn {

	private MathFn() {
	}

	/**
	 * Функция увеличивае/уменьшает номер месяца в дате.
	 * 
	 * @param value
	 *            Исходное число
	 * 
	 * @param places
	 *            Число десятичных знаков, по которым надо округлить число.
	 * 
	 * @return Число после округления.
	 */
	@ELFunction(name = "round")
	public static BigDecimal round(final BigDecimal value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal result = value.setScale(places, RoundingMode.HALF_UP);
		return result;
	}

}
