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

package gameobject.city;

import api.Json;
import exe.Industry;
import exe.io.Internationalized;
import gameobject.GameObject;
import gameobject.world.World;
import typeobject.Ideology;

/**
 * This class represents a party.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Party extends GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the parties of this class.
	 */
	@Internationalized
	public static String NAME;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * City this party belongs to.
	 */
	private final City city;
	/**
	 * Ideology of this party.
	 */
	private final Ideology ideology;
	/**
	 * Citizen who leads this party.
	 */
	private final Citizen leader;
	/**
	 * Identifier of this party's name in the ideology array of party names.
	 */
	private final int name;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new party.
	 */
	public Party(final World world, final City city, final Ideology ideology, final Citizen leader) {
		super(world);
		this.city = city;
		this.ideology = ideology;
		this.leader = leader;
		this.name = this.getPRNG().generateUniformInteger(Integer.MAX_VALUE);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("gameObject", "party");
		Party.NAME = i18n.get("name").as(String.class);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this party.
	 */
	public String getName() {
		final String[] partyNames = this.ideology.getPartyNames();
		return partyNames[this.name % partyNames.length];
	}

	/**
	 * Get the city this party belongs to.
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * Get the ideology of this party.
	 */
	public Ideology getIdeology() {
		return this.ideology;
	}

	/**
	 * Get the leader of this party.
	 */
	public Citizen getLeader() {
		return this.leader;
	}

}
