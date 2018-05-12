package org.q4s.dafobi.trans;

import java.util.Map;

/**
 * 
 * @author Q4S
 */
public abstract class AbstractTransaction implements ITransaction {

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	int count = stmt.executeUpdate(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Количество затронутых запросом строк.
	 */
	@Override
	public final int executeUpdate(final String statement,
			final Map<String, Object> parameters) {
		int count;
		
		try (IStatement stmt = prepare(statement);) {
			count = stmt.executeUpdate(parameters);
		}
		return count;
	}

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	boolean rc = stmt.execute(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Количество затронутых запросом строк.
	 */
	@Override
	public final boolean execute(final String statement,
			final Map<String, Object> parameters) {
		boolean rc;
		try (IStatement stmt = prepare(statement);) {
			rc = stmt.execute(parameters);
		}
		return rc;
	}

	/**
	 * Сокращенная версия кода:
	 * 
	 * <pre>
	 * try (IStatement stmt = transaction.prepare(sql);) {
	 * 	Iterable&lt;IRow&gt; rows = stmt.query(params);
	 * 	...
	 * }
	 * </pre>
	 * 
	 * @param statement
	 *            Текст оператора, который надо будет выполнять.
	 * 
	 * @param parameters
	 *            Значения параметров, с которыми выполняется запрос.
	 * 
	 * @return Итератор по полученным строкам.
	 * 
	 * @deprecated Похоже, здесь могут быть проблемы с освобождением ресурсов.
	 */
	@Override
	public Iterable<IRow> query(final String statement,
			final Map<String, Object> parameters) {
		Iterable<IRow> result = null;
		try (IStatement stmt = prepare(statement);) {
			result = stmt.query(parameters);
		}
		return result;
	}

}
