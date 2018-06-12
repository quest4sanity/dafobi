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

public interface IResultTable extends Iterable<IRow>, AutoCloseable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close();

	public boolean isClosed();

	/**
	 * @return Число колонок в таблице
	 */
	public int getCount();

	/**
	 * Возвращает имя колонки (например, алиас колонки в запросе).
	 * 
	 * @param index
	 *            Индекс колонки.
	 * 
	 * @return Имя колонки.
	 */
	public String getColumnName(int index);

	/**
	 * Возвращает индекс колонки по ее имени.
	 * 
	 * @param name
	 *            Имя колонки (например, алиас колонки в запросе)
	 * 
	 * @return Индекс колонки.
	 */
	public int getColumnIndex(String name);

	/**
	 * Возвращает тип данных колонки. 
	 * 
	 * @param index Индекс колонки. 
	 * 
	 * @return
	 */
	public DataType getColumnType(int index);
}
