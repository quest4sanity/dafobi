/*
 * (C) Copyright 2018 - Vladimir Bogdanov | Data Form Builder
 *
 * https://github.com/quest4sanity/dafobi
 *
 * Licensed under the LGPL, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     /LICENSE.txt or https://www.gnu.org/licenses/lgpl.txt
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

	public int executeUpdate(final String statement,
			final Map<String, DataParam> parameters);

	/**
	 * @deprecated Похоже, не удастся реализовать очистку ресурсов.
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public boolean execute(final String statement,
			final Map<String, DataParam> parameters);

	/**
	 * @deprecated Похоже, не удастся реализовать очистку ресурсов.
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Iterable<IRow> query(final String statement,
			final Map<String, DataParam> parameters);

	public void setAutocommit(boolean flag);

	public boolean getAutocommit();

	public void commit();

	public void rollback();

	public void getLastError();
}
