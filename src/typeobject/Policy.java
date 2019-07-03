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

package typeobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;

/**
 * This class represents a policy that is part of an ideology, can be decreed by
 * the government of a city and applies to everything owned by them.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Policy extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all policies.
	 */
	private static final Map<String, Policy> ALL_POLICIES_MAP;
	/**
	 * Global list of all policies.
	 */
	private static final List<Policy> ALL_POLICIES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this policy.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this policy.
	 */
	@Internationalized
	private String description;
	/**
	 * Ideology this policy is a part of.
	 */
	@Externalized
	private Ideology ideology;
	/**
	 * Policies that must be active when activating this policy.
	 */
	@Externalized
	private Policy[] requiredPolicies;
	/**
	 * Policies that cannot be active when activating this policy.
	 */
	@Externalized
	private Policy[] contradictingPolicies;
	/**
	 * Required technology to activate this policy.
	 */
	@Externalized
	private Technology requiredTechnology;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Contains references to the default policies.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 *
	 */
	public static class Policies {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		public static final Policy AGRARIAN_REFORM = Policy.getPolicy("agrarianReform");
		public static final Policy AGRARIAN_SUBSIDIES = Policy.getPolicy("agrarianSubsidies");
		public static final Policy ANIMAL_WELFARE = Policy.getPolicy("animalWelfare");
		public static final Policy ANTI_DETRACTION_LAW = Policy.getPolicy("antiDetractionLaw");
		public static final Policy BABY_CHECK = Policy.getPolicy("babyCheck");
		public static final Policy CHILDREN_RIGHTS = Policy.getPolicy("childrenRights");
		public static final Policy COMPULSORY_EDUCATION = Policy.getPolicy("compulsoryEducation");
		public static final Policy CONSCRIPTION = Policy.getPolicy("conscription");
		public static final Policy CONTRACEPTION_BAN = Policy.getPolicy("contraceptionBan");
		public static final Policy COPYRIGHT = Policy.getPolicy("copyright");
		public static final Policy CURFEW = Policy.getPolicy("curfew");
		public static final Policy DEATH_PENALTY_ABOLITION = Policy.getPolicy("deathPenaltyAbolition");
		public static final Policy DEMOCRACY = Policy.getPolicy("democracy");
		public static final Policy DRUG_CONTROL = Policy.getPolicy("drugControl");
		public static final Policy ENCRYPTION_BAN = Policy.getPolicy("encryptionBan");
		public static final Policy FAMILY_FRIENDLY_PROGRAMMING = Policy.getPolicy("familyFriendlyProgramming");
		public static final Policy FAMILY_SIZE_LIMIT = Policy.getPolicy("familySizeLimit");
		public static final Policy FOSSIL_FUEL_BAN = Policy.getPolicy("fossilFuelBan");
		public static final Policy FREE_MARRIAGE = Policy.getPolicy("freeMarriage");
		public static final Policy GAMBLING_BAN = Policy.getPolicy("gamblingBan");
		public static final Policy GENETIC_MODIFICATION_BAN = Policy.getPolicy("geneticModificationBan");
		public static final Policy GUN_CONTROL = Policy.getPolicy("gunControl");
		public static final Policy HEALTHCARE_REFORM = Policy.getPolicy("healthcareReform");
		public static final Policy HOUSING_REFORM = Policy.getPolicy("housingReform");
		public static final Policy INDUSTRY_SUBSIDIES = Policy.getPolicy("industrySubsidies");
		public static final Policy LEGAL_ABORTION = Policy.getPolicy("legalAbortion");
		public static final Policy LEGAL_EUTHANASIA = Policy.getPolicy("legalEuthanasia");
		public static final Policy LITTERING_BAN = Policy.getPolicy("litteringBan");
		public static final Policy LOTTERY = Policy.getPolicy("lottery");
		public static final Policy MARTIAL_LAW = Policy.getPolicy("martialLaw");
		public static final Policy NATURAL_BORN_CITIZEN_PRIVILEGE = Policy.getPolicy("naturalBornCitizenPrivilege");
		public static final Policy NET_NEUTRALITY = Policy.getPolicy("netNeutrality");
		public static final Policy POLLUTION_TAX = Policy.getPolicy("pollutionTax");
		public static final Policy PROHIBITION = Policy.getPolicy("prohibition");
		public static final Policy PROSTITUTION_BAN = Policy.getPolicy("prostitutionBan");
		public static final Policy REGULARIZATION = Policy.getPolicy("regularization");
		public static final Policy RENEWABLE_ENERGY_SUBSIDIES = Policy.getPolicy("renewableEnergySubsidies");
		public static final Policy RIGHT_TO_A_FAIR_TRIAL = Policy.getPolicy("rightToAFairTrial");
		public static final Policy SMOKING_BAN = Policy.getPolicy("smokingBan");
		public static final Policy SOCIAL_SECURITY = Policy.getPolicy("socialSecurity");
		public static final Policy TARIFFS = Policy.getPolicy("tariffs");
		public static final Policy TAX_CUT = Policy.getPolicy("taxCut");
		public static final Policy UNION_BAN = Policy.getPolicy("unionBan");
		public static final Policy UNIVERSAL_SUFFRAGE = Policy.getPolicy("universalSuffrage");
		public static final Policy VACCINATION_CAMPAIGN = Policy.getPolicy("vaccinationCampaign");
		public static final Policy WEALTH_TAX = Policy.getPolicy("wealthTax");

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all policies.
	 */
	static {
		ALL_POLICIES_MAP = new HashMap<>();
		ALL_POLICIES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "policy", "list");
		final Json data = Industry.DATA.get("typeObject", "policy", "list");
		for (final String key : data.keys()) {
			new Policy(key);
		}
		for (final String key : data.keys()) {
			final Policy policy = Policy.ALL_POLICIES_MAP.get(key);
			policy.name = i18n.get(key, "name").as(String.class);
			policy.description = i18n.get(key, "description").as(String.class);
			policy.ideology = Ideology.getIdeology(data.get(key, "ideology").as(String.class));
			policy.requiredPolicies = Policy.getPolicies(data.get(key, "requiredPolicies").as(String[].class));
			policy.contradictingPolicies = Policy
					.getPolicies(data.get(key, "contradictingPolicies").as(String[].class));
			policy.requiredTechnology = Technology.getTechnology(data.get(key, "requiredTechnology").as(String.class));
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new policy and adds it to the map of all policies under the given
	 * key and to the list of all policies.
	 *
	 * @param key
	 *                Key under which this policy will be added to the map of all
	 *                policies.
	 */
	private Policy(final String key) {
		super(Policy.ALL_POLICIES_LIST.size());
		Policy.ALL_POLICIES_MAP.put(key, this);
		Policy.ALL_POLICIES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the policies.
	 *
	 * @return A list of all the policies.
	 */
	public static List<Policy> getPolicies() {
		return Policy.ALL_POLICIES_LIST;
	}

	/**
	 * Gets the policies with the given keys.
	 *
	 * @param keys
	 *                 An array of policy keys.
	 * @return The array of policies with the given keys.
	 */
	public static Policy[] getPolicies(final String[] keys) {
		final Policy[] policies = new Policy[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			policies[i] = Policy.getPolicy(keys[i]);
		}
		return policies;
	}

	/**
	 * Gets the policy with the given key.
	 *
	 * @param key
	 *                An policy key.
	 * @return The policy with the given key.
	 */
	public static Policy getPolicy(final String key) {
		return Policy.ALL_POLICIES_MAP.get(key);
	}

	/**
	 * Gets the policies with the given ids.
	 *
	 * @param ids
	 *                An array of policy ids.
	 * @return The array of policies with the given ids.
	 */
	public static Policy[] getPolicies(final int[] ids) {
		final Policy[] policies = new Policy[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			policies[i] = Policy.getPolicy(ids[i]);
		}
		return policies;
	}

	/**
	 * Gets the policy with the given id.
	 *
	 * @param id
	 *               An policy id.
	 * @return The policy with the given id.
	 */
	public static Policy getPolicy(final int id) {
		return Policy.ALL_POLICIES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this policy.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this policy.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the ideology this policy is a part of.
	 */
	public Ideology getIdeology() {
		return this.ideology;
	}

	/**
	 * Get the policies that must be active when activating this policy.
	 */
	public Policy[] getRequiredPolicies() {
		return this.requiredPolicies;
	}

	/**
	 * Get the policies that cannot be active when activating this policy.
	 */
	public Policy[] getContradictingPolicies() {
		return this.contradictingPolicies;
	}

	/**
	 * Get the required technology to activate this policy.
	 */
	public Technology getRequiredTechnology() {
		return this.requiredTechnology;
	}

}
