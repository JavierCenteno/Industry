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

/**
 * This primary industry building produces water from underground sources, but
 * its capabilities are limited by local water level. The difference between
 * land level and water level in a tile is how deep a hole must be made in that
 * tile to reach water, and as the difference increases the amount of water that
 * can be obtained is limited.
 *
 * It must be noted that if the magma level exceeds the water level this
 * building won't work and if pollution level exceeds the water level, the water
 * will be contaminated.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
/*
 * TODO: This building belongs to the primary category.
 */
