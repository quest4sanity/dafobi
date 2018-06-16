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
package org.q4s.dafobi.trans.jdbc;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.q4s.dafobi.exception.TransactionException;
import org.q4s.dafobi.trans.AbstractStatement;
import org.q4s.dafobi.trans.DataParam;
import org.q4s.dafobi.trans.DataType;
import org.q4s.dafobi.trans.IResultTable;
import org.q4s.dafobi.trans.IStatement;

/**
 * Класс, реализующий инетрфейс {@link IStatement} для случая, когда работа с
 * базой данных осуществляется через JDBC.
 * 
 * @author Q4S
 * 
 */
public class JdbcStatement extends AbstractStatement {

	private final JdbcTransaction transaction;

	/**
	 * The statement this object is wrapping.
	 */
	private final PreparedStatement statement;

	/**
	 * Maps all parameter names to arrays of ints which are the parameter
	 * indices.
	 */
	private final Map<String, int[]> indexMap;

	/**
	 * Maps out parameter names to arrays of ints which are the parameter
	 * indices.
	 */
	private final Map<String, Integer> inOutParams;

	/**
	 * Запрос, превращенный в форму, пригодную для использования в JDBC.
	 */
	private final String parsedQuery;

	/**
	 * Запрос, из которого в дополнение ко всему удалены комментарии, а так же
	 * сделана другая возможная постобработка.
	 */
	private final String processedQuery;

	private final boolean isCallable;

	/**
	 * @see JdbcStatement
	 * 
	 * @param transaction
	 * @param query
	 */
	public JdbcStatement(final JdbcTransaction transaction, final String query) {
		try {
			this.transaction = transaction;

			indexMap = new TreeMap<String, int[]>();
			inOutParams = new TreeMap<String, Integer>();

			// Разберем запрос и выделим входящие и выходные параметры.
			// Учтем, что в начале вызова процедур может быть любое количетсво
			// переносов строки и пробелов, а JDBC этого не разрешает.
			parsedQuery = parse(query.replaceAll("^\\s+", ""), indexMap, inOutParams);

			// Выполним необязательную постобработку запроса. По-умолчанию она
			// включает в себя только удаление комментариев.
			processedQuery = processQuery(parsedQuery);

			// Процедурные запрос и обычные отрабатываются по разному.
			//
			isCallable = isOperatorCallable(processedQuery);
			if (!isCallable && getOutParamNames().length > 0) {
				// TODO Надо поменять на что-то вменяемое.
				throw new RuntimeException("Возвращаемые параметры можно использовать только в вызываемых операторах.");
			}

			Connection connection = transaction.getConnection();
			if (isCallable) {
				statement = connection.prepareCall(parsedQuery);

			} else {
				statement = connection.prepareStatement(parsedQuery);
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getProcessedQuery());
		}
	}

	/**
	 * Метод выполняется в цикле конструктора оператора. В него можно внести
	 * код, который допиливает запрос под особености драйвера JDBC.
	 * 
	 * @param sourceQuery
	 *            Запрос, в котором все имена параметров заменены на знаки ?
	 * 
	 * @return Запрос, после всех произведенных допиливаний.
	 */
	protected String processQuery(String sourceQuery) {
		return dropComment(sourceQuery).replaceAll("\\s+", " ").trim();
	}

	/**
	 * Метод выполняется в цикле конструктора оператора. В него можно внести
	 * код, который определит, является запрос вызовом процедуры (и могут ли по
	 * нему возвращаться выходные значения) или нет.
	 * 
	 * @param operator
	 *            Текст оператора, на основании которого надо определить,
	 *            является ли он вызовом процедуры или оператором базы данных.
	 * 
	 * @return true - если запрос является вызовом процедуры (или аналогом);
	 *         false - если это обычный запрос к базе.
	 */
	protected boolean isOperatorCallable(String operator) {
		// Запрос автоматически определяется как Callable, если он имеет
		// формат "{call ...}"
		return "{call".equalsIgnoreCase(operator.substring(0, 5));
	}

