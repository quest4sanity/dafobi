package org.q4s.dafobi.trans;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

import org.q4s.dafobi.exception.TransactionException;

/**
 * Оператор, получающий строки данных из базы или любого другого хранилища.
 * <p>
 * Оператор может содержать в тексте именованные параметры. Входящие параметры
 * обозначаются как <i>:имя_параметра</i>. Выходящие параметры (куда будут
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

	public IResultTable query(final Map<String, Object> parameters);

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

	public int executeUpdate(final Map<String, Object> parameters);

	/**
	 * Executes the statement.
	 * 
	 * @return true if the first result is a {@link ResultTable}
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 */
	public boolean execute();

	public boolean execute(final Map<String, Object> parameters);

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws TransactionException
	 *             if something went wrong
	 */
	public void addBatch();

	public void addBatch(final Map<String, Object> parameters);

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
	 * входят как входящие, так и выходные параметры.
	 * 
	 * @return Имена параметров запроса.
	 */
	public Set<String> getParameters();

	/**
	 * Возвращает набор имен выходных параметров, ожидаемых запросом. Поскольку
	 * возврат выходных параметров актуален только для случая хранимых процедур,
	 * то в случае SQL запросов метод не возвращает ничего (все параметры
	 * трактуются как входные).
	 * 
	 * @return Имена параметров запроса.
	 */
	public Set<String> getOutParameters();

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
	public void setObject(String name, Object value);

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * 
	 * @param value
	 *            parameter value
	 * 
	 * @param sqlType
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 * 
	 * @see PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(String name, Object value, int sqlType);

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
	public void setString(String name, String value);

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
	public void setInt(String name, Integer value);

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
	public void setLong(String name, Long value);

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
	public void setDate(String name, Date value);

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
	public void setTime(String name, Time value);

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
	public void setTimestamp(String name, Timestamp value);

	public Object getObject(String name);

}
