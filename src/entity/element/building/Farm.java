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

import java.util.ArrayList;
import java.util.Set;

import api.Json;
import entity.city.Citizen;
import entity.city.City;
import entity.element.building.category.Primary;
import entity.element.feature.Crop;
import entity.element.feature.Feature.FeatureUpgrade;
import entity.world.TectonicPlate.Tile;
import entity.world.Terrain;
import entity.world.Terrain.Orientation;
import entity.world.World;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import type.Resource;
import type.Technology;
import util.amount.Amount;
import util.math.IMath;

/**
 * This primary industry building raises plants in crops and produces resources
 * from those plants.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Farm extends Building implements Primary {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the buildings of this class.
	 */
	@Internationalized
	private static String NAME;
	/**
	 * Description of the buildings of this class.
	 */
	@Internationalized
	private static String DESCRIPTION;
	/**
	 * Depth of this building.
	 */
	@Externalized
	private static int SIZE_X;
	/**
	 * Width of this building.
	 */
	@Externalized
	private static int SIZE_Y;
	/**
	 * Maximum deviation of height this building can be built on.
	 */
	@Externalized
	private static int MAX_ROUGHNESS;
	/**
	 * Technology required to build buildings of this class.
	 */
	@Externalized
	private static Technology REQUIRED_TECHNOLOGY;
	/**
	 * Cost of the buildings of this class.
	 */
	@Externalized
	private static Amount[] COST;
	/**
	 * Number of managers buildings of this class can have.
	 */
	@Externalized
	private static int MANAGER_NUMBER;
	/**
	 * Number of farmers buildings of this class can have.
	 */
	@Externalized
	private static int FARMER_NUMBER;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Upgrades of this building.
	 */
	private final BuildingUpgrade[] upgrades;
	/**
	 * Workmode of this building.
	 */
	private final FarmWorkmode workmode;
	/**
	 * Managers of this building.
	 */
	private final Citizen[] managers;
	/**
	 * Number of managers of this building.
	 */
	private final int numberOfManagers;
	/**
	 * Farmers of this building.
	 */
	private final Citizen[] farmers;
	/**
	 * Number of farmers of this building.
	 */
	private final int numberOfFarmers;
	/**
	 * Crops used by this building.
	 */
	private final ArrayList<Crop> crops;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of farms.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see BuildingFactory
	 */
	public static class FarmFactory extends BuildingFactory<Farm> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final FarmFactory farmFactory = new FarmFactory();
			Building.FACTORIES.add(farmFactory);
			Primary.FACTORIES.add(farmFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return Farm.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return Farm.COST;
		}

		@Override
		public boolean check(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final City city) {
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			int toX;
			int toY;
			switch (orientation) {
			case NORTH:
				toX = fromX + Farm.SIZE_X;
				toY = fromY + Farm.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Farm.SIZE_Y;
				toY = fromY - Farm.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Farm.SIZE_X;
				toY = fromY - Farm.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Farm.SIZE_Y;
				toY = fromY + Farm.SIZE_X;
				break;
			default:
				toX = fromX;
				toY = fromY;
				break;
			}
			// List of all heights
			final double[] landLevels = new double[Farm.SIZE_X * Farm.SIZE_Y];
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
			if (deviationLand > Farm.MAX_ROUGHNESS) {
				return false;
			}
			return true;
		}

		@Override
		public Farm make(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final int health, final City city) {
			// Instance the class
			final Farm instance = new Farm(terrain.getWorld(), coordinateX, coordinateY, orientation, health, city);
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			int toX;
			int toY;
			switch (orientation) {
			case NORTH:
				toX = fromX + Farm.SIZE_X;
				toY = fromY + Farm.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Farm.SIZE_Y;
				toY = fromY - Farm.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Farm.SIZE_X;
				toY = fromY - Farm.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Farm.SIZE_Y;
				toY = fromY + Farm.SIZE_X;
				break;
			default:
				toX = fromX;
				toY = fromY;
				break;
			}
			// For every tile in the rectangle between from and to, put the element
			for (final Tile tile : terrain.getTileSet(fromX, fromY, toX, toY)) {
				tile.setElement(instance);
			}
			return instance;
		}

	}

	public static class Greenhouses extends BuildingUpgrade {

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

		private final Farm base;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public Greenhouses(final Farm base) {
			this.base = base;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return Greenhouses.NAME;
		}

		@Override
		public String getDescription() {
			return Greenhouses.DESCRIPTION;
		}

		@Override
		public Farm getBase() {
			return this.base;
		}

		@Override
		public void activate() {
			super.activate();
			final ArrayList<Crop> crops = this.getBase().crops;
			for (int i = 0; i < crops.size(); ++i) {
				final FeatureUpgrade[] upgrades = crops.get(i).getUpgrades();
				for (int j = 0; j < upgrades.length; ++j) {
					final FeatureUpgrade upgrade = upgrades[j];
					if (upgrade instanceof Crop.Greenhouse) {
						upgrade.activate();
					}
				}
			}
			/*
			 * TODO: activating an upgrade resets the health of the upgraded thing and
			 * consumes its cost in resources
			 */
		}

		@Override
		public void deactivate() {
			super.deactivate();
			final ArrayList<Crop> crops = this.getBase().crops;
			for (int i = 0; i < crops.size(); ++i) {
				final FeatureUpgrade[] upgrades = crops.get(i).getUpgrades();
				for (int j = 0; j < upgrades.length; ++j) {
					final FeatureUpgrade upgrade = upgrades[j];
					if (upgrade instanceof Crop.Greenhouse) {
						upgrade.deactivate();
					}
				}
			}
			/*
			 * TODO: deactivating an upgrade resets the health of the upgraded thing
			 */
		}

		@Override
		public Amount[] getCost() {
			return Greenhouses.COST;
		}

		@Override
		public Technology getRequiredTechnology() {
			return Greenhouses.REQUIRED_TECHNOLOGY;
		}

	}

	public static class Irrigation extends BuildingUpgrade {

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

		private final Farm base;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public Irrigation(final Farm base) {
			this.base = base;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return Irrigation.NAME;
		}

		@Override
		public String getDescription() {
			return Irrigation.DESCRIPTION;
		}

		@Override
		public Farm getBase() {
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
			return Irrigation.COST;
		}

		@Override
		public Technology getRequiredTechnology() {
			return Irrigation.REQUIRED_TECHNOLOGY;
		}

	}

	public static class MechanizedHarvesting extends BuildingUpgrade {

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

		private final Farm base;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public MechanizedHarvesting(final Farm base) {
			this.base = base;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return MechanizedHarvesting.NAME;
		}

		@Override
		public String getDescription() {
			return MechanizedHarvesting.DESCRIPTION;
		}

		@Override
		public Farm getBase() {
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
			return MechanizedHarvesting.COST;
		}

		@Override
		public Technology getRequiredTechnology() {
			return MechanizedHarvesting.REQUIRED_TECHNOLOGY;
		}

	}

	public static enum FarmWorkmode implements BuildingWorkmode {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		DEFAULT, FERTILIZER;

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Name of this workmode.
		 */
		private String name;
		/**
		 * Description of this workmode.
		 */
		private String description;
		/**
		 * Technology required to use this workmode.
		 */
		private Technology requiredTechnology;

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public Technology getRequiredTechnology() {
			return this.requiredTechnology;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("entity", "element", "building", "list", "farm");
		Farm.NAME = i18n.get("name").as(String.class);
		Farm.DESCRIPTION = i18n.get("description").as(String.class);
		Greenhouses.NAME = i18n.get("upgrades", "greenhouses", "name").as(String.class);
		Greenhouses.DESCRIPTION = i18n.get("upgrades", "greenhouses", "description").as(String.class);
		Irrigation.NAME = i18n.get("upgrades", "irrigation", "name").as(String.class);
		Irrigation.DESCRIPTION = i18n.get("upgrades", "irrigation", "description").as(String.class);
		MechanizedHarvesting.NAME = i18n.get("upgrades", "mechanizedHarvesting", "name").as(String.class);
		MechanizedHarvesting.DESCRIPTION = i18n.get("upgrades", "mechanizedHarvesting", "description").as(String.class);
		FarmWorkmode.DEFAULT.name = i18n.get("workmodes", "default", "name").as(String.class);
		FarmWorkmode.DEFAULT.description = i18n.get("workmodes", "default", "description").as(String.class);
		FarmWorkmode.FERTILIZER.name = i18n.get("workmodes", "fertilizer", "name").as(String.class);
		FarmWorkmode.FERTILIZER.description = i18n.get("workmodes", "fertilizer", "description").as(String.class);
		// data
		final Json data = Industry.DATA.get("entity", "element", "building", "list", "farm");
		Farm.SIZE_X = data.get("sizeX").as(int.class);
		Farm.SIZE_Y = data.get("sizeY").as(int.class);
		Farm.MAX_ROUGHNESS = data.get("maxRoughness").as(int.class);
		Farm.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		Farm.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key).as(int.class);
			Farm.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
		Farm.MANAGER_NUMBER = data.get("workers", "manager").as(int.class);
		Farm.FARMER_NUMBER = data.get("workers", "farmer").as(int.class);
		final Set<String> greenhousesCostKeys = data.get("upgrades", "greenhouses", "cost").keys();
		Greenhouses.COST = new Amount[greenhousesCostKeys.size()];
		int greenhousesCostIndex = 0;
		for (final String key : greenhousesCostKeys) {
			final int quantity = data.get("upgrades", "greenhouses", "cost", key).as(int.class);
			Farm.COST[greenhousesCostIndex] = new Amount(Resource.getResource(key), quantity);
			++greenhousesCostIndex;
		}
		Greenhouses.REQUIRED_TECHNOLOGY = Technology
				.getTechnology(data.get("upgrades", "greenhouses", "requiredTechnology").as(String.class));
		final Set<String> irrigationCostKeys = data.get("upgrades", "irrigation", "cost").keys();
		Irrigation.COST = new Amount[irrigationCostKeys.size()];
		int irrigationCostIndex = 0;
		for (final String key : irrigationCostKeys) {
			final int quantity = data.get("upgrades", "irrigation", "cost", key).as(int.class);
			Farm.COST[irrigationCostIndex] = new Amount(Resource.getResource(key), quantity);
			++irrigationCostIndex;
		}
		Irrigation.REQUIRED_TECHNOLOGY = Technology
				.getTechnology(data.get("upgrades", "irrigation", "requiredTechnology").as(String.class));
		final Set<String> mechanizedHarvestingCostKeys = data.get("upgrades", "mechanizedHarvesting", "cost").keys();
		Irrigation.COST = new Amount[mechanizedHarvestingCostKeys.size()];
		int mechanizedHarvestingCostIndex = 0;
		for (final String key : mechanizedHarvestingCostKeys) {
			final int quantity = data.get("upgrades", "mechanizedHarvesting", "cost", key).as(int.class);
			Farm.COST[mechanizedHarvestingCostIndex] = new Amount(Resource.getResource(key), quantity);
			++mechanizedHarvestingCostIndex;
		}
		MechanizedHarvesting.REQUIRED_TECHNOLOGY = Technology
				.getTechnology(data.get("upgrades", "mechanizedHarvesting", "requiredTechnology").as(String.class));
		FarmWorkmode.DEFAULT.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "default", "requiredTechnology").as(String.class));
		FarmWorkmode.FERTILIZER.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "fertilizer", "requiredTechnology").as(String.class));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Farm(final World world, final int coordinateX, final int coordinateY, final Orientation orientation,
			final int health, final City city) {
		super(world, coordinateX, coordinateY, orientation, health, city);
		this.upgrades = new BuildingUpgrade[] { new Greenhouses(this), new Irrigation(this),
				new MechanizedHarvesting(this) };
		this.workmode = FarmWorkmode.DEFAULT;
		this.managers = new Citizen[Farm.MANAGER_NUMBER];
		this.numberOfManagers = 0;
		this.farmers = new Citizen[Farm.FARMER_NUMBER];
		this.numberOfFarmers = 0;
		this.crops = new ArrayList<Crop>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return Farm.NAME;
	}

	@Override
	public String getDescription() {
		return Farm.DESCRIPTION;
	}

	@Override
	public BuildingUpgrade[] getUpgrades() {
		final BuildingUpgrade[] availableUpgrades = new BuildingUpgrade[this.upgrades.length];
		int availableUpgradeCount = 0;
		for (int index = 0; index < this.upgrades.length; ++index) {
			final BuildingUpgrade upgrade = this.upgrades[index];
			if ((upgrade.getActive() == false)
					&& this.getCity().getCountry().getTechnologies().contains(upgrade.getRequiredTechnology())) {
				availableUpgrades[availableUpgradeCount] = upgrade;
				++availableUpgradeCount;
			}
		}
		final BuildingUpgrade[] availableUpgradesTrimmed = new BuildingUpgrade[availableUpgradeCount];
		System.arraycopy(availableUpgrades, 0, availableUpgradesTrimmed, 0, availableUpgradeCount);
		return availableUpgradesTrimmed;
	}

	@Override
	public FarmWorkmode[] getWorkmodes() {
		final FarmWorkmode[] workmodes = FarmWorkmode.class.getEnumConstants();
		final FarmWorkmode[] availableWorkmodes = new FarmWorkmode[workmodes.length];
		int availableWorkmodeCount = 0;
		for (int index = 0; index < workmodes.length; ++index) {
			final FarmWorkmode workmode = workmodes[index];
			if ((workmode != this.workmode)
					&& this.getCity().getCountry().getTechnologies().contains(workmode.getRequiredTechnology())) {
				availableWorkmodes[availableWorkmodeCount] = workmode;
				++availableWorkmodeCount;
			}
		}
		final FarmWorkmode[] availableWorkmodesTrimmed = new FarmWorkmode[availableWorkmodeCount];
		System.arraycopy(availableWorkmodes, 0, availableWorkmodesTrimmed, 0, availableWorkmodeCount);
		return availableWorkmodesTrimmed;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

	@Override
	public int getSizeX() {
		return Farm.SIZE_X;
	}

	@Override
	public int getSizeY() {
		return Farm.SIZE_Y;
	}

	@Override
	public Resource[] produces() {
		// TODO
		return null;
	}

	@Override
	public Citizen[] getCitizens() {
		final Citizen[] citizens = new Citizen[this.managers.length + this.farmers.length];
		System.arraycopy(this.managers, 0, citizens, 0, this.managers.length);
		System.arraycopy(this.farmers, 0, citizens, this.managers.length, this.farmers.length);
		return citizens;
	}

	@Override
	public void spreadSicknesses() {
		// TODO
	}

	@Override
	public void tick() {
		/*
		 * Some buildings loose health periodically and require maintenance. Without it,
		 * they eventually collapse.
		 */
		--this.health;
		/*
		 * TODO: buildings take extra damage if any of their tiles is supposed to be
		 * land but their water or magma level is higher than land level. This makes
		 * floods and volcanic eruptions dangerous.
		 */
		/*
		 * TODO: Farms grow plants and harvest them as they grow. Once they're mature,
		 * they chop down the plants to gather its resources.
		 */
		// TODO: produce
		// TODO: pay the worker's wages
	}

}
