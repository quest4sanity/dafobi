package org.q4s.dafobi.exception;

import java.sql.SQLException;

/**
 * Исключительная ситуация, возникающая из-за ошибок во время выполнения
 * операторов транзакции.
 * 
 * @author Q4S
 * 
 */
public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String state;

	private int errorCode;

	private final String message;

	private final String operator;

	public TransactionException(SQLException e) {
		this(e.getSQLState(), e.getErrorCode(), e.getMessage(), null);
	}

	public TransactionException(SQLException e, String operator) {
		this(e.getSQLState(), e.getErrorCode(), e.getMessage(), operator);
	}

	public TransactionException(String state, int errorCode, String message,
			String operator) {
		super();

		this.state = state;
		this.errorCode = errorCode;
		this.message = message;

		this.fillInStackTrace();
		this.operator = operator;
	}

	public String getOperator() {
		return this.operator;
	}
}
