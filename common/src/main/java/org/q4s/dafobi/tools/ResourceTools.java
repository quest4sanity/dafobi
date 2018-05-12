package org.q4s.dafobi.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * <p>
 * Методы, упрощающие работу с ресурсами приложения. Сюда входят все файлы,
 * находящиеся по пути поиска классов приложения (например: текстовые или xml
 * файлы, sql запросы и т.п.)
 * </p>
 * 
 * @author vbogdanov
 * 
 */
public class ResourceTools {

	/**
	 * Это просто библиотека методов, и не более того.
	 */
	private ResourceTools() {
	}

	/**
	 * <p>
	 * Загрузка текста из файла ресурса (например текста SQL запроса).
	 * </p>
	 * 
	 * <p>
	 * За корень, начиная от которого будет искаться ресурс, берется каталог, в
	 * котором располагается приведенный в параметрах класс.
	 * </p>
	 * 
	 * @param cls
	 *            Класс, каталог которого является началом для поиска файла.
	 * 
	 * @param resourceName
	 *            Имя файла ресурса.
	 * 
	 * @param charsetName
	 *            Имя кодировки, в которой хранится ресурс.
	 * 
	 * @return Содержимое файла ресурса. Если ресурс не найден, то null.
	 * 
	 * @throws MissingResourceException
	 *             Если ресурс не найден или при его чтении возникли ошибки.
	 */
	public static final String getResource(Class<?> cls, String resourceName,
			String charsetName) {

		String content = null;
		try (InputStream inputStream = cls.getResourceAsStream(resourceName)) {
			content = loadStream(inputStream, charsetName);

		} catch (IOException e) {
			throw new MissingResourceException("Ошибка при загрузке ресурса",
					cls.getName(), resourceName);
		}
		return content;
	}

	/**
	 * <p>
	 * Загрузка текста из файла ресурса (например текста SQL запроса).
	 * </p>
	 * 
	 * <p>
	 * За корень, начиная от которого будет искаться ресурс, берется каталог, в
	 * котором располагается приведенный в параметрах класс.
	 * </p>
	 * 
	 * @param cls
	 *            Класс, каталог которого является началом для поиска файла.
	 * 
	 * @param resourceName
	 *            Имя файла ресурса.
	 * 
	 * @return Содержимое файла ресурса. Если ресурс не найден, то null.
	 * 
	 * @throws MissingResourceException
	 *             Если ресурс не найден или при его чтении возникли ошибки.
	 */
	public static final String getResource(Class<?> cls, String resourceName) {
		return getResource(cls, resourceName, "utf-8");
	}

	/**
	 * <p>
	 * Загрузка файла свойств из пакета Java.
	 * </p>
	 * 
	 * @param clazz
	 *            Класса, пакет которого рассматривается как корень каталога.
	 * 
	 * @param resourceName
	 *            Имя ресурса для файла свойств, которые надо загрузить.
	 * 
	 * @return Загруженные свойства.
	 * 
	 * @throws MissingResourceException
	 *             Если ресурс не найден или при его чтении возникли ошибки.
	 */
	public static final Properties loadProperties(Class<?> clazz,
			String resourceName) {
		Properties prop = new Properties();

		try (InputStream inputStream = clazz.getResourceAsStream(resourceName);) {
			prop.load(inputStream);

		} catch (IOException e) {
			throw new MissingResourceException("Ошибка при загрузке ресурса",
					clazz.getName(), resourceName);
		}
		return prop;
	}

	/**
	 * <p>
	 * Загрузка текста из файла.
	 * </p>
	 * 
	 * @param file
	 *            Имя файла.
	 * 
	 * @param charsetName
	 *            Имя кодировки, в которой хранится файл.
	 * 
	 * @return Содержимое файла. Если ресурс не найден, то null.
	 */
	public static String getFile(File file, String charsetName) {
		String content = null;
		try (InputStream inputStream = new FileInputStream(file);) {

			content = loadStream(inputStream, charsetName);

		} catch (IOException e) {
			throw new RuntimeException("Ошибка при загрузке файла "
					+ file.getAbsolutePath(), e);

		}
		return content;
	}

	/**
	 * <p>
	 * Загрузка текста из файла.
	 * </p>
	 * 
	 * @param file
	 *            Имя файла.
	 * 
	 * @return Содержимое файла. Если ресурс не найден, то null.
	 */
	public static String getFile(File file) {
		return getFile(file, "utf-8");
	}

	/**
	 * <p>
	 * Загрузка первых строк из файла.
	 * </p>
	 * 
	 * @param file
	 *            Имя файла.
	 * 
	 * @param charsetName
	 *            Имя кодировки, в которой хранится файл.
	 * 
	 * @param rows
	 *            Число строк, которые надо считать.
	 * 
	 * @return Содержимое первых строк файла. Если ресурс не найден, то null.
	 */
	public static String getFileHeader(File file, String charsetName, int rows) {
		String content = null;
		try (InputStream inputStream = new FileInputStream(file);) {

			content = loadStreamRows(inputStream, charsetName, rows);

		} catch (IOException e) {
			throw new RuntimeException("Ошибка при загрузке файла"
					+ file.getAbsolutePath(), e);
		}
		return content;
	}

	/**
	 * <p>
	 * Загрузка первых строк из файла.
	 * </p>
	 * 
	 * @param file
	 *            Имя файла.
	 * 
	 * @param rows
	 *            Число строк, которые надо считать.
	 * 
	 * @return Содержимое первых строк файла. Если ресурс не найден, то null.
	 */
	public static String getFileHeader(File file, int rows) {
		return getFileHeader(file, "utf-8", rows);
	}

	/**
	 * <p>
	 * Загрузка данных потока, представляющего собой текстовые данные.
	 * </p>
	 * 
	 * @param inputStream
	 *            Входящий поток
	 * 
	 * @return Текстовые данные, считанные из потока.
	 * 
	 * @throws IOException
	 *             При ошибках ввода-вывода.
	 */
	public static String loadStream(InputStream inputStream, String charsetName)
			throws IOException {

		String loadedText = null;

		if (inputStream != null) {
			try (InputStreamReader reader = new InputStreamReader(inputStream,
					charsetName);
					BufferedReader br = new BufferedReader(reader);) {

				String line;
				while ((line = br.readLine()) != null) {
					if (loadedText == null) {
						loadedText = line + "\n";
					} else {
						loadedText += line + "\n";
					}
				}
			}
		}
		return loadedText;
	}

	/**
	 * <p>
	 * Загрузка указанного числа строк из потока текстовых данных.
	 * </p>
	 * 
	 * @param inputStream
	 *            Входящий поток
	 * 
	 * @param charsetName
	 *            Имя кодировки, в которой хранится файл.
	 * 
	 * @param rows
	 *            Число строк, которые надо считать из потока.
	 * 
	 * @return Текстовые данные, считанные из потока.
	 * 
	 * @throws IOException
	 *             При ошибках ввода-вывода.
	 */
	public static String loadStreamRows(InputStream inputStream,
			String charsetName, int rows) throws IOException {

		String loadedText = null;

		if (inputStream != null) {
			try (InputStreamReader reader = new InputStreamReader(inputStream,
					charsetName);
					BufferedReader br = new BufferedReader(reader);) {

				String line;
				while ((line = br.readLine()) != null && rows-- > 0) {
					if (loadedText == null) {
						loadedText = line + "\n";
					} else {
						loadedText += line + "\n";
					}
				}
			}
		}
		return loadedText;
	}
}
