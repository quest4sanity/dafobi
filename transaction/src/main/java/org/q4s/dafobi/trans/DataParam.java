/*
 * (C) Copyright 2018 - Vladimir Bogdanov | Data Form Builder
 *
 * https://github.com/quest4sanity/dafobi
 *
 * Licensed under the LGPL, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     /LICENSE.txt or https://www.gnu.org/licenses/lgpl.txt
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

/**
 * Для получения объекта данного типа проще всего воспользоваться типом данных
 * {@link DataType}. Например так:
 * 
 * <pre>
 * DataParam param;
 * param = DataType.INTEGER.param(123);
 * </pre>
 * 
 * @author Q4S
 *
 */
public class DataParam {
	private final DataType type;
	private final Object value;

	public DataParam(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}