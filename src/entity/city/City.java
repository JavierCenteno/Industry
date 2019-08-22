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

package entity.city;

import entity.Entity;
import entity.world.World;

/**
 * This class represents a city.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class City extends Entity {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this city.
	 */
	private final String name;
	/**
	 * Country this city belongs to. Cities are part of a country.
	 *
	 * If the country controls other cities, a city can get orders, settlers or
	 * resources, declare independence from it...
	 *
	 * Otherwise, the government is assumed to be a national government and this is
	 * a city-state.
	 */
	private final Country country;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public City(final World world, final String name, final Country country) {
		super(world);
		this.name = name;
		this.country = country;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get this city's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the country this city belongs to.
	 */
	public Country getCountry() {
		return this.country;
	}

}
