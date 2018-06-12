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
package org.q4s.dafobi.trans;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.q4s.dafobi.exception.WrongIndexException;

/**
 * @author Q4S
 */
public class RowImpl implements IRow {

	/**
	 * Ссылка на информацию, описывающую данные, находящиеся в строке.
	 */
	private final IResultTable rowInfo;

	/**
	 * Значения данных строки.
	 */
	private final Object[] values;

	/**
	 * 
	 * @param statement
	 * @param values
	 */
	public RowImpl(IResultTable rowInfo, Object[] values) {
		this.rowInfo = rowInfo;
		this.values = converValues(values);
	}

	/**
	 * Метод приводит данные к разрешенным типам данных.
	 * 
	 * @param values
	 *            Данные строки, переданные из JDBC
	 * 
	 * @return Данные строки с приведенными типами.
	 */
	private Object[] converValues(final Object[] values) {
		Object[] newValues = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			Class<?> clazz = values[i].getClass();
			// Integer хранится как Long
			if (Integer.class.equals(clazz)) {
				newValues[i] = new Long((Integer) values[i]);

			} else {
				newValues[i] = values[i];
			}
		}
		return newValues;
	}

	IResultTable getMeta() {
		return rowInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#get(int)
	 */
	@Override
	public Object get(int columnNum) throws WrongIndexException {
		if (columnNum < 0 || columnNum >= values.length) {
			throw new WrongIndexException();
		}
		// Ожидается, что все данные будут неизменяемые по своей природе.
		return values[columnNum];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#get(java.lang.String)
	 */
	@Override
	public final Object get(String columnName) throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getString(int)
	 */
	@Override
	public String getString(int columnNum) throws WrongIndexException {
		return (String) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getString(java.lang.String)
	 */
	@Override
	public final String getString(String columnName) throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getInteger(int)
	 */
	@Override
	public Long getInteger(int columnNum) throws WrongIndexException {
		return (Long) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getInteger(java.lang.String)
	 */
	@Override
	public final Long getInteger(String columnName) throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getInteger(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getDecimal(int)
	 */
	@Override
	public BigDecimal getDecimal(int columnNum) throws WrongIndexException {
		return (BigDecimal) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getDecimal(java.lang.String)
	 */
	@Override
	public final BigDecimal getDecimal(String columnName)
			throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getDecimal(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getDate(int)
	 */
	@Override
	public Date getDate(int columnNum) throws WrongIndexException {
		return (Date) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getDate(java.lang.String)
	 */
	@Override
	public final Date getDate(String columnName) throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getDate(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getTime(int)
	 */
	@Override
	public Time getTime(int columnNum) throws WrongIndexException {
		return (Time) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getTime(java.lang.String)
	 */
	@Override
	public final Time getTime(String columnName) throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getTime(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int columnNum) throws WrongIndexException {
		return (Timestamp) get(columnNum);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IRow#getTimestamp(java.lang.String)
	 */
	@Override
	public final Timestamp getTimestamp(String columnName)
			throws WrongIndexException {
		int index = rowInfo.getColumnIndex(columnName);
		return getTimestamp(index);
	}

}
