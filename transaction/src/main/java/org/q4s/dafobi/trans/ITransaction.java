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
			final Map<String, Object> parameters);

	/**
	 * @deprecated Похоже, не удастся реализовать очистку ресурсов.
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public boolean execute(final String statement,
			final Map<String, Object> parameters);

	/**
	 * @deprecated Похоже, не удастся реализовать очистку ресурсов.
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Iterable<IRow> query(final String statement,
			final Map<String, Object> parameters);

	public void setAutocommit(boolean flag);

	public boolean getAutocommit();

	public void commit();

	public void rollback();

	public void getLastError();
}
