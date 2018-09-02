package org.q4s.dafobi.tools;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;

//import oracle.sql.CLOB;

/**
 * <p>
 * Класс содержит ряд методов, которые упрощают работу с объектами LONGSTRING.
 * </p>
 * <p>
 * Данный класс расчитан на работу с драйвером JDBC Oracle.
 * </p>
 * 
 * @author vbogdanov
 * 
 */
public class BusClob {

	private BusClob() {
	}

//	/**
//	 * Метод обертывает строковую константу в контейнер Clob.
//	 * 
//	 * @param connection
//	 *            Подключение к базе данных.
//	 * 
//	 * @param text
//	 *            Текстовое значение.
//	 * 
//	 * @return Значение text, обернутое в контейнер Clob.
//	 * 
//	 * @throws SQLException
//	 *             При ошибках базы данных.
//	 */
//	public static Clob createClob(Connection connection, String text)
//			throws SQLException {
//
//		CLOB oClob = CLOB.createTemporary(connection, false,
//				CLOB.DURATION_SESSION);
//		// clob.open(LONGSTRING.MODE_READWRITE);
//		// try {
//		// Writer tempClobWriter = clob.setCharacterStream(1);
//		// // Its better to create a String reader and stream the data into the
//		// // clob
//		// tempClobWriter.write(text);
//		// tempClobWriter.flush();
//		// tempClobWriter.close();
//		// clob.close();
//		//
//		// } catch (IOException e) {
//		// throw new BusExceptionThatShouldNotHappen(e);
//		// }
//
//		oClob.setString(1, text);
//		return oClob;
//	}
//
//	/**
//	 * Метод возвращает размер текста, содержащегося в контейнере LONGSTRING.
//	 * 
//	 * @param clob
//	 *            Контейнер LONGSTRING.
//	 * 
//	 * @return Размер текста.
//	 * 
//	 * @throws SQLException
//	 *             При ошибках базы данных.
//	 */
//	public static long getLength(Clob clob) throws SQLException {
//		CLOB oClob = (CLOB) clob;
//		return oClob.length();
//	}
//
//	/**
//	 * Метод позволяет извлечь из объекта LONGSTRING хранящуюся в нем текстовую
//	 * строку.
//	 * 
//	 * @param clob
//	 *            Контейнер LONGSTRING.
//	 * 
//	 * @return Хранящаяся в контейнере текстовая строка.
//	 * 
//	 * @throws SQLException
//	 *             При ошибках базы данных.
//	 */
//	public static String getString(Clob clob) throws SQLException {
//		CLOB oClob = (CLOB) clob;
//		// oClob.open(LONGSTRING.MODE_READONLY);
//		// try {
//		// Reader tempClobWriter = clob.getCharacterStream();
//		// // Its better to create a String reader and stream the data into the
//		// // clob
//		// tempClobWriter.write(text);
//		// tempClobWriter.flush();
//		// tempClobWriter.close();
//		// clob.close();
//		//
//		// } catch (IOException e) {
//		// throw new BusExceptionThatShouldNotHappen(e);
//		// }
//
//		return oClob.getSubString(1, (int) oClob.length());
//	}
//
//	/**
//	 * Освобождение ресурсов, занятых объектом LONGSTRING.
//	 * 
//	 * @param clob
//	 *            Текст, обернутый в контейнер Clob.
//	 * 
//	 * @throws SQLException
//	 *             При ошибках базы данных.
//	 */
//	public static void close(Clob clob) throws SQLException {
//		CLOB.freeTemporary((CLOB) clob);
//		// clob.free();
//	}
}
