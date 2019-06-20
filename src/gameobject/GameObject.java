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

package gameobject;

import api.RandomGenerator;
import gameobject.world.World;

/**
 * This class abstracts the idea of game objects. The instances of this class
 * are inherent to a particular game and their state may change while such a
 * game is taking place.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Count of all instances of game objects. Used for statistic keeping purposes.
	 */
	private static volatile long INSTANCE_COUNT = 0;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Unique ID of this object.
	 */
	private final long id;
	/**
	 * World this game object is part of.
	 */
	private final World world;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new game object.
	 */
	public GameObject(final World world) {
		this.id = GameObject.INSTANCE_COUNT;
		++GameObject.INSTANCE_COUNT;
		this.world = world;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Get the total number of game objects instanced.
	 */
	public static long getInstanceCount() {
		return GameObject.INSTANCE_COUNT;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int hashCode() {
		return (int) (this.id ^ (this.id >>> 32));
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof GameObject) {
			return ((GameObject) object).getId() == this.getId();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "#" + this.getId();
	}

	/**
	 * Get the id of this game object.
	 */
	public final long getId() {
		return this.id;
	}

	/**
	 * Get the world this game object is part of.
	 */
	public World getWorld() {
		return this.world;
	}

	/**
	 * Get the PRNG of the world this game object is part of.
	 */
	public RandomGenerator getPRNG() {
		return this.getWorld().getPRNG();
	}

	/**
	 * Causes the state of this object to progress by a time unit.
	 */
	public void tick() {
	}

}
