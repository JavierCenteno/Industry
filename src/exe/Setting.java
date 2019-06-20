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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import api.Json;
import exe.io.Externalized;
import exe.io.FileHandler;
import exe.io.Internationalized;

/**
 * This class stores the configuration.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Setting {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Configuration properties.
	 */
	private static Properties CONFIGURATION;
	/**
	 * Path of the configuration file, used to save and load configuration.
	 */
	private static final String CONFIGURATION_PATH;
	/**
	 * Map of all settings under the keys that identify them.
	 */
	public static final Map<String, Setting> SETTINGS;

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Key that uniquely identifies this setting.
	 */
	private final String key;
	/**
	 * Name of this setting.
	 */
	@Internationalized
	private final String name;
	/**
	 * Options available for this setting.
	 */
	private final Option[] options;
	/**
	 * Index of the currently selected option for this setting.
	 */
	private int optionIndex;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	public static class Option {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Name of this option.
		 */
		@Internationalized
		private final String name;
		/**
		 * Value of this option.
		 */
		@Externalized
		private final String value;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializer

		private Option(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the name of this option.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Get the value of this option.
		 */
		public String getValue() {
			return this.value;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		CONFIGURATION_PATH = FileHandler.CONFIGURATION_PATH + File.separatorChar + "configuration.properties";
		Setting.CONFIGURATION = FileHandler.loadProperties(Setting.CONFIGURATION_PATH);
		SETTINGS = new HashMap<String, Setting>();
		final Json i18n = Industry.I18N.get("util", "setting");
		final Json data = Industry.DATA.get("util", "setting");
		for (final String settingKey : data.keys()) {
			final String[] optionKeys = data.get(settingKey, "options").as(String[].class);
			final Option[] options = new Option[optionKeys.length];
			final String defaultOptionKey = data.get(settingKey, "default").as(String.class);
			int defaultOptionIndex = 0;
			for (int i = 0; i < optionKeys.length; ++i) {
				final String optionName = i18n.get(settingKey, optionKeys[i]).as(String.class);
				options[i] = new Option(optionName, optionKeys[i]);
				if (optionKeys[i].equals(defaultOptionKey)) {
					defaultOptionIndex = i;
				}
			}
			final String settingName = i18n.get(settingKey, "name").as(String.class);
			new Setting(settingKey, settingName, options, defaultOptionIndex);
		}
		Setting.loadConfiguration();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	private Setting(final String key, final String name, final Option[] options, final int optionIndex) {
		Setting.SETTINGS.put(key, this);
		this.key = key;
		this.name = name;
		this.options = options;
		this.optionIndex = optionIndex;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Loads the configuration from the file at CONFIGURATION_PATH to the
	 * CONFIGURATION variable.
	 *
	 * @see Setting#CONFIGURATION
	 * @see Setting#CONFIGURATION_PATH
	 */
	private static void loadConfiguration() {
		Setting.CONFIGURATION = FileHandler.loadProperties(Setting.CONFIGURATION_PATH);
		for (final Entry<Object, Object> entry : Setting.CONFIGURATION.entrySet()) {
			final Setting setting = Setting.SETTINGS.get(entry.getKey());
			for (int i = 0; i < setting.getOptions().length; ++i) {
				if (setting.getOptions()[i].getValue().equals(entry.getValue())) {
					setting.optionIndex = i;
				}
			}
		}
	}

	/**
	 * Saves the configuration from the CONFIGURATION variable to the file at
	 * CONFIGURATION_PATH.
	 *
	 * @see Setting#CONFIGURATION
	 * @see Setting#CONFIGURATION_PATH
	 */
	private static void saveConfiguration() {
		final String comments = "Industry configuration file. You can edit this file, but the insertion of unexpected values may result in unexpected behavior.";
		FileHandler.saveProperties(Setting.CONFIGURATION, comments, Setting.CONFIGURATION_PATH);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this setting.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the current value of this setting.
	 */
	public String getValue() {
		return this.options[this.optionIndex].getValue();
	}

	/**
	 * Get the options for this setting.
	 */
	public Option[] getOptions() {
		return this.options;
	}

	/**
	 * Set the value of this setting to the given option.
	 */
	public void setOption(final Option option) {
		for (int i = 0; i < this.options.length; ++i) {
			if (this.options[i].equals(option)) {
				this.optionIndex = i;
				Setting.CONFIGURATION.put(this.key, option.getValue());
			}
		}
		Setting.saveConfiguration();
	}

}
