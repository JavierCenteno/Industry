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

package type;

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
 * This class represents a type of resource that exists as a mineral vein.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class MineralType extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all mineral types.
	 */
	private static final Map<String, MineralType> ALL_MINERAL_TYPES_MAP;
	/**
	 * Global list of all mineral types.
	 */
	private static final List<MineralType> ALL_MINERAL_TYPES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this mineral type.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this mineral type.
	 */
	@Internationalized
	private String description;
	/**
	 * Resources produced when this mineral type is exploited.
	 */
	@Externalized
	private Amount[] production;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all mineral types.
	 */
	static {
		ALL_MINERAL_TYPES_MAP = new HashMap<>();
		ALL_MINERAL_TYPES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("type", "mineralType", "list");
		final Json data = Industry.DATA.get("type", "mineralType", "list");
		for (final String key : data.keys()) {
			final MineralType mineralType = new MineralType(key);
			mineralType.name = i18n.get(key, "name").as(String.class);
			mineralType.description = i18n.get(key, "description").as(String.class);
			final Set<String> productionKeys = data.get(key, "production").keys();
			mineralType.production = new Amount[productionKeys.size()];
			int index = 0;
			for (final String productionKey : productionKeys) {
				final int quantity = data.get(key, "production", productionKey).as(int.class);
				mineralType.production[index] = new Amount(Resource.getResource(productionKey), quantity);
				++index;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new mineral type and adds it to the map of all mineral types under
	 * the given key and to the list of all mineral types.
	 *
	 * @param key
	 *                Key under which this mineral type will be added to the map of
	 *                all mineral types.
	 */
	private MineralType(final String key) {
		super(MineralType.ALL_MINERAL_TYPES_LIST.size());
		MineralType.ALL_MINERAL_TYPES_MAP.put(key, this);
		MineralType.ALL_MINERAL_TYPES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the mineral types.
	 *
	 * @return A list of all the mineral types.
	 */
	public static List<MineralType> getAllMineralTypes() {
		return MineralType.ALL_MINERAL_TYPES_LIST;
	}

	/**
	 * Gets the mineral types with the given keys.
	 *
	 * @param keys
	 *                 An array of mineral type keys.
	 * @return The array of mineral types with the given keys.
	 */
	public static MineralType[] getMineralTypes(final String[] keys) {
		final MineralType[] mineralTypes = new MineralType[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			mineralTypes[i] = MineralType.getMineralType(keys[i]);
		}
		return mineralTypes;
	}

	/**
	 * Gets the mineral type with the given key.
	 *
	 * @param key
	 *                A mineral type key.
	 * @return The mineral type with the given key.
	 */
	public static MineralType getMineralType(final String key) {
		return MineralType.ALL_MINERAL_TYPES_MAP.get(key);
	}

	/**
	 * Gets the mineral types with the given ids.
	 *
	 * @param ids
	 *                An array of mineral type ids.
	 * @return The array of mineral types with the given ids.
	 */
	public static MineralType[] getMineralTypes(final int[] ids) {
		final MineralType[] mineralTypes = new MineralType[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			mineralTypes[i] = MineralType.getMineralType(ids[i]);
		}
		return mineralTypes;
	}

	/**
	 * Gets the mineral type with the given id.
	 *
	 * @param id
	 *               An mineral type id.
	 * @return The mineral type with the given id.
	 */
	public static MineralType getMineralType(final int id) {
		return MineralType.ALL_MINERAL_TYPES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this mineral type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this mineral type.
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
