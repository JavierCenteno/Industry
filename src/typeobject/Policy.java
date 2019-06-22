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
