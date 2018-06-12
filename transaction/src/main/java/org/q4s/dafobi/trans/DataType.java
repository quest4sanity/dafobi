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
package org.q4s.dafobi.trans;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Типы данных, с которыми умеет работать система, а так же ряда методов для
 * конвертации их в класс, символьные значения и пр.
 * 
 * @author Q4S
 * 
 */
public enum DataType {

	/**
	 * Строковый тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#VARCHAR}. Значения данного типа хранятся в классе
	 * {@link String}.
	 */
	STRING(java.sql.Types.VARCHAR, String.class) {
		@Override
		public String convert(Object sourceValue) {
			return convertValueToString(sourceValue);
		}
	},

	/**
	 * Тип данных "Длинная строка". Он связан с JDBC типом
	 * {@link java.sql.Types#CLOB}. Значения данного типа хранятся в классе
	 * {@link String}.
	 */
	LONGSTRING(java.sql.Types.CLOB, String.class) {
		@Override
		public String convert(Object sourceValue) {
			return convertValueToString(sourceValue);
		}
	},

	/**
	 * Целочисленный тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#INTEGER}. Значения данного типа хранятся в классе
	 * {@link Integer}.
	 */
	INTEGER(java.sql.Types.INTEGER, Integer.class) {
		@Override
		public Integer convert(Object sourceValue) {
			return convertValueToLong(sourceValue).intValue();
		}
	},

	/**
	 * Целочисленный тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#BIGINT}. Значения данного типа хранятся в классе
	 * {@link Long}.
	 */
	LONG(java.sql.Types.BIGINT, Long.class) {
		@Override
		public Long convert(Object sourceValue) {
			return convertValueToLong(sourceValue);
		}
	},

	/**
	 * Числовой тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#NUMERIC}. Значения данного типа хранятся в классе
	 * {@link java.math.BigDecimal}.
	 */
	NUMBER(java.sql.Types.NUMERIC, BigDecimal.class) {
		@Override
		public BigDecimal convert(Object sourceValue) {
			return convertValueToBigDecimal(sourceValue);
		}
	},

	/**
	 * Тип данных "Дата". Он связан с JDBC типом {@link java.sql.Types.DATE}.
	 * Значения данного типа хранятся в классе {@link java.sql.Types.Date}.
	 */
	DATE(java.sql.Types.DATE, Date.class) {
		@Override
		public Date convert(Object sourceValue) {
			// TODO вообще-то надо делать проверку на наличие времени.
			return new Date(convertValueToTimestamp(sourceValue).getTime());
		}
	},

	/**
	 * Тип данных "Время". Он связан с JDBC типом {@link java.sql.Types#TIME} .
	 * Значения данного типа хранятся в классе {@link java.sql.Types.Time}.
	 */
	TIME(java.sql.Types.TIME, Time.class) {
		@Override
		public Time convert(Object sourceValue) {
			// TODO вообще-то надо делать проверку на наличие даты.
			return new Time(convertValueToTimestamp(sourceValue).getTime());
		}
	},

	/**
	 * Тип данных "Дата-время". Он связан с JDBC типом
	 * {@link java.sql.Types#TIMESTAMP}. Значения данного типа хранятся в классе
	 * {@link java.sql.Types.Timestamp}.
	 */
	DATETIME(java.sql.Types.TIMESTAMP, Timestamp.class) {
		@Override
		public Timestamp convert(Object sourceValue) {
			return convertValueToTimestamp(sourceValue);
		}
	};

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
	private DataType(int jdbcType, Class<?> typeClass) {
		this.jdbcType = jdbcType;
		this.typeClass = typeClass;
	}

	/**
	 * @return Java класс в котором хрянятся значения типа данных.
	 */
	public Class<?> typeClass() {
		return this.typeClass;
	}

	/**
	 * @return Константа, определяющая тип данных в JDBC, связанная с типом
	 *         данных.
	 */
	public int jdbcType() {
		return this.jdbcType;
	}

	/**
	 * Метод преобразует переданное в параметре значение в текущий тип данных.
	 * 
	 * @param sourceValue
	 *            Значение, которое надо привести к текущему типу.
	 * 
	 * @return Значение, приведенное к текущему типу данных.
	 */
	public abstract Object convert(Object sourceValue);

