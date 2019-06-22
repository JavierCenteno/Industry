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
import java.util.Set;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import util.amount.Amount;

/**
 * This class represents a type of resource that exists as part of the soil.
 * Soils with a SoilType can't grow plants. SoilTypes can't appear in sloped
 * tiles.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class SoilType extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all soil types.
	 */
	private static final Map<String, SoilType> ALL_SOIL_TYPES_MAP;
	/**
	 * Global list of all soil types.
	 */
	private static final List<SoilType> ALL_SOIL_TYPES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this soil type.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this soil type.
	 */
	@Internationalized
	private String description;
	/**
	 * Resources produced when this soil type is exploited.
	 */
	@Externalized
	private Amount[] production;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all soil types.
	 */
	static {
		ALL_SOIL_TYPES_MAP = new HashMap<>();
		ALL_SOIL_TYPES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "soilType", "list");
		final Json data = Industry.DATA.get("typeObject", "soilType", "list");
		for (final String key : data.keys()) {
			final SoilType soilType = new SoilType(key);
			soilType.name = i18n.get(key, "name").as(String.class);
			soilType.description = i18n.get(key, "description").as(String.class);
			final Set<String> productionKeys = data.get(key, "production").keys();
			soilType.production = new Amount[productionKeys.size()];
			int index = 0;
			for (final String productionKey : productionKeys) {
				final int quantity = data.get(key, "production", productionKey).as(int.class);
				soilType.production[index] = new Amount(Resource.getResource(productionKey), quantity);
				++index;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new soil type and adds it to the map of all soil types under the
	 * given key and to the list of all soil types.
	 *
	 * @param key
	 *                Key under which this soil type will be added to the map of all
	 *                soil types.
	 */
	private SoilType(final String key) {
		super(SoilType.ALL_SOIL_TYPES_LIST.size());
		SoilType.ALL_SOIL_TYPES_MAP.put(key, this);
		SoilType.ALL_SOIL_TYPES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the soil types.
	 *
	 * @return A list of all the soil types.
	 */
	public static List<SoilType> getAllSoilTypes() {
		return SoilType.ALL_SOIL_TYPES_LIST;
	}

	/**
	 * Gets the soil types with the given keys.
	 *
	 * @param keys
	 *                 An array of soil type keys.
	 * @return The array of soil types with the given keys.
	 */
	public static SoilType[] getSoilTypes(final String[] keys) {
		final SoilType[] soilTypes = new SoilType[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			soilTypes[i] = SoilType.getSoilType(keys[i]);
		}
		return soilTypes;
	}

	/**
	 * Gets the soil type with the given key.
	 *
	 * @param key
	 *                A soil type key.
	 * @return The soil type with the given key.
	 */
	public static SoilType getSoilType(final String key) {
		return SoilType.ALL_SOIL_TYPES_MAP.get(key);
	}

	/**
	 * Gets the soil types with the given ids.
	 *
	 * @param ids
	 *                An array of soil type ids.
	 * @return The array of soil types with the given ids.
	 */
	public static SoilType[] getSoilTypes(final int[] ids) {
		final SoilType[] soilTypes = new SoilType[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			soilTypes[i] = SoilType.getSoilType(ids[i]);
		}
		return soilTypes;
	}

	/**
	 * Gets the soil type with the given id.
	 *
	 * @param id
	 *               An soil type id.
	 * @return The soil type with the given id.
	 */
	public static SoilType getSoilType(final int id) {
		return SoilType.ALL_SOIL_TYPES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this soil type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this soil type.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the resources produced when this animal type is exploited.
	 */
	public Amount[] getProduction() {
		return this.production;
	}

}
