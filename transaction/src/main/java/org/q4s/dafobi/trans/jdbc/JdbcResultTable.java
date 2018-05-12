package org.q4s.dafobi.trans.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;

import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IRow;
import org.q4s.dafobi.trans.AbstractResultTable;
import org.q4s.dafobi.trans.IStatement;
import org.q4s.dafobi.trans.RowImpl;

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
				addColumnInfo(meta.getColumnName(i),
						DataType.typeOf(meta.getColumnType(i)));
			}
			protect();

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
	 * выполняется перед {@link Iterator#next()}, и последний никогда не
	 * выполняется, если первый вернул false. В случае читтерских попыток вызова
	 * медода {@link Iterator#next()} будет сгененирована исключительная
	 * ситуация.
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
					int count = getCount();
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
				throw new RuntimeException(
						"Method next() must be preceded by a method hasNext() "
								+ "that must return true.");
			} else {
				IRow nextRow = this.nextRow;
				this.nextRow = null;
				return nextRow;
			}
		}

		@Override
		public void remove() {
			throw new RuntimeException(
					"Method remove() can't be used in this kind of iterator");
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
	 * @see org.q4s.dafobi.trans.IResultTable#isClosed()
	 */
	@Override
	public boolean isClosed() {
		try {
			return resultSet.isClosed();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
