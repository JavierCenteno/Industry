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
import gameobject.element.building.category.PublicHealth;
import gameobject.world.TectonicPlate.Tile;
import gameobject.world.Terrain;
import gameobject.world.Terrain.Orientation;
import gameobject.world.World;
import typeobject.Resource;
import typeobject.Technology;
import util.amount.Amount;
import util.math.IMath;

/**
 * This public health building heals wounded citizens and tries to cure their
 * sicknesses.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Hospital extends Building implements PublicHealth {

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
	 * Number of nurses buildings of this class can have.
	 */
	@Externalized
	private static int NURSE_NUMBER;
	/**
	 * Number of physicians buildings of this class can have.
	 */
	@Externalized
	private static int PHYSICIAN_NUMBER;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Upgrades of this building.
	 */
	private final BuildingUpgrade[] upgrades;
	/**
	 * Workmode of this building.
	 */
	private final HospitalWorkmode workmode;
	/**
	 * Managers of this building.
	 */
	private final Citizen[] managers;
	/**
	 * Number of managers of this building.
	 */
	private final int numberOfManagers;
	/**
	 * Nurses of this building.
	 */
	private final Citizen[] nurses;
	/**
	 * Number of nurses of this building.
	 */
	private final int numberOfNurses;
	/**
	 * Physicians of this building.
	 */
	private final Citizen[] physicians;
	/**
	 * Number of physicians of this building.
	 */
	private final int numberOfPhysicians;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of hospitals.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see BuildingFactory
	 */
	public static class HospitalFactory extends BuildingFactory<Hospital> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final HospitalFactory hospitalFactory = new HospitalFactory();
			Building.FACTORIES.add(hospitalFactory);
			PublicHealth.FACTORIES.add(hospitalFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return Hospital.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return Hospital.COST;
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
				toX = fromX + Hospital.SIZE_X;
				toY = fromY + Hospital.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Hospital.SIZE_Y;
				toY = fromY - Hospital.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Hospital.SIZE_X;
				toY = fromY - Hospital.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Hospital.SIZE_Y;
				toY = fromY + Hospital.SIZE_X;
				break;
			default:
				toX = fromX;
				toY = fromY;
				break;
			}
			// List of all heights
			final double[] landLevels = new double[Hospital.SIZE_X * Hospital.SIZE_Y];
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
			if (deviationLand > Hospital.MAX_ROUGHNESS) {
				return false;
			}
			return true;
		}

		@Override
		public Hospital make(final Terrain terrain, final int coordinateX, final int coordinateY,
				final Orientation orientation, final int health, final City city) {
			// Instance the class
			final Hospital instance = new Hospital(terrain.getWorld(), coordinateX, coordinateY, orientation, health,
					city);
			final int fromX = coordinateX;
			final int fromY = coordinateY;
			int toX;
			int toY;
			switch (orientation) {
			case NORTH:
				toX = fromX + Hospital.SIZE_X;
				toY = fromY + Hospital.SIZE_Y;
				break;
			case EAST:
				toX = fromX + Hospital.SIZE_Y;
				toY = fromY - Hospital.SIZE_X;
				break;
			case SOUTH:
				toX = fromX - Hospital.SIZE_X;
				toY = fromY - Hospital.SIZE_Y;
				break;
			case WEST:
				toX = fromX - Hospital.SIZE_Y;
				toY = fromY + Hospital.SIZE_X;
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

	public static enum HospitalWorkmode implements BuildingWorkmode {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		MEDICAL_CENTER, UNIVERSITY_HOSPITAL;

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
		final Json i18n = Industry.I18N.get("gameObject", "element", "building", "hospital");
		Hospital.NAME = i18n.get("name").as(String.class);
		Hospital.DESCRIPTION = i18n.get("description").as(String.class);
		HospitalWorkmode.MEDICAL_CENTER.name = i18n.get("workmodes", "medicalCenter", "name").as(String.class);
		HospitalWorkmode.MEDICAL_CENTER.description = i18n.get("workmodes", "medicalCenter", "description")
				.as(String.class);
		HospitalWorkmode.UNIVERSITY_HOSPITAL.name = i18n.get("workmodes", "universityHospital", "name")
				.as(String.class);
		HospitalWorkmode.UNIVERSITY_HOSPITAL.description = i18n.get("workmodes", "universityHospital", "description")
				.as(String.class);
		// data
		final Json data = Industry.DATA.get("gameObject", "element", "building", "hospital");
		Hospital.SIZE_X = data.get("sizeX").as(int.class);
		Hospital.SIZE_Y = data.get("sizeY").as(int.class);
		Hospital.MAX_ROUGHNESS = data.get("maxRoughness").as(int.class);
		Hospital.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		Hospital.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key).as(int.class);
			Hospital.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
		Hospital.MANAGER_NUMBER = data.get("workers", "manager").as(int.class);
		Hospital.NURSE_NUMBER = data.get("workers", "nurse").as(int.class);
		Hospital.PHYSICIAN_NUMBER = data.get("workers", "physician").as(int.class);
		HospitalWorkmode.MEDICAL_CENTER.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "medicalCenter", "requiredTechnology").as(String.class));
		HospitalWorkmode.UNIVERSITY_HOSPITAL.requiredTechnology = Technology
				.getTechnology(data.get("workmodes", "universityHospital", "requiredTechnology").as(String.class));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Hospital(final World world, final int coordinateX, final int coordinateY, final Orientation orientation,
			final int health, final City city) {
		super(world, coordinateX, coordinateY, orientation, health, city);
		this.upgrades = new BuildingUpgrade[] {};
		this.workmode = HospitalWorkmode.MEDICAL_CENTER;
		this.managers = new Citizen[Hospital.MANAGER_NUMBER];
		this.numberOfManagers = 0;
		this.nurses = new Citizen[Hospital.NURSE_NUMBER];
		this.numberOfNurses = 0;
		this.physicians = new Citizen[Hospital.PHYSICIAN_NUMBER];
		this.numberOfPhysicians = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return Hospital.NAME;
	}

	@Override
	public String getDescription() {
		return Hospital.DESCRIPTION;
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
	public HospitalWorkmode[] getWorkmodes() {
		final HospitalWorkmode[] workmodes = HospitalWorkmode.class.getEnumConstants();
		final HospitalWorkmode[] availableWorkmodes = new HospitalWorkmode[workmodes.length];
		int availableWorkmodeCount = 0;
		for (int index = 0; index < workmodes.length; ++index) {
			final HospitalWorkmode workmode = workmodes[index];
			if ((workmode != this.workmode)
					&& this.getCity().getCountry().getTechnologies().contains(workmode.getRequiredTechnology())) {
				availableWorkmodes[availableWorkmodeCount] = workmode;
				++availableWorkmodeCount;
			}
		}
		final HospitalWorkmode[] availableWorkmodesTrimmed = new HospitalWorkmode[availableWorkmodeCount];
		System.arraycopy(availableWorkmodes, 0, availableWorkmodesTrimmed, 0, availableWorkmodeCount);
		return availableWorkmodesTrimmed;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

	@Override
	public int getSizeX() {
		return Hospital.SIZE_X;
	}

	@Override
	public int getSizeY() {
		return Hospital.SIZE_Y;
	}

	@Override
	public Resource[] produces() {
		return new Resource[0];
	}

	@Override
	public Citizen[] getCitizens() {
		final Citizen[] citizens = new Citizen[this.managers.length + this.nurses.length + this.physicians.length];
		System.arraycopy(this.managers, 0, citizens, 0, this.managers.length);
		System.arraycopy(this.nurses, 0, citizens, this.managers.length, this.nurses.length);
		System.arraycopy(this.physicians, 0, citizens, this.managers.length + this.nurses.length,
				this.physicians.length);
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
		 * TODO:
		 *
		 * for Citizen worker in this.workers: for as many patients as a doctor can
		 * have: pick a citizen c among all citizens living in the area if(c.isSick() &&
		 * worker.getType() == "citizen.physician"): //doctors give part of the health
		 * diseases remove c.health += ( worker.skill[index of biology skill] /
		 * maxSkill) * c.getSickness().lethality building.consume(1 of drug) else:
		 * //nurses increase health by 1 regardless c.health += 1 building.consume(1 of
		 * drug)
		 *
		 * If this is a university hospital, it works as a university as well.
		 * Otherwise, it is a plain medical center.
		 */
	}

}
