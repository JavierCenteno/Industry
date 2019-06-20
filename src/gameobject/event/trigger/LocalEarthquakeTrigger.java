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

package gameobject.event.trigger;

import gameobject.world.World;

/**
 * This class causes earthquakes inside a terrain.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class LocalEarthquakeTrigger extends EventTrigger {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public LocalEarthquakeTrigger(final World world) {
		super(world);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public void tick() {
		/*
		 * TODO: the likelihood and strenght of earthquakes in a pair of plates is
		 * proportional to how different the movement vectors (magma flow) between the
		 * two plates are. Earthquakes damage all elements in the plates where they
		 * happen, with maximum damage in the plates where it happened and decreasing
		 * damage in more distant plates.
		 */
	}

}
