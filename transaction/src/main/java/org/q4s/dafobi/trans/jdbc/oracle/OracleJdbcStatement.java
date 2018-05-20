package org.q4s.dafobi.trans.jdbc.oracle;

import org.q4s.dafobi.trans.jdbc.JdbcStatement;
import org.q4s.dafobi.trans.jdbc.JdbcTransaction;

/**
 * Oracle в отличии от стандартного JDBC умеет работать с DECLARE-BEGIN-END
 * блоком и имеет нормальную поддержку CLOB.
 * 
 * @author Q4S
 * 
 */
public class OracleJdbcStatement extends JdbcStatement {

	/**
	 * @see OracleJdbcStatement
	 * 
	 * @param transaction
	 * @param query
	 */
	public OracleJdbcStatement(JdbcTransaction transaction, String query) {
		super(transaction, query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.jdbc.JdbcStatement#isQueryCallable()
	 */
	@Override
	protected boolean isQueryCallable() {
		// Запрос автоматически определяется как Callable, если он имеет
		// формат "{call ...}" или "[declare ... ]begin ... end".
		// TODO Конструкция BEGIN-END существует только в Oracle
		String processedQuery = getProcessedQuery();
		return "{call".equalsIgnoreCase(processedQuery.substring(0, 5))
				|| "begin".equalsIgnoreCase(processedQuery.substring(0, 5))
				|| "declare".equalsIgnoreCase(processedQuery.substring(0, 7));
	}

}
