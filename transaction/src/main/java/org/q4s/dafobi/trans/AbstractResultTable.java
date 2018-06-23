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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывает метаинформацию заголовка таблицы данных. Сюда входят: имена
 * столбцов, типы данных, классы и т.п.
 * 
 * @author Q4S
 */
public abstract class AbstractResultTable implements IResultTable {

	/**
	 * Оператор, вернувший данный набор данных.
	 */
	private final IStatement statement;

	protected AbstractResultTable(IStatement statement) {
		this.statement = statement;
	}

	private class ColumnMetaInfo {
		final String name;
		final DataType type;

		public ColumnMetaInfo(String name, DataType type) {
			this.name = name.toLowerCase();
			this.type = type;
		}
	}

	/**
	 * Список описаний колонок. Список используется как временное хранилище на
	 * этапе накопления информации. Потом данные переписываются в массив.
	 */
	private List<ColumnMetaInfo> columnMetaList = new ArrayList<ColumnMetaInfo>();

	/**
	 * Массив описаний колонок.
	 */
	private ColumnMetaInfo[] columnMeta = null;

	/**
	 * Метод добавляет информацию о колонке данных для строки. Индекс колонки
	 * при этом будет соответствовать порядку добавления информации.
	 * 
	 * @param name
	 *            Имя колонки.
	 * 
	 * @param Type
	 *            Тип колонки
	 */
	protected final void addColumnInfo(String name, DataType type) {
		if (isProtected()) {
			throw new UnsupportedOperationException(
					"Можно добавлять информацию о новых колонках только, пока данные не закрыты для изменения");
		} else {
			columnMetaList.add(new ColumnMetaInfo(name, type));
		}

	}

	/**
	 * Метод делает класс неизменяемым (на случай шаловливых ручек).
	 */
	protected final void protect() {
		if (isProtected()) {
			throw new UnsupportedOperationException("Данные уже были закрыты для изменения");
		} else {
			columnMeta = columnMetaList.toArray(new ColumnMetaInfo[0]);
			columnMetaList = null;
		}
	}

	protected final boolean isProtected() {
		return columnMeta != null;
	}

	private final String INFO_IS_NOT_PROTECTED_YET = "Нельзя пользоваться информацией о таблице, пока она не защищена";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#count()
	 */
	@Override
	public final int count() {
		if (!isProtected()) {
			throw new UnsupportedOperationException(INFO_IS_NOT_PROTECTED_YET);
		}
		return columnMeta.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnIndex(java.lang.String)
	 */
	@Override
	public final int getColumnIndex(String name) {
		if (!isProtected()) {
			throw new UnsupportedOperationException(INFO_IS_NOT_PROTECTED_YET);
		}
		for (int i = 0; i < columnMeta.length; i++) {
			if (columnMeta[i].name.equalsIgnoreCase(name)) {
				return i;
			}
		}
		throw new IllegalArgumentException(MessageFormat.format("Неверное имя колонки запроса: {0}", name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnName(int)
	 */
	@Override
	public final String getColumnName(int index) {
		if (!isProtected()) {
			throw new UnsupportedOperationException(INFO_IS_NOT_PROTECTED_YET);
		}
		return columnMeta[index].name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IResultTable#getColumnType(int)
	 */
	@Override
	public final DataType getColumnType(int index) {
		if (!isProtected()) {
			throw new UnsupportedOperationException(INFO_IS_NOT_PROTECTED_YET);
		}
		return columnMeta[index].type;
	}

	/**
	 * @return Оператор, вернувший данный набор данных.
	 */
	public final IStatement getStatement() {
		return statement;
	}

}
