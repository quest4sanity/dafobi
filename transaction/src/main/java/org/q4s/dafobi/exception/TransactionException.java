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

	/**
	 * Code identifying the exception
	 */
	private final String state;

	/**
	 * Vendor's error code
	 */
	private final int errorCode;

	/**
	 * Description of the exception
	 */
	private final String message;

	/**
	 * Operator that caused the exception
	 */
	private final String operator;

	public TransactionException(SQLException e) {
		this(e.getSQLState(), e.getErrorCode(), e.getMessage(), null);
	}

	public TransactionException(SQLException e, String operator) {
		this(e.getSQLState(), e.getErrorCode(), e.getMessage(), operator);
	}

	public TransactionException(String state, int errorCode, String message, String operator) {
		super();

		this.state = state;
		this.errorCode = errorCode;
		this.message = message;

		this.fillInStackTrace();
		this.operator = operator;
	}

	/**
	 * @return Code identifying the exception
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return Vendor's error code
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return Description of the exception
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return Operator that caused the exception
	 */
	public String getOperator() {
		return this.operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder("Transaction error. State: ").append(state).append("; Error code: ")
				.append(errorCode).append("; Message: \"").append(message).append("\"");
		if (operator != null && operator.trim().length() > 0) {
			result.append("; Operator: \"").append(operator).append("\"");
		}
		return result.append(".").toString();
	}
}
