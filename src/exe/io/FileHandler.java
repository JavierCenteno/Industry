/*
 * This software is a city building and resource management strategy game.
 * Copyright (C) 2019 Javier Centeno Vega
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package exe.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.imageio.ImageIO;

import api.Json;
import exe.Exceptions;
import impl.JsonReaderImplementation;
import impl.JsonWriterImplementation;

/**
 *
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class FileHandler {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Default charset.
	 */
	public static final Charset DEFAULT_CHARSET;
	/**
	 * Path to the audio folder.
	 */
	public static final String AUDIO_PATH;
	/**
	 * Path to the configuration folder.
	 */
	public static final String CONFIGURATION_PATH;
	/**
	 * Path to the data folder.
	 */
	public static final String DATA_PATH;
	/**
	 * Path to the font folder.
	 */
	public static final String FONT_PATH;
	/**
	 * Path to the graphic folder.
	 */
	public static final String GRAPHIC_PATH;
	/**
	 * Path to the internationalization folder.
	 */
	public static final String I18N_PATH;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		DEFAULT_CHARSET = StandardCharsets.UTF_8;
		Properties paths = null;
		try {
			// load paths
			paths = new Properties();
			final InputStream pathsStream = FileHandler.class.getResourceAsStream("paths.properties");
			paths.load(pathsStream);
			pathsStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		AUDIO_PATH = paths.getProperty("audio").replace('/', File.separatorChar);
		CONFIGURATION_PATH = paths.getProperty("configuration").replace('/', File.separatorChar);
		DATA_PATH = paths.getProperty("data").replace('/', File.separatorChar);
		FONT_PATH = paths.getProperty("font").replace('/', File.separatorChar);
		GRAPHIC_PATH = paths.getProperty("graphic").replace('/', File.separatorChar);
		I18N_PATH = paths.getProperty("i18n").replace('/', File.separatorChar);
		// set to null so it can be garbage collected
		paths = null;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Loads the class with the given name, throwing any exception generated in the
	 * process as an unchecked exception.
	 *
	 * @param className
	 *                      The name of a class.
	 * @see Class#forName(String, boolean, ClassLoader)
	 */
	public static void loadClass(final String className) {
		try {
			Class.forName(className, true, FileHandler.class.getClassLoader());
		} catch (final ClassNotFoundException exception) {
			throw Exceptions.classNotFoundException(exception);
		}
	}

	/**
	 * Loads the JSON file at the given path as the default charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param path
	 *                 The path to a file represented as a sequence of the different
	 *                 elements of the path.
	 */
	public static Json loadJson(final String... path) {
		return FileHandler.loadJson(FileHandler.DEFAULT_CHARSET, path);
	}

	/**
	 * Loads the JSON file at the given path as the given charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param charset
	 *                    A charset.
	 * @param path
	 *                    The path to a file represented as a sequence of the
	 *                    different elements of the path.
	 */
	public static Json loadJson(final Charset charset, final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileInputStream inputStream = new FileInputStream(joinedPath)) {
			final Json json = new JsonReaderImplementation(inputStream, charset).read();
			return json;
		} catch (final IOException exception) {
			throw Exceptions.loadingException(exception);
		}
	}

	/**
	 * Saves the JSON file at the given path as the default charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param json
	 *                 A JSON file to be stored.
	 * @param path
	 *                 The path to a file represented as a sequence of the different
	 *                 elements of the path.
	 */
	public static void saveJson(final Json json, final String... path) {
		FileHandler.saveJson(json, FileHandler.DEFAULT_CHARSET, path);
	}

	/**
	 * Saves the JSON file at the given path as the given charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param json
	 *                    A JSON file to be stored.
	 * @param charset
	 *                    A charset.
	 * @param path
	 *                    The path to a file represented as a sequence of the
	 *                    different elements of the path.
	 */
	public static void saveJson(final Json json, final Charset charset, final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileOutputStream outputStream = new FileOutputStream(joinedPath)) {
			new JsonWriterImplementation(outputStream, charset).write(json, "\n", "\t", " ");
		} catch (final IOException exception) {
			throw Exceptions.savingException(exception);
		}
	}

	/**
	 * Loads the properties file at the given path as the default charset, joining
	 * the elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param path
	 *                 The path to a file represented as a sequence of the different
	 *                 elements of the path.
	 */
	public static Properties loadProperties(final String... path) {
		return FileHandler.loadProperties(FileHandler.DEFAULT_CHARSET, path);
	}

	/**
	 * Loads the properties file at the given path as the given charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param charset
	 *                    A charset.
	 * @param path
	 *                    The path to a file represented as a sequence of the
	 *                    different elements of the path.
	 */
	public static Properties loadProperties(final Charset charset, final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileInputStream inputStream = new FileInputStream(joinedPath)) {
			final Properties properties = new Properties();
			properties.load(new InputStreamReader(inputStream, charset));
			return properties;
		} catch (final IOException exception) {
			throw Exceptions.loadingException(exception);
		}
	}

	/**
	 * Saves the properties file at the given path as the default charset, joining
	 * the elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param properties
	 *                       A properties file to be stored.
	 * @param comments
	 *                       Comments to be added at the beginning of the properties
	 *                       file.
	 * @param path
	 *                       The path to a file represented as a sequence of the
	 *                       different elements of the path.
	 */
	public static void saveProperties(final Properties properties, final String comments, final String... path) {
		FileHandler.saveProperties(properties, comments, FileHandler.DEFAULT_CHARSET, path);
	}

	/**
	 * Saves the properties file at the given path as the given charset, joining the
	 * elements of the path using the path separator character, throwing any
	 * exception generated in the process as an unchecked exception.
	 *
	 * @param properties
	 *                       A properties file to be stored.
	 * @param comments
	 *                       Comments to be added at the beginning of the properties
	 *                       file.
	 * @param charset
	 *                       A charset.
	 * @param path
	 *                       The path to a file represented as a sequence of the
	 *                       different elements of the path.
	 */
	public static void saveProperties(final Properties properties, final String comments, final Charset charset,
			final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileOutputStream outputStream = new FileOutputStream(joinedPath)) {
			final OutputStreamWriter writer = new OutputStreamWriter(outputStream, FileHandler.DEFAULT_CHARSET);
			properties.store(writer, comments);
		} catch (final IOException exception) {
			throw Exceptions.savingException(exception);
		}
	}

	/**
	 * Loads the image file at the given path, joining the elements of the path
	 * using the path separator character, throwing any exception generated in the
	 * process as an unchecked exception.
	 *
	 * @param path
	 *                 The path to a file.
	 */
	public static BufferedImage loadImage(final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileInputStream inputStream = new FileInputStream(joinedPath)) {
			final BufferedImage image = ImageIO.read(inputStream);
			return image;
		} catch (final IOException exception) {
			throw Exceptions.loadingException(exception);
		}
	}

	/**
	 * Saves the image file at the given path, joining the elements of the path
	 * using the path separator character, throwing any exception generated in the
	 * process as an unchecked exception. Assumes the format of the image file from
	 * the extension of the path.
	 *
	 * @param properties
	 *                       A properties file to be stored.
	 * @param path
	 *                       The path to a file.
	 */
	public static void saveImage(final BufferedImage image, final String... path) {
		final String joinedPath = String.join(File.separator, path);
		try (final FileOutputStream output = new FileOutputStream(joinedPath)) {
			ImageIO.write(image, path[path.length - 1].substring(path[path.length - 1].lastIndexOf('.') + 1), output);
		} catch (final IOException exception) {
			throw Exceptions.savingException(exception);
		}
	}

}
