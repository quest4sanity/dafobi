package org.q4s.dafobi.trans;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывает метаинформацию о строке данных. Сюда входят: имена столбцов,
 * типы данных, классы и т.п.
 * 
 * @author Q4S
 */
public abstract class AbstractResultTable implements IResultTable {

	/**
	 * Оператор, вернувший данный набор данных.
	 */
	private final IStatement statement;

	protected AbstractResultTable(IStatement statement) {
		this.statement = statement;
	}

	private class ColumnMetaInfo {
		final String name;
		final DataType type;

		public ColumnMetaInfo(String name, DataType type) {
			this.name = name;
			this.type = type;
		}

	}

	private List<ColumnMetaInfo> columnMetaList = new ArrayList<ColumnMetaInfo>();

	private ColumnMetaInfo[] columnMeta;

	/**
	 * Если значение переменной false, то можно добавлять информацию, но нельзя
	 * ее читать. Если она равна true, то можно читать информацию, но больше
	 * нельзя ее добавлять. Включение защиты метаинформации делает класс
	 * неизменяемым. Причем, вернуть класс обратно к изменяемому состоянию
	 * невозможно.
	 */
	private boolean protect;

	/**
	 * Метод добавляет информацию о колонке данных для строки. Индекс колонки
	 * при этом будет соответствовать порядку добавления информации.
	 * 
	 * @param name
	 *            Имя колонки.
	 * 
	 * @param Type
	 *            Тип колонки
	 */
	protected void addColumnInfo(String name, DataType type) {
		if (protect) {
			// TODO Написать внятное сообщение
			throw new RuntimeException();
		} else {
			columnMetaList.add(new ColumnMetaInfo(name, type));
		}

	}

	/**
	 * Метод делает класс неизменяемым (на случай шаловливых ручек).
	 */
	protected void protect() {
		// TODO Написать внятное сообщение
		if (protect) {
			throw new RuntimeException();
		} else {
			columnMeta = columnMetaList.toArray(new ColumnMetaInfo[0]);
			columnMetaList = null;
			protect = true;
		}
	}

	protected boolean isProtected() {
		return protect;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Реализовать метод
		if (!protect) {
			throw new RuntimeException();
		}
		return columnMeta.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnIndex(java.lang.String)
	 */
	@Override
	public int getColumnIndex(String name) {
		// TODO Реализовать метод
		if (!protect) {
			throw new RuntimeException();
		}
		for (int i = 0; i < columnMeta.length; i++) {
			if (columnMeta[i].equals(name)) {
				return i;
			}
		}
		throw new IllegalArgumentException(new StringBuilder(
				"Неверное имя колонки запроса: ").append(name).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnName(int)
	 */
	@Override
	public String getColumnName(int index) {
		// TODO Реализовать метод
		if (!protect) {
			throw new RuntimeException();
		}
		return columnMeta[index].name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnType(int)
	 */
	@Override
	public DataType getColumnType(int index) {
		// TODO Реализовать метод
		if (!protect) {
			throw new RuntimeException();
		}
		return columnMeta[index].type;
	}

	/**
	 * @return Оператор, вернувший данный набор данных.
	 */
	public IStatement getStatement() {
		return statement;
	}

}
