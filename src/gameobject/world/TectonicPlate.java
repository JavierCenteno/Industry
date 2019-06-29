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

package gameobject.world;

import gameobject.GameObject;
import gameobject.city.City;
import gameobject.element.Element;
import typeobject.AnimalType;
import typeobject.MineralType;
import typeobject.PlantType;
import typeobject.SoilType;
import util.integermatrix.IntegerMatrix;
import util.integermatrix.ObjectMatrix;
import util.integermatrix.SmallIntegerMatrix;
import util.integermatrix.SmallObjectMatrix;
import util.math.IMath;

/**
 * This class represents a small grid of tiles which is part of a world and the
 * basic unit for terrain generation.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class TectonicPlate extends GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Proper name given to this province.
	 */
	public String name;
	/**
	 * Terrain this tectonic plate is part of.
	 */
	private final Terrain terrain;
	/**
	 * Index of this plate in the terrain.
	 */
	public final int plateIndexX, plateIndexY;
	/**
	 * Vector of movement of this plate used for terrain generation.
	 */
	public final int magmaFlowX, magmaFlowY;
	/**
	 * City this tectonic plate ("province") belongs to.
	 */
	/*
	 * TODO: Players can make territorial claims of plates that aren't owned or
	 * other players own. If no one else claims them, they belong to the player who
	 * claimed them. Upon conflict, treaties or wars can be made.
	 */
	public City city;
	/**
	 * Can lock tectonic plates to put them in quarantine. Quarantine has to be
	 * enforced by security. It can be breached by units or civilians.
	 */
	private boolean unlocked;
	/**
	 * Grid of integers representing the levels of magma in the different tiles of
	 * this plate.
	 */
	private final IntegerMatrix magma;
	/**
	 * Grid of integers representing the levels of land in the different tiles of
	 * this plate.
	 */
	private final IntegerMatrix land;
	/**
	 * Grid of integers representing the levels of water in the different tiles of
	 * this plate.
	 */
	private final IntegerMatrix water;
	/**
	 * Grid of integers representing the levels of pollution in the different tiles
	 * of this plate.
	 */
	private final IntegerMatrix pollution;
	/**
	 * Grid of objects representing the soil types in this plate.
	 */
	private final ObjectMatrix<SoilType> soilTypes;
	/**
	 * Grid of objects representing the mineral types in this plate.
	 */
	private final ObjectMatrix<MineralType> mineralTypes;
	/**
	 * Grid of objects representing the plant types in this plate.
	 */
	private final ObjectMatrix<PlantType> plantTypes;
	/**
	 * Grid of objects representing the animal types in this plate.
	 */
	private final ObjectMatrix<AnimalType> animalTypes;
	/**
	 * Grid of objects representing the elements in this plate.
	 */
	private final ObjectMatrix<Element> elements;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Represents a single immutable tile. This class is used to obtain and
	 * manipulate data about a particular tile of a tectonic plate. Tiles are
	 * squares of land with 16 meters side.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 *
	 */
	public static class Tile {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		/**
		 * Increase of the angle taken by the inclination of a tile at the different
		 * months of the year.
		 *
		 * @see util.idate.IDate#getMonth()
		 */
		private static final double[] ANGLE_DELTAS = new double[] {
				// January (summer in the south, winter in the north)
				+(Math.sqrt(3d) / 2d),
				// February (summer in the south, winter in the north)
				1d,
				// March (summer in the south, winter in the north)
				+(Math.sqrt(3d) / 2d),
				// April (fall in the south, spring in the north)
				+(1d / 2d),
				// May (fall in the south, spring in the north)
				0d,
				// June (fall in the south, spring in the north)
				-(1d / 2d),
				// July (winter in the south, summer in the north)
				-(Math.sqrt(3d) / 2d),
				// August (winter in the south, summer in the north)
				-1d,
				// September (winter in the south, summer in the north)
				-(Math.sqrt(3d) / 2d),
				// October (spring in the south, fall in the north)
				-(1d / 2d),
				// November (spring in the south, fall in the north)
				0d,
				// December (spring in the south, fall in the north)
				+(1d / 2d) };

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Tectonic plate this tile belongs to.
		 */
		private final TectonicPlate tectonicPlate;
		/**
		 * Index of this tile within its tectonic plate.
		 */
		private final int x, y;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Constructs a tile that is part of the given tectonic plate with the given
		 * indices within the plate.
		 */
		private Tile(final TectonicPlate tectonicPlate, final int x, final int y) {
			this.tectonicPlate = tectonicPlate;
			this.x = x;
			this.y = y;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the tectonic plate of this tile.
		 */
		public TectonicPlate getTectonicPlate() {
			return this.tectonicPlate;
		}

		/**
		 * Get the city of this tile.
		 */
		public City getCity() {
			return this.tectonicPlate.city;
		}

		/**
		 * Get the x coordinate of this tile in the terrain it is part of.
		 */
		public int getCoordinateX() {
			return ((this.tectonicPlate.plateIndexX * this.tectonicPlate.terrain.getTectonicPlateSize())
					- this.tectonicPlate.terrain.baseX) + this.x;
		}

		/**
		 * Get the y coordinate of this tile in the terrain it is part of.
		 */
		public int getCoordinateY() {
			return ((this.tectonicPlate.plateIndexY * this.tectonicPlate.terrain.getTectonicPlateSize())
					- this.tectonicPlate.terrain.baseY) + this.y;
		}

		/**
		 * Get the distance along the x axis between this tile and the one passed as an
		 * argument.
		 */
		public int getDistanceToX(final Tile tile) {
			return (((this.tectonicPlate.plateIndexX - tile.tectonicPlate.plateIndexX)
					* this.tectonicPlate.terrain.getTectonicPlateSize()) + this.x) - tile.x;
		}

		/**
		 * Get the distance along the y axis between this tile and the one passed as an
		 * argument.
		 */
		public int getDistanceToY(final Tile tile) {
			return (((this.tectonicPlate.plateIndexY - tile.tectonicPlate.plateIndexY)
					* this.tectonicPlate.terrain.getTectonicPlateSize()) + this.y) - tile.y;
		}

		/**
		 * Get the square of the distance between this tile and the one passed as an
		 * argument. Faster to calculate than the euclidean distance.
		 */
		public int getSquaredDistanceTo(final Tile tile) {
			final int increaseX = this.getDistanceToX(tile);
			final int increaseY = this.getDistanceToY(tile);
			return (increaseX * increaseX) + (increaseY * increaseY);
		}

		/**
		 * Get the distance between this tile and the one passed as an argument.
		 */
		public double getDistanceTo(final Tile tile) {
			return Math.sqrt((double) (this.getSquaredDistanceTo(tile)));
		}

		/**
		 * Get the closest tile to this tile among the ones passed as arguments.
		 *
		 * @param tile1
		 *                  A tile.
		 * @param tile2
		 *                  A tile.
		 * @return Which tile among tile1 and tile2 is closest to this tile.
		 */
		public Tile getClosestTile(final Tile tile1, final Tile tile2) {
			if (this.getSquaredDistanceTo(tile1) < this.getSquaredDistanceTo(tile2)) {
				return tile1;
			} else {
				return tile2;
			}
		}

		/**
		 * Get the magma level of this tile.
		 */
		public int getMagma() {
			return this.tectonicPlate.magma.get(this.x, this.y);
		}

		/**
		 * Set the magma level of this tile.
		 */
		public void setMagma(final int i) {
			this.tectonicPlate.magma.set(this.x, this.y, i);
		}

		/**
		 * Add to the magma level of this tile.
		 */
		public void addMagma(final int i) {
			this.tectonicPlate.magma.add(this.x, this.y, i);
		}

		/**
		 * Get the land level of this tile.
		 */
		public int getLand() {
			return this.tectonicPlate.land.get(this.x, this.y);
		}

		/**
		 * Set the land level of this tile.
		 */
		public void setLand(final int i) {
			this.tectonicPlate.land.set(this.x, this.y, i);
		}

		/**
		 * Add to the land level of this tile.
		 */
		public void addLand(final int i) {
			this.tectonicPlate.land.add(this.x, this.y, i);
		}

		/**
		 * Calculates the derivative with respect to x at x, y.
		 */
		private double diffX() {
			if (this.x == 0) {
				return this.tectonicPlate.land.get(this.x + 1, this.y) - this.tectonicPlate.land.get(this.x, this.y);
			}
			if (this.x == (this.tectonicPlate.terrain.getTectonicPlateSize() - 1)) {
				return this.tectonicPlate.land.get(this.x, this.y) - this.tectonicPlate.land.get(this.x - 1, this.y);
			}
			return ((double) (this.tectonicPlate.land.get(this.x + 1, this.y)
					- this.tectonicPlate.land.get(this.x - 1, this.y))) / 2.0d;
		}

		/**
		 * Calculates the derivative with respect to y at x, y.
		 */
		private double diffY() {
			if (this.y == 0) {
				return this.tectonicPlate.land.get(this.x, this.y + 1) - this.tectonicPlate.land.get(this.x, this.y);
			}
			if (this.y == (this.tectonicPlate.terrain.getTectonicPlateSize() - 1)) {
				return this.tectonicPlate.land.get(this.x, this.y) - this.tectonicPlate.land.get(this.x, this.y - 1);
			}
			return ((double) (this.tectonicPlate.land.get(this.x, this.y + 1)
					- this.tectonicPlate.land.get(this.x, this.y - 1))) / 2.0d;
		}

		/**
		 * Get the slope of the land of this tile as the modulus of the gradient of the
		 * derivative with respect to x and y.
		 */
		public double getSlope() {
			final double diffX = this.diffX();
			final double diffY = this.diffY();
			return Math.sqrt((diffX * diffX) + (diffY * diffY));
		}

		/**
		 * Calculates the second derivative with respect to x at x, y.
		 */
		private double curvX() {
			if (this.x == 0) {
				return ((this.tectonicPlate.land.get(this.x, this.y)
						- (2 * this.tectonicPlate.land.get(this.x + 1, this.y)))
						+ this.tectonicPlate.land.get(this.x + 2, this.y)) / 2d;
			}
			if (this.x == 1) {
				return ((this.tectonicPlate.land.get(this.x - 1, this.y)
						- (3 * this.tectonicPlate.land.get(this.x, this.y)))
						+ (2 * this.tectonicPlate.land.get(this.x + 2, this.y))) / 4d;
			}
			if (this.x == (this.tectonicPlate.terrain.getTectonicPlateSize() - 2)) {
				return ((this.tectonicPlate.land.get(this.x - 2, this.y)
						- (3 * this.tectonicPlate.land.get(this.x, this.y)))
						+ (2 * this.tectonicPlate.land.get(this.x + 1, this.y))) / 4d;
			}
			if (this.x == (this.tectonicPlate.terrain.getTectonicPlateSize() - 1)) {
				return ((this.tectonicPlate.land.get(this.x - 2, this.y)
						- (2 * this.tectonicPlate.land.get(this.x - 1, this.y)))
						+ this.tectonicPlate.land.get(this.x, this.y)) / 2d;
			}
			return ((this.tectonicPlate.land.get(this.x - 2, this.y)
					- (2 * this.tectonicPlate.land.get(this.x, this.y)))
					+ this.tectonicPlate.land.get(this.x + 2, this.y)) / 4d;
		}

		/**
		 * Calculates the second derivative with respect to x at x, y.
		 */
		private double curvY() {
			if (this.y == 0) {
				return ((this.tectonicPlate.land.get(this.x, this.y)
						- (2 * this.tectonicPlate.land.get(this.x, this.y + 1)))
						+ this.tectonicPlate.land.get(this.x, this.y + 2)) / 2d;
			}
			if (this.y == 1) {
				return (((2 * this.tectonicPlate.land.get(this.x, this.y - 1))
						- (3 * this.tectonicPlate.land.get(this.x, this.y)))
						+ this.tectonicPlate.land.get(this.x, this.y + 2)) / 4d;
			}
			if (this.y == (this.tectonicPlate.terrain.getTectonicPlateSize() - 2)) {
				return ((this.tectonicPlate.land.get(this.x, this.y - 2)
						- (3 * this.tectonicPlate.land.get(this.x, this.y)))
						+ (2 * this.tectonicPlate.land.get(this.x, this.y + 1))) / 4d;
			}
			if (this.y == (this.tectonicPlate.terrain.getTectonicPlateSize() - 1)) {
				return ((this.tectonicPlate.land.get(this.x, this.y - 2)
						- (2 * this.tectonicPlate.land.get(this.x, this.y - 1)))
						+ this.tectonicPlate.land.get(this.x, this.y)) / 2d;
			}
			return ((this.tectonicPlate.land.get(this.x, this.y - 2)
					- (2 * this.tectonicPlate.land.get(this.x, this.y)))
					+ this.tectonicPlate.land.get(this.x, this.y + 2)) / 4d;
		}

		/**
		 * Get the curvature of the land of this tile as the modulus of the gradient of
		 * the second derivative with respect to x and y.
		 */
		public double getCurvature() {
			final double curvX = this.curvX();
			final double curvY = this.curvY();
			return Math.sqrt((curvX * curvX) + (curvY * curvY));
		}

		/**
		 * Get the surface temperature of this tile in Celsius for a given change to the
		 * axial tilt.
		 */
		private int getTemperature(final double deltaAngle) {
			final int magma = this.getMagma();
			final int land = this.getLand();
			final int water = this.getWater();
			if ((magma >= land) && (magma >= water)) {
				return 1000;
			} else {
				/*
				 * Average angle with which the sunlight hits a latitude y, between -1 and 1,
				 * with -1 being the angle at the most southern point and 1 being the angle at
				 * the most northern point. In this value, we distinguish between southern and
				 * northern because the axial tilt causes a variation in angle whose sign is
				 * dependent on the hemisphere.
				 */
				final double averageSunlightAngle = (double) this.getCoordinateY()
						/ (double) this.tectonicPlate.terrain.getTotalMaxY();
				// Current angle with which sunlight hits
				final double currentSunlightAngle = averageSunlightAngle + deltaAngle;
				// Absolute inclination of the sun rays
				double absoluteSunlightAngle = Math.abs(currentSunlightAngle);
				/*
				 * If it goes over the pole, do two minus the angle (if it goes over 1.0 by
				 * adding, it continues going under 1.0 by removing).
				 */
				if (absoluteSunlightAngle > 1.0d) {
					absoluteSunlightAngle = 2.0d - absoluteSunlightAngle;
				}
				// Variation incurred in temperature by latitude
				final double deltaTemperatureLatitude = absoluteSunlightAngle
						* (double) this.tectonicPlate.terrain.temperatureDifference;
				// Variation incurred in temperature by height
				final double deltaTemperatureHeight = (double) IMath.maximum(land, water)
						* this.tectonicPlate.terrain.heightCoolingFactor;
				// Total integer variation incurred in temperature
				final int variation = (int) (deltaTemperatureLatitude + deltaTemperatureHeight);
				return this.tectonicPlate.terrain.baseTemperature + variation;
			}
		}

		/**
		 * Get the average yearly temperature of this tile in Celsius.
		 */
		public int getAverageTemperature() {
			return this.getTemperature(0d);
		}

		/**
		 * Get the current temperature of this tile in Celsius taking into account
		 * seasonal changes.
		 */
		public int getCurrentTemperature() {
			// Seasonal change in angle
			final double deltaAngle = Tile.ANGLE_DELTAS[this.tectonicPlate.getWorld().getDate().getMonth()]
					* this.tectonicPlate.terrain.axialTilt;
			return this.getTemperature(deltaAngle);
		}

		/**
		 * Get the temperature of this tile in Celsius at all months.
		 */
		public int[] getMonthlyTemperatures() {
			final int[] monthlyTemperatures = new int[Tile.ANGLE_DELTAS.length];
			for (int i = 0; i < Tile.ANGLE_DELTAS.length; ++i) {
				monthlyTemperatures[i] = this.getTemperature(Tile.ANGLE_DELTAS[i]);
			}
			return monthlyTemperatures;
		}

		/**
		 * Get the atmospheric pressure of this tile in millibars for a given
		 * temperature.
		 */
		private int getAtmosphericPressureForTemperature(final int temperature) {
			final int variationFromTemperature = temperature / 2;
			// Air pressure drops at roughly 12 millibars per 100 meters
			final int variationFromLand = this.getLand() / -8;
			return 1000 + variationFromTemperature + variationFromLand;
		}

		/**
		 * Get the average yearly atmospheric pressure of this tile in millibars.
		 */
		public int getAverageAtmosphericPressure() {
			return this.getAtmosphericPressureForTemperature(this.getAverageTemperature());
		}

		/**
		 * Get the current atmospheric pressure of this tile in millibars taking into
		 * account seasonal changes.
		 */
		public int getCurrentAtmosphericPressure() {
			return this.getAtmosphericPressureForTemperature(this.getCurrentTemperature());
		}

		/**
		 * Get the atmospheric pressure of this tile in millibars at all months.
		 */
		public int[] getMonthlyAtmosphericPressures() {
			final int[] monthlyAtmosphericPressures = this.getMonthlyTemperatures();
			for (int i = 0; i < monthlyAtmosphericPressures.length; ++i) {
				monthlyAtmosphericPressures[i] = this
						.getAtmosphericPressureForTemperature(monthlyAtmosphericPressures[i]);
			}
			return monthlyAtmosphericPressures;
		}

		/**
		 * Get the water level of this tile.
		 */
		public int getWater() {
			return this.tectonicPlate.water.get(this.x, this.y);
		}

		/**
		 * Set the water level of this tile.
		 */
		public void setWater(final int i) {
			this.tectonicPlate.water.set(this.x, this.y, i);
		}

		/**
		 * Add to the water level of this tile.
		 */
		public void addWater(final int i) {
			this.tectonicPlate.water.add(this.x, this.y, i);
		}

		/**
		 * Get the humidity of this tile, defined as the difference between the water
		 * level and the land level.
		 */
		public int getHumidity() {
			return this.getWater() - this.getLand();
		}

		/**
		 * Get the pollution level of this tile.
		 */
		public int getPollution() {
			return this.tectonicPlate.pollution.get(this.x, this.y);
		}

		/**
		 * Set the pollution level of this tile.
		 */
		public void setPollution(final int i) {
			this.tectonicPlate.pollution.set(this.x, this.y, i);
		}

		/**
		 * Add to the pollution level of this tile.
		 */
		public void addPollution(final int i) {
			this.tectonicPlate.pollution.add(this.x, this.y, i);
		}

		/**
		 * Get the current base soil color of this tile as an ARBG integer.
		 *
		 * The resulting color depends on the temperature and humidity and ranges
		 * between FFE0D0C0 for dry hot, FF807060 for wet hot, FFD0D0D0 for dry cold and
		 * FF707070 for wet cold.
		 *
		 * Accepted temperatures range between -32 and 32. Any value outside of that
		 * range will be treated as its closest value within that range.
		 *
		 * Accepted humidities range between -48 and 0. Any value outside of that range
		 * will be treated as its closest value within that range.
		 *
		 */
		public int getSoilColor() {
			final int temperature = this.getCurrentTemperature();
			final int humidity = this.getHumidity();
			int deltaTemperature;
			int deltaHumidity;
			if (temperature <= -32) {
				deltaTemperature = 0;
			} else if (temperature >= 32) {
				deltaTemperature = 16;
			} else {
				deltaTemperature = (temperature + 32) / 4;
			}
			if (humidity <= -48) {
				deltaHumidity = 96;
			} else if (humidity >= 0) {
				deltaHumidity = 0;
			} else {
				deltaHumidity = -2 * humidity;
			}
			final int baseColor = 112 + deltaHumidity;
			final int red = baseColor + deltaTemperature;
			final int green = baseColor;
			final int blue = baseColor - deltaTemperature;
			return (0xFF000000) | (red << 16) | (green << 8) | (blue);
		}

		/**
		 * Get the soil type in this tile.
		 */
		public SoilType getSoilType() {
			return this.tectonicPlate.soilTypes.get(this.x, this.y);
		}

		/**
		 * Set the soil type in this tile.
		 */
		public void setSoilType(final SoilType soilType) {
			this.tectonicPlate.soilTypes.set(this.x, this.y, soilType);
		}

		/**
		 * Get the mineral type in this tile.
		 */
		public MineralType getMineralType() {
			return this.tectonicPlate.mineralTypes.get(this.x, this.y);
		}

		/**
		 * Set the mineral type in this tile.
		 */
		public void setMineralType(final MineralType mineralType) {
			this.tectonicPlate.mineralTypes.set(this.x, this.y, mineralType);
		}

		/**
		 * Get the plant type in this tile.
		 */
		public PlantType getPlantType() {
			return this.tectonicPlate.plantTypes.get(this.x, this.y);
		}

		/**
		 * Set the plant type in this tile.
		 */
		public void setPlantType(final PlantType plantType) {
			this.tectonicPlate.plantTypes.set(this.x, this.y, plantType);
		}

		/**
		 * Get the animal type in this tile.
		 */
		public AnimalType getAnimalType() {
			return this.tectonicPlate.animalTypes.get(this.x, this.y);
		}

		/**
		 * Set the animal type in this tile.
		 */
		public void setAnimalType(final AnimalType animalType) {
			this.tectonicPlate.animalTypes.set(this.x, this.y, animalType);
		}

		/**
		 * Get the element on this tile.
		 */
		public Element getElement() {
			return this.tectonicPlate.elements.get(this.x, this.y);
		}

		/**
		 * Set the element on this tile.
		 */
		public void setElement(final Element element) {
			this.tectonicPlate.elements.set(this.x, this.y, element);
		}

		/**
		 * Get whether this tile is currently occupied by an element.
		 */
		public boolean isOccupied() {
			return this.getElement() != null;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a tectonic plate which belongs to a specific city and has a specific
	 * side length in tiles.
	 *
	 * @param city
	 *                              City this tectonic plate belongs to.
	 * @param tectonicPlateSize
	 *                              Lenght of the sides of this tectonic plate in
	 *                              tiles.
	 */
	public TectonicPlate(final Terrain terrain, final int plateIndexX, final int plateIndexY, final int magmaFlowX,
			final int magmaFlowY, final City city, final int tectonicPlateSize) {
		super(terrain.getWorld());
		this.terrain = terrain;
		this.plateIndexX = plateIndexX;
		this.plateIndexY = plateIndexY;
		this.magmaFlowX = magmaFlowX;
		this.magmaFlowY = magmaFlowY;
		this.name = "";
		this.city = city;
		this.unlocked = true;
		this.magma = new SmallIntegerMatrix(tectonicPlateSize, tectonicPlateSize);
		this.land = new SmallIntegerMatrix(tectonicPlateSize, tectonicPlateSize);
		this.water = new SmallIntegerMatrix(tectonicPlateSize, tectonicPlateSize);
		this.pollution = new SmallIntegerMatrix(tectonicPlateSize, tectonicPlateSize);
		this.soilTypes = new SmallObjectMatrix<>(tectonicPlateSize, tectonicPlateSize);
		this.mineralTypes = new SmallObjectMatrix<>(tectonicPlateSize, tectonicPlateSize);
		this.plantTypes = new SmallObjectMatrix<>(tectonicPlateSize, tectonicPlateSize);
		this.animalTypes = new SmallObjectMatrix<>(tectonicPlateSize, tectonicPlateSize);
		this.elements = new SmallObjectMatrix<>(tectonicPlateSize, tectonicPlateSize);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	public String getProperName() {
		return this.name;
	}

	public Terrain getTerrain() {
		return this.terrain;
	}

	public City getCity() {
		return this.city;
	}

	public boolean isUnlocked() {
		return this.unlocked;
	}

	public void lock() {
		this.unlocked = false;
	}

	public void unlock() {
		this.unlocked = true;
	}

	public Tile getTile(final int x, final int y) {
		if ((x < 0) || (y < 0) || (x >= this.terrain.getTectonicPlateSize())
				|| (y >= this.terrain.getTectonicPlateSize())) {
			return null;
		}
		return new Tile(this, x, y);
	}

}
