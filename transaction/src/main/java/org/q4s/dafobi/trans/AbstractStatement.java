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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Класс, реализующий базовый скелет для запросов.
 * 
 * @author Q4S
 * 
 */
public abstract class AbstractStatement implements IStatement {

	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#query(java.util.Map)
	 */
	@Override
	public IResultTable query(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		return query();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#execute(java.util.Map)
	 */
	@Override
	public boolean execute(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		return execute();
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#executeUpdate(java.util.Map)
	 */
	@Override
	public int executeUpdate(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		return executeUpdate();
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#addBatch(java.util.Map)
	 */
	@Override
	public void addBatch(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		addBatch();
	}

	private Map<String, DataParam> params = new TreeMap<String, DataParam>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#setParam(java.lang.String,
	 * org.q4s.dafobi.trans.DataParam)
	 */
	@Override
	public void setParam(String name, DataParam value) {
		params.put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getParam(java.lang.String,
	 * org.q4s.dafobi.trans.DataType)
	 */
	@Override
	public Object getParam(String name, DataType type) {
		return type.convert(params.get(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#getParamNames()
	 */
	@Override
	public String[] getParamNames() {
		return params.keySet().toArray(new String[0]);
	}

}
