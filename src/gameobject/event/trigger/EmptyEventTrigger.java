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
 * This class represents an event that has no effect in the game.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class EmptyEventTrigger extends EventTrigger {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public EmptyEventTrigger(final World world) {
		super(world);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public void tick() {
		/*
		 * TODO: pushes a new without effects
		 *
		 * examples:
		 *
		 * - three headed monkey sighted [no effect]
		 *
		 * - goats deemed species most likely to suffer from dementia [no effect]
		 *
		 * - ufo sighted [can only happen on digital era and onwards, no effect]
		 *
		 * - {sickness} awareness day [can only happen on information era and onwards,
		 * no effect]
		 *
		 * - year of {cause} [can only happen on digital era and onwards, no effect]
		 */
	}

}
