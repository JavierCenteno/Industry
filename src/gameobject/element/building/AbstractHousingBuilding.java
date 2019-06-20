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

import gameobject.city.Citizen;
import gameobject.city.City;
import gameobject.city.FamilyUnit;
import gameobject.element.building.category.Housing;
import gameobject.world.Terrain.Orientation;
import gameobject.world.World;
import util.ArrayUtil;

/**
 * This class offers some base code to make the implementation of housing
 * buildings easier.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class AbstractHousingBuilding extends Building implements Housing {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	private final FamilyUnit[] units;
	private final int numberOfUnits;
	private Building[] foodSuppliers;
	private Building[] healthcareSuppliers;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public AbstractHousingBuilding(final World world, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city, final int numberOfUnits) {
		super(world, coordinateX, coordinateY, orientation, health, city);
		this.units = new FamilyUnit[numberOfUnits];
		this.numberOfUnits = 0;
		this.foodSuppliers = new Building[0];
		this.healthcareSuppliers = new Building[0];
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	protected void addFoodSupplier(final Building foodSupplier) {
		this.foodSuppliers = ArrayUtil.add(this.foodSuppliers, foodSupplier);
	}

	protected void removeFoodSupplier(final Building foodSupplier) {
		this.foodSuppliers = ArrayUtil.removeFirst(this.foodSuppliers, foodSupplier);
	}

	protected void addHealthcareSupplier(final Building healthcareSupplier) {
		this.healthcareSuppliers = ArrayUtil.add(this.healthcareSuppliers, healthcareSupplier);
	}

	protected void removeHealthcareSupplier(final Building healthcareSupplier) {
		this.healthcareSuppliers = ArrayUtil.removeFirst(this.healthcareSuppliers, healthcareSupplier);
	}

	@Override
	public Citizen[] getCitizens() {
		int numberOfCitizens = 0;
		for (int i = 0; i < this.numberOfUnits; ++i) {
			numberOfCitizens += this.units[i].getMembers().length;
		}
		final Citizen[] citizens = new Citizen[numberOfCitizens];
		numberOfCitizens = 0;
		for (int i = 0; i < this.numberOfUnits; ++i) {
			System.arraycopy(this.units[i].getMembers(), 0, citizens, numberOfCitizens,
					this.units[i].getMembers().length);
			numberOfCitizens += this.units[i].getMembers().length;
		}
		return citizens;
	}

}
