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

import java.util.Iterator;

/**
 * Особенностью данного класса является то, что при закрытии его командой
 * {@link #close()} так же будет закрыт породивший его оператор. Подобное
 * поведение бывает нужно тогда, когда оператор создается где-то в дебрях кода,
 * а на выходе получаются уже готовые данные. Закрыть сразу его нельзя (данные
 * будут потеряны), но после работы с данными закрыть нужно (иначе утекут
 * ресурсы). Именно такая ситуация возникает в методе:
 * {@link ITransaction#query(String, java.util.Map)}.
 * 
 * @author Q4S
 *
 */
public class AutoclosableResultTable implements IResultTable {

	private final IResultTable resultTable;

	/**
	 * @see AutoclosableResultTable
	 * 
	 * @param resultTable
	 */
	public AutoclosableResultTable(IResultTable resultTable) {
		super();
		this.resultTable = resultTable;
	}

	@Override
	public void close() {
		resultTable.close();
		resultTable.getStatement().close();
	}

	@Override
	public IStatement getStatement() {
		return resultTable.getStatement();
	}

	@Override
	public Iterator<IRow> iterator() {
		return resultTable.iterator();
	}

	@Override
	public boolean isClosed() {
		return resultTable.isClosed();
	}

	@Override
	public int getCount() {
		return resultTable.getCount();
	}

	@Override
	public String getColumnName(int index) {
		return resultTable.getColumnName(index);
	}

	@Override
	public int getColumnIndex(String name) {
		return getColumnIndex(name);
	}

	@Override
	public DataType getColumnType(int index) {
		return resultTable.getColumnType(index);
	}

}
