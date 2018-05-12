package org.q4s.dafobi.trans;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.q4s.dafobi.exception.HasNotDoneYetException;

/**
 * Данный класс содержит список констант для типов данных, с которыми умеет
 * работать система, а так же ряда методов для конвертации их в класс,
 * символьные значения и пр.
 * 
 * @author Q4S
 * 
 */
public final class DataType {

	/**
	 * Строковый тип данных
	 */
	public final static DataType STRING = new DataType("STRING",
			java.sql.Types.VARCHAR, String.class);

	/**
	 * Целочисленный тип данных.
	 */
	public final static DataType INTEGER = new DataType("INTEGER",
			java.sql.Types.INTEGER, Long.class);

	/**
	 * Числовой тип данных.
	 */
	public final static DataType NUMBER = new DataType("NUMBER",
			java.sql.Types.NUMERIC, BigDecimal.class);

	/**
	 * Тип данных "Дата"
	 */
	public final static DataType DATE = new DataType("DATE",
			java.sql.Types.DATE, Date.class);

	/**
	 * Тип данных "Время"
	 */
	public final static DataType TIME = new DataType("TIME",
			java.sql.Types.TIME, Time.class);

	/**
	 * Тип данных "Дата-время"
	 */
	public final static DataType TIMESTAMP = new DataType("TIMESTAMP",
			java.sql.Types.TIMESTAMP, Timestamp.class);

	/**
	 * Тип данных "Большая строка"
	 * 
	 * TODO Надо переименовать в LONGSTRING
	 */
	public final static DataType CLOB = new DataType("CLOB",
			java.sql.Types.CLOB, String.class);

	/**
	 * Список всех определенных типов данных.
	 */
	private final static DataType[] types = { STRING, INTEGER, NUMBER, DATE,
			TIME, TIMESTAMP, CLOB };

	/**
	 * Строковое значение типа данных.
	 */
	private final String typeCode;

	/**
	 * Значение типа данных в JDBC
	 */
	private final int jdbcType;

	/**
	 * Класс хранения типа данных.
	 */
	private final Class<?> typeClass;

	/**
	 * Конструктор создания типа данных.
	 * 
	 * @param typeCode
	 *            Строковое значение типа данных.
	 * 
	 * @param jdbcType
	 *            Значение типа данных в JDBC
	 * 
	 * @param typeClass
	 *            Класс хранения типа данных.
	 */
	private DataType(String typeCode, int jdbcType, Class<?> typeClass) {
		this.typeCode = typeCode;
		this.jdbcType = jdbcType;
		this.typeClass = typeClass;
	}

	/**
	 * <p>
	 * Функция по типу данных возвращает его символьный код.
	 * </p>
	 * 
	 * @return Символьный код тип данных.
	 */
	public String getTypeCode() {
		return this.typeCode;
	}

	/**
	 * <p>
	 * Функция по типу данных возвращает его Java класс.
	 * </p>
	 * 
	 * @return Java класс типа данных.
	 */
	public Class<?> getTypeClass() {
		return this.typeClass;
	}

	/**
	 * <p>
	 * Функция возвращает тип JDBC типа данных.
	 * </p>
	 * 
	 * @return Константа, определяющая тип данных в JDBC.
	 */
	public int getJdbcType() {
		return this.jdbcType;
	}

