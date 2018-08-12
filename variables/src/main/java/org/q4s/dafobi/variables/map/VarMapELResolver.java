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
package org.q4s.dafobi.variables.map;

import java.beans.FeatureDescriptor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import org.q4s.dafobi.variables.IVar;
import org.q4s.dafobi.variables.VarMap;

/**
 * Данный ресолвер получает данные, сохраненные в карте переменных.
 * 
 * @see CompositeELResolver
 * @see ELResolver
 * @see VarMap
 */
public class VarMapELResolver extends ELResolver {

	private static VarMapELResolver instance;

	private final String PROPERTY_NOT_FOUND = "Not existing property {1}";

	private final String PROPERTY_NOT_WRITABLE = "Not writable property {1}";

	/**
	 * @return {@link VarMapELResolver}.
	 */
	public static VarMapELResolver getInstance() {
		if (instance == null) {
			instance = new VarMapELResolver();
		}
		return instance;
	}

	/**
	 * Creates a new read {@link VarMapELResolver}.
	 */
	private VarMapELResolver() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (base == null || !(base instanceof VarMap)) {
			throw new UnsupportedOperationException();
		}

		// С учетом фоновой конвертации значений в нужный тип, самым общим
		// типом окажется Object.class.
		Class<?> clazz = Object.class;

		context.setPropertyResolved(true);
		return clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Object getValue(ELContext context, Object base, Object property) {
		if (base == null || !(base instanceof VarMap)) {
			throw new UnsupportedOperationException();
		}

		VarMap valuesStorage = (VarMap) base;
		IVar var = valuesStorage.get((String) property);
		if (var == null) {
			throw new PropertyNotFoundException(MessageFormat.format(PROPERTY_NOT_FOUND, base, property));

		} else {
			Object value = var.get();

			context.setPropertyResolved(true);
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	public void setValue(ELContext context, Object base, Object property, Object val) {
		if (base == null || !(base instanceof VarMap)) {
			throw new UnsupportedOperationException();
		}

		VarMap valuesStorage = (VarMap) base;
		IVar var = valuesStorage.get((String) property);
		if (var == null) {
			throw new PropertyNotFoundException(MessageFormat.format(PROPERTY_NOT_FOUND, base, property));

		} else if (var.isReadOnly()) {
			throw new PropertyNotWritableException(MessageFormat.format(PROPERTY_NOT_WRITABLE, base, property));

		} else {
			var.set(val);
			context.setPropertyResolved(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (base == null || !(base instanceof VarMap)) {
			throw new UnsupportedOperationException();
		}

		VarMap valuesStorage = (VarMap) base;
		IVar var = valuesStorage.get((String) property);
		if (var == null) {
			throw new PropertyNotFoundException(MessageFormat.format(PROPERTY_NOT_FOUND, base, property));

		} else {
			boolean rc = var.isReadOnly();

			context.setPropertyResolved(true);
			return rc;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		if (base == null || !(base instanceof VarMap)) {
			throw new UnsupportedOperationException();
		}

		VarMap values = (VarMap) base;
		Iterator<?> iter = values.keySet().iterator();
		List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
		while (iter.hasNext()) {
			Object key = iter.next();
			FeatureDescriptor descriptor = new FeatureDescriptor();
			String name = (key == null) ? null : key.toString();
			descriptor.setName(name);
			descriptor.setDisplayName(name);
			// descriptor.setShortDescription("");
			// descriptor.setExpert(false);
			// descriptor.setHidden(false);
			// descriptor.setPreferred(false);
			// descriptor.setValue("type", key == null ? null : key.getClass());
			// descriptor.setValue("resolvableAtDesignTime", Boolean.TRUE);
			list.add(descriptor);
		}
		return list.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return String.class;
	}
}
