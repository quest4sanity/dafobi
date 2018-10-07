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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import q4s.dafobi.variables.annotations.ELFunction;
import q4s.dafobi.variables.annotations.ELFunctionGroup;

/**
 * Функции вычислимых выражений, оперирующие с датой.
 * 
 * @author Q4S
 * 
 */
@ELFunctionGroup(prefix = "date")
public class DateFn {

	private DateFn() {
	}

	/**
	 * Функция увеличивае/уменьшает номер месяца в дате.
	 * 
	 * @param date
	 *            Исходная дата
	 * 
	 * @param amount
	 *            На сколько месяцев надо изменить дату (меньше нуля, если надо
	 *            уменьшить).
	 * 
	 * @return Дата с увеличенным/уменьшенным номером месяца.
	 */
	@ELFunction(name = "add_month")
	public static Date addMonth(final Date date, int amount) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, amount);
		return new Date(calendar.getTime().getTime());
	}

	/**
	 * Функция сбрасывает дату на первый день месяца.
	 * 
	 * @param date
	 *            Дата, для которой надо получить перевый день месяца.
	 * 
	 * @return Результат.
	 */
	@ELFunction(name = "trunc_month")
	public static Date truncMonth(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		return new Date(calendar.getTime().getTime());
	}

	/**
	 * Функция возвращает последний день месяца для указанной даты.
	 * 
	 * @param date
	 *            Исходная дата
	 * 
	 * @return Дата последего дня месяца.
	 */
	@ELFunction(name = "last_day")
	public static Date lastDay(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return new Date(calendar.getTime().getTime());
	}

	/**
	 * Функция возвращает отформатированное строковое представление для
	 * указанной даты.
	 * 
	 * @param format
	 *            Формат, в соответствии с которым происходит форматирование
	 *            строки
	 * 
	 * @param date
	 *            Исходная дата
	 * 
	 * @see SimpleDateFormat Правила форматирования.
	 * 
	 * @return Отформатированная строка для указанной даты.
	 */
	@ELFunction(name = "format")
	public static String format(final String format, final Date date) {
		String result = new SimpleDateFormat(format).format(date);
		return result;
	}

	/**
	 * Функция разбирает строку и преобразует ее в дату, используя значение
	 * формата.
	 * 
	 * @param format
	 *            Формат, в соответствии с которым происходит разбор.
	 * 
	 * @param str
	 *            Строковое представление даты.
	 * 
	 * @return Итоговая дата.
	 */
	@ELFunction(name = "parse")
	public static Date parse(final String format, final String str) {
		Date result;
		try {
			result = new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
			result = null;
		}
		return result;
	}

}
