package org.q4s.dafobi.trans;

import java.sql.Statement;
import java.util.Map;

import org.q4s.dafobi.exception.TransactionException;

/**
 * Оператор, получающий строки данных из базы или любого другого хранилища.
 * <p>
 * Оператор может содержать в тексте именованные параметры. Входные параметры
 * обозначаются как <i>:имя_параметра</i>. Выходные параметры (куда будут
 * помещаться результаты обработки запроса) обозначаются как
 * <i>&имя_параметра</i>.
 * 
 * @author Q4S
 * 
 */
public interface IStatement extends AutoCloseable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close();

	/**
	 * Executes the statement, which must be a query.
	 * 
	 * @return the query results
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 */
	public IResultTable query();

	public IResultTable query(final Map<String, DataParam> parameters);

	/**
	 * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
	 * statement; or an SQL statement that returns nothing, such as a DDL
	 * statement.
	 * 
	 * @return number of rows affected
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 */
	public int executeUpdate();

	public int executeUpdate(final Map<String, DataParam> parameters);

	/**
	 * Executes the statement.
	 * 
	 * @return true if the first result is a {@link ResultTable}
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 */
	public boolean execute();

	public boolean execute(final Map<String, DataParam> parameters);

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws TransactionException
	 *             if something went wrong
	 */
	public void addBatch();

	public void addBatch(final Map<String, DataParam> parameters);

	/**
	 * Executes all of the batched statements.
	 * 
	 * See {@link Statement#executeBatch()} for details.
	 * 
	 * @return update counts for each statement
	 * 
	 * @throws TransactionException
	 *             if something went wrong
	 */
	public int[] executeBatch();

	/**
	 * Возвращает набор имен параметров, которые ожидает запрос. В этот набор
	 * входят как входные, так и выходные параметры.
	 * 
	 * @return Имена параметров запроса.
	 */
	public String[] getParamNames();

	/**
	 * Возвращает набор имен выходных параметров, ожидаемых запросом. Поскольку
	 * возврат выходных параметров актуален только для случая хранимых процедур,
	 * то в случае SQL запросов метод не возвращает ничего (все параметры
	 * трактуются как входные).
	 * 
	 * @return Имена параметров запроса.
	 */
	public String[] getOutParamNames();

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * 
	 * @param value
	 *            parameter value
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 */
	public void setParam(String name, DataParam value);

	public Object getParam(String name, DataType type);

}
