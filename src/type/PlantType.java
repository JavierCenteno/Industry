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
import entity.world.TectonicPlate.Tile;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import util.amount.Amount;

/**
 * This class represents a type of plant.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class PlantType extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all plant types.
	 */
	private static final Map<String, PlantType> ALL_PLANT_TYPES_MAP;
	/**
	 * Global list of all plant types.
	 */
	private static final List<PlantType> ALL_PLANT_TYPES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this plant type.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this plant type.
	 */
	@Internationalized
	private String description;
	/**
	 * The minimum temperature this plant type can live on, also called frost
	 * resistance.
	 */
	@Externalized
	private int minimumTemperature;
	/**
	 * The maximum temperature this plant type can live on, also called heat
	 * resistance.
	 */
	@Externalized
	private int maximumTemperature;
	/**
	 * The minimum humidity this plant type can live on, also called drought
	 * resistance.
	 */
	@Externalized
	private int minimumHumidity;
	/**
	 * The maximum humidity this plant type can live on, also called flood
	 * resistance.
	 */
	@Externalized
	private int maximumHumidity;
	/**
	 * The amount by which this plant varies local pollution.
	 */
	@Externalized
	private int pollution;
	/**
	 * Amounts of resources produced when this plant type is exploited.
	 */
	@Externalized
	private Amount[] production;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all plant types.
	 */
	static {
		ALL_PLANT_TYPES_MAP = new HashMap<>();
		ALL_PLANT_TYPES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("type", "plantType", "list");
		final Json data = Industry.DATA.get("type", "plantType", "list");
		for (final String key : data.keys()) {
			final PlantType plantType = new PlantType(key);
			plantType.name = i18n.get(key, "name").as(String.class);
			plantType.description = i18n.get(key, "description").as(String.class);
			plantType.minimumTemperature = data.get(key, "minimumTemperature").as(int.class);
			plantType.maximumTemperature = data.get(key, "maximumTemperature").as(int.class);
			plantType.minimumHumidity = data.get(key, "minimumHumidity").as(int.class);
			plantType.maximumHumidity = data.get(key, "maximumHumidity").as(int.class);
			plantType.pollution = data.get(key, "pollution").as(int.class);
			final Set<String> productionKeys = data.get(key, "production").keys();
			plantType.production = new Amount[productionKeys.size()];
			int index = 0;
			for (final String productionKey : productionKeys) {
				final int quantity = data.get(key, "production", productionKey).as(int.class);
				plantType.production[index] = new Amount(Resource.getResource(productionKey), quantity);
				++index;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new plant type and adds it to the map of all plant types under the
	 * given key and to the list of all plant types.
	 *
	 * @param key
	 *                Key under which this plant type will be added to the map of
	 *                all plant types.
	 */
	private PlantType(final String key) {
		super(PlantType.ALL_PLANT_TYPES_LIST.size());
		PlantType.ALL_PLANT_TYPES_MAP.put(key, this);
		PlantType.ALL_PLANT_TYPES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the plant types.
	 *
	 * @return A list of all the plant types.
	 */
	public static List<PlantType> getAllPlantTypes() {
		return PlantType.ALL_PLANT_TYPES_LIST;
	}

	/**
	 * Gets the plant types that produce a given resource.
	 *
	 * @param resource
	 *                     A resource.
	 * @return A list of plant types that produce a given resource.
	 */
	public static List<PlantType> getPlantTypesProducing(final Resource resource) {
		final List<PlantType> plantTypesProducingResource = new ArrayList<>();
		for (final PlantType plantType : PlantType.ALL_PLANT_TYPES_LIST) {
			for (final Amount plantTypeAmount : plantType.production) {
				if (resource.equals(plantTypeAmount.getResource())) {
					plantTypesProducingResource.add(plantType);
				}
			}
		}
		return plantTypesProducingResource;
	}

	/**
	 * Check if a given plant type can survive in a given climate.
	 *
	 * @param plantType
	 *                      A plant type.
	 * @param tile
	 *                      A tile.
	 * @return Whether the given plant type can survive in the climate of the given
	 *         tile.
	 */
	private static boolean isFitForClimate(final PlantType plantType, final Tile tile) {
		final int[] temperatures = tile.getMonthlyTemperatures();
		int minimumTemperature = Integer.MAX_VALUE;
		int maximumTemperature = Integer.MIN_VALUE;
		for (int i = 0; i < temperatures.length; ++i) {
			final int temperature = temperatures[i];
			if (temperature < minimumTemperature) {
				minimumTemperature = temperature;
			}
			if (temperature > maximumTemperature) {
				maximumTemperature = temperature;
			}
		}
		final int humidity = tile.getHumidity();
		return (plantType.minimumTemperature <= minimumTemperature)
				&& (maximumTemperature <= plantType.maximumTemperature) && (plantType.minimumHumidity <= humidity)
				&& (humidity <= plantType.maximumHumidity);
	}

	/**
	 * Get the plant types that can be grown in a given tile.
	 *
	 * @param tile
	 *                 A tile.
	 * @return The plant types that can be grown with the given tile.
	 */
	public static List<PlantType> getPlantTypesForClimate(final Tile tile) {
		final List<PlantType> plantTypesForClimate = new ArrayList<>();
		for (final PlantType plantType : PlantType.ALL_PLANT_TYPES_LIST) {
			if (PlantType.isFitForClimate(plantType, tile)) {
				plantTypesForClimate.add(plantType);
			}
		}
		return plantTypesForClimate;
	}

	/**
	 * Gets the plant types with the given keys.
	 *
	 * @param keys
	 *                 An array of plant type keys.
	 * @return The array of plant types with the given keys.
	 */
	public static PlantType[] getPlantTypes(final String[] keys) {
		final PlantType[] plantTypes = new PlantType[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			plantTypes[i] = PlantType.getPlantType(keys[i]);
		}
		return plantTypes;
	}

	/**
	 * Gets the plant type with the given key.
	 *
	 * @param key
	 *                A plant type key.
	 * @return The plant type with the given key.
	 */
	public static PlantType getPlantType(final String key) {
		return PlantType.ALL_PLANT_TYPES_MAP.get(key);
	}

	/**
	 * Gets the plant types with the given ids.
	 *
	 * @param ids
	 *                An array of plant type ids.
	 * @return The array of plant types with the given ids.
	 */
	public static PlantType[] getPlantTypes(final int[] ids) {
		final PlantType[] plantTypes = new PlantType[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			plantTypes[i] = PlantType.getPlantType(ids[i]);
		}
		return plantTypes;
	}

	/**
	 * Gets the plant type with the given id.
	 *
	 * @param id
	 *               An plant type id.
	 * @return The plant type with the given id.
	 */
	public static PlantType getPlantType(final int id) {
		return PlantType.ALL_PLANT_TYPES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this plant type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this plant type.
	 */
	public String getDescription() {
		return this.description;
	}

	public int getMinimumTemperature() {
		return this.minimumTemperature;
	}

	public int getMaximumTemperature() {
		return this.maximumTemperature;
	}

	public int getMinimumHumidity() {
		return this.minimumHumidity;
	}

	public int getMaximumHumidity() {
		return this.maximumHumidity;
	}

	public int getPollution() {
		return this.pollution;
	}

	/**
	 * Get the amounts of resources produced when this plant type is exploited.
	 */
	public Amount[] getProduction() {
		return this.production;
	}

}