	/**
	 * Метод связывает значение параметра с типом данных в одном объекте
	 * {@link DataParam} . В таком виде значение может быть использовано в
	 * методах {@link IStatement} и {@link ITransaction}.
	 * 
	 * @param value
	 *            Значение данных.
	 * 
	 * @return Пара тип-значение для использования в операторах транзакции.
	 */
	public DataParam param(Object value) {
		if (value == null) {
			return new DataParam(this, null);

		} else if (this.typeClass.isAssignableFrom(value.getClass())) {
			return new DataParam(this, value);
		}
		throw new IllegalArgumentException(new StringBuilder("Неверный тип данных").toString());
	}

	/**
	 * <p>
	 * Функция по классу типа возвращает сам тип данных.Если с данным классом
	 * связано несколько типов данных, то возвращен будет первый по порядку.
	 * </p>
	 * 
	 * @param clazz
	 *            Класс переменной.
	 * 
	 * @return Тип данных.
	 */
	public static DataType valueOf(Class<?> clazz) {
		DataType[] values = values();
		int count = values().length;
		for (int i = 0; i < count; i++) {
			Class<?> baseClazz = values[i].typeClass;
			if (baseClazz.isAssignableFrom(clazz)) {
				return values[i];
			}
		}

		throw new RuntimeException(new StringBuilder("Class ").append(clazz.getName())
				.append(" is not supported as DataType yet").toString());
	}

