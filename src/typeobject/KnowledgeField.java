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
import exe.io.Internationalized;

/**
 * This class represents a knowledge field.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class KnowledgeField extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all knowledge fields.
	 */
	private static final Map<String, KnowledgeField> ALL_KNOWLEDGE_FIELDS_MAP;
	/**
	 * Global list of all knowledge fields.
	 */
	private static final List<KnowledgeField> ALL_KNOWLEDGE_FIELDS_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this knowledge field.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this knowledge field.
	 */
	@Internationalized
	private String description;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Represents a set of levels of knowledge on every knowledge field.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 *
	 */
	public static class SkillSet {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		/**
		 * Maximum value skills can have.
		 */
		public static final byte SKILL_CAP = 100;

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Level of each knowledge field this skill set has.
		 */
		private final byte[] skills;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Creates a new skill set.
		 */
		public SkillSet() {
			this.skills = new byte[KnowledgeField.ALL_KNOWLEDGE_FIELDS_LIST.size()];
		}

		/**
		 * Creates a new skill set as a copy of a given skill set.
		 */
		public SkillSet(final SkillSet skillSet) {
			this();
			System.arraycopy(skillSet.skills, 0, this.skills, 0, this.skills.length);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the level of the given knowledge field this skill set has.
		 */
		public byte getSkill(final KnowledgeField knowledgeField) {
			return this.skills[knowledgeField.id];
		}

		/**
		 * Set the level of the given knowledge field this skill set has.
		 */
		public void setSkill(final KnowledgeField knowledgeField, final byte value) {
			this.skills[knowledgeField.id] = value;
		}

		/**
		 * Add to the level of the given knowledge field this skill set has.
		 */
		public void addSkill(final KnowledgeField knowledgeField, final byte value) {
			final int newValue = this.skills[knowledgeField.id] + value;
			if (newValue <= 0) {
				this.skills[knowledgeField.id] = 0;
			} else if (newValue >= SkillSet.SKILL_CAP) {
				this.skills[knowledgeField.id] = SkillSet.SKILL_CAP;
			} else {
				this.skills[knowledgeField.id] = (byte) newValue;
			}
		}

		/**
		 * Checks whether this skill set satisfies the given skill set.
		 */
		public boolean satisfies(final SkillSet skillSet) {
			for (int i = 0; i < this.skills.length; ++i) {
				if (this.skills[i] < skillSet.skills[i]) {
					return false;
				}
			}
			return true;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all occupations.
	 */
	static {
		ALL_KNOWLEDGE_FIELDS_MAP = new HashMap<>();
		ALL_KNOWLEDGE_FIELDS_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "knowledgeField");
		final Json data = Industry.DATA.get("typeObject", "knowledgeField");
		for (final String key : data.keys()) {
			final KnowledgeField knowledgeField = new KnowledgeField(key);
			knowledgeField.name = i18n.get(key, "name").as(String.class);
			knowledgeField.description = i18n.get(key, "description").as(String.class);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new knowledge field and adds it to the map of all knowledge fields
	 * under the given key and to the list of all knowledge fields.
	 *
	 * @param key
	 *                Key under which this knowledge field will be added to the map
	 *                of all knowledge fields.
	 */
	private KnowledgeField(final String key) {
		super(KnowledgeField.ALL_KNOWLEDGE_FIELDS_LIST.size());
		KnowledgeField.ALL_KNOWLEDGE_FIELDS_MAP.put(key, this);
		KnowledgeField.ALL_KNOWLEDGE_FIELDS_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the knowledge fields.
	 *
	 * @return A list of all the knowledge fields.
	 */
	public static List<KnowledgeField> getAllKnowledgeFields() {
		return KnowledgeField.ALL_KNOWLEDGE_FIELDS_LIST;
	}

	/**
	 * Gets the knowledge fields with the given keys.
	 *
	 * @param keys
	 *                 An array of knowledge field keys.
	 * @return The array of knowledge fields with the given keys.
	 */
	public static KnowledgeField[] getKnowledgeFields(final String[] keys) {
		final KnowledgeField[] knowledgeFields = new KnowledgeField[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			knowledgeFields[i] = KnowledgeField.getKnowledgeField(keys[i]);
		}
		return knowledgeFields;
	}

	/**
	 * Gets the knowledge field with the given key.
	 *
	 * @param key
	 *                A knowledge field key.
	 * @return The knowledge field with the given key.
	 */
	public static KnowledgeField getKnowledgeField(final String key) {
		return KnowledgeField.ALL_KNOWLEDGE_FIELDS_MAP.get(key);
	}

	/**
	 * Gets the knowledge fields with the given ids.
	 *
	 * @param ids
	 *                An array of knowledge field ids.
	 * @return The array of knowledge fields with the given ids.
	 */
	public static KnowledgeField[] getKnowledgeFields(final int[] ids) {
		final KnowledgeField[] knowledgeFields = new KnowledgeField[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			knowledgeFields[i] = KnowledgeField.getKnowledgeField(ids[i]);
		}
		return knowledgeFields;
	}

	/**
	 * Gets the knowledge field with the given id.
	 *
	 * @param id
	 *               A knowledge field id.
	 * @return The knowledge field with the given id.
	 */
	public static KnowledgeField getKnowledgeField(final int id) {
		return KnowledgeField.ALL_KNOWLEDGE_FIELDS_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this knowledge field.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this knowledge field.
	 */
	public String getDescription() {
		return this.description;
	}

}
