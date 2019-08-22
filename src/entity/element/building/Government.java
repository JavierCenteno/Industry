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

import java.util.Set;

import api.Json;
import entity.city.Citizen;
import entity.city.City;
import entity.element.building.category.Political;
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
 * This political building is the government of a country. It is considered to
 * be the residence and workplace of its leader. It stores information about its
 * political system and code of laws.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Government extends Building implements Political {

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
	 * Number of governors buildings of this class can have.
	 */
	@Externalized
	private static int GOVERNOR_NUMBER;
	/**
	 * Number of managers buildings of this class can have.
	 */
	@Externalized
	private static int MANAGER_NUMBER;
	/**
	 * Number of bureaucrats buildings of this class can have.
	 */
	@Externalized
	private static int BUREAUCRAT_NUMBER;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Upgrades of this building.
	 */
	private final BuildingUpgrade[] upgrades;
	/**
	 * Workmode of this building.
	 */
	private final GovernmentWorkmode workmode;
	/**
	 * Governors of this building.
	 */
	private final Citizen[] governors;
	/**
	 * Number of governors of this building.
	 */
	private final int numberOfGovernors;
	/**
	 * Managers of this building.
	 */
	private final Citizen[] managers;
	/**
	 * Number of managers of this building.
	 */
	private final int numberOfManagers;
	/**
	 * Bureaucrats of this building.
	 */
	private final Citizen[] bureaucrats;
	/**
	 * Number of bureaucrats of this building.
	 */
	private final int numberOfBureaucrats;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of governments.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see BuildingFactory
	 */
	public static class GovernmentFactory extends BuildingFactory<Government> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final GovernmentFactory governmentFactory = new GovernmentFactory();
			Building.FACTORIES.add(governmentFactory);
			Political.FACTORIES.add(governmentFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return Government.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return Government.COST;
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
				toX = fromX + Government.SIZE_X;
				toY = fromY + Government.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Government.SIZE_Y;
				toY = fromY - Government.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Government.SIZE_X;
				toY = fromY - Government.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Government.SIZE_Y;
				toY = fromY + Government.SIZE_X;
				break;
			default:
				toX = fromX;
				toY = fromY;
				break;
			}
			// List of all heights
			final double[] landLevels = new double[Government.SIZE_X * Government.SIZE_Y];
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
			if (deviationLand > Government.MAX_ROUGHNESS) {
				return false;
			}
			return true;
		}

		@Override
		public Government make(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final int health, final City city) {
			// Instance the class
			final Government instance = new Government(terrain.getWorld(), coordinateX, coordinateY, orientation,
					health, city);
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			int toX;
			int toY;
			switch (orientation) {
			case NORTH:
				toX = fromX + Government.SIZE_X;
				toY = fromY + Government.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Government.SIZE_Y;
				toY = fromY - Government.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Government.SIZE_X;
				toY = fromY - Government.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Government.SIZE_Y;
				toY = fromY + Government.SIZE_X;
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

	public static enum GovernmentWorkmode implements BuildingWorkmode {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		SERVE_THE_PEOPLE;

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
		final Json i18n = Industry.I18N.get("entity", "element", "building", "list", "government");
		Government.NAME = i18n.get("name").as(String.class);
		Government.DESCRIPTION = i18n.get("description").as(String.class);
		GovernmentWorkmode.SERVE_THE_PEOPLE.name = i18n.get("workmodes", "serveThePeople", "name").as(String.class);
		GovernmentWorkmode.SERVE_THE_PEOPLE.description = i18n.get("workmodes", "serveThePeople", "description")
				.as(String.class);
		// data
		final Json data = Industry.DATA.get("entity", "element", "building", "list", "government");
		Government.SIZE_X = data.get("sizeX").as(int.class);
		Government.SIZE_Y = data.get("sizeY").as(int.class);
		Government.MAX_ROUGHNESS = data.get("maxRoughness").as(int.class);
		Government.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		Government.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key).as(int.class);
			Government.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
		Government.GOVERNOR_NUMBER = data.get("workers", "governor").as(int.class);
		Government.MANAGER_NUMBER = data.get("workers", "manager").as(int.class);
		Government.BUREAUCRAT_NUMBER = data.get("workers", "bureaucrat").as(int.class);
		GovernmentWorkmode.SERVE_THE_PEOPLE.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "serveThePeople", "requiredTechnology").as(String.class));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Government(final World world, final int coordinateX, final int coordinateY, final Orientation orientation,
			final int health, final City city) {
		super(world, coordinateX, coordinateY, orientation, health, city);
		this.upgrades = new BuildingUpgrade[] {};
		this.workmode = GovernmentWorkmode.SERVE_THE_PEOPLE;
		this.governors = new Citizen[Government.GOVERNOR_NUMBER];
		this.numberOfGovernors = 0;
		this.managers = new Citizen[Government.MANAGER_NUMBER];
		this.numberOfManagers = 0;
		this.bureaucrats = new Citizen[Government.BUREAUCRAT_NUMBER];
		this.numberOfBureaucrats = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return Government.NAME;
	}

	@Override
	public String getDescription() {
		return Government.DESCRIPTION;
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
	public GovernmentWorkmode[] getWorkmodes() {
		final GovernmentWorkmode[] workmodes = GovernmentWorkmode.class.getEnumConstants();
		final GovernmentWorkmode[] availableWorkmodes = new GovernmentWorkmode[workmodes.length];
		int availableWorkmodeCount = 0;
		for (int index = 0; index < workmodes.length; ++index) {
			final GovernmentWorkmode workmode = workmodes[index];
			if ((workmode != this.workmode)
					&& this.getCity().getCountry().getTechnologies().contains(workmode.getRequiredTechnology())) {
				availableWorkmodes[availableWorkmodeCount] = workmode;
				++availableWorkmodeCount;
			}
		}
		final GovernmentWorkmode[] availableWorkmodesTrimmed = new GovernmentWorkmode[availableWorkmodeCount];
		System.arraycopy(availableWorkmodes, 0, availableWorkmodesTrimmed, 0, availableWorkmodeCount);
		return availableWorkmodesTrimmed;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

	@Override
	public int getSizeX() {
		return Government.SIZE_X;
	}

	@Override
	public int getSizeY() {
		return Government.SIZE_Y;
	}

	@Override
	public Resource[] produces() {
		return new Resource[0];
	}

	@Override
	public Citizen[] getCitizens() {
		final Citizen[] citizens = new Citizen[this.governors.length + this.managers.length + this.bureaucrats.length];
		System.arraycopy(this.governors, 0, citizens, 0, this.governors.length);
		System.arraycopy(this.managers, 0, citizens, this.governors.length, this.managers.length);
		System.arraycopy(this.bureaucrats, 0, citizens, this.governors.length + this.managers.length,
				this.bureaucrats.length);
		return citizens;
	}

	@Override
	public void spreadSicknesses() {
		// TODO
	}

	@Override
	public void tick() {
		super.tick();
		// TODO: produce
	}

}
