package q4s.dafobi.tools;

import java.util.Collection;

/**
 * Утилиты для работы с коллекциями.
 * 
 * @author vbogdanov
 */
public class CollectionTools {

	/**
	 * Данный класс является просто пакетом статических функций.
	 */
	private CollectionTools() {
	}

	/**
	 * Склеивание строкового представления элементов коллекции через разделитель
	 * в одну строку.
	 * 
	 * @param source
	 *            Исходная коллекция элементов.
	 * 
	 * @param delimiter
	 *            Разделитель.
	 * 
	 * @return Итоговая строка.
	 * 
	 */
	public static <R extends Object> String concat(final Collection<R> source,
			String delimiter) {
		if (source == null) {
			return null;
		}

		if (delimiter == null) {
			delimiter = "";
		}

		StringBuffer result = new StringBuffer();
		for (Object object : source) {
			result.append(delimiter).append(object);
		}
		if (result.length() == 0) {
			return "";
		} else {
			return result.substring(delimiter.length());
		}
	}
}
