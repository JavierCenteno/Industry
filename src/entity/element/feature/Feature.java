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

package entity.element.feature;

import java.util.ArrayList;
import java.util.List;

import entity.city.City;
import entity.element.Element;
import entity.world.Terrain;
import entity.world.World;
import util.amount.Amount;

/**
 * This class abstracts the idea of a feature in a world. Features can be
 * natural or artificial, they have no workers and they may be of arbitrary
 * size. Features can have upgrades.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Feature extends Element {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * List of factories for all types of feature there are.
	 */
	public static final List<FeatureFactory<?>> FACTORIES = new ArrayList<FeatureFactory<?>>();

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Size of the feature in each dimension.
	 */
	private final int sizeX, sizeY;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of features that returns instances of a class that extends feature.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <F>
	 *            Class of the features this factory returns.
	 */
	public static abstract class FeatureFactory<F extends Feature> extends ElementFactory<F> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Checks whether F can be built with these parameters.
		 *
		 * @param terrain
		 *                        Terrain where we're building F.
		 * @param coordinateX
		 *                        Coordinate of the feature along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the feature along the y axis.
		 * @param sizeX
		 *                        Depth of the feature in the world.
		 * @param sizeY
		 *                        Width of the feature in the world.
		 * @param city
		 *                        City the feature belongs to.
		 * @return Whether F can be built with these parameters.
		 */
		public abstract boolean check(Terrain terrain, int coordinateX, int coordinateY, int sizeX, int sizeY,
				City city);

		/**
		 * Instances F and places the instance on a terrain.
		 *
		 * @param terrain
		 *                        Terrain where we're building F.
		 * @param coordinateX
		 *                        Coordinate of the feature along the x axis.
		 * @param coordinateY
		 *                        Coordinate of the feature along the y axis.
		 * @param sizeX
		 *                        Depth of the feature in the world.
		 * @param sizeY
		 *                        Width of the feature in the world.
		 * @param health
		 *                        Health this feature will start off with.
		 * @param city
		 *                        City the feature belongs to.
		 * @return The instance of F that has been placed on the terrain.
		 */
		public abstract F make(Terrain terrain, int coordinateX, int coordinateY, int sizeX, int sizeY, int health,
				City city);

	}

	/**
	 * Upgrade that can modify the properties or behavior of a feature if it's set.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static abstract class FeatureUpgrade extends ElementUpgrade {

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public FeatureUpgrade() {
			super();
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Feature(final World world, final int coordinateX, final int coordinateY, final int health, final int sizeX,
			final int sizeY, final City city) {
		super(world, coordinateX, coordinateY, health, city);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the upgrades of this feature.
	 *
	 * @return The upgrades of this feature.
	 */
	public abstract FeatureUpgrade[] getUpgrades();

	/**
	 * Gets this feature's depth.
	 *
	 * @return This feature's depth.
	 */
	public int getSizeX() {
		return this.sizeX;
	}

	/**
	 * Gets this feature's width.
	 *
	 * @return This feature's width.
	 */
	public int getSizeY() {
		return this.sizeY;
	}

	/**
	 * Removes this feature and returns the resources it's made of. May have side
	 * effects.
	 */
	public abstract Amount[] clear();

}
