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
package q4s.dafobi.data;

import java.util.Iterator;

import q4s.dafobi.jaxb.common.DataType;

/**
 * Особенностью данного класса является то, что при закрытии его командой
 * {@link #close()} так же будет закрыт породивший его оператор
 * {@link IStatement}. Подобное поведение бывает нужно тогда, когда оператор
 * создается где-то в дебрях кода, а на выходе из метода имеются уже готовые
 * данные. Закрыть оператор сразу было нельзя (так как в этом случае данные бы
 * потерялись), а вот после работы с данными закрыть его нужно обязательно
 * (иначе начнут утекать ресурсы). Именно такая ситуация возникает в методе:
 * {@link IConnection#query(String, java.util.Map)}.
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
	public int count() {
		return resultTable.count();
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
