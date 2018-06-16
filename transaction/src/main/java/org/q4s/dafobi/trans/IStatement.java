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
	 * @return true - if statement is closed
	 */
	public boolean isClosed();

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
	 * Executes the statement.
	 * 
	 * @return true if the first result is a {@link ResultTable}
	 * 
	 * @throws TransactionException
	 *             if an error occurred
	 */
	public int execute();

	/**
	 * Executes the statement.
	 * 
	 * @param parameters
	 *            Карта параметров. Если для оператора определены выходные
	 *            параметры, то они будут трактованы на входные-выходные
	 *            (INOUT). Если такая трактовка не верна, то следует
	 *            воспользоваться методом {@link #execute()}.
	 * 
	 * @return
	 */
	public int execute(final Map<String, DataParam> parameters);

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws TransactionException
	 *             if something went wrong
	 */
	public void addBatch();

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @param parameters
	 *            Карта параметров. Если для оператора определены выходные
	 *            параметры, то они будут трактованы на входные-выходные
	 *            (INOUT). Если такая трактовка не верна, то следует
	 *            воспользоваться методом {@link #addBatch()}.
	 * 
	 * @return
	 */
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
	 * @return Имена параметров запроса. Все имена содержатся в нижнем регистре.
	 */
	public String[] getParamNames();

	/**
	 * Возвращает набор имен выходных параметров, ожидаемых запросом. Поскольку
	 * возврат выходных параметров актуален только для случая хранимых процедур,
	 * то в случае запросов получения табличных данных (вроде SQL запросов)
	 * метод не возвращает ничего (все параметры трактуются как входные).
	 * 
	 * @return Имена параметров запроса. Все имена содержатся в нижнем регистре.
	 *         Если запрос не поддерживает выходных параметров, то возвращается
	 *         пустой массив.
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

	/**
	 * Получение значения параметра, приведенное к указанному типу.
	 * 
	 * @param name
	 *            parameter name
	 * 
	 * @param type
	 *            expected parameter type
	 * 
	 * @return parameter value
	 */
	public Object getParam(String name, DataType type);

}
