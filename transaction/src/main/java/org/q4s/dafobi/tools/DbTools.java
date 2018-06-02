package org.q4s.dafobi.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.ITransaction;

import com.borlas.bus.kernel.parameters.VariablesStorage;

/**
 * <p>
 * Сервис для работы с базой данных. Он содержит методы, уменьшающие количество
 * строк кода, необходимых для выполнения программистских рутинных функций по
 * обмену с базой.
 * </p>
 * 
 * @author vbogdanov
 * 
 */
public class DbTools {

	/**
	 * Это просто библиотека методов, и не более того.
	 */
	private DbTools() {
	}

	/**
	 * <p>
	 * Выполнение простого SQL оператора, который не возвращает никаких
	 * значений.
	 * </p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param operator
	 *            - Текст запроса.
	 * 
	 * @param arguments
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runOperator(Connection connection, String operator)
			throws SQLException {

		VariablesStorage arguments = new VariablesStorage();
		runOperator(connection, operator, arguments);
	}

	/**
	 * <p>
	 * Выполнение простого SQL оператора, который не возвращает никаких
	 * значений.
	 * </p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param operator
	 *            - Текст запроса.
	 * 
	 * @param arguments
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runOperator(ITransaction transaction,
			String operator) throws SQLException {
		runOperator(transaction.getConnection(), operator);
	}

	/**
	 * <p>
	 * Выполнение простого SQL оператора, который не возвращает никаких
	 * значений.
	 * </p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param operator
	 *            - Текст запроса.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runOperator(Connection connection,
			String operator, VariablesStorage parameters) throws SQLException {

		NamedPreparedStatement statement = null;
		try {
			// - Получим подключение
			// - Подготовим запрос,
			// - Установим параметры
			// - Выполним его
			statement = new NamedPreparedStatement(connection, operator);
			for (String arg : statement.getParamNames()) {
				statement.setParam(arg, parameters.get(arg));
			}
			statement.execute();

		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	/**
	 * <p>
	 * Выполнение простого SQL оператора, который не возвращает никаких
	 * значений.
	 * </p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param operator
	 *            - Текст запроса.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runOperator(ITransaction transaction,
			String operator, VariablesStorage parameters) throws SQLException {
		runOperator(transaction.getConnection(), operator, parameters);
	}

	/**
	 * <p>
	 * Выполнение пакета из нескольких SQL операторов, ни один из которых не
	 * возвращает никаких значений.
	 * </p>
	 * <p>
	 * Операторы должны быть разделены строкой, состоящей только из символа '/'.
	 * </p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param manyOperators
	 *            - Текст, состоящий из нескольких запросов.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runManyOperators(Connection connection,
			String manyOperators) throws SQLException {

		VariablesStorage arguments = new VariablesStorage();
		runManyOperators(connection, manyOperators, arguments);
	}

	/**
	 * <p>
	 * Выполнение пакета из нескольких SQL операторов, ни один из которых не
	 * возвращает никаких значений.
	 * </p>
	 * <p>
	 * Операторы должны быть разделены строкой, состоящей только из символа '/'.
	 * </p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param manyOperators
	 *            - Текст, состоящий из нескольких запросов.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runManyOperators(ITransaction transaction,
			String manyOperators) throws SQLException {

		runManyOperators(transaction.getConnection(), manyOperators);
	}

	/**
	 * <p>
	 * Выполнение пакета из нескольких SQL операторов, ни один из которых не
	 * возвращает никаких значений.
	 * </p>
	 * <p>
	 * Операторы должны быть разделены строкой, состоящей только из символа '/'.
	 * </p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param manyOperators
	 *            - Текст, состоящий из нескольких запросов.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runManyOperators(Connection connection,
			String manyOperators, VariablesStorage parameters)
			throws SQLException {

		String deviderStr = "\\n[\\s]*\\/[\\s]*\\n";
		Pattern p = Pattern.compile(deviderStr, Pattern.DOTALL
				| Pattern.UNIX_LINES | Pattern.UNICODE_CASE);
		String[] operators = p.split(manyOperators + "\n");

		for (String operator : operators) {
			runOperator(connection, operator, parameters);
		}
	}

	/**
	 * <p>
	 * Выполнение пакета из нескольких SQL операторов, ни один из которых не
	 * возвращает никаких значений.
	 * </p>
	 * <p>
	 * Операторы должны быть разделены строкой, состоящей только из символа '/'.
	 * </p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param manyOperators
	 *            - Текст, состоящий из нескольких запросов.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runManyOperators(ITransaction transaction,
			String manyOperators, VariablesStorage parameters)
			throws SQLException {

		runManyOperators(transaction.getConnection(), manyOperators, parameters);
	}

	/**
	 * <p>
	 * Выполнение простого SQL запроса с возвратом списка строк.
	 * </p>
	 * <p>
	 * Всвязи с тем, что данная функция возвращает строки в виде списка хешей
	 * строк, не рекомендуется использовать ее для работы с большим количеством
	 * строк.
	 * <p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param queryOperator
	 *            - Текст запроса.
	 * 
	 * @return Список полученных строк.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final List<VariablesStorage> runSelect(Connection connection,
			String queryOperator) throws SQLException {

		VariablesStorage parameters = new VariablesStorage();
		return runSelect(connection, queryOperator, parameters);
	}

	/**
	 * <p>
	 * Выполнение простого SQL запроса с возвратом списка строк.
	 * </p>
	 * <p>
	 * Всвязи с тем, что данная функция возвращает строки в виде списка хешей
	 * строк, не рекомендуется использовать ее для работы с большим количеством
	 * строк.
	 * <p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param queryOperator
	 *            - Текст запроса.
	 * 
	 * @return Список полученных строк.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final List<VariablesStorage> runSelect(
			ITransaction transaction, String queryOperator) throws SQLException {

		return runSelect(transaction.getConnection(), queryOperator);
	}

	/**
	 * <p>
	 * Выполнение простого SQL запроса с возвратом списка строк.
	 * </p>
	 * <p>
	 * Всвязи с тем, что данная функция возвращает строки в виде списка хешей
	 * строк, не рекомендуется использовать ее для работы с большим количеством
	 * строк.
	 * <p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param queryOperator
	 *            - Текст запроса.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @return Список полученных строк.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final List<VariablesStorage> runSelect(Connection connection,
			String queryOperator, VariablesStorage parameters)
			throws SQLException {

		NamedPreparedStatement statement = null;
		ResultSet rs = null;
		List<VariablesStorage> rows = new ArrayList<VariablesStorage>();
		try {
			// - Получим подключение
			// - Подготовим запрос,
			// - Установим параметры
			// - Выполним его
			statement = new NamedPreparedStatement(connection, queryOperator);
			for (String arg : statement.getParamNames()) {
				statement.setParam(arg, parameters.get(arg));
			}
			rs = statement.executeQuery();

			// - Соберем метаданные
			// - Сохраним число столбцов
			// - Сохраним названия столбцов
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			String[] propertyNames = new String[numberOfColumns];
			int[] propertyTypes = new int[numberOfColumns];
			for (int i = 0; i < numberOfColumns; i++) {
				propertyNames[i] = rsMetaData.getColumnName(i + 1);
				propertyTypes[i] = rsMetaData.getColumnType(i + 1);
			}

			// Считаем возвращаемые запросом строки в список.
			VariablesStorage rowMap;
			while (rs.next()) {
				rowMap = new VariablesStorage();
				for (int i = 0; i < numberOfColumns; i++) {
					Object value = rs.getObject(i + 1);
					// С клобами работает по особому
					if (propertyTypes[i] == java.sql.Types.CLOB) {
						rowMap.put(propertyNames[i],
								BusClob.getString((java.sql.Clob) value));
					} else {
						rowMap.put(propertyNames[i], value);
					}
				}
				rows.add(rowMap);
			}

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
		}

		// Возвращаем считанные из базы строки.
		return rows;
	}

	/**
	 * <p>
	 * Выполнение простого SQL запроса с возвратом списка строк.
	 * </p>
	 * <p>
	 * Всвязи с тем, что данная функция возвращает строки в виде списка хешей
	 * строк, не рекомендуется использовать ее для работы с большим количеством
	 * строк.
	 * <p>
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param queryOperator
	 *            - Текст запроса.
	 * 
	 * @param parameters
	 *            - Аргументы для запроса.
	 * 
	 * @return Список полученных строк.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final List<VariablesStorage> runSelect(
			ITransaction transaction, String queryOperator,
			VariablesStorage parameters) throws SQLException {

		return runSelect(transaction.getConnection(), queryOperator, parameters);
	}

	/**
	 * <p>
	 * Выполнение хранимой процедуры с возможным возвращением значений.
	 * </p>
	 * 
	 * @param connection
	 *            - Подключение к базе данных.
	 * 
	 * @param operator
	 *            - Текст запроса.
	 * 
	 * @param parameters
	 *            - Входные и выходные параметры для запроса. <i>Внимание!!!
	 *            Обязательно должны быть заполнены параметры для выходных
	 *            значений (пусть даже и фиктивными значениями). Их тип
	 *            требуется для работы выходных параметров.</i>
	 * 
	 * @return Выходные значения
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runCallable(Connection connection,
			String operator, VariablesStorage parameters) throws SQLException {

		NamedPreparedStatement statement = null;
		try {
			// - Получим подключение
			// - Подготовим запрос,
			// - Установим параметры
			// - Выполним его
			statement = new NamedPreparedStatement(connection, operator);
			for (String arg : statement.getParamNames()) {
				statement.setParam(arg, parameters.get(arg));
			}

			for (String arg : statement.getOutParamNames()) {
				int jdbcType = DataType.toJdbcType(DataType
						.valueOf(parameters.get(arg).getClass()));
				statement.registerOutParameter(arg, jdbcType);
			}

			statement.executeUpdate();

			for (String arg : statement.getOutParamNames()) {
				parameters.put(arg, statement.getParam(arg));
			}

		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}

	/**
	 * @see #runCallable(Connection, String, VariablesStorage)
	 * 
	 * @param transaction
	 *            - Транзакция, в которой происходит работа.
	 * 
	 * @param operator
	 *            - Выполняемый оператор.
	 * 
	 * @param parameters
	 *            - Параметры, передаваемые оператору.
	 * 
	 * @throws SQLException
	 *             В случае ошибок при работе с базой.
	 */
	public static final void runCallable(ITransaction transaction,
			String operator, VariablesStorage parameters) throws SQLException {
		runCallable(transaction.getConnection(), operator, parameters);
	}

	/**
	 * <p>
	 * Превращает имя колонки в имя свойства. Вспомогательная функция.
	 * </p>
	 * 
	 * @param columnName
	 *            Имя колонки таблицы или алиаса запроса.
	 * 
	 * @return Имя свойства в венгерской нотации.
	 */
	public static final String column2property(final String columnName) {
		String[] parts = columnName.toLowerCase().split("_");
		String property = "";
		for (int i = 0; i < parts.length; i++) {
			if (i == 0) {
				property = parts[i];
			} else {
				String word = parts[i];
				property += word.substring(0, 1).toUpperCase()
						+ word.substring(1);
			}
		}
		return property;
	}

	/**
	 * <p>
	 * Превращает имя колонки в имя класа. Вспомогательная функция.
	 * </p>
	 * 
	 * @param columnName
	 *            Имя колонки таблицы или алиаса запроса.
	 * 
	 * @return Имя класса в венгерской нотации.
	 */
	public static final String column2class(final String columnName) {
		String[] parts = columnName.toLowerCase().split("_");
		String property = "";
		for (int i = 0; i < parts.length; i++) {
			String word = parts[i];
			property += word.substring(0, 1).toUpperCase() + word.substring(1);
		}
		return property;
	}
}