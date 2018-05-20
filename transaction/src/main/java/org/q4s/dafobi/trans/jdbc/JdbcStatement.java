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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
	 * Запрос, из которого в дополнение ко всему удалены комментарии.
	 */
	private final String cleanQuery;

	/**
	 * @see JdbcStatement
	 * 
	 * @param transaction
	 * @param query
	 */
	public JdbcStatement(final JdbcTransaction transaction, final String query) {
		try {
			this.transaction = transaction;

			indexMap = new HashMap<String, int[]>();
			inOutParams = new HashMap<String, Integer>();

			// Разберем запрос и выделим входящие и выходные параметры.
			// Учтем, что в начале вызова процедур может быть любое количетсво
			// переносов строки и пробелов, а JDBC этого не разрешает.
			parsedQuery = parse(query.replaceAll("^\\s+", ""), indexMap,
					inOutParams);

			cleanQuery = dropComment(parsedQuery).replaceAll("\\s+", " ")
					.trim();

			// Запрос автоматически определяется как Callable, если он имеет
			// формат "{call ...}" или "[declare ... ]begin ... end".
			// TODO Конструкция BEGIN-END существует только в Oracle
			Connection connection = transaction.getConnection();
			if ("{call".equalsIgnoreCase(cleanQuery.substring(0, 5))
					|| "begin".equalsIgnoreCase(cleanQuery.substring(0, 5))
					|| "declare".equalsIgnoreCase(cleanQuery.substring(0, 7))) {
				statement = connection.prepareCall(parsedQuery);

			} else {
				statement = connection.prepareStatement(parsedQuery);
			}

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
		}
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
	private static final String parse(final String query, final Map paramMap,
			final Map<String, Integer> inOutParams) {

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

				} else if (c == '/' && i + 1 < length
						&& query.charAt(i + 1) == '*') {
					// Начало комментария
					inComment = true;
					parsedQuery.append(c);
					c = query.charAt(++i);

				} else if ((c == ':' || c == '&') && i + 1 < length
						&& Character.isJavaIdentifierStart(query.charAt(i + 1))) {
					int j = i + 2;
					while (j < length
							&& Character.isJavaIdentifierPart(query.charAt(j))) {
						j++;
					}
					String name = query.substring(i + 1, j).toUpperCase();
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
							throw new RuntimeException(
									"Нельзя дважды определять как выходную "
											+ "одну и ту же переменную");
						}
						inOutParams.put(name.toUpperCase(), new Integer(index));
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
	 * <p>
	 * Удаление комментариев (вида /&#42; &#42;/ ) из текста.
	 * </p>
	 * 
	 * <p>
	 * Однако, ввиду того, что в методе используются регулярные выражения, в
	 * случае, если в тексте присутствуют сложные вложенные комментарии, он
	 * может отработать с ошибками. Поэтому настоятельно рекомендуется во всех
	 * файлах, к которым будет применена данная функция видоизменять символы
	 * вложенных комментариев (например экранировать их).
	 * </p>
	 * 
	 * @param query
	 *            Текст, из которого надо удалить комментарии.
	 * 
	 * @return Текст, после удаления комментариев.
	 */
	public static final String dropComment(String query) {
		String dropComments = "/\\*[\\s\\S]*?\\*/";
		Pattern p = Pattern.compile(dropComments, Pattern.DOTALL
				| Pattern.UNIX_LINES | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(query); // get a matcher object
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.AbstractStatement#close()
	 */
	@Override
	public void close() {
		super.close();

		try {
			statement.close();

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
			// if (transaction == null) {
			return new JdbcResultTable(this, statement.executeQuery());
			// } else {
			// // Если запрос выполняется в рамках транзакции, то
			// // автоматически регистрируем resultset.
			// return transaction.register(statement.executeQuery());
			// }

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#execute()
	 */
	@Override
	public boolean execute() {
		try {
			return statement.execute();

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#executeUpdate()
	 */
	@Override
	public int executeUpdate() {
		try {
			return statement.executeUpdate();

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
		}
	}

	/**
	 * @return the parsedQuery
	 */
	public String getParsedQuery() {
		return parsedQuery;
	}

	/**
	 * @return the cleanQuery
	 */
	public String getCleanQuery() {
		return cleanQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getParameters()
	 */
	@Override
	public Set<String> getParameters() {
		return indexMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getOutParameters()
	 */
	@Override
	public Set<String> getOutParameters() {
		if (statement instanceof CallableStatement) {
			return inOutParams.keySet();

		} else {
			return new TreeSet<String>();
		}
	}

	/**
	 * Регистрация исходящего параметра. Поскольку исходящие параметры
	 * определяются по тексту запроса, данный оператор фактически лишь связывает
	 * с именем выходного параметра тип данных, который через него возвращается.
	 * Рекомендуется использовать данный метод в связки с
	 * {@link #getOutParameters()}.
	 * 
	 * @param name
	 *            Название параметра.
	 * 
	 * @param sqlType
	 *            Тип данных параметра (как он определен в {@link Types}).
	 */
	public void registerOutParameter(String name, DataType type) {
		if (statement instanceof CallableStatement) {
			try {
				int index = inOutParams.get(name.toUpperCase());
				((CallableStatement) statement).registerOutParameter(index,
						type.jdbcType());

			} catch (SQLException e) {
				throw new IllegalArgumentException(e);
			}

		} else {
			throw new IllegalArgumentException(
					"Выходные параметры определяются только при вызове "
							+ "процедур или begin-end блоков.");
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
			statement.addBatch();

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
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
			return statement.executeBatch();

		} catch (SQLException e) {
			throw new TransactionException(e, getCleanQuery());
		}
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
	protected int[] getIndexes(String name) {
		int[] indexes = (int[]) indexMap.get(name.toUpperCase());
		if (indexes == null) {
			throw new IllegalArgumentException("Parameter not found: " + name);
		}
		return indexes;
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
			int[] indexes = getIndexes(name.toUpperCase());
			for (int i = 0; i < indexes.length; i++) {
				Object value = param.getValue();
				switch (param.getType().jdbcType()) {
				case Types.CLOB:
					statement.setClob(indexes[i], value == null ? null
							: (Clob) value);
					break;

				case Types.VARCHAR:
					statement.setString(indexes[i], value == null ? null
							: (String) value);
					break;

				case Types.INTEGER:
					statement.setInt(indexes[i], value == null ? null
							: (Integer) value);
					break;

				case Types.BIGINT:
					statement.setLong(indexes[i], value == null ? null
							: (Long) value);
					break;

				case Types.NUMERIC:
					statement.setBigDecimal(indexes[i], value == null ? null
							: (BigDecimal) value);
					break;

				case Types.DATE:
					statement.setDate(indexes[i], value == null ? null
							: (Date) value);
					break;

				case Types.TIME:
					statement.setTime(indexes[i], value == null ? null
							: (Time) value);
					break;

				case Types.TIMESTAMP:
					// С полем Timestamp в Оракле происходит хрень.
					// Купируем баг путем перевода в другой тип.
					statement.setObject(indexes[i], value == null ? null
							: new Date(((Timestamp) value).getTime()));
					break;

				default:
					throw new UnsupportedOperationException(new StringBuilder(
							"Тип данных").append(param.getClass().getName())
							.append(" не поддерживается.").toString());
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
		try {
			Integer index = inOutParams.get(name.toUpperCase());
			if (index != null) {
				return ((CallableStatement) statement).getObject(index);
			}

		} catch (SQLException e) {
		}
		return null;
	}

}
