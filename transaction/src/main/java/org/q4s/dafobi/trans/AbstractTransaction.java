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

/**
 * 
 * @author Q4S
 */
public abstract class AbstractTransaction implements ITransaction {

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	int count = stmt.executeUpdate(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Количество затронутых запросом строк.
	 */
	@Override
	public final int executeUpdate(final String statement,
			final Map<String, DataParam> parameters) {
		int count;
		
		try (IStatement stmt = prepare(statement);) {
			count = stmt.executeUpdate(parameters);
		}
		return count;
	}

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	boolean rc = stmt.execute(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Количество затронутых запросом строк.
	 */
	@Override
	public final boolean execute(final String statement,
			final Map<String, DataParam> parameters) {
		boolean rc;
		try (IStatement stmt = prepare(statement);) {
			rc = stmt.execute(parameters);
		}
		return rc;
	}

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	Iterable&lt;IRow&gt; rows = stmt.query(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Итератор по полученным строкам.
	 * 
	 * @deprecated Похоже, здесь могут быть проблемы с освобождением ресурсов.
	 */
	@Override
	public Iterable<IRow> query(final String statement,
			final Map<String, DataParam> parameters) {
		Iterable<IRow> result = null;
		try (IStatement stmt = prepare(statement);) {
			result = stmt.query(parameters);
		}
		return result;
	}

}
