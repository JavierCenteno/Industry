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

package entity.element.feature;

import java.util.Set;

import api.Json;
import entity.city.City;
import entity.world.TectonicPlate.Tile;
import entity.world.Terrain;
import entity.world.World;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import type.Resource;
import type.Technology;
import util.amount.Amount;
import util.math.IMath;

/**
 * A crop built by a farm.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Crop extends Feature {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the features of this class.
	 */
	@Internationalized
	private static String NAME;
	/**
	 * Description of the features of this class.
	 */
	@Internationalized
	private static String DESCRIPTION;
	/**
	 * Maximum deviation of height this building can be built on.
	 */
	@Externalized
	private static int MAX_ROUGHNESS;
	/**
	 * Technology required to build features of this class.
	 */
	@Externalized
	private static Technology REQUIRED_TECHNOLOGY;
	/**
	 * Cost of building a tile of a features of this class.
	 */
	@Externalized
	private static Amount[] COST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Upgrades of this feature.
	 */
	private final FeatureUpgrade[] upgrades;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of crops.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see FeatureFactory
	 */
	public static class CropFactory extends FeatureFactory<Crop> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final CropFactory cropFactory = new CropFactory();
			Feature.FACTORIES.add(cropFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return Crop.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return Crop.COST;
		}

		@Override
		public boolean check(final Terrain terrain, final int coordinateX, final int coordinateY, final int sizeX,
				final int sizeY, final City city) {
			// Coordinates of starting and ending tiles
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			final int toX = fromX + sizeX;
			final int toY = fromY + sizeY;
			// List of all heights
			final double[] landLevels = new double[sizeX * sizeY];
			final int landIndex = -1;
			// For every tile in the rectangle between from and to, check the tiles
			for (final Tile tile : terrain.getTileSet(fromX, fromY, toX, toY)) {
				final City localCity = tile.getCity();
				final int localMagma = tile.getMagma();
				final int localLand = tile.getLand();
				final int localWater = tile.getWater();
				final boolean isOccupied = tile.isOccupied();
				// Can't build on foreign terrain
				if (localCity != city) {
					return false;
				}
				// Can't build on magma
				if (localMagma > localLand) {
					return false;
				}
				// Can't build on flooded terrain
				if (localWater > localLand) {
					return false;
				}
				// Can't build if there's something already in the tile
				if (isOccupied) {
					return false;
				}
				landLevels[landIndex] = (double) localLand;
			}
			// Mean of all heights
			final double meanLand = IMath.mean(landLevels);
			// Deviation of all heights
			final double deviationLand = IMath.absoluteDeviation(meanLand, landLevels);
			if (deviationLand > Crop.MAX_ROUGHNESS) {
				return false;
			}
			return true;
		}

		@Override
		public Crop make(final Terrain terrain, final int coordinateX, final int coordinateY, final int sizeX,
				final int sizeY, final int health, final City city) {
			// Instance the class
			final Crop instance = new Crop(terrain.getWorld(), coordinateX, coordinateY, sizeX, sizeY, health, city);
			// Coordinates of starting and ending tiles
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			final int toX = fromX + sizeX;
			final int toY = fromY + sizeY;
			// For every tile in the rectangle between from and to, put the element
			for (final Tile tile : terrain.getTileSet(fromX, fromY, toX, toY)) {
				tile.setElement(instance);
			}
			return instance;
		}

	}

	public static class Greenhouse extends FeatureUpgrade {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		/**
		 * Name of the upgrades of this class.
		 */
		private static String NAME;
		/**
		 * Description of the upgrades of this class.
		 */
		private static String DESCRIPTION;
		/**
		 * Cost of the upgrades of this class.
		 */
		private static Amount[] COST;
		/**
		 * Technology required to set the upgrades of this class.
		 */
		private static Technology REQUIRED_TECHNOLOGY;

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		private final Crop base;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public Greenhouse(final Crop base) {
			this.base = base;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return Greenhouse.NAME;
		}

		@Override
		public String getDescription() {
			return Greenhouse.DESCRIPTION;
		}

		@Override
		public Crop getBase() {
			return this.base;
		}

		@Override
		public void activate() {
			/*
			 * TODO: activating an upgrade resets the health of the upgraded thing and
			 * consumes its cost in resources
			 */
		}

		@Override
		public void deactivate() {
			/*
			 * TODO: deactivating an upgrade resets the health of the upgraded thing
			 */
		}

		@Override
		public Amount[] getCost() {
			return Greenhouse.COST;
		}

		@Override
		public Technology getRequiredTechnology() {
			return Greenhouse.REQUIRED_TECHNOLOGY;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("entity", "element", "feature", "list", "crop");
		Crop.NAME = i18n.get("name").as(String.class);
		Crop.DESCRIPTION = i18n.get("description").as(String.class);
		// data
		final Json data = Industry.DATA.get("entity", "element", "feature", "list", "crop");
		Crop.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		Crop.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key, "quantity").as(int.class);
			Crop.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
		final Set<String> greenhouseCostKeys = data.get("upgrades", "greenhouse", "cost").keys();
		Greenhouse.COST = new Amount[greenhouseCostKeys.size()];
		int greenhouseCostIndex = 0;
		for (final String key : greenhouseCostKeys) {
			final int quantity = data.get("upgrades", "greenhouse", "cost", key).as(int.class);
			Crop.COST[greenhouseCostIndex] = new Amount(Resource.getResource(key), quantity);
			++greenhouseCostIndex;
		}
		Greenhouse.REQUIRED_TECHNOLOGY = Technology
				.getTechnology(data.get("upgrades", "greenhouse", "requiredTechnology").as(String.class));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Crop(final World world, final int coordinateX, final int coordinateY, final int sizeX, final int sizeY,
			final int health, final City city) {
		super(world, coordinateX, coordinateY, sizeX, sizeY, health, city);
		this.upgrades = new FeatureUpgrade[] { new Greenhouse(this) };
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return Crop.NAME;
	}

	@Override
	public String getDescription() {
		return Crop.DESCRIPTION;
	}

	@Override
	public FeatureUpgrade[] getUpgrades() {
		final FeatureUpgrade[] availableUpgrades = new FeatureUpgrade[this.upgrades.length];
		int availableUpgradeCount = 0;
		for (int index = 0; index < this.upgrades.length; ++index) {
			final FeatureUpgrade upgrade = this.upgrades[index];
			if ((upgrade.getActive() == false)
					&& this.getCity().getCountry().getTechnologies().contains(upgrade.getRequiredTechnology())) {
				availableUpgrades[availableUpgradeCount] = upgrade;
				++availableUpgradeCount;
			}
		}
		final FeatureUpgrade[] availableUpgradesTrimmed = new FeatureUpgrade[availableUpgradeCount];
		System.arraycopy(availableUpgrades, 0, availableUpgradesTrimmed, 0, availableUpgradeCount);
		return availableUpgradesTrimmed;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

	@Override
	public Amount[] clear() {
		// TODO
		return null;
	}

}
