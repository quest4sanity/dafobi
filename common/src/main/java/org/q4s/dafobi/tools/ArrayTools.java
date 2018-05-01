/**
 * 
 */
package org.q4s.dafobi.tools;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * Утилиты для работы с массивами.
 * 
 * @author vbogdanov
 */
public class ArrayTools {

	/**
	 * Данный класс является просто пакетом статических функций.
	 */
	private ArrayTools() {
	}

	/**
	 * Искать элемент в массиве.
	 * 
	 * @param arr
	 *            Массив, в котором ищется элемент.
	 * 
	 * @param needle
	 *            Элемент, который ищется в массиве.
	 * 
	 * @return Позиция в массиве данного элемента, или -1, если элемент не
	 *         найден.
	 */
	public static <R extends Object> int search(R[] arr, R needle) {
		if (arr == null) {
			return -1;
		}

		for (int i = 0; i < arr.length; i++) {
			if (Objects.equals(arr[i], needle)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * <p>
	 * Склеить несколько однородных массивов в один массив.
	 * </p>
	 * 
	 * @param arrs
	 *            Массив массивов.
	 * 
	 * @return Массив, полученный в результате конкатенации всех переданных
	 *         массивов.
	 */
	@SafeVarargs
	public static <R extends Object> R[] concat(R[]... arrs) {
		int count = arrs.length;

		if (count == 1) {
			return arrs[0].clone();
		} else {
			// Получим число элементов в склееном массиве.
			int total = 0;
			// Получим так же пример элемента (от него нужен только объект
			// класса).
			R someItem = null;
			for (int i = 0; i < count; i++) {
				int len = arrs[i].length;
				total += len;

				if (someItem == null && len > 0) {
					someItem = arrs[i][0];
				}
			}

			@SuppressWarnings("unchecked")
			R[] result = (R[]) Array.newInstance(someItem.getClass(), total);
			int position = 0;
			for (int i = 0; i < count; i++) {
				int len = arrs[i].length;
				System.arraycopy(arrs[i], 0, result, position, len);
				position += len;
			}

			return result;
		}
	}

	/**
	 * Склеивание элементов строкового массива через разделитель в одну строку.
	 * 
	 * @param source
	 *            Исходный массив элементов.
	 * 
	 * @param delimiter
	 *            Разделитель.
	 * 
	 * @return Итоговая строка.
	 * 
	 */
	public static String join(final String[] source, final String delimiter) {
		if (source == null) {
			return null;

		} else if (source.length == 0) {
			return "";

		} else {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < source.length; i++) {
				result.append(source[i]).append(delimiter);
			}
			return result.substring(0, result.length() - delimiter.length());
		}
	}
}
