package org.q4s.dafobi.tools;

import java.util.Objects;

/**
 * @Deprecated Методы можно найти в классе {@link Objects}
 */
public class ObjectTools {

	private ObjectTools() {
	}

	/**
	 * Функция используется для сравнения двух объектов. При этом Null считается
	 * валидным значением и два объекта Null считаются равными.
	 * 
	 * @deprecated Метод избыточен. Вместо него следует использовать стандартный
	 *             {@link Objects#equals(Object, Object)}.
	 * 
	 * @param obj1
	 *            Первый объект.
	 * 
	 * @param obj2
	 *            Второй объект.
	 * 
	 * @return true, если объекты равны (или оба Null).
	 */
	public static <R> boolean equal(final R obj1, final R obj2) {
		return obj1 == null ? obj2 == null : obj1.equals(obj2);
	}

	/**
	 * Функция используется для сравнения двух объектов. При этом Null считается
	 * валидным значением, являющимся наименьшим из всех, а два объекта Null
	 * считаются равными.
	 * 
	 * @param obj1
	 *            Первый объект.
	 * 
	 * @param obj2
	 *            Второй объект.
	 * 
	 * @return 0, если объекты равны; -1, если первый меньше второго; 1, если
	 *         первый больше второго.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <R extends Comparable> int compare(final R obj1, final R obj2) {
		if (obj1 == obj2) {
			return 0;

		} else if (obj1 == null) {
			return -1;

		} else if (obj2 == null) {
			return 1;

		} else {
			return obj1.compareTo(obj2);
		}
	}
}
