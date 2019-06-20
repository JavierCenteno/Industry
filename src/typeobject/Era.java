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

package typeobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;

/**
 * This class represents the different eras a country can be in.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Era extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all eras.
	 */
	private static final Map<String, Era> ALL_ERAS_MAP;
	/**
	 * Global list of all eras.
	 */
	private static final List<Era> ALL_ERAS_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this era.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this era.
	 */
	@Internationalized
	private String description;
	/**
	 * Starting year of this era.
	 */
	@Externalized
	private int startingYear;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all eras.
	 */
	static {
		ALL_ERAS_MAP = new HashMap<>();
		ALL_ERAS_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "era");
		final Json data = Industry.DATA.get("typeObject", "era");
		for (final String key : data.keys()) {
			final Era era = new Era(key);
			era.name = i18n.get(key, "name").as(String.class);
			era.description = i18n.get(key, "description").as(String.class);
			era.startingYear = data.get(key, "startingYear").as(int.class);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new era and adds it to the map of all eras under the given key and
	 * to the list of all eras.
	 *
	 * @param key
	 *                Key under which this era will be added to the map of all eras.
	 */
	private Era(final String key) {
		super(Era.ALL_ERAS_LIST.size());
		Era.ALL_ERAS_MAP.put(key, this);
		Era.ALL_ERAS_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the eras.
	 *
	 * @return A list of all the eras.
	 */
	public static List<Era> getAllEras() {
		return Era.ALL_ERAS_LIST;
	}

	/**
	 * Gets the eras with the given keys.
	 *
	 * @param keys
	 *                 An array of era keys.
	 * @return The array of eras with the given keys.
	 */
	public static Era[] getEras(final String[] keys) {
		final Era[] eras = new Era[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			eras[i] = Era.getEra(keys[i]);
		}
		return eras;
	}

	/**
	 * Gets the era with the given key.
	 *
	 * @param key
	 *                An era key.
	 * @return The era with the given key.
	 */
	public static Era getEra(final String key) {
		return Era.ALL_ERAS_MAP.get(key);
	}

	/**
	 * Gets the eras with the given ids.
	 *
	 * @param ids
	 *                An array of era ids.
	 * @return The array of eras with the given ids.
	 */
	public static Era[] getEras(final int[] ids) {
		final Era[] eras = new Era[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			eras[i] = Era.getEra(ids[i]);
		}
		return eras;
	}

	/**
	 * Gets the era with the given id.
	 *
	 * @param id
	 *               An era id.
	 * @return The era with the given id.
	 */
	public static Era getEra(final int id) {
		return Era.ALL_ERAS_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this era.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this era.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the starting year of this era
	 */
	public int getStartingYear() {
		return this.startingYear;
	}

}