	/**
	 * Parses a query with named parameters. The parameter-index mappings are
	 * put into the map, and the parsed query is returned. DO NOT CALL FROM
	 * CLIENT CODE. This method is non-private so JUnit code can test it.
	 * 
	 * @param query
	 *            query to parse
	 * 
	 * @param paramMap
	 *            map to hold parameter-index mappings
	 * 
	 * @return the parsed query
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final String parse(final String query, final Map paramMap, final Map<String, Integer> inOutParams) {

		// I was originally using regular expressions, but they didn't work well
		// for ignoring
		// parameter-like strings inside quotes.
		int length = query.length();
		StringBuffer parsedQuery = new StringBuffer(length);
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		boolean inComment = false;
		int index = 1;

		// Информация собирается в LinkedList, а под конец перекладывается
		// в массив, как и заявлено в переменной indexMap.
		for (int i = 0; i < length; i++) {
			char c = query.charAt(i);
			if (inSingleQuote) {
				// В строковой константе параметры не учитываются.
				if (c == '\'') {
					inSingleQuote = false;
				}

			} else if (inDoubleQuote) {
				// В строковой константе параметры не учитываются.
				if (c == '"') {
					inDoubleQuote = false;
				}

			} else if (inComment) {
				// В режиме комментария параметры не учитываются.
				if (c == '*' && i + 1 < length && query.charAt(i + 1) == '/') {
					inComment = false;
					parsedQuery.append(c);
					c = query.charAt(++i);
				}

			} else {
				if (c == '\'') {
					// Начало строки
					inSingleQuote = true;

				} else if (c == '"') {
					// Начало строки
					inDoubleQuote = true;

				} else if (c == '/' && i + 1 < length && query.charAt(i + 1) == '*') {
					// Начало комментария
					inComment = true;
					parsedQuery.append(c);
					c = query.charAt(++i);

				} else if ((c == ':' || c == '&') && i + 1 < length
						&& Character.isJavaIdentifierStart(query.charAt(i + 1))) {
					int j = i + 2;
					while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
						j++;
					}
					String name = query.substring(i + 1, j).toLowerCase();
					i += name.length(); // skip past the end if the parameter

					List indexList = (List) paramMap.get(name);
					if (indexList == null) {
						indexList = new LinkedList();
						paramMap.put(name, indexList);
					}
					indexList.add(new Integer(index));

					// Если это выходной параметр, то добавим его в список.
					if (c == '&') {
						if (inOutParams.containsKey(name)) {
							throw new RuntimeException("Нельзя определять переменную выходной дважды");
						}
						inOutParams.put(name.toLowerCase(), new Integer(index));
					}

					c = '?'; // replace the parameter with a question mark

					index++;
				}
			}
			parsedQuery.append(c);
		}

		// replace the lists of Integer objects with arrays of ints
		for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Map.Entry) itr.next();
			List list = (List) entry.getValue();
			int[] indexes = new int[list.size()];
			int i = 0;
			for (Iterator itr2 = list.iterator(); itr2.hasNext();) {
				Integer x = (Integer) itr2.next();
				indexes[i++] = x.intValue();
			}
			entry.setValue(indexes);
		}

		return parsedQuery.toString();
	}

	/**
	 * Returns the indexes for a name of parameter.
	 * 
	 * @param name
	 *            parameter's name
	 * 
	 * @return parameter's indexes
	 * 
	 * @throws IllegalArgumentException
	 *             if the parameter does not exist
	 */
	protected final int[] getIndexes(String name) {
		int[] indexes = (int[]) indexMap.get(name.toLowerCase());
		if (indexes == null) {
			throw new IllegalArgumentException("Parameter not found: " + name);
		}
		return indexes;
	}

	/**
	 * @return the parsedQuery
	 */
	public final String getParsedQuery() {
		return parsedQuery;
	}

	/**
	 * Метод возвращает запрос, из которого были удалены комментарии, и над
	 * которым могла быть произведена специфичная для драйвера обработка.
	 * 
	 * @return the processedQuery
	 */
	public final String getProcessedQuery() {
		return processedQuery;
	}

