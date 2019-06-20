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

package exe;

import java.time.Year;
import java.util.Properties;

import api.Json;
import exe.io.FileHandler;

/**
 * This is the main class of the game.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Industry {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Version of this software.
	 */
	public static final String VERSION = "0.1B";
	/**
	 * Copyright years.
	 */
	public static final String COPYRIGHT = "©2011 - " + Year.now().toString();
	/**
	 * Toggles debug mode.
	 */
	public static final boolean DEBUG = false;
	/**
	 * Internationalized data.
	 */
	public static Json I18N;
	/**
	 * Externalized data.
	 */
	public static Json DATA;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// load configuration
		/*
		 * Typically this is done through the setting class but we need the language to
		 * load the internationalization and we need to load the internationalization to
		 * load the settings, so we load the configuration manually this time.
		 */
		final Properties configuration = FileHandler.loadProperties(FileHandler.CONFIGURATION_PATH,
				"configuration.properties");
		final String language = configuration.getProperty("language");
		Industry.I18N = FileHandler.loadJson(FileHandler.I18N_PATH, language + ".json");
		Industry.DATA = FileHandler.loadJson(FileHandler.DATA_PATH, "data.json");
		/*
		 * Load classes so class initializers will populate internationalized and
		 * externalized fields with the loaded data.
		 */
		final String[] classNames = FileHandler.loadJson(FileHandler.DATA_PATH, "class.json").as(String[].class);
		for (final String className : classNames) {
			FileHandler.loadClass(className);
		}
		// set to null so it can be garbage collected
		Industry.I18N = null;
		Industry.DATA = null;
		System.gc();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

}