	/**
	 * <p>
	 * Функция по типу данных JDBC возвращает сам тип данных. Если с данным JDBC
	 * типом связано несколько типов данных, то возвращен будет первый по
	 * порядку.
	 * </p>
	 * 
	 * @param jdbcType
	 *            Тип JDBC.
	 * 
	 * @return Тип данных.
	 */
	public static DataType valueOf(final int jdbcType) {
		DataType[] values = values();
		int count = values().length;
		for (int i = 0; i < count; i++) {
			if (values[i].jdbcType == jdbcType) {
				return values[i];
			}
		}

		throw new RuntimeException(new StringBuilder("JDBC type ").append(jdbcType)
				.append(" is not supported as DataType yet").toString());
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
	public static String toName(Class<?> clazz) {
		return valueOf(clazz).name();
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
	public static String toName(int jdbcType) {
		return valueOf(jdbcType).name();
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
	public static Class<?> toClass(String typeCode) {
		return valueOf(typeCode).typeClass;
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
	public static Class<?> toClass(int jdbcType) {
		return valueOf(jdbcType).typeClass;
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
	public static int toJdbcType(String typeCode) {
		return valueOf(typeCode).jdbcType;
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
	public static int toJdbcType(Class<?> clazz) {
		return valueOf(clazz).jdbcType;
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

		throw new IllegalArgumentException();
	}

	/**
	 * <p>
	 * Функция на основании типа данных и строкового значения формирует значение
	 * правильного класса.
	 * </p>
	 * 
	 * @param parType
	 *            Тип данных, в который надо сконверитровать строковое значение.
	 * 
	 * @param parValue
	 *            Строковое значение.
	 * 
	 * @return Значение
	 * 
	 * @deprecated Этот метод заменен теперь методом {@link #convert(Object)}.
	 */
	private static Object parseValue(final DataType parType, final String parValue) {
		Object value;
		if (parValue == null) {
			value = null;

		} else {
			switch (parType) {
			case STRING:
			case LONGSTRING:
				value = parValue;
				break;

			case INTEGER:
				value = Integer.valueOf(parValue);
				break;

			case LONG:
				value = Long.valueOf(parValue);
				break;

			case NUMBER:
				value = BigDecimal.valueOf(Double.valueOf(parValue));
				break;

			case TIME:
				value = Time.valueOf(parValue);
				break;

			case DATE:
				value = Date.valueOf(parValue);
				break;

			case DATETIME:
				value = Timestamp.valueOf(parValue);
				break;

			default:
				throw new RuntimeException("ValueHoldersStorage with type " + parType + " are not supported");
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
	private static Object convertValue(Object sourceValue, Class<?> destClass) {
		if (sourceValue == null) {
			return null;
		}
		
		DataType type;
		Object value = sourceValue;
		if (destClass.isAssignableFrom(DataParam.class)) {
			DataParam param = (DataParam) sourceValue;
			type = param.getType();
			value = param.getValue();
		} 
		
		if (destClass.isAssignableFrom(sourceValue.getClass())) {
			return sourceValue;

		} else if (String.class.isAssignableFrom(destClass)) {
			return convertValueToString(sourceValue);

		} else if (BigDecimal.class.isAssignableFrom(destClass)) {
			return convertValueToBigDecimal(sourceValue);

		} else if (Boolean.class.isAssignableFrom(destClass)) {
			return convertValueToBoolean(sourceValue);

		} else if (Timestamp.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);

		} else if (Time.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);

		} else if (Date.class.isAssignableFrom(destClass)) {
			return convertValueToTimestamp(sourceValue);
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения в строку.
	 */
	private static String convertValueToString(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} 

		Object value = sourceValue;
		if(sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		}
		
		if (value instanceof String || value instanceof BigDecimal || value instanceof Integer
				|| value instanceof Long || value instanceof Number) {
			return value.toString();

		} else if (value instanceof Date || value instanceof Time) {
			throw new UnsupportedOperationException();

		} else if (value instanceof Timestamp) {
			return value.toString().replaceAll("\\.[0-9]+$", "");
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения в целочисленное число.
	 */
	private static Long convertValueToLong(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} 

		Object value = sourceValue;
		if(sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		}

		if (value instanceof String) {
			return new Long((String) value);

		} else if (value instanceof BigDecimal) {
			BigDecimal val = (BigDecimal) value;
			if (val.scale() == 0) {
				return val.longValue();
			} else {
				throw new IllegalArgumentException();
			}

		} else if (value instanceof Long) {
			return (Long) value;

		} else if (value instanceof Integer) {
			return ((Integer) value).longValue();

		} else if (value instanceof Double) {
			Double val = (Double) value;
			Double floorVal = Math.floor(val);
			if ((val - floorVal) == 0) {
				return floorVal.longValue();
			} else {
				throw new IllegalArgumentException();
			}

		} else if (value instanceof Float) {
			Float val = (Float) value;
			Double floorVal = Math.floor(val);
			if ((val - floorVal) == 0) {
				return floorVal.longValue();
			} else {
				throw new IllegalArgumentException();
			}

		} else if (value instanceof Short) {
			return ((Short) value).longValue();

		} else if (value instanceof Number) {
			Number val = (Number) value;
			Double floorVal = Math.floor(val.doubleValue());
			return floorVal.longValue();
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения в число.
	 */
	private static BigDecimal convertValueToBigDecimal(Object sourceValue) {
		if (sourceValue == null) {
			return null;

		} 
		
		Object value = sourceValue;
		if(sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		}

		if (value instanceof String) {
			return new BigDecimal((String) value);

		} else if (value instanceof BigDecimal) {
			return (BigDecimal) value;

		} else if (value instanceof Long) {
			return new BigDecimal((Long) value);

		} else if (value instanceof Integer) {
			return new BigDecimal((Long) value);

		} else if (value instanceof Double) {
			return new BigDecimal((Double) value);

		} else if (value instanceof Float) {
			return new BigDecimal((Float) value);

		} else if (value instanceof Short) {
			return new BigDecimal((Short) value);

		} else if (value instanceof Number) {
			return new BigDecimal(((Number) value).toString());
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения в дату-время.
	 */
	private static Timestamp convertValueToTimestamp(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} 
		
		Object value = sourceValue;
		if(sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		}

		if (value instanceof Timestamp) {
			return (Timestamp) value;

		} else if (value instanceof String) {
			String strValue = ((String) value).replaceAll("\\.[0-9]+$", "");
			Timestamp ts = Timestamp.valueOf(strValue);
			return ts;

		} else if (value instanceof Date) {
			Date dateValue = (Date) value;
			Timestamp ts = new Timestamp(dateValue.getTime());
			return ts;

		} else if (value instanceof java.util.Date) {
			java.util.Date dateValue = (java.util.Date) value;
			Timestamp ts = new Timestamp(dateValue.getTime());
			return ts;
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения в логический тип.
	 * 
	 * @deprecated Типу данных Boolean не место в этом объекте.
	 */
	private static Boolean convertValueToBoolean(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		} 
		
		Object value = sourceValue;
		if(sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		}

		if (value instanceof Boolean) {
			return (Boolean) value;

		} else if (value instanceof BigDecimal) {
			return !new BigDecimal(0).equals(value);

		} else if (value instanceof Long) {
			return !new Long(0).equals(((Long) value));

		} else if (value instanceof String) {
			switch (((String) value).toUpperCase()) {
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
}
