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

package gameobject.element.equipment;

import java.util.ArrayList;
import java.util.List;

import gameobject.city.City;
import gameobject.element.Element;
import gameobject.world.Terrain;
import gameobject.world.World;

/**
 * This class abstracts the idea of an equipment that can have a location, carry
 * units, resources and equipment, be driven by units and perform actions under
 * their control. Equipment can have upgrades.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Equipment extends Element {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * List of factories for all types of equipment there are.
	 */
	public static final List<EquipmentFactory<?>> FACTORIES = new ArrayList<EquipmentFactory<?>>();

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of equipments that returns instances of a class that extends
	 * equipment.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <E>
	 *            Class of the equipments this factory returns.
	 */
	public static abstract class EquipmentFactory<E extends Equipment> extends ElementFactory<E> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Checks whether E can be built with these parameters.
		 *
		 * @param terrain
		 *                        Terrain where we're spawning E.
		 * @param coordinateX
		 *                        Coordinate of the equipment along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the equipment along the y axis.
		 * @param city
		 *                        City the equipment belongs to.
		 * @return Whether E can be spawned with these parameters.
		 */
		public abstract boolean check(final Terrain terrain, final int coordinateX, final int coordinateY,
				final City city);

		/**
		 * Instances E and places the instance on a terrain.
		 *
		 * @param terrain
		 *                        Terrain where we're spawning E.
		 * @param coordinateX
		 *                        Coordinate of the equipment along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the equipment along the y axis.
		 * @param health
		 *                        Health this equipment will start off with.
		 * @param city
		 *                        City the equipment belongs to.
		 * @return The instance of E that has been placed on the terrain.
		 */
		public abstract E make(final Terrain terrain, final int coordinateX, final int coordinateY, final int health,
				final City city);

	}

	/**
	 * Upgrade that can modify the properties or behavior of an equipment if it's
	 * set.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static abstract class EquipmentUpgrade extends ElementUpgrade {

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public EquipmentUpgrade() {
			super();
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Equipment(final World world, final int coordinateX, final int coordinateY, final int health,
			final City city) {
		super(world, coordinateX, coordinateY, health, city);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the upgrades of this equipment.
	 *
	 * @return The upgrades of this equipment.
	 */
	public abstract EquipmentUpgrade[] getUpgrades();

}
