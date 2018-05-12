package org.q4s.dafobi.trans.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.q4s.dafobi.trans.AbstractTransaction;
import org.q4s.dafobi.trans.IStatement;

public class JdbcTransaction extends AbstractTransaction {

	private final Connection connection;

	public JdbcTransaction(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return Связь с подключением через JDBC.
	 */
	public final Connection getConnection() {
		return this.connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.AbstractTransaction#close()
	 */
	@Override
	public void close() {
		// Завершение транзакции не приводит к разрыву подключения.
		// getConnection().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.AbstractTransaction#prepare(java.lang.String)
	 */
	@Override
	public IStatement prepare(final String statement) {
		return new JdbcStatement(this, statement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransactionn#getLastError()
	 */
	@Override
	public void getLastError() {
		// try {
		// connection.getL;
		//
		// } catch (SQLException e) {
		// throw new RuntimeException(e);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.q4s.dafobi.trans.ITransactionon#commit()
	 */
	@Override
	public void commit() {
		try {
			connection.commit();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seorg.q4s.dafobi.trans.ITransactionion#rollback()
	 */
	@Override
	public void rollback() {
		try {
			connection.rollback();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#setAutocommit(boolean)
	 */
	@Override
	public void setAutocommit(boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.ITransaction#getAutocommit()
	 */
	@Override
	public boolean getAutocommit() {
		try {
			return connection.getAutoCommit();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