	/**
	 * Метод выполняется в цикле конструктора оператора. В него можно внести
	 * код, который определит, является запрос вызовом процедуры (и могут ли по
	 * нему возвращаться выходные значения) или нет.
	 * 
	 * @return true - если запрос является вызовом процедуры (или аналогом);
	 *         false - если это обычный запрос к базе.
	 */
	public final boolean isCallable() {
		return isCallable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getParameters()
	 */
	@Override
	public final String[] getParamNames() {
		return indexMap.keySet().toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getOutParameters()
	 */
	@Override
	public final String[] getOutParamNames() {
		if (statement instanceof CallableStatement) {
			return inOutParams.keySet().toArray(new String[0]);

		} else {
			return new String[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.AbstractStatement#setParam(java.lang.String,
	 * org.q4s.dafobi.trans.DataParam)
	 */
	@Override
	public void setParam(String name, DataParam param) {
		super.setParam(name, param);

		try {
			int[] indexes = getIndexes(name.toLowerCase());
			for (int i = 0; i < indexes.length; i++) {
				Object value = param.getValue();

				switch (param.getType().jdbcType()) {
				case Types.CLOB:
					statement.setClob(indexes[i], value == null ? null : (Clob) value);
					break;

				case Types.VARCHAR:
					statement.setString(indexes[i], value == null ? null : (String) value);
					break;

				case Types.INTEGER:
					statement.setInt(indexes[i], value == null ? null : (Integer) value);
					break;

				case Types.BIGINT:
					statement.setLong(indexes[i], value == null ? null : (Long) value);
					break;

				case Types.NUMERIC:
					statement.setBigDecimal(indexes[i], value == null ? null : (BigDecimal) value);
					break;

				case Types.DATE:
					statement.setDate(indexes[i], value == null ? null : (Date) value);
					break;

				case Types.TIME:
					statement.setTime(indexes[i], value == null ? null : (Time) value);
					break;

				case Types.TIMESTAMP:
					statement.setTimestamp(indexes[i], value == null ? null : (Timestamp) value);
					// С полем Timestamp в Оракле происходит хрень.
					// Купируем баг путем перевода в другой тип.
					// statement.setObject(indexes[i], value == null ? null :
					// new Date(((Timestamp) value).getTime()));
					break;

				default:
					throw new UnsupportedOperationException(new StringBuilder("Тип данных")
							.append(param.getClass().getName()).append(" не поддерживается.").toString());
				}
			}

		} catch (IllegalArgumentException e) {
			// Просто пропускаем случаи, когда параметр устанавливается зря.

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getParam(java.lang.String,
	 * org.q4s.dafobi.trans.DataType)
	 */
	@Override
	public Object getParam(String name, DataType type) {
		Integer index = inOutParams.get(name.toLowerCase());
		if (index != null) {
			CallableStatement callStmt = (CallableStatement) statement;
			Object value;
			try {
				switch (type.jdbcType()) {
				case Types.CLOB:
					value = callStmt.getClob(index);
					break;

				case Types.VARCHAR:
					value = callStmt.getString(index);
					break;

				case Types.INTEGER:
					value = callStmt.getInt(index);
					break;

				case Types.BIGINT:
					value = callStmt.getLong(index);
					break;

				case Types.NUMERIC:
					value = callStmt.getBigDecimal(index);
					break;

				case Types.DATE:
					value = callStmt.getDate(index);
					break;

				case Types.TIME:
					value = callStmt.getTime(index);
					break;

				case Types.TIMESTAMP:
					value = callStmt.getTimestamp(index);
					break;

				default:
					throw new UnsupportedOperationException(new StringBuilder("Тип данных")
							.append(type.getClass().getName()).append(" не поддерживается.").toString());
				}
				return value;

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.AbstractStatement#close()
	 */
	@Override
	public void close() {
		try {
			statement.close();

		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#isClosed()
	 */
	@Override
	public boolean isClosed() {
		try {
			return statement.isClosed();

		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#query()
	 */
	@Override
	public IResultTable query() {
		try {
			if (isCallable()) {
				boolean rc = statement.execute();
				if (statement.getMoreResults()) {
					return new JdbcResultTable(this, statement.getResultSet());
				} else {
					// TODO Надо поменять на что-то вменяемое.
					throw new RuntimeException("Оператор не вернул ResultSet");
				}

			} else {
				return new JdbcResultTable(this, statement.executeQuery());
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getProcessedQuery());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#execute()
	 */
	@Override
	public int execute() {
		try {
			if (isCallable()) {
				statement.execute();
				return 0;
			} else {
				return statement.executeUpdate();
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getProcessedQuery());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#addBatch()
	 */
	@Override
	public void addBatch() {
		try {
			if (isCallable()) {
				// TODO Надо поменять на что-то вменяемое.
				throw new RuntimeException("Вызываемый оператор не может использоваться в пакете");
			} else {
				statement.addBatch();
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getProcessedQuery());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#executeBatch()
	 */
	@Override
	public int[] executeBatch() {
		try {
			if (isCallable()) {
				// TODO Надо поменять на что-то вменяемое.
				throw new RuntimeException("Вызываемый оператор не может использоваться в пакете");
			} else {
				return statement.executeBatch();
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getProcessedQuery());
		}
	}

	/**
	 * <p>
	 * Метод удаляет комментарии вида <tt>/&#42;&#42;/</tt> из текста запроса.
	 * </p>
	 * 
	 * <p>
	 * <b>Внимание!</b> Ввиду того, что в методе используются регулярные
	 * выражения, в случае, если в тексте присутствуют сложные вложенные
	 * комментарии, он может отработать с ошибками. Поэтому настоятельно
	 * рекомендуется во всех файлах, к которым будет применена данная функция
	 * видоизменять символы вложенных комментариев (например экранировать их).
	 * То же касается символов комментариев внутри строковых констант. Их надо
	 * разбивать на подстроки или прятать в функции.
	 * </p>
	 * 
	 * @param query
	 *            Текст, из которого надо удалить комментарии.
	 * 
	 * @return Текст запроса, после удаления комментариев.
	 */
	public static final String dropComment(String query) {
		String dropComments = "/\\*[\\s\\S]*?\\*/";
		Pattern p = Pattern.compile(dropComments, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(query); // get a matcher object
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
