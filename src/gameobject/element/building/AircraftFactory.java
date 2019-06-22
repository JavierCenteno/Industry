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

import java.util.Set;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import gameobject.city.Citizen;
import gameobject.city.City;
import gameobject.element.building.category.Tertiary;
import gameobject.world.TectonicPlate.Tile;
import gameobject.world.Terrain;
import gameobject.world.Terrain.Orientation;
import gameobject.world.World;
import typeobject.Resource;
import typeobject.Technology;
import util.amount.Amount;
import util.math.IMath;

/**
 * This light industry building uses raw resources to make diverse types of air
 * units on demand.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class AircraftFactory extends Building implements Tertiary {

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
	 * Number of operators buildings of this class can have.
	 */
	@Externalized
	private static int OPERATOR_NUMBER;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Upgrades of this building.
	 */
	private final BuildingUpgrade[] upgrades;
	/**
	 * Workmode of this building.
	 */
	private final AircraftFactoryWorkmode workmode;
	/**
	 * Managers of this building.
	 */
	private final Citizen[] managers;
	/**
	 * Number of managers of this building.
	 */
	private final int numberOfManagers;
	/**
	 * Operators of this building.
	 */
	private final Citizen[] operators;
	/**
	 * Number of operators of this building.
	 */
	private final int numberOfOperators;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of aircraft factories.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see BuildingFactory
	 */
	public static class AircraftFactoryFactory extends BuildingFactory<AircraftFactory> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final AircraftFactoryFactory aircraftFactoryFactory = new AircraftFactoryFactory();
			Building.FACTORIES.add(aircraftFactoryFactory);
			Tertiary.FACTORIES.add(aircraftFactoryFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return AircraftFactory.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return AircraftFactory.COST;
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
				toX = fromX + AircraftFactory.SIZE_X;
				toY = fromY + AircraftFactory.SIZE_Y;
				break;
			case EAST:
				toX = fromX + AircraftFactory.SIZE_Y;
				toY = fromY - AircraftFactory.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - AircraftFactory.SIZE_X;
				toY = fromY - AircraftFactory.SIZE_Y;
				break;
			case WEST:
				toX = fromX - AircraftFactory.SIZE_Y;
				toY = fromY + AircraftFactory.SIZE_X;
				break;
			default:
				toX = fromX;
				toY = fromY;
				break;
			}
			// List of all heights
			final double[] landLevels = new double[AircraftFactory.SIZE_X * AircraftFactory.SIZE_Y];
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
			if (deviationLand > AircraftFactory.MAX_ROUGHNESS) {
				return false;
			}
			return true;
		}

		@Override
		public AircraftFactory make(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final int health, final City city) {
			// Instance the class
			final AircraftFactory instance = new AircraftFactory(terrain.getWorld(), coordinateX, coordinateY,
					orientation, health, city);
			// Coordinates of starting and ending tiles
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			int toX;
			int toY;
			switch (orientation) {
			case NORTH:
				toX = fromX + AircraftFactory.SIZE_X;
				toY = fromY + AircraftFactory.SIZE_Y;
				break;
			case EAST:
				toX = fromX + AircraftFactory.SIZE_Y;
				toY = fromY - AircraftFactory.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - AircraftFactory.SIZE_X;
				toY = fromY - AircraftFactory.SIZE_Y;
				break;
			case WEST:
				toX = fromX - AircraftFactory.SIZE_Y;
				toY = fromY + AircraftFactory.SIZE_X;
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

	public static class AssemblyLine extends BuildingUpgrade {

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

		private final AircraftFactory base;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public AssemblyLine(final AircraftFactory base) {
			this.base = base;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return AssemblyLine.NAME;
		}

		@Override
		public String getDescription() {
			return AssemblyLine.DESCRIPTION;
		}

		@Override
		public AircraftFactory getBase() {
			return this.base;
		}

		@Override
		public void activate() {
			super.activate();
			/*
			 * TODO: activating an upgrade resets the health of the upgraded thing and
			 * consumes its cost in resources
			 */
		}

		@Override
		public void deactivate() {
			super.deactivate();
			/*
			 * TODO: deactivating an upgrade resets the health of the upgraded thing
			 */
		}

		@Override
		public Amount[] getCost() {
			return AssemblyLine.COST;
		}

		@Override
		public Technology getRequiredTechnology() {
			return AssemblyLine.REQUIRED_TECHNOLOGY;
		}

	}

	public static enum AircraftFactoryWorkmode implements BuildingWorkmode {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		DEFAULT;

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
		final Json i18n = Industry.I18N.get("gameObject", "element", "building", "list", "aircraftFactory");
		AircraftFactory.NAME = i18n.get("name").as(String.class);
		AircraftFactory.DESCRIPTION = i18n.get("description").as(String.class);
		AssemblyLine.NAME = i18n.get("upgrades", "assemblyLine", "name").as(String.class);
		AssemblyLine.DESCRIPTION = i18n.get("upgrades", "assemblyLine", "description").as(String.class);
		AircraftFactoryWorkmode.DEFAULT.name = i18n.get("workmodes", "default", "name").as(String.class);
		AircraftFactoryWorkmode.DEFAULT.description = i18n.get("workmodes", "default", "description").as(String.class);
		// data
		final Json data = Industry.DATA.get("gameObject", "element", "building", "list", "aircraftFactory");
		AircraftFactory.SIZE_X = data.get("sizeX").as(int.class);
		AircraftFactory.SIZE_Y = data.get("sizeY").as(int.class);
		AircraftFactory.MAX_ROUGHNESS = data.get("maxRoughness").as(int.class);
		AircraftFactory.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		AircraftFactory.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key, "quantity").as(int.class);
			AircraftFactory.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
		AircraftFactory.MANAGER_NUMBER = data.get("workers", "manager").as(int.class);
		AircraftFactory.OPERATOR_NUMBER = data.get("workers", "operator").as(int.class);
		final Set<String> assemblyLineCostKeys = data.get("upgrades", "assemblyLine", "cost").keys();
		AssemblyLine.COST = new Amount[assemblyLineCostKeys.size()];
		int assemblyLineCostIndex = 0;
		for (final String key : assemblyLineCostKeys) {
			final int quantity = data.get("upgrades", "assemblyLine", "cost", key).as(int.class);
			AircraftFactory.COST[assemblyLineCostIndex] = new Amount(Resource.getResource(key), quantity);
			++assemblyLineCostIndex;
		}
		AssemblyLine.REQUIRED_TECHNOLOGY = Technology
				.getTechnology(data.get("upgrades", "assemblyLine", "requiredTechnology").as(String.class));
		AircraftFactoryWorkmode.DEFAULT.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "default", "requiredTechnology").as(String.class));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public AircraftFactory(final World world, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city) {
		super(world, coordinateX, coordinateY, orientation, health, city);
		this.upgrades = new BuildingUpgrade[] {};
		this.workmode = AircraftFactoryWorkmode.DEFAULT;
		this.managers = new Citizen[AircraftFactory.MANAGER_NUMBER];
		this.numberOfManagers = 0;
		this.operators = new Citizen[AircraftFactory.OPERATOR_NUMBER];
		this.numberOfOperators = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return AircraftFactory.NAME;
	}

	@Override
	public String getDescription() {
		return AircraftFactory.DESCRIPTION;
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
	public AircraftFactoryWorkmode[] getWorkmodes() {
		final AircraftFactoryWorkmode[] workmodes = AircraftFactoryWorkmode.class.getEnumConstants();
		final AircraftFactoryWorkmode[] availableWorkmodes = new AircraftFactoryWorkmode[workmodes.length];
		int availableWorkmodeCount = 0;
		for (int index = 0; index < workmodes.length; ++index) {
			final AircraftFactoryWorkmode workmode = workmodes[index];
			if ((workmode != this.workmode)
					&& this.getCity().getCountry().getTechnologies().contains(workmode.getRequiredTechnology())) {
				availableWorkmodes[availableWorkmodeCount] = workmode;
				++availableWorkmodeCount;
			}
		}
		final AircraftFactoryWorkmode[] availableWorkmodesTrimmed = new AircraftFactoryWorkmode[availableWorkmodeCount];
		System.arraycopy(availableWorkmodes, 0, availableWorkmodesTrimmed, 0, availableWorkmodeCount);
		return availableWorkmodesTrimmed;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

	@Override
	public int getSizeX() {
		return AircraftFactory.SIZE_X;
	}

	@Override
	public int getSizeY() {
		return AircraftFactory.SIZE_Y;
	}

	@Override
	public Resource[] produces() {
		return new Resource[0];
	}

	@Override
	public Citizen[] getCitizens() {
		final Citizen[] citizens = new Citizen[this.managers.length + this.operators.length];
		System.arraycopy(this.managers, 0, citizens, 0, this.managers.length);
		System.arraycopy(this.operators, 0, citizens, this.managers.length, this.operators.length);
		return citizens;
	}

	@Override
	public void spreadSicknesses() {
		// TODO
	}

	@Override
	public void tick() {
		super.tick();
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
		// TODO: produce
	}

}
