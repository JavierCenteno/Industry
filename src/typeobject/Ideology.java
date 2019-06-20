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
import util.math.IMath;

/**
 * This class represents an ideology.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Ideology extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all ideologies.
	 */
	private static final Map<String, Ideology> ALL_IDEOLOGIES_MAP;
	/**
	 * Global list of all ideologies.
	 */
	private static final List<Ideology> ALL_IDEOLOGIES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this ideology.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this ideology.
	 */
	@Internationalized
	private String description;
	/**
	 * Names parties representing this ideology can have.
	 */
	@Internationalized
	private String[] partyNames;
	/**
	 * Era this ideology starts off at.
	 */
	@Externalized
	private Era startingEra;
	/**
	 * Color that represents this ideology.
	 */
	@Externalized
	private int color;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Represents a set of levels of support on every ideology.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 *
	 */
	public static class BeliefSet {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		/**
		 * Maximum value beliefs can have.
		 */
		private static final byte BELIEF_CAP = 100;

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Level of each ideology this belief set has.
		 */
		private final byte[] beliefs;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Creates a new belief set.
		 */
		public BeliefSet() {
			this.beliefs = new byte[Ideology.ALL_IDEOLOGIES_LIST.size()];
		}

		/**
		 * Creates a new belief set as a copy of a given belief set.
		 */
		public BeliefSet(final BeliefSet beliefSet) {
			this();
			System.arraycopy(beliefSet.beliefs, 0, this.beliefs, 0, this.beliefs.length);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the level of the given ideology this belief set has.
		 */
		public byte getBelief(final Ideology ideology) {
			return this.beliefs[ideology.id];
		}

		/**
		 * Set the level of the given ideology this belief set has.
		 */
		public void setBelief(final Ideology ideology, final byte value) {
			this.beliefs[ideology.id] = value;
		}

		/**
		 * Add to the level of the given ideology this belief set has.
		 */
		public void addBelief(final Ideology ideology, final byte value) {
			final int newValue = this.beliefs[ideology.id] + value;
			if (newValue <= 0) {
				this.beliefs[ideology.id] = 0;
			} else if (newValue >= BeliefSet.BELIEF_CAP) {
				this.beliefs[ideology.id] = BeliefSet.BELIEF_CAP;
			} else {
				this.beliefs[ideology.id] = (byte) newValue;
			}
		}

		/**
		 * Get the ideology with the strongest belief level in this belief set.
		 */
		public Ideology getStrongestBelief() {
			return Ideology.ALL_IDEOLOGIES_LIST.get(IMath.maximumIndex(this.beliefs));
		}

		/**
		 * Calculates the affinity of this belief set with the given belief set as a
		 * value from 0 to 100.
		 */
		public int affinityWith(final BeliefSet beliefSet) {
			int totalAgreement = 0;
			for (int i = 0; i < this.beliefs.length; ++i) {
				totalAgreement += BeliefSet.BELIEF_CAP - IMath.absolute(this.beliefs[i] - beliefSet.beliefs[i]);
			}
			totalAgreement /= this.beliefs.length;
			return totalAgreement;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all occupations.
	 */
	static {
		ALL_IDEOLOGIES_MAP = new HashMap<>();
		ALL_IDEOLOGIES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "ideology");
		final Json data = Industry.DATA.get("typeObject", "ideology");
		for (final String key : data.keys()) {
			new Ideology(key);
		}
		for (final String key : data.keys()) {
			final Ideology ideology = Ideology.getIdeology(key);
			ideology.name = i18n.get(key, "name").as(String.class);
			ideology.description = i18n.get(key, "description").as(String.class);
			ideology.partyNames = i18n.get(key, "partyNames").as(String[].class);
			ideology.color = Integer.parseInt(data.get(key, "color").as(String.class), 16);
			ideology.startingEra = Era.getEra(data.get(key, "startingEra").as(String.class));
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new ideology and adds it to the map of all ideologies under the
	 * given key and to the list of all ideologies.
	 *
	 * @param key
	 *                Key under which this ideology will be added to the map of all
	 *                ideologies.
	 */
	private Ideology(final String key) {
		super(Ideology.ALL_IDEOLOGIES_LIST.size());
		Ideology.ALL_IDEOLOGIES_MAP.put(key, this);
		Ideology.ALL_IDEOLOGIES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the ideologies.
	 *
	 * @return A list of all the ideologies.
	 */
	public static List<Ideology> getIdeologies() {
		return Ideology.ALL_IDEOLOGIES_LIST;
	}

	/**
	 * Gets the ideologies with the given keys.
	 *
	 * @param keys
	 *                 An array of ideology keys.
	 * @return The array of ideologies with the given keys.
	 */
	public static Ideology[] getIdeologies(final String[] keys) {
		final Ideology[] ideologies = new Ideology[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			ideologies[i] = Ideology.getIdeology(keys[i]);
		}
		return ideologies;
	}

	/**
	 * Gets the ideology with the given key.
	 *
	 * @param key
	 *                An ideology key.
	 * @return The ideology with the given key.
	 */
	public static Ideology getIdeology(final String key) {
		return Ideology.ALL_IDEOLOGIES_MAP.get(key);
	}

	/**
	 * Gets the ideologies with the given ids.
	 *
	 * @param ids
	 *                An array of ideology ids.
	 * @return The array of ideologies with the given ids.
	 */
	public static Ideology[] getIdeologies(final int[] ids) {
		final Ideology[] ideologies = new Ideology[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			ideologies[i] = Ideology.getIdeology(ids[i]);
		}
		return ideologies;
	}

	/**
	 * Gets the ideology with the given id.
	 *
	 * @param id
	 *               An ideology id.
	 * @return The ideology with the given id.
	 */
	public static Ideology getIdeology(final int id) {
		return Ideology.ALL_IDEOLOGIES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this ideology.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this ideology.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the names parties representing this ideology can have.
	 */
	public String[] getPartyNames() {
		return this.partyNames;
	}

	/**
	 * Get the era this ideology starts off at.
	 */
	public Era getStartingEra() {
		return this.startingEra;
	}

	/**
	 * Get the color that represents this ideology.
	 */
	public int getColor() {
		return this.color;
	}

}
