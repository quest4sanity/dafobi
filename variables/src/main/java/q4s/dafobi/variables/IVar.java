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
package q4s.dafobi.variables;

import q4s.dafobi.jaxb.common.DataType;

/**
 * Интерфейс определяет методы для контейнера. Контейнер - это абстрактный
 * механизм, позволяющий устанавливать, хранить, получать и приводить тип
 * значения. Контейнер может представлять собой как простое хранилище для
 * переменной {@link Var}, так и вычислимое выражение {@link Expression},
 * которое может указывать (а может и не указывать) на другое, хранящееся
 * где-то, значение.
 * 
 * @author Q4S
 * 
 */
public interface IVar {

	/**
	 * В ряде случаев значение данных может защищено от изменения. Например,
	 * такое возможно в случае сложных вычислимых выражений, которые не
	 * подразумевают обратного изменение их значения.
	 * 
	 * @return true - если значение нельзя изменить.
	 */
	public abstract boolean isReadOnly();

	/**
	 * <p>
	 * Получить значение из контейнера.
	 * </p>
	 * <p>
	 * В тех случаях, когда с помощью функции {@link #setType(DataType)}
	 * установлен тип, тогда значение приводится к данному типу.
	 * </p>
	 * 
	 * @return Текущее значение из контейнера.
	 */
	public abstract Object get();

	/**
	 * <p>
	 * Установить новое значение в контейнере.
	 * </p>
	 * <p>
	 * В тех случаях, когда с помощью функции {@link #setType(DataType)}
	 * установлен тип, тогда значение проверяется на соответствие данному типу.
	 * </p>
	 * 
	 * @param value
	 *            Новое значение в контейнере.
	 */
	public abstract void set(Object value);

	/**
	 * В ряде случаев значение данных может защищено от изменения. Например,
	 * такое возможно в случае сложных вычислимых выражений, которые не
	 * подразумевают обратного изменение их значения.
	 * 
	 * @param context
	 *            Контекст, в котором значение проверятется на запись.
	 * 
	 * @return true - если значение нельзя изменить.
	 */
	public abstract boolean isReadOnly(IContext context);

	/**
	 * <p>
	 * Значение может быть не только переменной, но и выражением. А последние
	 * могут вычисляться как в рамках контекста по-умолчанию, так и указанного
	 * параметрически. Данный метод и позволяет получить значение переменной,
	 * учитывая параметрический контекст.
	 * </p>
	 * <p>
	 * В тех случаях, когда с помощью функции {@link #setType(DataType)}
	 * установлен тип, тогда значение приводится к данному типу.
	 * </p>
	 * 
	 * @param context
	 *            Контекст, в котором получается значение.
	 * 
	 * @return Текущее значение из контейнера с учетом контекста.
	 */
	public abstract Object get(IContext context);

	/**
	 * <p>
	 * Значение может быть не только переменной, но и выражением. А последние
	 * могут вычисляться как в рамках контекста по-умолчанию, так и указанного
	 * параметрически. Данный метод и позволяет установить новое значение в
	 * контейнере.
	 * </p>
	 * <p>
	 * В тех случаях, когда с помощью функции {@link #setType(DataType)}
	 * установлен тип, тогда значение проверяется на соответствие данному типу.
	 * </p>
	 * 
	 * @param context
	 *            Контекст, в котором получается значение.
	 * 
	 * @param value
	 *            Новое значение.
	 */
	public abstract void set(IContext context, Object value);

	/**
	 * <p>
	 * Получить значение класса, установленное функцией
	 * {@link #setType(DataType)}.
	 * </p>
	 * 
	 * @return Тип данных для значения переменной.
	 */
	public abstract DataType getType();

	/**
	 * <p>
	 * Очень часто известно заранее, какого класса значение должно храниться в
	 * контейнере. С помощью данного метода происходит уведомление об этом
	 * класса контейнера. После установки типа все значения, которые будут
	 * читаться и устанавливаться, будут приводиться к данному типу.
	 * </p>
	 * 
	 * @param type
	 *            Класс, которому должно соответствовать значение в контейнере.
	 */
	public abstract void setType(DataType type);

}