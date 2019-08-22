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
import exe.io.Internationalized;

/**
 * This class represents a job that may be fulfilled by a citizen.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Occupation extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all occupations.
	 */
	private static final Map<String, Occupation> ALL_OCCUPATIONS_MAP;
	/**
	 * Global list of all occupations.
	 */
	private static final List<Occupation> ALL_OCCUPATIONS_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this occupation.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this occupation.
	 */
	@Internationalized
	private String description;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Contains references to the default occupations.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 *
	 */
	public static class Occupations {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		public static final Occupation ANALYST = Occupation.getOccupation("analyst");
		public static final Occupation ARTIST = Occupation.getOccupation("artist");
		public static final Occupation ATTENDANT = Occupation.getOccupation("attendant");
		public static final Occupation BANKER = Occupation.getOccupation("banker");
		public static final Occupation BUREAUCRAT = Occupation.getOccupation("bureaucrat");
		public static final Occupation CHILD = Occupation.getOccupation("child");
		public static final Occupation CRIMINAL = Occupation.getOccupation("criminal");
		public static final Occupation DIPLOMAT = Occupation.getOccupation("diplomat");
		public static final Occupation DRIVER = Occupation.getOccupation("driver");
		public static final Occupation ENGINEER = Occupation.getOccupation("engineer");
		public static final Occupation FARMER = Occupation.getOccupation("farmer");
		public static final Occupation GOVERNOR = Occupation.getOccupation("governor");
		public static final Occupation HOMEMAKER = Occupation.getOccupation("homemaker");
		public static final Occupation JUDGE = Occupation.getOccupation("judge");
		public static final Occupation LABORER = Occupation.getOccupation("laborer");
		public static final Occupation MANAGER = Occupation.getOccupation("manager");
		public static final Occupation MERCHANT = Occupation.getOccupation("merchant");
		public static final Occupation MINER = Occupation.getOccupation("miner");
		public static final Occupation MINISTER = Occupation.getOccupation("minister");
		public static final Occupation NURSE = Occupation.getOccupation("nurse");
		public static final Occupation OPERATOR = Occupation.getOccupation("operator");
		public static final Occupation PHYSICIAN = Occupation.getOccupation("physician");
		public static final Occupation POLICE = Occupation.getOccupation("police");
		public static final Occupation PROFESSOR = Occupation.getOccupation("professor");
		public static final Occupation RANCHER = Occupation.getOccupation("rancher");
		public static final Occupation RETIREE = Occupation.getOccupation("retiree");
		public static final Occupation SENATOR = Occupation.getOccupation("senator");
		public static final Occupation SOLDIER = Occupation.getOccupation("soldier");
		public static final Occupation STUDENT = Occupation.getOccupation("student");
		public static final Occupation UNEMPLOYED = Occupation.getOccupation("unemployed");

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all occupations.
	 */
	static {
		ALL_OCCUPATIONS_MAP = new HashMap<>();
		ALL_OCCUPATIONS_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("type", "occupation", "list");
		final Json data = Industry.DATA.get("type", "occupation", "list");
		for (final String key : data.keys()) {
			final Occupation occupation = new Occupation(key);
			occupation.name = i18n.get(key, "name").as(String.class);
			occupation.description = i18n.get(key, "description").as(String.class);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new occupation and adds it to the map of all occupations under the
	 * given key and to the list of all occupations.
	 *
	 * @param key
	 *                Key under which this occupation will be added to the map of
	 *                all occupations.
	 */
	private Occupation(final String key) {
		super(Occupation.ALL_OCCUPATIONS_LIST.size());
		Occupation.ALL_OCCUPATIONS_MAP.put(key, this);
		Occupation.ALL_OCCUPATIONS_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the occupations.
	 *
	 * @return A list of all the occupations.
	 */
	public static List<Occupation> getAllOccupations() {
		return Occupation.ALL_OCCUPATIONS_LIST;
	}

	/**
	 * Gets the occupations with the given keys.
	 *
	 * @param keys
	 *                 An array of occupation keys.
	 * @return The array of occupations with the given keys.
	 */
	public static Occupation[] getOccupations(final String[] keys) {
		final Occupation[] occupations = new Occupation[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			occupations[i] = Occupation.getOccupation(keys[i]);
		}
		return occupations;
	}

	/**
	 * Gets the occupation with the given key.
	 *
	 * @param key
	 *                An occupation key.
	 * @return The occupation with the given key.
	 */
	public static Occupation getOccupation(final String key) {
		return Occupation.ALL_OCCUPATIONS_MAP.get(key);
	}

	/**
	 * Gets the occupations with the given ids.
	 *
	 * @param ids
	 *                An array of occupation ids.
	 * @return The array of occupations with the given ids.
	 */
	public static Occupation[] getOccupations(final int[] ids) {
		final Occupation[] occupations = new Occupation[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			occupations[i] = Occupation.getOccupation(ids[i]);
		}
		return occupations;
	}

	/**
	 * Gets the occupation with the given id.
	 *
	 * @param id
	 *               An occupation id.
	 * @return The occupation with the given id.
	 */
	public static Occupation getOccupation(final int id) {
		return Occupation.ALL_OCCUPATIONS_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this occupation.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this occupation.
	 */
	public String getDescription() {
		return this.description;
	}

}
