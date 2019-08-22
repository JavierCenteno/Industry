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

package entity.world;

import java.util.ArrayList;
import java.util.List;

import api.RandomGenerator;
import entity.Entity;
import entity.city.Citizen;
import entity.city.City;
import entity.element.building.Building;
import entity.element.building.Building.BuildingFactory;
import entity.element.feature.Feature;
import entity.element.feature.Feature.FeatureFactory;
import entity.element.knot.Knot.KnotFactory;
import entity.element.unit.Unit;
import entity.element.unit.Unit.UnitFactory;
import entity.world.TectonicPlate.Tile;
import entity.world.Terrain.Orientation;
import exe.Industry;
import generators.Xorshift64StarGenerator;
import type.Era;
import type.TerrainShape;
import util.idate.IDate;

/**
 * This class represents a world.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class World extends Entity {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Version of the game at which this world was created.
	 */
	private final String version;
	/**
	 *
	 */
	private final RandomGenerator PRNG;
	/**
	 *
	 */
	private final Terrain terrain;
	/**
	 * Date of this terrain.
	 */
	private final IDate date;
	/**
	 * List of all cities in this terrain.
	 */
	private final List<City> cities = new ArrayList<City>();

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializer

	/*
	 * tectonicPlateSizeExponent: 04: microscopic 05: tiny 06: small 07: medium 08:
	 * large 09: huge 10: gigantic 11: extreme
	 *
	 * worldSizeExponent: 14: microscopic 15: tiny 16: small 17: medium 18: large
	 * 19: huge 20: gigantic 21: extreme
	 *
	 * playableAreaSizeExponent: 02: microscopic 03: tiny 04: small 05: medium 06:
	 * large 07: huge 08: gigantic 09: extreme
	 */
	public World(final byte[] seed, final TerrainShape terrainShape, final Era era, final int tectonicPlateSizeExponent,
			final int worldSizeExponent, final double crustThicknessFactor, final double seaLevelFactor,
			final double roughnessFactor, final double axisTilt, final double distanceToSunFactor,
			final int playableAreaSizeExponent) {
		super(null);
		this.version = new String(Industry.VERSION);
		this.PRNG = new Xorshift64StarGenerator(seed);
		// Era 0 starts at 1700, each era is 50 years long
		this.date = new IDate((short) (era.getStartingYear()), (byte) (0));
		// The number of plates is roughly inversely proportional to the size of the
		// plates for a constant world size.
		final int totalPlateCountExponent = worldSizeExponent - tectonicPlateSizeExponent;
		final int playablePlateCountExponent = playableAreaSizeExponent - tectonicPlateSizeExponent;
		// Latitude of the whole world in number of plates
		// + 1 to the exponent because our map is twice as wide as it is long
		// - 1 to the total to impose this number to be odd
		final int totalPlatesX = (1 << (totalPlateCountExponent + 1)) - 1;// 2^(totalPlateCountExponent + 1) - 1
		// Longitude of the whole world in number of plates
		// - 1 to the total to impose this number to be odd
		final int totalPlatesY = (1 << totalPlateCountExponent) - 1;// 2^totalPlateCountExponent - 1
		// Latitude of the playable area in number of plates
		// - 1 to the total to impose this number to be odd
		final int playablePlatesX = (1 << playablePlateCountExponent) - 1;// 2^playablePlateCountExponent - 1
		// Longitude of the playable area in number of plates
		// - 1 to the total to impose this number to be odd
		final int playablePlatesY = playablePlatesX;// 2^playablePlateCountExponent - 1
		// Lenght of the side of the tectonic plates in number of tiles
		// - 1 to the total to impose this number to be odd
		final int tectonicPlateSize = (1 << tectonicPlateSizeExponent) - 1;// 2^tectonicPlateSizeExponent - 1
		// 300ºK / factor of distance to sun
		final int absoluteTemperatureAtEquator = (int) (300.0d / distanceToSunFactor);
		// To celsius
		final int temperatureAtEquator = absoluteTemperatureAtEquator - 273;
		// We assume the poles are 60 degrees less than the equator
		final int temperatureDifference = -60;
		this.terrain = new Terrain(this,
				// Shape of the terrain
				terrainShape,
				// Latitude of the whole world in number of plates
				totalPlatesX,
				// Longitude of the whole world in number of plates
				totalPlatesY,
				// Latitude of the playable area in number of plates
				playablePlatesX,
				// Longitude of the playable area in number of plates
				playablePlatesY,
				// Tectonic plate size
				tectonicPlateSize,
				// Height levels of different things are proportional to plate size
				// Crust thickness is proportional to the size of the tectonic plates
				(int) (tectonicPlateSize * crustThicknessFactor),
				// Sea level is proportional to the size of the tectonic plates
				(int) (tectonicPlateSize * seaLevelFactor),
				// Roughness is proportional to the size of the tectonic plates
				(int) (tectonicPlateSize * roughnessFactor),
				// Axis tilt
				axisTilt,
				// Temperature at equator
				temperatureAtEquator,
				// Temperature at the poles - temperature at the equator
				temperatureDifference);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	public String getVersion() {
		return this.version;
	}

	@Override
	public RandomGenerator getPRNG() {
		return this.PRNG;
	}

	public int getTectonicPlateSize() {
		return this.terrain.getTectonicPlateSize();
	}

	public int getBaseMagma() {
		return this.terrain.getBaseMagma();
	}

	public int getBaseHeight() {
		return this.terrain.getBaseHeight();
	}

	public int getBaseRoughness() {
		return this.terrain.getBaseRoughness();
	}

	public int getBaseWater() {
		return this.terrain.getBaseWater();
	}

	public int getTotalSizeX() {
		return this.terrain.getTotalSizeX();
	}

	public int getTotalSizeY() {
		return this.terrain.getTotalSizeY();
	}

	public int getTotalMinX() {
		return this.terrain.getTotalMinX();
	}

	public int getTotalMinY() {
		return this.terrain.getTotalMinY();
	}

	public int getTotalMaxX() {
		return this.terrain.getTotalMaxX();
	}

	public int getTotalMaxY() {
		return this.terrain.getTotalMaxY();
	}

	public int getPlayableSizeX() {
		return this.terrain.getPlayableSizeX();
	}

	public int getPlayableSizeY() {
		return this.terrain.getPlayableSizeY();
	}

	public int getPlayableMinX() {
		return this.terrain.getPlayableMinX();
	}

	public int getPlayableMinY() {
		return this.terrain.getPlayableMinY();
	}

	public int getPlayableMaxX() {
		return this.terrain.getPlayableMaxX();
	}

	public int getPlayableMaxY() {
		return this.terrain.getPlayableMaxY();
	}

	public Tile getTile(final int x, final int y) {
		return this.terrain.getTile(x, y);
	}

	/**
	 * Get the current date.
	 */
	public IDate getDate() {
		return this.date;
	}

	public Iterable<Tile> getAllTiles() {
		return this.terrain.getAllTiles();
	}

	public Iterable<Tile> getTileSet(final int fromX, final int fromY, final int toX, final int toY) {
		return this.terrain.getTileSet(fromX, fromY, toX, toY);
	}

	public Iterable<TectonicPlate> getAllTectonicPlates() {
		return this.terrain.getAllTectonicPlates();
	}

	public Iterable<Building> getAllBuildings() {
		return this.terrain.getAllBuildings();
	}

	public Iterable<Feature> getAllFeatures() {
		return this.terrain.getAllFeatures();
	}

	public Iterable<Unit> getAllUnits() {
		return this.terrain.getAllUnits();
	}

	public Iterable<Citizen> getAllCitizens() {
		return this.terrain.getAllCitizens();
	}

	public Iterable<City> getAllCities() {
		return this.cities;
	}

	public void newBuilding(final BuildingFactory<?> buildingFactory, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city) {
		this.terrain.newBuilding(buildingFactory, coordinateX, coordinateY, orientation, health, city);
	}

	public void newFeature(final FeatureFactory<?> featureFactory, final int coordinateX, final int coordinateY,
			final int sizeX, final int sizeY, final int health, final City city) {
		this.terrain.newFeature(featureFactory, coordinateX, coordinateY, sizeX, sizeY, health, city);
	}

	public void newKnot(final KnotFactory<?> knotFactory, final int fromX, final int fromY, final int toX,
			final int toY, final City city) {
		this.terrain.newKnot(knotFactory, fromX, fromY, toX, toY, city);
	}

	public void newUnit(final UnitFactory<?> unitFactory, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city) {
		this.terrain.newUnit(unitFactory, coordinateX, coordinateY, orientation, health, city);
	}

	@Override
	public void tick() {
		this.date.increase();
	}

}
