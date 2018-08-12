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
package org.q4s.dafobi.variables;

import java.util.Map;
import java.util.TreeMap;

import javax.el.ELResolver;

import org.q4s.dafobi.variables.map.VarMapELResolver;

/**
 * <p>
 * Класс для параметров. Параметры задают переменные, которые могут со временем
 * меняться. Отдельный класс понадобился для того, чтобы его мог опознать
 * ELResolver.
 * </p>
 * <p>
 * Названия атрибутов регистронезависимые. Внутри самого класса они хранятся в
 * верхнем регистре.
 * </p>
 ** 
 * @author vbogdanov
 * 
 */
public class VarMap extends TreeMap<String, AbstractVar> implements
		IELResolvable {

	private static final long serialVersionUID = 8269541473971341994L;

	/**
	 * 
	 */
	public VarMap() {
		super();
	}

	/**
	 * @param m
	 */
	public VarMap(Map<? extends String, ? extends AbstractVar> m) {
		super(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.borlas.bus.kernel.expression.IELResolvable#getElResolver()
	 */
	@Override
	public ELResolver getElResolver() {
		return VarMapELResolver.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeMap#get(java.lang.Object)
	 */
	@Override
	public AbstractVar get(Object key) {
		return super.get(((String) key).toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AbstractVar put(String key, AbstractVar value) {
		return super.put(key.toUpperCase(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(((String) key).toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeMap#remove(java.lang.Object)
	 */
	@Override
	public AbstractVar remove(Object key) {
		return super.remove(((String) key).toUpperCase());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TreeMap#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends AbstractVar> m) {
		for (Map.Entry<? extends String, ? extends AbstractVar> e : m
				.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Функция возвращает карту со значениями всех переменных.
	 * 
	 * @return Значения всех переменных.
	 */
	public Map<String, Object> getAllValues() {
		Map<String, Object> values = new TreeMap<String, Object>();
		for (Map.Entry<String, AbstractVar> item : entrySet()) {
			values.put(item.getKey(), item.getValue().get());
		}
		return values;
	}

	/**
	 * Метод используется в основном при отображении данных при отладке
	 * приложения. Он конвертирует хранилище в вид, пригодный для использования
	 * тегом <ui:repeat /> 
	 */
	public Object[][] allDebugValues() {
		Object[][] result = new Object[size()][2];
		
		int i = 0;
		for (Map.Entry<String, AbstractVar> entry : entrySet()) {
			result[i][0] = entry.getKey();
			result[i][1] = entry.getValue().get();
			i++;
		}
		return result;
	}
}
