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

import java.util.Comparator;

import api.Json;
import entity.Entity;
import entity.element.building.Building;
import entity.world.World;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import type.Ideology.BeliefSet;
import type.KnowledgeField.SkillSet;
import type.Occupation;
import type.Sickness;
import util.idate.IDate;

/**
 * This class represents a citizen.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Citizen extends Entity {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the citizens of this class.
	 */
	@Internationalized
	public static String NAME;
	/**
	 * Names a female citizen can have.
	 */
	@Internationalized
	public static String[] FEMALE_NAMES;
	/**
	 * Names a male citizen can have.
	 */
	@Internationalized
	public static String[] MALE_NAMES;
	/**
	 * Surnames a citizen can have.
	 */
	@Internationalized
	public static String[] SURNAMES;
	/**
	 * Number of months it takes for a citizen to be born.
	 */
	@Externalized
	private static int GESTATION_PERIOD;
	/**
	 * Number of months it takes for a citizen to achieve maturity and be considered
	 * an adult.
	 */
	@Externalized
	private static int MATURITY_PERIOD;
	/**
	 * Number of months citizens live to on average.
	 */
	@Externalized
	private static int LIFE_EXPECTANCY;
	/**
	 * Maximum health citizens can have.
	 */
	@Externalized
	private static int VITALITY;
	/**
	 * Comparator that compares citizens by date of birth.
	 */
	public static final Comparator<Citizen> DATE_OF_BIRTH_COMPARATOR = (r1, r2) -> r1.birthDate.compareTo(r2.birthDate);
	/**
	 * Comparator that compares citizens by age.
	 */
	public static final Comparator<Citizen> AGE_COMPARATOR = (r1, r2) -> r2.birthDate.compareTo(r1.birthDate);

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Identifier of this citizen's name in the arrays of names.
	 */
	private final int name;
	/**
	 * Identifier of this citizen's surname in the array of surnames.
	 */
	private int surname;
	/**
	 * This citizen's biological sex.
	 */
	public Sex sex;
	/**
	 * Birth date of this citizen.
	 */
	public IDate birthDate;
	/**
	 * Death date of this citizen, null if it's still alive.
	 */
	public IDate deathDate;
	/**
	 * Number of sickness affecting the citizen (number of trailing places of the
	 * sicknesses array that indicate active sicknesses).
	 */
	public int currentSicknessAmount;
	/**
	 * Sicknesses that have affected this citizen.
	 */
	public Sickness[] sicknesses;
	/**
	 * Family unit this citizen is a part of.
	 */
	public FamilyUnit familyUnit;
	/**
	 * Parents of this citizen. May be null.
	 */
	public Citizen mother, father;
	/**
	 * Spouse of this citizen.
	 */
	public Citizen spouse;
	/**
	 * Offspring of this citizen.
	 */
	public Citizen[] offspring;
	/**
	 * Building where this citizen works.
	 */
	public Building workplace;
	/**
	 * Occupation of this citizen.
	 */
	public Occupation occupation;
	/**
	 * Needs of citizens.
	 */
	public byte foodNeed, healthNeed, workNeed, housingNeed, entertainmentNeed, politicalNeed;
	/**
	 * Array of the value of ideologies this citizen has.
	 */
	public byte[] ideologies;
	/**
	 * Set of skills this citizen has.
	 */
	public SkillSet skills;
	/**
	 * Set of ideologies this citizen has.
	 */
	public BeliefSet beliefs;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Enumeration representing the biological sex of a citizen.
	 *
	 * @author Javier Centeno Vega
	 *
	 */
	public static enum Sex {
		MALE, FEMALE
	};

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("entity", "citizen");
		Citizen.NAME = i18n.get("name").as(String.class);
		Citizen.FEMALE_NAMES = i18n.get("femaleNames").as(String[].class);
		Citizen.MALE_NAMES = i18n.get("maleNames").as(String[].class);
		Citizen.SURNAMES = i18n.get("surnames").as(String[].class);
		// data
		final Json data = Industry.DATA.get("entity", "citizen");
		Citizen.GESTATION_PERIOD = data.get("gestationPeriod").as(int.class);
		Citizen.MATURITY_PERIOD = data.get("maturityPeriod").as(int.class);
		Citizen.LIFE_EXPECTANCY = data.get("lifeExpectancy").as(int.class);
		Citizen.VITALITY = data.get("vitality").as(int.class);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Citizen(final World world, final FamilyUnit familyUnit, final Citizen mother, final Citizen father) {
		super(world);
		this.sex = this.getPRNG().pick(Sex.class.getEnumConstants());
		this.birthDate = this.getWorld().getDate().clone();
		this.deathDate = null;
		this.currentSicknessAmount = 0;
		this.sicknesses = new Sickness[0];
		this.familyUnit = familyUnit;
		this.mother = mother;
		this.father = father;
		this.spouse = null;
		this.offspring = new Citizen[0];
		this.workplace = null;
		this.occupation = null;
		// Pick a random name
		this.name = this.getPRNG().generateUniformInteger(Integer.MAX_VALUE);
		switch (this.sex) {
		case FEMALE:
			if (mother != null) {
				// Female citizens inherit their mother's surname
				this.surname = mother.surname;
			} else {
				// Pick a random surname
				this.surname = this.getPRNG().generateUniformInteger(Integer.MAX_VALUE);
			}
			break;
		case MALE:
			if (father != null) {
				// Male citizens inherit their father's surname
				this.surname = father.surname;
			} else {
				// Pick a random surname
				this.surname = this.getPRNG().generateUniformInteger(Integer.MAX_VALUE);
			}
			break;
		default:
			break;
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this citizen.
	 */
	public String getName() {
		if (this.name < 0) {
			return "?";
		}
		if (this.sex == Sex.FEMALE) {
			return Citizen.FEMALE_NAMES[this.name % Citizen.FEMALE_NAMES.length];
		} else {
			return Citizen.MALE_NAMES[this.name % Citizen.MALE_NAMES.length];
		}
	}

	/**
	 * Get the surname of this citizen.
	 */
	public String getSurname() {
		if (this.surname < 0) {
			return "?";
		}
		return Citizen.SURNAMES[this.surname % Citizen.SURNAMES.length];
	}

	/**
	 * Kills this citizen.
	 */
	public void kill() {
		// TODO: If this citizen is working somewhere, fire them. Corpses can't have a
		// job.
	}

	/**
	 * Tries to infect this citizen with a certain sickness. If the citizen hasn't
	 * already suffered through that sickness, it'll be added to the list of
	 * sicknesses.
	 *
	 * @param sickness
	 *                     Sickness to infect this citizen with.
	 */
	public void infect(final Sickness sickness) {
		int index = this.sicknesses.length;
		while (--index >= 0) {
			if (this.sicknesses[index] == sickness) {
				return;
			}
		}
		final Sickness[] newSicknesses = new Sickness[this.sicknesses.length + 1];
		System.arraycopy(this.sicknesses, 0, newSicknesses, 0, this.sicknesses.length);
		newSicknesses[this.sicknesses.length] = sickness;
		this.sicknesses = newSicknesses;
		++this.currentSicknessAmount;
	}

	/**
	 * Cures this citizen of the first sickness it got that's still active.
	 */
	public void cure() {
		if (this.currentSicknessAmount > 0) {
			--this.currentSicknessAmount;
		}
	}

	/**
	 * Triggers all sicknesses to spread and damage this citizen.
	 */
	public void triggerSicknesses() {
		// For all diseases affecting this citizen
		for (int sicknessIndex = this.sicknesses.length - 1; sicknessIndex >= (this.sicknesses.length
				- this.currentSicknessAmount); --sicknessIndex) {
			// One of the sicknesses currently affecting this citizen
			final Sickness sickness = this.sicknesses[sicknessIndex];
			/*
			 * TODO: determine if the citizen dies on a chance based on the sickness'
			 * lethality.
			 */
			// Sicknesses only last for a month. If a citizen is sick, the sickness is unset
			// automatically before the tick ends.
			this.cure();
		}
	}

	/**
	 * Calculates from 0 to 100 (inclusive) how much this citizen agrees with the
	 * citizen passed as a parameter.
	 */
	public int getOpinionOnCitizen(final Citizen citizen) {
		return this.beliefs.affinityWith(citizen.beliefs);
	}

	@Override
	public void tick() {
		super.tick();
	}

}
