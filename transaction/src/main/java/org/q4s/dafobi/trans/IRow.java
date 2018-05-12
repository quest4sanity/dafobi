package org.q4s.dafobi.trans;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.q4s.dafobi.exception.WrongIndexException;

/*
 * 
 * @author Q4S
 *
 */
public interface IRow {
	/**
	 * Получение значения по номеру колонки. 
	 * 
	 * @param columnNum Номер колонки
	 * 
	 * @return Значение, связанные с данным номером.
	 *
	 */
	public Object get(int columnNum) throws WrongIndexException;
	
	public Object get(String columnName) throws WrongIndexException;
	
	public String getString(int columnNum) throws WrongIndexException;
	
	public String getString(String columnName) throws WrongIndexException;
	
	public Long getInteger(int columnNum) throws WrongIndexException;
	
	public Long getInteger(String columnName) throws WrongIndexException;
	
	public BigDecimal getDecimal(int columnNum) throws WrongIndexException;
	
	public BigDecimal getDecimal(String columnName) throws WrongIndexException;
	
	public Date getDate(int columnNum) throws WrongIndexException;
	
	public Date getDate(String columnName) throws WrongIndexException;
	
	public Time getTime(int columnNum) throws WrongIndexException;
	
	public Time getTime(String columnName) throws WrongIndexException;
	
	public Timestamp getTimestamp(int columnNum) throws WrongIndexException;
	
	public Timestamp getTimestamp(String columnName) throws WrongIndexException;
	
}
