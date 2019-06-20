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

package gameobject.element.building;

import java.util.ArrayList;
import java.util.List;

import gameobject.city.Citizen;
import gameobject.city.City;
import gameobject.element.Element;
import gameobject.world.Terrain;
import gameobject.world.Terrain.Orientation;
import gameobject.world.World;
import typeobject.Resource;
import util.amount.Amount;

/**
 * This class abstracts the idea of a building that can perform an action.
 * Buildings can have upgrades and workmodes.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Building extends Element {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * List of factories for all types of building there are.
	 */
	public static final List<BuildingFactory<?>> FACTORIES = new ArrayList<BuildingFactory<?>>();

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Orientation of the element in the world.
	 */
	private final Orientation orientation;
	/**
	 * Resources stored in this building.
	 */
	private final List<Amount> storage;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of buildings that returns instances of a class that extends building.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <B>
	 *            Class of the buildings this factory returns.
	 */
	public static abstract class BuildingFactory<B extends Building> extends ElementFactory<B> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Checks whether B can be built with these parameters.
		 *
		 * @param terrain
		 *                        Terrain for which we're checking whether B can be
		 *                        built.
		 * @param coordinateX
		 *                        Coordinate of the building along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the building along the y axis.
		 * @param orientation
		 *                        Orientation of the building.
		 * @param city
		 *                        City the building belongs to.
		 * @return Whether B can be built with these parameters.
		 */
		public abstract boolean check(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final City city);

		/**
		 * Instances B and places the instance on a terrain.
		 *
		 * @param terrain
		 *                        Terrain where we're building B.
		 * @param coordinateX
		 *                        Coordinate of the building along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the building along the y axis.
		 * @param orientation
		 *                        Orientation of the building.
		 * @param health
		 *                        Health this building will start off with.
		 * @param city
		 *                        City the building belongs to.
		 * @return The instance of B that has been placed on the terrain.
		 */
		public abstract B make(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final int health, final City city);

	}

	/**
	 * Upgrade that can modify the properties or behavior of a building if it's set.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static abstract class BuildingUpgrade extends ElementUpgrade {

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public BuildingUpgrade() {
			super();
		}

	}

	/**
	 * Workmode that can specify the behavior of a building.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static interface BuildingWorkmode extends ElementWorkmode {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Building(final World world, final int coordinateX, final int coordinateY, final Orientation orientation,
			final int health, final City city) {
		super(world, coordinateX, coordinateY, health, city);
		this.orientation = orientation;
		this.storage = new ArrayList<Amount>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the upgrades of this building.
	 *
	 * @return The upgrades of this building.
	 */
	public abstract BuildingUpgrade[] getUpgrades();

	/**
	 * Get the workmodes this building can have.
	 *
	 * @return The workmodes this building can have.
	 */
	public abstract BuildingWorkmode[] getWorkmodes();

	/**
	 * Get the orientation of this building.
	 *
	 * @return The orientation of this building.
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}

	/**
	 * Gets this building's depth.
	 *
	 * @return This building's depth.
	 */
	public abstract int getSizeX();

	/**
	 * Gets this building's width.
	 *
	 * @return This building's width.
	 */
	public abstract int getSizeY();

	/**
	 * Get the resources this building produces. Used when finding suppliers for a
	 * resource.
	 *
	 * @return The resources this building produces.
	 */
	public abstract Resource[] produces();

	/**
	 * Demand a given amount from this building.
	 *
	 * @param amount
	 *                   Amount to be demanded.
	 * @return Amount provided by this building.
	 */
	public Amount demand(final Amount amount) {
		for (final Amount a : this.storage) {
			if (a.getResource().equals(amount.getResource())) {
				return a.fork(amount.getQuantity());
			}
		}
		return new Amount(amount.getResource(), 0);
	}

	/**
	 * Returns all the citizens in this building, both workers and users.
	 *
	 * @return All the citizens in this building.
	 */
	public abstract Citizen[] getCitizens();

	/**
	 * Spreads the sicknesses of every being in this building.
	 */
	public abstract void spreadSicknesses();

	@Override
	public void tick() {
		this.spreadSicknesses();
	}

}
