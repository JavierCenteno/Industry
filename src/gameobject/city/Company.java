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

import java.util.ArrayList;
import java.util.List;

import api.Json;
import exe.Industry;
import exe.io.Internationalized;
import gameobject.GameObject;
import gameobject.element.building.Building;
import gameobject.world.World;

/**
 * This class represents a company.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Company extends GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the companies of this class.
	 */
	@Internationalized
	public static String NAME;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Identifier of this company's name in the arrays of names.
	 */
	private int name;
	/**
	 * City this company belongs to.
	 */
	private final City city;
	/**
	 * Citizen who owns this company.
	 */
	private final Citizen owner;
	/**
	 * Buildings owned by this company.
	 */
	private final List<Building> buildings;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("gameObject", "company");
		Company.NAME = i18n.get("name").as(String.class);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new company.
	 */
	public Company(final World world, final City city, final Citizen owner) {
		super(world);
		this.city = city;
		this.owner = owner;
		this.buildings = new ArrayList<Building>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this company.
	 */
	public String getName() {
		return Citizen.SURNAMES[this.name];
	}

	/**
	 * Get the city this company belongs to.
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * Get the citizen who owns this company.
	 */
	public Citizen getOwner() {
		return this.owner;
	}

	/**
	 * Get the buildings this company owns.
	 */
	public List<Building> getBuildings() {
		return this.buildings;
	}

}
