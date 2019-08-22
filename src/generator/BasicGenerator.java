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

package generator;

import entity.world.TectonicPlate;
import entity.world.World;
import entity.world.TectonicPlate.Tile;
import entity.world.Terrain.TectonicPlateIterable.TectonicPlateIterator;

/**
 * This class defines a basic map generator.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class BasicGenerator implements Generator {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	private static final int HUMIDITY_RADIUS = 1;
	private static final int HUMIDITY_MAGNITUDE = 1;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * World to be generated.
	 */
	private final World world;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public BasicGenerator(final World world) {
		this.world = world;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Adds a blob of magma given by an interpolation function with a certain radius
	 * and height, centered at coordinates x, y.
	 */
	public void addMagma(final Interpolation interpolation, final int x, final int y, final int radius,
			final int height) {
		for (final Tile tile : this.world.getTileSet(x - radius, y - radius, x + radius, y + radius)) {
			if (tile == null) {
				continue;
			}
			final int iterationX = x - tile.getCoordinateX();
			final int iterationY = y - tile.getCoordinateY();
			final int iterationHeight = interpolation.at(iterationX, iterationY, radius, height);
			tile.addMagma(iterationHeight);
		}
	}

	/**
	 * Adds a blob of land given by an interpolation function with a certain radius
	 * and height, centered at coordinates x, y.
	 */
	public void addLand(final Interpolation interpolation, final int x, final int y, final int radius,
			final int height) {
		for (final Tile tile : this.world.getTileSet(x - radius, y - radius, x + radius, y + radius)) {
			if (tile == null) {
				continue;
			}
			final int iterationX = x - tile.getCoordinateX();
			final int iterationY = y - tile.getCoordinateY();
			final int iterationHeight = interpolation.at(iterationX, iterationY, radius, height);
			tile.addLand(iterationHeight);
		}
	}

	/**
	 * Adds a blob of water given by an interpolation function with a certain radius
	 * and height, centered at coordinates x, y.
	 */
	public void addWater(final Interpolation interpolation, final int x, final int y, final int radius,
			final int height) {
		for (final Tile tile : this.world.getTileSet(x - radius, y - radius, x + radius, y + radius)) {
			if (tile == null) {
				continue;
			}
			// If the tile is already flooded, do nothing
			if (tile.getWater() >= tile.getLand()) {
				continue;
			}
			final int iterationX = x - tile.getCoordinateX();
			final int iterationY = y - tile.getCoordinateY();
			final int iterationHeight = interpolation.at(iterationX, iterationY, radius, height);
			tile.addWater(iterationHeight);
			// If the tile has been flooded, ignore it
			if (tile.getWater() >= tile.getLand()) {
				tile.setWater(tile.getLand() - 1);
			}
		}
	}

	@Override
	public void generate() {
		// Set the variables we'll need
		final int tectonicPlateSize = this.world.getTectonicPlateSize();
		final int baseMagma = this.world.getBaseMagma();
		final int baseHeight = this.world.getBaseHeight();
		final int baseRoughness = this.world.getBaseRoughness();
		final int baseWater = this.world.getBaseWater();
		// We fill the terrain with tectonic plates with randomly generated magma flow
		// vectors
		TectonicPlateIterator tectonicPlateIterator = (TectonicPlateIterator) this.world.getAllTectonicPlates()
				.iterator();
		while (tectonicPlateIterator.hasNext()) {
			tectonicPlateIterator.next();
			final int magmaFlowX = this.world.getPRNG().generateUniformInteger(this.world.getTectonicPlateSize());
			final int magmaFlowY = this.world.getPRNG().generateUniformInteger(this.world.getTectonicPlateSize());
			tectonicPlateIterator.newTectonicPlate(magmaFlowX, magmaFlowY, null);
		}
		// We generate the basic levels
		for (final Tile tile : this.world.getAllTiles()) {
			tile.addMagma(baseMagma);
			tile.addLand(baseHeight);
			tile.addWater(baseWater);
		}
		// We generate the terrain
		tectonicPlateIterator = (TectonicPlateIterator) this.world.getAllTectonicPlates().iterator();
		while (tectonicPlateIterator.hasNext()) {
			final TectonicPlate tectonicPlate = tectonicPlateIterator.next();
			final int magnitude = (int) Math.sqrt((tectonicPlate.magmaFlowX * tectonicPlate.magmaFlowX)
					+ (tectonicPlate.magmaFlowY * tectonicPlate.magmaFlowY));
			this.addMagma(
					// Smoothstep
					Interpolation.SMOOTHSTEP,
					// The function is centered at the center of the plate minus magma flow.
					// It's minus because magma is generated at the opposite point of the direction
					// of its flow
					tectonicPlateIterator.getCenterX() - tectonicPlate.magmaFlowX,
					tectonicPlateIterator.getCenterY() - tectonicPlate.magmaFlowY,
					// The radius is the magnitude of the vector... obviously
					magnitude,
					// We set height to (magnitude / maximum magnitude) * (base roughness)
					(baseRoughness * magnitude * 4) / (tectonicPlateSize * tectonicPlateSize));
			this.addLand(
					// Smoothstep
					Interpolation.SMOOTHSTEP,
					// The function is centered at the center of the plate plus magma flow
					tectonicPlateIterator.getCenterX() + tectonicPlate.magmaFlowX,
					tectonicPlateIterator.getCenterY() + tectonicPlate.magmaFlowY,
					// The radius is the magnitude of the vector... obviously
					magnitude,
					// We set height to (magnitude / maximum magnitude) * (base roughness)
					(baseRoughness * magnitude * 4) / (tectonicPlateSize * tectonicPlateSize));
		}
		// We generate the water
		for (final Tile tile : this.world.getAllTiles()) {
			// If land is below sea level, then water level is the sea level
			// (...the Netherlands would probably disagree but eh)
			if (tile.getLand() <= this.world.getBaseWater()) {
				tile.setWater(this.world.getBaseWater());
			}
		}
		// We generate the humidity
		for (final Tile tile : this.world.getAllTiles()) {
			if (tile.getWater() >= tile.getLand()) {
				this.addWater(Interpolation.SMOOTHSTEP, tile.getCoordinateX(), tile.getCoordinateY(),
						this.world.getTectonicPlateSize() * BasicGenerator.HUMIDITY_RADIUS,
						this.world.getTectonicPlateSize() * BasicGenerator.HUMIDITY_MAGNITUDE);
			}
		}

		/*
		 * TODO:
		 *
		 * Use different interpolation functions.
		 *
		 * We could use the conic one for mountains, and the smoothstep one for hills...
		 *
		 * A donut shaped one for atolls or extinct volcanoes: abs(distance to the
		 * center - donut radius)
		 *
		 * Mountains with different shapes... or stacked smoothsteps for mountain
		 * ranges.
		 *
		 * Maybe the lower the height, the wider the function such that high values
		 * create tall mountains while low values create wide islands.
		 *
		 * Maybe mountains influence number of mountains in the direction they point at.
		 */

		/*
		 * TODO:
		 *
		 * basalt type: extrusive igneous granularity: 0 fertility: 0
		 *
		 * feldspar type: general igneous granularity: 0 fertility: 0
		 *
		 * marble type: metamorphic (appears when slope is mad high) granularity: 0
		 * fertility: 0
		 *
		 * limestone type: sedimentary granularity: 0 fertility: 0 sand type:
		 * sedimentary granularity: 1 fertility: 1 silt type: sedimentary granularity: 2
		 * fertility: 3 clay type: sedimentary granularity: 3 fertility: 2 peat type:
		 * sedimentary granularity: 4 fertility: 4
		 *
		 *
		 * bauxite rarity: 31
		 *
		 * cassiterite rarity: 5
		 *
		 * cinnabar rarity: 2
		 *
		 * coal rarity: 194
		 *
		 * cuprite rarity: 22
		 *
		 * galena rarity: 14
		 *
		 * gem rarity: 2
		 *
		 * goldore rarity: 1
		 *
		 * gypsum rarity: 4
		 *
		 * ironore rarity: 97
		 *
		 * naturalgas rarity: 157
		 *
		 * niter rarity: 46
		 *
		 * petroleum rarity: 148
		 *
		 * phosphate rarity: 50
		 *
		 * salt rarity: 60
		 *
		 * sulfur rarity: 45
		 *
		 * uraninite rarity: 3
		 *
		 * water rarity: 40
		 *
		 * wolframite rarity: 3
		 *
		 *
		 * Okay, so here's how this shit should work.
		 *
		 * SoilGenerator{
		 *
		 * decide soil:
		 *
		 *
		 * int granularity = temperature + humidity - height - slope;
		 *
		 * slope >~ 0: curvature > 0: (sedimentary rocks)
		 *
		 * granularity > 0: sandstone
		 *
		 * granularity >> 0: sand
		 *
		 * granularity >>> 0: silt
		 *
		 * granularity >>>> 0: clay
		 *
		 * curvature < 0: (igneous rocks) feldspar
		 *
		 * slope >> 0: (metamorphic rocks) marble
		 *
		 *
		 * magma > height: basalt, set height to magma + 1 except if slope(magma) == 0
		 *
		 *
		 * decide ore:
		 *
		 * sedimentary: cassiterite, cuprite, galena, ironore, naturalgas, petroleum,
		 * phosphate, uraninite, water
		 *
		 * igneous: bauxite, cassiterite, goldore, wolframite
		 *
		 * metamorphic : bauxite, goldore
		 *
		 *
		 * the closer magma is to surface, the purer mineral deposits are
		 *
		 * REMEMBER, PLANTS CANNOT SPAWN IN A TILE WHERE A SOILTYPE EXISTS NOR ON SLOPED
		 * TERRAIN
		 *
		 * REMEMBER, MINERALDEPOSITS AND SOILTYPES CANNOT SPAWN IN SLOPED TILES
		 *
		 * }
		 *
		 */
	}

}
