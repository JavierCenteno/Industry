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

package entity.event.trigger;

import entity.world.World;

/**
 * This class causes ash clouds inside a terrain.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class LocalAshCloudTrigger extends EventTrigger {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public LocalAshCloudTrigger(final World world) {
		super(world);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public void tick() {
		/*
		 * TODO: the likelihood of an ash cloud happening in a tectonic plate depends on
		 * the number of magma tiles in that tectonic plate. When an ash cloud happens,
		 * planes cannot travel to any airports in that plate or any plates in a 3x3
		 * radius and pollution increases in those plates.
		 */
	}

}
