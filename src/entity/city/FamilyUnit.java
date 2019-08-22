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

package entity.city;

import entity.Entity;
import entity.element.building.Building;
import entity.element.equipment.Equipment;
import entity.world.World;

/**
 * This class represents a group of citizens that live together in an housing
 * unit and share a wealth level.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class FamilyUnit extends Entity {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Maximum number of members a family unit can have.
	 */
	public static final int MAXIMUM_FAMILY_UNIT_SIZE = 16;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Members of this family unit.
	 */
	public Citizen[] members;
	/**
	 * Building where this family unit lives.
	 */
	public Building home;
	/**
	 * Homemaker of this family unit. It is necessary to have one if there are
	 * minors in the family unit.
	 */
	public Citizen homemaker;
	/**
	 * Equipment this citizen owns.
	 */
	public Equipment[] possessions;
	/**
	 * Money owned by this family unit.
	 */
	public int wealth;
	/**
	 *
	 */
	public int lastMonthEarning;
	/**
	 *
	 */
	public int lastMonthSpending;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public FamilyUnit(final World world, final Citizen... members) {
		super(world);
		this.members = members;
		this.home = null;
		this.homemaker = null;
		this.possessions = new Equipment[0];
		this.wealth = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	public Citizen[] getMembers() {
		return this.members;
	}

	@Override
	public void tick() {
		super.tick();
		/*
		 * TODO: order of actions
		 *
		 * For every member in this family unit:
		 *
		 * If single and adult, look for a couple (If homosexual couples are enabled,
		 * there's a 1 in 8 chance of being same sex). The couple will leave their
		 * family unit to join this one.
		 *
		 *
		 * For every member in this family unit:
		 *
		 * If unemployed (except if child and child labor is banned) and there are job
		 * openings that require studies the member doesn't have, study for the
		 * available job openings. If there are job openings, join the job. If there's a
		 * job opening that pays better than current job, join it. If not found a job,
		 * try to continue studies anyway.
		 *
		 * If employed, work and collect salary.
		 *
		 * If employed and either parent is also employed and there's a housing opening,
		 * form a new family unit with spouse and children (unless they aren't part of
		 * the family unit).
		 *
		 *
		 * Pay taxes.
		 *
		 *
		 * For every member in this family unit:
		 *
		 * If married, calculate probability of having a child (increases with the child
		 * death rate, increases with the overall death rate, increases with wealth,
		 * decreases with number of children in family unit).
		 *
		 *
		 * If there are children in this family and there's no homemaker:
		 *
		 * If there's a homemaker or a retiree already in their family, the homemaker or
		 * retiree will be the child's caretaker
		 *
		 * If there are no homemakers or retirees in the family the family will try to
		 * send the child to a nursery (granted that they can afford the fee!)
		 *
		 * if they don't have a retiree to do that if there's an unemployed in the
		 * family the unemployed will become a homemaker and the caretaker of the child
		 *
		 * if all adults in the family are employed the one making the least money will
		 * quit their job, become a homemaker and the caretaker of the child
		 *
		 *
		 * Every month, family members with jobs work and collect their salaries (add
		 * them to the wealth level of the family).
		 *
		 * They spend it in the following order (skipping a step if they can't afford
		 * it):
		 *
		 * food -> healthcare -> housing -> entertainment and luxuries until they run
		 * out
		 *
		 * If they can't afford housing, they'll try to find a cheaper house, if they
		 * can't, they'll become homeless. If there's a house opening of a better
		 * quality than the current house and the difference between current monthly
		 * earnings and current monthly spending is bigger than the difference between
		 * the rent of the opening and the rent of the other house, they move there.
		 */
	}

}
