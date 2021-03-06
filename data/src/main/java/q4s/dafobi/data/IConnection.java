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

import java.util.Map;

import q4s.dafobi.common.DataParam;
import q4s.dafobi.exception.DataException;

/**
 * Данный интерфейс представляет методы, предназначенные для запуска операций по
 * обработке данных. Данный интерфейс не привязан непосредственно к базам
 * данных, и может быть как реализацией JDBC доступа, так и какого-нибудь
 * альтернативного механизма (вроде сетевого). Но, внезависимости от реализации,
 * общий подход по работе с данными сводится к следующему. Данные выбираются
 * таблично. Параметры, передаваемые данным имеют символьные имена. Построчный
 * возврат данных тоже происходит через символьные имена колонок. Данные
 * сохраняются в рамках транзакции (если что-то пошло не так, то можно все
 * откатить).
 * 
 * @author Q4S
 *
 */
public interface IConnection extends AutoCloseable {

	/**
	 * Предварительная подготовка запроса. Как правило в нее входит
	 * предобработка и компиляция (в случае необходимости).
	 * 
	 * @param statement
	 *            Текст оператора, который предстоит выполнить.
	 * 
	 * @return Подготовленный к дальнейшей работе оператор.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public IStatement prepare(final String statement);

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	int count = stmt.execute(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @see IStatement#execute(Map)
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнить.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Количество затронутых запросом строк.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public int execute(final String statement, final Map<String, DataParam> parameters);

	/**
	 * Иногда бывает желательно выполнить не один оператор, а сразу несколько
	 * объединенных в один текстовый файл (такой файл называется скриптом).
	 * 
	 * @see IStatement#execute(Map)
	 * 
	 * @param script
	 *            Текст скрипта, который надо будет выполнить. Один скрипт может
	 *            содержать несколько операторов, которые будут выполняться в
	 *            рамках одной транзакции в порядке их следования.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми будут выполняться все
	 *            операторы. Если какой-то из операторов представляет собой
	 *            вызов процедуры с выходными параметрами, то значения выходных
	 *            параметров будут записаны в карту и могут впоследствии быть
	 *            использованы следующими по порядку операторами.
	 * 
	 * @return Количество затронутых каждым из операторов строк. Если оператор
	 *         представляет собой вызов процедуры, то количество равно 0.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public int[] executeScript(final String script, final Map<String, DataParam> parameters);

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * IStatement stmt = transaction.prepare(sql);
	 * IResultTable rt = stmt.query(params);
	 * 	...
	 * </pre>
	 * 
	 * Во избежании утечки ресурсов обязательно закрывайте возвращаемый набор
	 * данных после его обработки. При этом так же будет освобожден породивший
	 * этот набор оператор.
	 * 
	 * @see IStatement#query(Map)
	 * 
	 * @param statement
	 *            Текст оператора, который надо выполнить.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Табличный набор данных.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public IResultTable query(final String statement, final Map<String, DataParam> parameters);

	/**
	 * Часто бывает нужно выполнить запрос, возвращающий только одну строку. В
	 * этом случае нет необходимости писать код, отрабатывающий получение сразу
	 * нескольких строк и освобождающий после этого ресурсы.
	 * <p>
	 * Данный метод всегда возвращает первую строку, которую вернул запрос, а
	 * после этого освобождает все ресурсы.
	 * 
	 * @see IStatement#query(Map)
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнить.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Первая строка, которую вернул запрос. Если оператор не вернул
	 *         ничего, то значение будет null.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public IRow queryRow(final String statement, final Map<String, DataParam> parameters);

	/**
	 * @param flag
	 *            true - приводит к тому, что commit будет делаться после
	 *            каждого оператора; false - приводит к тому, что commit надо
	 *            будет вызывать вручную с помощью метода {@link #commit()}.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public void setAutocommit(boolean flag);

	/**
	 * @return true - если commit делается после каждого оператора; false - если
	 *         commit надо вызывать вручную с помощью метода {@link #commit()}.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public boolean getAutocommit();

	/**
	 * Подтверждение сделанных в рамках транзакции изменений. После этого они
	 * становятся постоянными.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public void commit();

	/**
	 * Отмена всех сделанных в рамках транзакции изменений. Данные возвращаются
	 * к состоянию, в котором они были до начала всех изменений.
	 * 
	 * @throws DataException
	 *             if an error occurred
	 */
	public void rollback();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close();

	/**
	 * @deprecated Назначение оператора пока непонятно.
	 */
	public void getLastError();
}
