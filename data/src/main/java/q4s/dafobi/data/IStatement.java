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

import java.sql.Statement;
import java.util.Map;

import q4s.dafobi.common.DataParam;
import q4s.dafobi.exception.DataException;
import q4s.dafobi.jaxb.common.DataType;

/**
 * Оператор, получающий строки данных из базы или любого другого хранилища.
 * <p>
 * Оператор может содержать в тексте именованные параметры. Входные параметры
 * обозначаются как <code>:имя_параметра</code>. Выходные параметры (куда будут
 * помещаться результаты обработки запроса) обозначаются как
 * <code>&имя_параметра</code>.
 * 
 * @author Q4S
 * 
 */
public interface IStatement extends AutoCloseable {

	/**
	 * Выполнить оператор, который возвращает таблицу с данными. В случае JDBC
	 * то может быть как запрос SELECT так и вызов процедуры, возвращающей
	 * курсор. В случае же создания альтернативные реализации интерфейса, этот
	 * метод должен всегда проверять, что зпрос возвращает таблицу с данными.
	 * <p>
	 * Если данные возвращаются с помощью процедуры, то такая процедура не может
	 * возвращать выходные параметры. Применение их маркеров приведет к ошибке.
	 * 
	 * @return the query results
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public IResultTable query();

	/**
	 * Сокращенная версия для кода:
	 * 
	 * <pre>
	 * stmt.setParam("par_name", DataType.STRING.param("Value")); // например
	 * ...
	 * int count = stmt.query();
	 * ...
	 * </pre>
	 * 
	 * @see IStatement#query()
	 * 
	 * @param parameters
	 *            Карта входных параметров.
	 * 
	 * @return the query results
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public IResultTable query(final Map<String, DataParam> parameters);

	/**
	 * Выполнить оператор, который не возвращает таблицу с данными. В случае
	 * JDBC это могут быть DDL запросы или вызов процедуры, не возвращающей
	 * курсор. Однако, взамен такая процедура может возвращать данные через
	 * выходные параметры. Такие параметры маркируются с помощью знака '&'.
	 * Пример кода:
	 * 
	 * <pre>
	 * {call owner.proc_name(&out_par, :in_par1, :in_par2)}
	 * </pre>
	 * 
	 * Так же возможен вызов функций. В этом случае синтакс будет таким:
	 * 
	 * <pre>
	 * {&out_par = call owner.func_name(:in_par1, :in_par2)}
	 * </pre>
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statements or (2) 0 for statements that return nothing or
	 *         procedures and functions.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public int execute();

	/**
	 * Сокращенная версия для кода:
	 * 
	 * <pre>
	 * stmt.setParam("par_name", DataType.STRING.param("Value")); // например
	 * ...
	 * int count = stmt.execute();
	 * ...
	 * </pre>
	 * 
	 * @see IStatement#execute()
	 * 
	 * @param parameters
	 *            Карта параметров. Если для оператора определены выходные
	 *            параметры, то они будут трактованы на входные-выходные
	 *            (INOUT). Если такая трактовка не верна, то следует
	 *            воспользоваться методом {@link #execute()}.
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML)
	 *         statements or (2) 0 for statements that return nothing or
	 *         procedures and functions.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public int execute(final Map<String, DataParam> parameters);

	/**
	 * Adds the current set of parameters as a batch entry.
	 * 
	 * @throws DataException
	 *             if something went wrong
	 */
	public void addBatch();

	/**
	 * Сокращенная версия для кода:
	 * 
	 * <pre>
	 * stmt.setParam("par_name", DataType.STRING.param("Value")); // например
	 * ...
	 * int count = stmt.addBatch();
	 * ...
	 * </pre>
	 * 
	 * @param parameters
	 *            Карта параметров. Если для оператора определены выходные
	 *            параметры, то они будут трактованы на входные-выходные
	 *            (INOUT). Если такая трактовка не верна, то следует
	 *            воспользоваться методом {@link #addBatch()}.
	 * 
	 * @throws DataException
	 *             if something went wrong
	 */
	public void addBatch(final Map<String, DataParam> parameters);

	/**
	 * Executes all of the batched statements.
	 * 
	 * See {@link Statement#executeBatch()} for details.
	 * 
	 * @return update counts for each statement or 0s if thouse are procedure
	 *         calls.
	 * 
	 * @throws DataException
	 *             if something went wrong
	 */
	public int[] executeBatch();

	/**
	 * Возвращает набор имен параметров, ожидаемых запросом. В этот набор входят
	 * все параметры - как входные, так и выходные параметры.
	 * 
	 * @return Имена параметров запроса. Все имена содержатся в нижнем регистре.
	 */
	public String[] getParamNames();

	/**
	 * Возвращает набор имен выходных параметров, ожидаемых запросом. Поскольку
	 * возврат выходных параметров актуален только в случае хранимых процедур,
	 * то в случае запросов получения табличных данных (вроде SQL запросов) или
	 * запросов DDL метод не возвращает ничего (все параметры трактуются как
	 * входные).
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
	 * @param type
	 *            parameter type
	 * 
	 * @param value
	 *            parameter value
	 * 
	 * @throws DataException
	 *             if an error occurred
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 */
	public void setParam(String name, DataType type, Object value);

	/**
	 * Sets a parameter.
	 * 
	 * @param name
	 *            parameter name
	 * 
	 * @param value
	 *            parameter value
	 * 
	 * @throws DataException
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

}
