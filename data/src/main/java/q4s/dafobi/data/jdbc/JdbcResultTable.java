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
package q4s.dafobi.data.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import q4s.dafobi.data.AbstractResultTable;
import q4s.dafobi.data.IRow;
import q4s.dafobi.data.IStatement;
import q4s.dafobi.data.RowImpl;
import q4s.dafobi.jaxb.common.DataType;

/**
 * @author Q4S
 * 
 */
public class JdbcResultTable extends AbstractResultTable {

	private final ResultSet resultSet;

	public JdbcResultTable(IStatement statement, ResultSet resultSet) {
		super(statement);

		this.resultSet = resultSet;

		// Получим метаинформацию о строке данных и зафиксируем ее
		try {
			ResultSetMetaData meta = resultSet.getMetaData();
			int count = meta.getColumnCount();
			for (int i = 1; i <= count; i++) {
				// TODO Надо тщательно проверить, как этот код работает
				addColumnInfo(meta.getColumnName(i), DataType.valueOf(meta.getColumnType(i)));
			}
			protect();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() {
		try {
			resultSet.close();

		} catch (SQLException e) {
			new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.IResultTable#isClosed()
	 */
	@Override
	public boolean isClosed() {
		try {
			return resultSet.isClosed();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<IRow> iterator() {
		return this.new RowIterator();
	}

	/**
	 * Данный итератор реализует выборку данных из {@link ResultSet}. Поскольку
	 * {@link ResultSet} не поддерживает упреждающую выборку, то данный итератор
	 * предполагает, что метод {@link Iterator#hasNext()} алгоритмически всегда
	 * выполняется перед методом {@link Iterator#next()}, и последний никогда не
	 * выполняется, если первый вернул false. В случае читтерских попыток вызова
	 * медода {@link Iterator#next()} минуя вызов {@link Iterator#hasNext()}
	 * будет сгененирована исключительная ситуация.
	 * 
	 * @author Q4S
	 * 
	 */
	private class RowIterator implements Iterator<IRow> {
		/**
		 * Возвращает следующую строку из потока данных.
		 * 
		 * @return Сформированная строка данных, если таковая нашлась. <br>
		 *         Если больше строк нет, то null.
		 */
		private IRow getNextRow() {
			try {
				ResultSet resultSet = JdbcResultTable.this.resultSet;

				boolean rc = resultSet.next();
				if (rc) {
					int count = count();
					Object[] values = new Object[count];
					for (int i = 0; i < count; i++) {
						values[i] = resultSet.getObject(i + 1);
					}
					IRow newRow = new RowImpl(JdbcResultTable.this, values);
					return newRow;

				} else {
					return null;
				}

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Закешированное значение строки.
		 */
		private IRow nextRow;

		@Override
		public boolean hasNext() {
			nextRow = getNextRow();
			return nextRow != null;
		}

		@Override
		public IRow next() {
			// protection against cheating
			if (nextRow == null) {
				throw new UnsupportedOperationException(
						"Method next() must be preceded by a method hasNext() that must return true.");
			} else {
				IRow nextRow = this.nextRow;
				this.nextRow = null;
				return nextRow;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Method remove() can't be used in this kind of iterator");
		}
	}

}
