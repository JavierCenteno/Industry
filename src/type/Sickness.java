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

package type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;

/**
 * This class represents sicknesses that may be present in a world and spread
 * among its beings.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Sickness extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all sicknesses.
	 */
	private static final Map<String, Sickness> ALL_SICKNESSES_MAP;
	/**
	 * Global list of all sicknesses.
	 */
	private static final List<Sickness> ALL_SICKNESSES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this sickness.
	 */
	@Internationalized
	private String name;
	/**
	 * Likelihood of contagion of this sickness.
	 */
	@Externalized
	private int infectivity;
	/**
	 * Amount of damage this sickness causes.
	 */
	@Externalized
	private int lethality;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all sicknesses.
	 */
	static {
		ALL_SICKNESSES_MAP = new HashMap<>();
		ALL_SICKNESSES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("type", "sickness", "list");
		final Json data = Industry.DATA.get("type", "sickness", "list");
		for (final String key : data.keys()) {
			final Sickness sickness = new Sickness(key);
			sickness.name = i18n.get(key, "name").as(String.class);
			sickness.infectivity = data.get(key, "infectivity").as(int.class);
			sickness.lethality = data.get(key, "lethality").as(int.class);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new sickness and adds it to the map of all sicknesses under the
	 * given key and to the list of all sicknesses.
	 *
	 * @param key
	 *                Key under which this sickness will be added to the map of all
	 *                sicknesses.
	 */
	private Sickness(final String key) {
		super(Sickness.ALL_SICKNESSES_LIST.size());
		Sickness.ALL_SICKNESSES_MAP.put(key, this);
		Sickness.ALL_SICKNESSES_LIST.add(this);
		this.infectivity = 0;
		this.lethality = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the sicknesses.
	 *
	 * @return A list of all the sicknesses.
	 */
	public static List<Sickness> getAllSicknesses() {
		return Sickness.ALL_SICKNESSES_LIST;
	}

	/**
	 * Gets the sicknesses with the given keys.
	 *
	 * @param keys
	 *                 An array of sickness keys.
	 * @return The array of sicknesses with the given keys.
	 */
	public static Sickness[] getSicknesses(final String[] keys) {
		final Sickness[] sicknesses = new Sickness[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			sicknesses[i] = Sickness.getSickness(keys[i]);
		}
		return sicknesses;
	}

	/**
	 * Gets the sickness with the given key.
	 *
	 * @param key
	 *                A sickness key.
	 * @return The sickness with the given key.
	 */
	public static Sickness getSickness(final String key) {
		return Sickness.ALL_SICKNESSES_MAP.get(key);
	}

	/**
	 * Gets the sicknesses with the given ids.
	 *
	 * @param ids
	 *                An array of sickness ids.
	 * @return The array of sicknesses with the given ids.
	 */
	public static Sickness[] getSicknesses(final int[] ids) {
		final Sickness[] sicknesses = new Sickness[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			sicknesses[i] = Sickness.getSickness(ids[i]);
		}
		return sicknesses;
	}

	/**
	 * Gets the sickness with the given id.
	 *
	 * @param id
	 *               A sickness id.
	 * @return The sickness with the given id.
	 */
	public static Sickness getSickness(final int id) {
		return Sickness.ALL_SICKNESSES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this sickness.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the likelihood of contagion of this sickness.
	 */
	public int getInfectivity() {
		return this.infectivity;
	}

	/**
	 * Get the amount of damage this sickness causes.
	 */
	public int getLethality() {
		return this.lethality;
	}

}
