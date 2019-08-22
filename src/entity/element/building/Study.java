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

package entity.element.building;

/**
 * This leisure building modifies the quality of entertainment buildings.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
/*
 * TODO: This building belongs to the leisure category.
 */
/*
 * study quality of service: sum(w.creativity for w in workers)
 *
 * the quality of cinemas, theaters, art galleries... is the sum of the
 * qualities of all studies, sorted by quality in a descending way with the
 * quality of each one multiplied by an descending power of two
 *
 * for example: [qualities of studies] -> resulting quality of entertainment
 * building [100] -> 100*1 [75, 50, 100] -> 100*1 + 75*0.5 + 50*0.25
 */
