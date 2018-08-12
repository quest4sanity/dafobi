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
package org.q4s.dafobi.variables.context;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

/**
 * <p>
 * Читтерский стартовый ресолвер. Для всех переменных кроме переменной, имя
 * которой хранится в константе {@link ELResolverNull#ROOT_VAR_NAME}, возвращает
 * null. Необходимость в нем возникла из-за того, что по изначальной задумке для
 * цепочки из null-ов необходимо возвращать итоговый null, а не ошибки. Но
 * поскольку EL-выражения устрены так, что для корневого ресолвера базовый
 * объект так же всегда будет равен null, то, чтобы избежать путанницы, пришлось
 * добавить данный ресолвер перед корневым и надеяться, что ни один пользователь
 * не додумается называть (например) колонки DataStore именем из константы
 * {@link ELResolverNull#ROOT_VAR_NAME}.
 * </p>
 * 
 * @see CompositeELResolver
 * @see ELResolver
 * @since JSP 2.1
 */
public class ELResolverNull extends ELResolver {

	/**
	 * Имя переменной, через которую происходит выход ко всем элементам системы.
	 */
	public static final String ROOT_VAR_NAME = "dafobi_el_root";

	private static ELResolverNull instance;

	/**
	 * @return {@link ELResolverNull}.
	 */
	public static ELResolverNull getInstance() {
		if (instance == null) {
			instance = new ELResolverNull();
		}
		return instance;
	}

	/**
	 * Creates a new read {@link ELResolverNull}.
	 */
	private ELResolverNull() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Class<?> getType(ELContext context, Object base, Object property) {
		context.setPropertyResolved(true);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Object getValue(ELContext context, Object base, Object property) {
		context.setPropertyResolved(true);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	public void setValue(ELContext context, Object base, Object property,
			Object val) {

		context.setPropertyResolved(true);
		throw new PropertyNotWritableException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		context.setPropertyResolved(true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {

		// Может быть когда-нибудь в будущем.

		// if (base == null) {
		// Iterator<?> iter = implementer.keySet().iterator();
		// List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
		// while (iter.hasNext()) {
		// Object key = iter.next();
		// FeatureDescriptor descriptor = new FeatureDescriptor();
		// String name = (key == null) ? null : key.toString();
		// descriptor.setName(name);
		// descriptor.setDisplayName(name);
		// descriptor.setShortDescription("");
		// descriptor.setExpert(false);
		// descriptor.setHidden(false);
		// descriptor.setPreferred(true);
		// descriptor
		// .setValue("type", key == null ? null : key.getClass());
		// descriptor.setValue("resolvableAtDesignTime", Boolean.TRUE);
		// list.add(descriptor);
		// }
		// return list.iterator();
		// }
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// if (base == null) {
		// return Object.class;
		// }
		throw new UnsupportedOperationException();
	}
}
