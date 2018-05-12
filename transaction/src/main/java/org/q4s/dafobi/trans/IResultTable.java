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
