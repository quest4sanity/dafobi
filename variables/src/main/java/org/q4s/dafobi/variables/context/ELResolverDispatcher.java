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
import javax.el.PropertyNotFoundException;

import org.q4s.dafobi.variables.IELResolvable;

/**
 * <p>
 * При работе с выражениями Dafobi каждый из объектов (свойств) в выражении
 * (кроме, возможно, конечных) должен реализовывать интерфейс
 * {@link IELResolvable}, предоставляющий ресолвер, знающий, какие свойства
 * данный объект предоставляет в EL выражения, и как с ними работать.
 * <p>
 * Данный ресолвер является корневым. Он сам по себе не делает ничего, а только
 * лишь переадресует запрос ресолверу, определенному в корневом объекте в EL
 * выражении.
 * <p>
 * 
 * @see CompositeELResolver
 * @see ELResolver
 * @since JSP 2.1
 */
public class ELResolverDispatcher extends ELResolver {

	/**
	 * Creates a new read {@link ELResolverDispatcher}.
	 */
	public ELResolverDispatcher() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		return secondStageResolver.getType(context, base, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		return secondStageResolver.getValue(context, base, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object, java.lang.Object)
	 */
	public void setValue(ELContext context, Object base, Object property, Object val) {
		/*
		 * Если вдруг так случилось, что вместо ожидаемого NULL-а val содержит
		 * 0, то необходимо установить аргумент VM
		 * -Dorg.apache.el.parser.COERCE_TO_ZERO=false
		 */
		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		secondStageResolver.setValue(context, base, property, val);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object,
	 * java.lang.Object)
	 */
	public boolean isReadOnly(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		return secondStageResolver.isReadOnly(context, base, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {

		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		return secondStageResolver.getFeatureDescriptors(context, base);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext,
	 * java.lang.Object)
	 */
	public Class<?> getCommonPropertyType(ELContext context, Object base) {

		if (context == null) {
			throw new NullPointerException();
		}

		ELResolver secondStageResolver;
		if (base == null) {
			secondStageResolver = ELResolverNull.getInstance();

		} else if (base instanceof IELResolvable) {
			secondStageResolver = ((IELResolvable) base).getElResolver();

		} else {
			throw new PropertyNotFoundException();
		}
		return secondStageResolver.getCommonPropertyType(context, base);
	}
}
