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
package q4s.dafobi.data;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

import q4s.dafobi.common.DataParam;
import q4s.dafobi.jaxb.common.DataType;

/**
 * Класс, реализующий базовый скелет для запросов.
 * 
 * @author Q4S
 * 
 */
public abstract class AbstractStatement implements IStatement {

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.IStatement#query(java.util.Map)
	 */
	@Override
	public final IResultTable query(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		return query();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.IStatement#execute(java.util.Map)
	 */
	@Override
	public final int execute(Map<String, DataParam> parameters) {
		for (String name : getParamNames()) {
			DataParam param = parameters.get(name);
			if (param == null) {
				throw new IllegalArgumentException(
						MessageFormat.format("Отсутствует описание параметра с именем: {0}", name));
			}
			setParam(name, param);
		}

		int count = execute();

		// Если для обычного оператора указаны выходные параметры,
		// то тут может возникнуть ошибка.
		for (String name : getOutParamNames()) {
			DataParam param = parameters.get(name);
			DataType type = param.getType();
			Object value = getParam(name, type);
			parameters.put(name, type.param(value));
		}
		return count;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.IStatement#addBatch(java.util.Map)
	 */
	@Override
	public final void addBatch(Map<String, DataParam> parameters) {
		for (Entry<String, DataParam> param : parameters.entrySet()) {
			setParam(param.getKey(), param.getValue());
		}
		addBatch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.IStatement#setParam(java.lang.String,
	 * q4s.dafobi.data.DataType, java.lang.Object)
	 */
	@Override
	public final void setParam(String name, DataType type, Object value) {
		setParam(name, type.param(value));
	}

	// private Map<String, DataParam> params = new TreeMap<String, DataParam>();
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see q4s.dafobi.data.IStatement#setParam(java.lang.String,
	// * q4s.dafobi.data.DataParam)
	// */
	// @Override
	// public void setParam(String name, DataParam value) {
	// params.put(name.toLowerCase(), value);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see q4s.dafobi.data.IStatement#getParam(java.lang.String,
	// * q4s.dafobi.data.DataType)
	// */
	// @Override
	// public Object getParam(String name, DataType type) {
	// return type.convert(params.get(name.toLowerCase()));
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see q4s.dafobi.data.IStatement#getParamNames()
	// */
	// @Override
	// public String[] getParamNames() {
	// return params.keySet().toArray(new String[0]);
	// }

}
