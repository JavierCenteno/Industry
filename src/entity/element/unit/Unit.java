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

package entity.element.unit;

import java.util.ArrayList;
import java.util.List;

import entity.city.City;
import entity.element.Element;
import entity.world.Terrain;
import entity.world.World;
import entity.world.Terrain.Orientation;

/**
 * This class abstracts the idea of a unit that can move around a world and
 * perform actions. Units can have workmodes.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Unit extends Element {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * List of factories for all types of unit there are.
	 */
	public static final List<UnitFactory<?>> FACTORIES = new ArrayList<UnitFactory<?>>();

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of units that returns instances of a class that extends unit.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <E>
	 *            Class of the units this factory returns.
	 */
	public static abstract class UnitFactory<U extends Unit> extends ElementFactory<U> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Checks whether U can be spawned with these parameters.
		 *
		 * @param terrain
		 *                        Terrain where we're spawning U.
		 * @param coordinateX
		 *                        Coordinate of the unit along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the unit along the y axis.
		 * @param orientation
		 *                        Orientation of the unit.
		 * @param city
		 *                        City the unit belongs to.
		 * @return Whether U can be spawned with these parameters.
		 */
		public abstract boolean check(Terrain terrain, int coordinateX, int coordinateY, Orientation orientation,
				City city);

		/**
		 * Instances U and places the instance on a terrain.
		 *
		 * @param terrain
		 *                        Terrain where we're spawning U.
		 * @param coordinateX
		 *                        Coordinate of the unit along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the unit along the y axis.
		 * @param orientation
		 *                        Orientation of the unit.
		 * @param health
		 *                        Health this unit will start off with.
		 * @param city
		 *                        City the unit belongs to.
		 * @return The instance of U that has been placed on the terrain.
		 */
		public abstract U make(Terrain terrain, int coordinateX, int coordinateY, Orientation orientation, int health,
				City city);

	}

	/**
	 * Workmode that can specify the behavior of an unit.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static interface UnitWorkmode extends ElementWorkmode {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Unit(final World world, final int coordinateX, final int coordinateY, final int health, final City city) {
		super(world, coordinateX, coordinateY, health, city);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the workmodes this unit can have.
	 *
	 * @return The workmodes this unit can have.
	 */
	public abstract UnitWorkmode[] getWorkmodes();

}
