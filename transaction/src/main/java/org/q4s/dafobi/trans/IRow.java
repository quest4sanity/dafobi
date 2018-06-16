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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.q4s.dafobi.exception.WrongIndexException;

/**
 * Строка данных, представляет собой набор именовых и нумерованных значений
 * (начиная от нуля). Используя индекс или имя, можно получить доступ к данным.
 * Если при этом мы так же знаем, какого типа там содержатся данные, то можем
 * использовать возвращающий соответствующее значение метод.
 * 
 * @author Q4S
 *
 */
public interface IRow {
	/**
	 * Получение значения по номеру колонки.
	 * 
	 * @param columnNum
	 *            Номер колонки
	 * 
	 * @return Значение, связанные с данным номером и строкой.
	 *
	 */
	public Object get(int columnNum) throws WrongIndexException;

	/**
	 * Получение значения по имени колонки.
	 * 
	 * @param columnName
	 *            Символьное имя колонки
	 * 
	 * @return Значение, связанные с данным именем и строкой.
	 *
	 */
	public Object get(String columnName) throws WrongIndexException;

	public String getString(int columnNum) throws WrongIndexException;

	public String getString(String columnName) throws WrongIndexException;

	public Long getInteger(int columnNum) throws WrongIndexException;

	public Long getInteger(String columnName) throws WrongIndexException;

	public BigDecimal getDecimal(int columnNum) throws WrongIndexException;

	public BigDecimal getDecimal(String columnName) throws WrongIndexException;

	public Date getDate(int columnNum) throws WrongIndexException;

	public Date getDate(String columnName) throws WrongIndexException;

	public Time getTime(int columnNum) throws WrongIndexException;

	public Time getTime(String columnName) throws WrongIndexException;

	public Timestamp getTimestamp(int columnNum) throws WrongIndexException;

	public Timestamp getTimestamp(String columnName) throws WrongIndexException;

}
