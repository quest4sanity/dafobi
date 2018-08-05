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
import java.util.regex.Pattern;

import org.q4s.dafobi.common.DataParam;

/**
 * Данный класс реализует общую часть функционала, которую должна реализовывать
 * транзакция.
 * 
 * @author Q4S
 */
public abstract class AbstractTransaction implements ITransaction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#execute(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public final int execute(final String statement, final Map<String, DataParam> parameters) {
		int count;

		try (IStatement stmt = prepare(statement);) {
			count = stmt.execute(parameters);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#executeScript(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public final int[] executeScript(String script, Map<String, DataParam> parameters) {
		String[] operators = parseScript(script);
		int[] rc = new int[operators.length];

		int i = 0;
		for (String operator : operators) {
			rc[i++] = execute(operator, parameters);
		}
		return rc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#query(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public final IResultTable query(final String statement, final Map<String, DataParam> parameters) {
		IResultTable result = null;
		try (IStatement stmt = prepare(statement);) {
			result = new AutoclosableResultTable(stmt.query(parameters));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#queryRow(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public final IRow queryRow(final String statement, final Map<String, DataParam> parameters) {
		try (IResultTable result = query(statement, parameters)) {
			for (IRow row : result) {
				return row;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#close()
	 */
	@Override
	public void close() {
		// Изменения должны фиксировать явно с помощью оператора commit
		// кроме случаев, когда установлен флаг autocommit.
		if (getAutocommit()) {
			commit();
		} else {
			rollback();
		}
	}

	/**
	 * Метод берет текст скрипта и разбирает его на операторы. Операторы могут
	 * представлять собой как опеаторы DDL, так и вызовы процедур.
	 * 
	 * @param script
	 *            Текст скрипта из нескольких операторов.
	 * 
	 * @return Список текстов операторов, каждый из которых может быть выполнен
	 *         методом {@link #execute(String, Map)}.
	 */
	public String[] parseScript(String script) {
		// Ниже реализован самый примитивный подход - разделителем
		// операторов считается строка, состоящая только из одного
		// символа '/'. При этом не учитывается, что она может находиться
		// в стоковой константе или в комментарии.
		String deviderStr = "\\n[\\s]*\\/[\\s]*\\n";
		Pattern p = Pattern.compile(deviderStr, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.UNICODE_CASE);
		String[] operators = p.split(script + "\n");
		return operators;
	}
}