	/**
	 * <p>
	 * Функция по символическому имени типа возвращает сам тип данных.
	 * </p>
	 * 
	 * @param typeCode
	 *            Символьный код тип данных.
	 * 
	 * @return Тип данных.
	 */
	public static DataType typeOf(final String typeCode) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].typeCode.equals(typeCode)) {
				return types[i];
			}
		}

		throw new RuntimeException("Тип данных с именем " + typeCode
				+ " не существует.");
	}

	/**
	 * <p>
	 * Функция по классу типа возвращает сам тип данных.
	 * </p>
	 * 
	 * @param clazz
	 *            Класс переменной.
	 * 
	 * @return Тип данных.
	 */
	public static DataType typeOf(Class<?> clazz) {
		for (int i = 0; i < types.length; i++) {
			Class<?> baseClazz = types[i].typeClass;
			if (baseClazz.isAssignableFrom(clazz)) {
				return types[i];
			}
		}

		throw new RuntimeException("Класс " + clazz.getName()
				+ " не поддерживается системой в качестве типа данных.");
	}

	/**
	 * <p>
	 * Функция по типу данных JDBC возвращает сам тип данных.
	 * </p>
	 * 
	 * @param jdbcType
	 *            Тип JDBC.
	 * 
	 * @return Тип данных.
	 */
	public static DataType typeOf(final int jdbcType) {
		int typeTosearch = jdbcType;
		// Приведение разнородных типов
		switch (jdbcType) {
		case java.sql.Types.BIGINT:
			typeTosearch = INTEGER.jdbcType;
			break;
		}

		for (int i = 0; i < types.length; i++) {
			if (types[i].jdbcType == typeTosearch) {
				return types[i];
			}
		}

		throw new RuntimeException("JDBC type " + jdbcType
				+ " is not supported yet");
	}

	/**
	 * <p>
	 * Функция по классу типа данных возвращает его символьный код.
	 * </p>
	 * 
	 * @param clazz
	 *            Класс переменной.
	 * 
	 * @return Символьный код тип данных.
	 */
	public static String convertToCode(Class<?> clazz) {
		return typeOf(clazz).typeCode;
	}

	/**
	 * <p>
	 * Функция по типу JDBC типа данных возвращает его символьный код.
	 * </p>
	 * 
	 * @param jdbcType
	 *            Тип данных JDBC.
	 * 
	 * @return Символьный код тип данных.
	 */
	public static String convertToCode(int jdbcType) {
		return typeOf(jdbcType).typeCode;
	}

	/**
	 * <p>
	 * Функция по имени типа данных возвращает его класс.
	 * </p>
	 * 
	 * @param typeCode
	 *            Символьный код типа данных.
	 * 
	 * @return Java класс типа данных.
	 */
	public static Class<?> convertToClass(String typeCode) {
		return typeOf(typeCode).typeClass;
	}

	/**
	 * <p>
	 * Функция по типу JDBC типа данных возвращает его класс.
	 * </p>
	 * 
	 * @param jdbcType
	 *            Тип данных JDBC.
	 * 
	 * @return Java класс типа данных.
	 */
	public static Class<?> convertToClass(int jdbcType) {
		return typeOf(jdbcType).typeClass;
	}

	/**
	 * <p>
	 * Функция по классу типа данных возвращает его символьный код.
	 * </p>
	 * 
	 * @param typeCode
	 *            Символьный код типа данных.
	 * 
	 * @return Константа, определяющая тип данных в JDBC.
	 */
	public static int convertToJdbcType(String typeCode) {
		return typeOf(typeCode).jdbcType;
	}

	/**
	 * <p>
	 * Функция по классу типа данных возвращает его символьный код.
	 * </p>
	 * 
	 * @param clazz
	 *            Класс переменной.
	 * 
	 * @return Константа, определяющая тип данных в JDBC.
	 */
	public static int convertToJdbcType(Class<?> clazz) {
		return typeOf(clazz).jdbcType;
	}

	/**
	 * <p>
	 * Функция на основании типа данных и строкового значения формирует значение
	 * параметра нужного типа.
	 * </p>
	 * 
	 * @param parType
	 *            Java класс типа параметра.
	 * 
	 * @param parValue
	 *            Значение параметра.
	 * @return
	 * 
	 *         TODO Неверное описание. Да и сам код надо будет исправить.
	 */
	public static Object parseValue(final String parType, final String parValue) {
		Object value;
		if (parValue == null) {
			value = null;

		} else {
			switch (parType.toLowerCase()) {
			case "string":
				value = parValue;
				break;

			case "number":
				value = BigDecimal.valueOf(Double.valueOf(parValue));
				break;

			case "timestamp":
				value = Timestamp.valueOf(parValue);
				break;

			case "time":
				value = Time.valueOf(parValue);
				break;

			case "date":
				value = Date.valueOf(parValue);
				break;

			default:
				throw new RuntimeException("ValueHoldersStorage with type "
						+ parType + " are not supported");
			}
		}
		return value;
	}

	/**
	 * <p>
	 * Метод конвертирует значение между типами данных.
	 * </p>
	 * <p>
	 * Внимание!!! В виду хронической нехватки времени данный код написан далеко
	 * не полностью, так что он может во время выполнения выдавать ошибки. В
	 * этом случае придется сесть на попу, вкурить, что не доделано и доделать.
	 * </p>
	 * 
	 * @param sourceValue
	 *            Исходное значение, которо надо сконвертировать.
	 * 
	 * @param destClass
	 *            Класс, в который надо сконвертировать значение.
	 * 
	 * @return Сконвертированное в нужный класс значение.
	 */
	public static Object convertValue(Object sourceValue, Class<?> destClass) {
		if (sourceValue == null) {
			return null;

		} else if (destClass.isAssignableFrom(sourceValue.getClass())) {
			return sourceValue;

		} else if (String.class.isAssignableFrom(destClass)) {
			return convertValueToString(sourceValue);

		} else if (BigDecimal.class.isAssignableFrom(destClass)) {
			return convertValueToNumber(sourceValue);

		} else if (Boolean.class.isAssignableFrom(destClass)) {
			return convertValueToBoolean(sourceValue);

		} else if (Timestamp.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);

		} else if (Time.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);

		} else if (Date.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);
		}

		throw new HasNotDoneYetException();
	}

	/**
	 * Конвертирование значения в строку.
	 */
	public static String convertValueToString(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} else if (sourceValue instanceof String) {
			return (String) sourceValue;

		} else if (sourceValue instanceof BigDecimal) {
			return sourceValue.toString();

		} else if (sourceValue instanceof Long) {
			return sourceValue.toString();

		} else if (sourceValue instanceof Boolean) {
			return sourceValue == null ? null : (((Boolean) sourceValue) ? "Д"
					: "Н");
		} else if (sourceValue instanceof Timestamp) {
			return sourceValue.toString().replaceAll("\\.[0-9]+$", "");

		}

		throw new HasNotDoneYetException();
	}

	/**
	 * Конвертирование значения в число.
	 */
	public static BigDecimal convertValueToNumber(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} else if (sourceValue instanceof BigDecimal) {
			return (BigDecimal) sourceValue;

		} else if (sourceValue instanceof Long) {
			return new BigDecimal(((Long) sourceValue).longValue());

		} else if (sourceValue instanceof String) {
			return new BigDecimal((String) sourceValue);

		} else if (sourceValue instanceof Boolean) {
			return sourceValue == null ? null : new BigDecimal(
					((Boolean) sourceValue) ? 1 : 0);
		}

		throw new HasNotDoneYetException();
	}

	/**
	 * Конвертирование значения в логический тип.
	 */
	public static Boolean convertValueToBoolean(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} else if (sourceValue instanceof Boolean) {
			return (Boolean) sourceValue;

		} else if (sourceValue instanceof BigDecimal) {
			return !new BigDecimal(0).equals(sourceValue);

		} else if (sourceValue instanceof Long) {
			return !new Long(0).equals(((Long) sourceValue));

		} else if (sourceValue instanceof String) {
			switch (((String) sourceValue).toUpperCase()) {
			case "Д":
			case "Y":
			case "T":
			case "TRUE":
				return true;

			case "Н":
			case "N":
			case "F":
			case "FALSE":
				return false;
			}
		}
		return null;
	}

	/**
	 * Конвертирование значения в дату-время.
	 */
	public static Timestamp convertValueToTimestamp(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} else if (sourceValue instanceof Timestamp) {
			return (Timestamp) sourceValue;

		} else if (sourceValue instanceof String) {
			String value = ((String) sourceValue).replaceAll("\\.[0-9]+$", "");
			Timestamp ts = Timestamp.valueOf(value);
			return ts;

		} else if (sourceValue instanceof Date) {
			Date dateValue = (Date) sourceValue;
			Timestamp ts = new Timestamp(dateValue.getTime());
			return ts;

		} else if (sourceValue instanceof java.util.Date) {
			java.util.Date dateValue = (java.util.Date) sourceValue;
			Timestamp ts = new Timestamp(dateValue.getTime());
			return ts;
		}

		throw new HasNotDoneYetException();
	}

	/**
	 * Сравнение двух объектов, используя знание об их типах даных.
	 * 
	 * @param obj1
	 * 
	 * @param obj2
	 * 
	 * @return
	 */
	public static int compare(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return 0;

		} else if (obj1 == null) {
			return -1;

		} else if (obj2 == null) {
			return 1;

		} else if (obj1 instanceof String && obj2 instanceof String) {
			return ((String) obj1).compareTo((String) obj2);

		} else if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return ((BigDecimal) obj1).compareTo((BigDecimal) obj2);

		} else if (obj1 instanceof Long && obj2 instanceof Long) {
			return ((Long) obj1).compareTo((Long) obj2);

		} else if (obj1 instanceof Timestamp && obj2 instanceof Timestamp) {
			return ((Timestamp) obj1).compareTo((Timestamp) obj2);

		} else if (obj1 instanceof Boolean && obj2 instanceof Boolean) {
			return ((Boolean) obj1).compareTo((Boolean) obj2);
		}

		throw new HasNotDoneYetException();
	}
}
