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
 * This class represents technologies that can be researched. Cities need to
 * fully research a technology to be able to perform actions that depend on it.
 *
 * Technologies may depend on other technologies to be researched. A basic
 * technology is a technology that has no previous technologies required.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Technology extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all technologies.
	 */
	private static final Map<String, Technology> ALL_TECHNOLOGIES_MAP;
	/**
	 * Global list of all technologies.
	 */
	private static final List<Technology> ALL_TECHNOLOGIES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this technology.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this technology.
	 */
	@Internationalized
	private String description;
	/**
	 * Technologies required to research this one. This field is determined from the
	 * data source.
	 */
	@Externalized
	private Technology[] requirements;
	/**
	 * Technologies that can be researched if this technology is researched. This
	 * field is derived from other technologies' requirements.
	 */
	@Externalized
	private Technology[] followUps;
	/**
	 * Knowledge field this technology belongs to.
	 */
	@Externalized
	private KnowledgeField knowledgeField;
	/**
	 * Era this technology belongs to.
	 */
	@Externalized
	private Era era;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all technologies.
	 */
	static {
		ALL_TECHNOLOGIES_MAP = new HashMap<>();
		ALL_TECHNOLOGIES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "technology");
		final Json data = Industry.DATA.get("typeObject", "technology");
		// Initialize technologies
		for (final String key : data.keys()) {
			new Technology(key);
		}
		// Load internationalization and requirements
		for (final String key : data.keys()) {
			final Technology technology = Technology.ALL_TECHNOLOGIES_MAP.get(key);
			technology.name = i18n.get(key, "name").as(String.class);
			technology.description = i18n.get(key, "description").as(String.class);
			technology.requirements = Technology.getTechnologies(data.get(key, "requirements").as(String[].class));
			for (final Technology requirement : technology.requirements) {
				requirement.addFollowUp(technology);
			}
			technology.knowledgeField = KnowledgeField
					.getKnowledgeField(data.get(key, "knowledgeField").as(String.class));
			technology.era = Era.getEra(data.get(key, "era").as(String.class));
		}
		// Initialize the requirements of technology "theory of everything" to all
		// technologies without a follow up.
		final ArrayList<Technology> technologiesWithNoFollowUp = new ArrayList<Technology>();
		for (final Technology technology : Technology.ALL_TECHNOLOGIES_LIST) {
			if (technology.followUps.length == 0) {
				technologiesWithNoFollowUp.add(technology);
			}
		}
		final Technology theoryOfEverything = Technology.getTechnology("theoryOfEverything");
		theoryOfEverything.requirements = technologiesWithNoFollowUp
				.toArray(new Technology[technologiesWithNoFollowUp.size()]);
		for (final Technology requirement : theoryOfEverything.requirements) {
			requirement.addFollowUp(theoryOfEverything);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new technology and adds it to the map of all technologies under the
	 * given key and to the list of all technologies.
	 *
	 * @param key
	 *                Key under which this technology will be added to the map of
	 *                all technologies.
	 */
	public Technology(final String key) {
		super(Technology.ALL_TECHNOLOGIES_LIST.size());
		Technology.ALL_TECHNOLOGIES_MAP.put(key, this);
		Technology.ALL_TECHNOLOGIES_LIST.add(this);
		this.requirements = null;
		this.followUps = new Technology[0];
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the technologies.
	 *
	 * @return A list of all the technologies.
	 */
	public static List<Technology> getAllTechnologies() {
		return Technology.ALL_TECHNOLOGIES_LIST;
	}

	/**
	 * Gets the technologies with the given keys.
	 *
	 * @param keys
	 *                 An array of technology keys.
	 * @return The array of technologies with the given keys.
	 */
	public static Technology[] getTechnologies(final String[] keys) {
		final Technology[] technologies = new Technology[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			technologies[i] = Technology.getTechnology(keys[i]);
		}
		return technologies;
	}

	/**
	 * Gets the technology with the given key.
	 *
	 * @param key
	 *                A technology key.
	 * @return The technology with the given key.
	 */
	public static Technology getTechnology(final String key) {
		return Technology.ALL_TECHNOLOGIES_MAP.get(key);
	}

	/**
	 * Gets the technologies with the given ids.
	 *
	 * @param ids
	 *                An array of technology ids.
	 * @return The array of technologies with the given ids.
	 */
	public static Technology[] getTechnologies(final int[] ids) {
		final Technology[] technologies = new Technology[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			technologies[i] = Technology.getTechnology(ids[i]);
		}
		return technologies;
	}

	/**
	 * Gets the technology with the given id.
	 *
	 * @param id
	 *               A technology id.
	 * @return The technology with the given id.
	 */
	public static Technology getTechnology(final int id) {
		return Technology.ALL_TECHNOLOGIES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this technology.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this technology.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the technologies required to research this one.
	 */
	public Technology[] getRequirements() {
		return this.requirements;
	}

	/**
	 * Get the technologies that can be researched if this technology is researched.
	 */
	public Technology[] getFollowups() {
		return this.followUps;
	}

	/**
	 * Adds a follow up technology to the list of follow ups from this technology.
	 *
	 * @param technology
	 *                       Follow up to be added to the list of follow ups.
	 */
	private void addFollowUp(final Technology technology) {
		final Technology[] newFollowUps = new Technology[this.followUps.length + 1];
		System.arraycopy(this.followUps, 0, newFollowUps, 0, this.followUps.length);
		newFollowUps[this.followUps.length] = technology;
		this.followUps = newFollowUps;
	}

	/**
	 * Get the knowledge field this technology belongs to.
	 */
	public KnowledgeField getKnowledgeField() {
		return this.knowledgeField;
	}

}
