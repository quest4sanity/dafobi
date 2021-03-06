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
package q4s.dafobi.jaxb.common;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import q4s.dafobi.common.DataParam;

/**
 * Стандартные типы данных, с которыми умеет работать система. Каждый из типов
 * содержит в себе знание о том, в каких переменных хранятся значения этого
 * типа, с каким типом данных JDBC он связан. Кроме того, одной из способностей
 * типов данных является приведение значений других типов к указанному.
 * 
 * @author Q4S
 * 
 */
@XmlType(name = "dataType")
@XmlEnum
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

	// /**
	// * Тип данных "Длинная строка". Он связан с JDBC типом
	// * {@link java.sql.Types#LONGVARBINARY}. Значения данного типа хранятся в
	// классе
	// * {@link String}.
	// */
	// STREAM(java.sql.Types.LONGVARBINARY, String.class) {
	// @Override
	// public String convert(Object sourceValue) {
	// return convertValueToString(sourceValue);
	// }
	// },

	/**
	 * Короткий целочисленный тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#TINYINT}. Значения данного типа хранятся в классе
	 * {@link java.lang.Character}.
	 */
	CHAR(java.sql.Types.CHAR, Character.class) {
		@Override
		public Integer convert(Object sourceValue) {
			// TODO Надо реализовать конвертацию.
			return null; // convertValueToLong(sourceValue).intValue();
		}
	},

	/**
	 * Малый целочисленный тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#SMALLINT}. Значения данного типа хранятся в классе
	 * {@link java.lang.Short}.
	 */
	SHORT(java.sql.Types.SMALLINT, Short.class) {
		@Override
		public Integer convert(Object sourceValue) {
			// TODO Надо реализовать конвертацию.
			return null; // convertValueToLong(sourceValue).intValue();
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
	 * Целочисленный тип данных повышенной разрядности. Он связан с JDBC типом
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
	 * Числовой тип данных с плавающей точкой. Он связан с JDBC типом
	 * {@link java.sql.Types#FLOAT}. Значения данного типа хранятся в классе
	 * {@link java.lang.Float}.
	 */
	FLOAT(java.sql.Types.FLOAT, Float.class) {
		@Override
		public BigDecimal convert(Object sourceValue) {
			// TODO Надо реализовать конвертацию.
			return null; // convertValueToBigDecimal(sourceValue);
		}
	},

	/**
	 * Числовой тип данных с плавающей точкой повышенной точности. Он связан с
	 * JDBC типом {@link java.sql.Types#DOUBLE}. Значения данного типа хранятся
	 * в классе {@link java.lang.Double}.
	 */
	DOUBLE(java.sql.Types.DOUBLE, Double.class) {
		@Override
		public BigDecimal convert(Object sourceValue) {
			// TODO Надо реализовать конвертацию.
			return null; // convertValueToBigDecimal(sourceValue);
		}
	},

	/**
	 * Числовой тип данных. Он связан с JDBC типом
	 * {@link java.sql.Types#NUMERIC}. Значения данного типа хранятся в классе
	 * {@link java.math.BigDecimal}.
	 */
	DECIMAL(java.sql.Types.NUMERIC, BigDecimal.class) {
		@Override
		public BigDecimal convert(Object sourceValue) {
			return convertValueToDecimal(sourceValue);
		}
	},

	/**
	 * Тип данных "Дата". Он связан с JDBC типом {@link java.sql.Types.DATE}.
	 * Значения данного типа хранятся в классе {@link java.sql.Date}.
	 */
	DATE(java.sql.Types.DATE, Date.class) {
		@Override
		public Date convert(Object sourceValue) {
			return convertValueToDate(sourceValue);
		}
	},

	/**
	 * Тип данных "Время". Он связан с JDBC типом {@link java.sql.Types#TIME} .
	 * Значения данного типа хранятся в классе {@link java.sql.Time}.
	 */
	TIME(java.sql.Types.TIME, Time.class) {
		@Override
		public Time convert(Object sourceValue) {
			return convertValueToTime(sourceValue);
		}
	},

	/**
	 * Тип данных "Дата-время". Он связан с JDBC типом
	 * {@link java.sql.Types#TIMESTAMP}. Значения данного типа хранятся в классе
	 * {@link java.sql.Timestamp}.
	 */
	TIMESTAMP(java.sql.Types.TIMESTAMP, Timestamp.class) {
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
	 * Метод преобразует переданное в параметре значение в значение текущего
	 * типа данных.
	 * 
	 * @param sourceValue
	 *            Значение, которое надо привести к текущему типу данных.
	 * 
	 * @return Значение, приведенное к текущему типу данных.
	 */
	public abstract Object convert(Object sourceValue);

	/**
	 * Метод связывает значение в параметре с текущим типом данных в одном
	 * объекте {@link DataParam} .
	 * 
	 * @param value
	 *            Значение данных.
	 * 
	 * @return Пара тип-значение.
	 */
	public DataParam param(Object value) {
		if (value == null) {
			return new DataParam(this, null);

		} else if (this.typeClass.isAssignableFrom(value.getClass())) {
			return new DataParam(this, value);
		}
		throw new IllegalArgumentException(MessageFormat.format("Класс {0} не может быть преобразован к типу {1}.",
				value.getClass().getName(), this));
	}

	// public static DataType fromValue(String v) {
	// return valueOf(v);
	// }

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

		throw new RuntimeException(MessageFormat.format("Class {0} is not supported as DataType yet", clazz.getName()));
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

		throw new RuntimeException(MessageFormat.format("JDBC type {0} is not supported as DataType yet", jdbcType));
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
	 * Функция по классу типа данных возвращает его тип данных JDBC.
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
	 * Функция по классу типа данных возвращает его тип данных JDBC.
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
	 * 
	 * @deprecated Не уверен, что данному методу здесь место.
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

	// /**
	// * <p>
	// * Функция на основании типа данных и строкового значения формирует
	// значение
	// * правильного класса.
	// * </p>
	// *
	// * @param parType
	// * Тип данных, в который надо сконверитровать строковое значение.
	// *
	// * @param parValue
	// * Строковое значение.
	// *
	// * @return Значение
	// *
	// * @deprecated Этот метод заменен теперь методом {@link #convert(Object)}.
	// */
	// private static Object parseValue(final DataType parType, final String
	// parValue) {
	// Object value;
	// if (parValue == null) {
	// value = null;
	//
	// } else {
	// switch (parType) {
	// case STRING:
	// case LONGSTRING:
	// value = parValue;
	// break;
	//
	// case INTEGER:
	// value = Integer.valueOf(parValue);
	// break;
	//
	// case LONG:
	// value = Long.valueOf(parValue);
	// break;
	//
	// case DECIMAL:
	// value = BigDecimal.valueOf(Double.valueOf(parValue));
	// break;
	//
	// case TIME:
	// value = Time.valueOf(parValue);
	// break;
	//
	// case DATE:
	// value = Date.valueOf(parValue);
	// break;
	//
	// case TIMESTAMP:
	// value = Timestamp.valueOf(parValue);
	// break;
	//
	// default:
	// throw new RuntimeException("ValueHoldersStorage with type " + parType + "
	// are not supported");
	// }
	// }
	// return value;
	// }

	// /**
	// * <p>
	// * Метод конвертирует значение между типами данных.
	// * </p>
	// * <p>
	// * Внимание!!! В виду хронической нехватки времени данный код написан
	// далеко
	// * не полностью, так что он может во время выполнения выдавать ошибки. В
	// * этом случае придется сесть на попу, вкурить, что не доделано и
	// доделать.
	// * </p>
	// *
	// * @param sourceValue
	// * Исходное значение, которо надо сконвертировать.
	// *
	// * @param destClass
	// * Класс, в который надо сконвертировать значение.
	// *
	// * @return Сконвертированное в нужный класс значение.
	// *
	// * @deprecated Этот метод нигде не используется.
	// */
	// private static Object convertValue(Object sourceValue, Class<?>
	// destClass) {
	// if (sourceValue == null) {
	// return null;
	// }
	//
	// DataType type;
	// Object value = sourceValue;
	// if (destClass.isAssignableFrom(DataParam.class)) {
	// DataParam param = (DataParam) sourceValue;
	// type = param.getType();
	// value = param.getValue();
	// }
	//
	// if (destClass.isAssignableFrom(sourceValue.getClass())) {
	// return sourceValue;
	//
	// } else if (String.class.isAssignableFrom(destClass)) {
	// return convertValueToString(sourceValue);
	//
	// } else if (BigDecimal.class.isAssignableFrom(destClass)) {
	// return convertValueToBigDecimal(sourceValue);
	//
	// } else if (Boolean.class.isAssignableFrom(destClass)) {
	// return convertValueToBoolean(sourceValue);
	//
	// } else if (Timestamp.class.isAssignableFrom(destClass)) {
	// return convertValueToTimestamp(sourceValue);
	//
	// } else if (Time.class.isAssignableFrom(destClass)) {
	// return convertValueToTimestamp(sourceValue);
	//
	// } else if (Date.class.isAssignableFrom(destClass)) {
	// return convertValueToTimestamp(sourceValue);
	// }
	//
	// throw new IllegalArgumentException();
	// }

	private static final SimpleDateFormat dateFomat = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat timeFomat = new SimpleDateFormat("hh:mm:ss");

	private static SimpleDateFormat dateTimeFomat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * Конвертирование значения в строку.
	 */
	private static String convertValueToString(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		}

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
		}

		if (value instanceof String) {
			return (String) value;

		} else if (value instanceof Number) {
			return value.toString();

		} else if (value instanceof Date) {
			return dateFomat.format((Date) value);

		} else if (value instanceof Time) {
			return timeFomat.format((Time) value);

		} else if (value instanceof Timestamp) {
			return dateTimeFomat.format((Timestamp) value);
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

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
		}

		if (value instanceof Long) {
			return (Long) value;

		} else if (value instanceof Integer || value instanceof Short || value instanceof Character) {
			return ((Number) value).longValue();

		} else if (value instanceof String) {
			return new Long((String) value);

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

		} else if (value instanceof BigDecimal) {
			BigDecimal val = (BigDecimal) value;
			if (val.scale() == 0) {
				return val.longValue();
			} else {
				throw new IllegalArgumentException();
			}

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
	private static BigDecimal convertValueToDecimal(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		}

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
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
	 * Конвертирование значения в дату.
	 */
	private static Date convertValueToDate(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		}

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
		}

		if (value instanceof Date) {
			return (Date) value;

		} else if (value instanceof java.util.Date) {
			return new Date(((java.util.Date) value).getTime());

		} else if (value instanceof String) {
			try {
				Date dt = new Date(dateFomat.parse((String) value).getTime());
				return dt;

			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}

		throw new IllegalArgumentException();
	}

	/**
	 * Конвертирование значения во время.
	 */
	private static Time convertValueToTime(Object sourceValue) {
		if (sourceValue == null) {
			return null;
		}

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
		}

		if (value instanceof Time) {
			return (Time) value;

		} else if (value instanceof java.util.Date) {
			return new Time(((java.util.Date) value).getTime());

		} else if (value instanceof String) {
			try {
				Time tm = new Time(timeFomat.parse((String) value).getTime());
				return tm;

			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
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

		Object value;
		if (sourceValue instanceof DataParam) {
			value = ((DataParam) sourceValue).getValue();
		} else {
			value = sourceValue;
		}

		if (value instanceof Timestamp) {
			return (Timestamp) value;

		} else if (value instanceof java.util.Date) {
			return new Timestamp(((java.util.Date) value).getTime());

		} else if (value instanceof String) {
			try {
				return new Timestamp(dateTimeFomat.parse((String) value).getTime());

			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		}

		throw new IllegalArgumentException();
	}

	// /**
	// * Конвертирование значения в логический тип.
	// *
	// * @deprecated Типу данных Boolean не место в этом объекте.
	// */
	// private static Boolean convertValueToBoolean(Object sourceValue) {
	// if (sourceValue == null) {
	// return null;
	// }
	//
	// Object value;
	// if (sourceValue instanceof DataParam) {
	// value = ((DataParam) sourceValue).getValue();
	// } else {
	// value = sourceValue;
	// }
	//
	// if (value instanceof Boolean) {
	// return (Boolean) value;
	//
	// } else if (value instanceof BigDecimal) {
	// return !new BigDecimal(0).equals(value);
	//
	// } else if (value instanceof Long) {
	// return !new Long(0).equals(((Long) value));
	//
	// } else if (value instanceof String) {
	// switch (((String) value).toUpperCase()) {
	// case "Д":
	// case "Y":
	// case "T":
	// case "TRUE":
	// return true;
	//
	// case "Н":
	// case "N":
	// case "F":
	// case "FALSE":
	// return false;
	// }
	// }
	// return null;
	// }
}
