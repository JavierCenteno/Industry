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

package entity.element;

import entity.Entity;
import entity.city.City;
import entity.world.World;
import type.Technology;
import util.amount.Amount;

/**
 * This class abstracts the idea of an element that is located in a terrain and
 * can be owned by a country, created, destroyed, damaged or repaired.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Element extends Entity {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Coordinates of the element in the world.
	 */
	private final int coordinateX, coordinateY;
	/**
	 * Current health of the element.
	 */
	protected int health;
	/**
	 * City this element belongs to.
	 */
	private final City city;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of elements that returns instances of a class that extends element.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <E>
	 *            Class of the elements this factory returns.
	 */
	public static abstract class ElementFactory<E extends Element> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the required technology to build this element.
		 */
		public abstract Technology getRequiredTechnology();

		/**
		 * Get the amounts of resources required to build this element.
		 */
		public abstract Amount[] getCost();

	}

	/**
	 * Upgrade that can modify the properties or behavior of an element if it's set.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static abstract class ElementUpgrade {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Whether this upgrade is active.
		 */
		private boolean active;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public ElementUpgrade() {
			this.active = false;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the name of this workmode.
		 */
		public abstract String getName();

		/**
		 * Get the description of this workmode.
		 */
		public abstract String getDescription();

		/**
		 * Get the element where this upgrade is installed.
		 */
		public abstract Element getBase();

		/**
		 * Get whether this upgrade is active.
		 */
		public boolean getActive() {
			return this.active;
		}

		/**
		 * Set this upgrade to active.
		 */
		public void activate() {
			this.active = true;
		}

		/**
		 * Set this upgrade to inactive.
		 */
		public void deactivate() {
			this.active = false;
		}

		/**
		 * Get the required technology to activate this upgrade.
		 */
		public abstract Technology getRequiredTechnology();

		/**
		 * Get the amounts of resources required to activate this upgrade.
		 */
		public abstract Amount[] getCost();

	}

	/**
	 * Workmode that can specify the behavior of an element.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static interface ElementWorkmode {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		/**
		 * An empty array of workmodes.
		 */
		public static final ElementWorkmode[] EMPTY_ARRAY = new ElementWorkmode[0];

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the name of this workmode.
		 */
		public abstract String getName();

		/**
		 * Get the description of this workmode.
		 */
		public abstract String getDescription();

		/**
		 * Get the required technology to use this workmode.
		 */
		public abstract Technology getRequiredTechnology();

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new element.
	 *
	 * @param coordinateX
	 *                        Coordinate of the element along the x axis.
	 * @param coordinateY
	 *                        Coordinate of the element along the y axis.
	 * @param city
	 *                        City that owns the element.
	 */
	public Element(final World world, final int coordinateX, final int coordinateY, final int health, final City city) {
		super(world);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.health = health;
		this.city = city;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this element.
	 */
	public abstract String getName();

	/**
	 * Get the description of this element.
	 */
	public abstract String getDescription();

	/**
	 * Get the x coordinate (longitude) of this element.
	 */
	public int getCoordinateX() {
		return this.coordinateX;
	}

	/**
	 * Get the y coordinate (latitude) of this element.
	 */
	public int getCoordinateY() {
		return this.coordinateY;
	}

	/**
	 * Get the health of this element.
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Get the city this element belongs to.
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * Changes this element's health.
	 *
	 * @param health
	 *                   Number to be added to this element's health.
	 */
	public abstract void heal(int health);

}
