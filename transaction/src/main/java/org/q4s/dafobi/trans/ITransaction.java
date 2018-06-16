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

import java.util.Map;

public interface ITransaction extends AutoCloseable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close();

	/**
	 * Предварительная подготовка запроса. Как правило в нее входит
	 * предобработка и компиляция его (в случае необходимости).
	 * 
	 * @param statement
	 *            Текст оператора, который предстоит выполнить.
	 * 
	 * @return Подготовленный к дальнейшей работе оператор.
	 */
	public IStatement prepare(final String statement);

	/**
	 * Выполнение команды без возврата данных. Это может быть оператор DDL или
	 * вызов процедуры. В последнем случае возможно использование выходных
	 * параметров.
	 * 
	 * @param statement
	 *            Текст оператора.
	 * 
	 * @param parameters
	 *            Карта с типированными параметрами.
	 * 
	 * @return Количество строк, которые были обновлены.
	 */
	public int execute(final String statement, final Map<String, DataParam> parameters);

	/**
	 * Выполнение команды с возвратом данных. Это может быть оператор SELECT или
	 * вызов процедуры.
	 * 
	 * @param statement
	 *            Текст оператора.
	 * 
	 * @param parameters
	 *            Карта с типированными параметрами.
	 * 
	 * @return Табличный набор данных.
	 */
	public IResultTable query(final String statement, final Map<String, DataParam> parameters);

	public void setAutocommit(boolean flag);

	public boolean getAutocommit();

	public void commit();

	public void rollback();

	public void getLastError();
}
