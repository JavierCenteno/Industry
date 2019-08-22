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
 * This class represents a type of animal.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class AnimalType extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all animal types.
	 */
	private static final Map<String, AnimalType> ALL_ANIMAL_TYPES_MAP;
	/**
	 * Global list of all animal types.
	 */
	private static final List<AnimalType> ALL_ANIMAL_TYPES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this animal type.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this animal type.
	 */
	@Internationalized
	private String description;
	/**
	 * Amounts of resources this animal type eats, which affects where this animal
	 * type can spawn and production rate for a given amount of food.
	 */
	@Externalized
	private Amount[] food;
	/**
	 * Amounts of resources this animal type produces when it is exploited.
	 */
	@Externalized
	private Amount[] production;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all animal types.
	 */
	static {
		ALL_ANIMAL_TYPES_MAP = new HashMap<>();
		ALL_ANIMAL_TYPES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("type", "animalType", "list");
		final Json data = Industry.DATA.get("type", "animalType", "list");
		for (final String key : data.keys()) {
			final AnimalType animalType = new AnimalType(key);
			animalType.name = i18n.get(key, "name").as(String.class);
			animalType.description = i18n.get(key, "description").as(String.class);
			final Set<String> foodKeys = data.get(key, "food").keys();
			animalType.food = new Amount[foodKeys.size()];
			int index;
			index = 0;
			for (final String foodKey : foodKeys) {
				final int quantity = data.get(key, "food", foodKey).as(int.class);
				animalType.food[index] = new Amount(Resource.getResource(foodKey), quantity);
				++index;
			}
			final Set<String> productionKeys = data.get(key, "production").keys();
			animalType.production = new Amount[productionKeys.size()];
			index = 0;
			for (final String productionKey : productionKeys) {
				final int quantity = data.get(key, "production", productionKey).as(int.class);
				animalType.production[index] = new Amount(Resource.getResource(productionKey), quantity);
				++index;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new animal type and adds it to the map of all animal types under
	 * the given key and to the list of all animal types.
	 *
	 * @param key
	 *                Key under which this animal type will be added to the map of
	 *                all animal types.
	 */
	private AnimalType(final String key) {
		super(AnimalType.ALL_ANIMAL_TYPES_LIST.size());
		AnimalType.ALL_ANIMAL_TYPES_MAP.put(key, this);
		AnimalType.ALL_ANIMAL_TYPES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the animal types.
	 *
	 * @return A list of all the animal types.
	 */
	public static List<AnimalType> getAllAnimalTypes() {
		return AnimalType.ALL_ANIMAL_TYPES_LIST;
	}

	/**
	 * Gets the animal types that produce a given resource.
	 *
	 * @param resource
	 *                     A resource.
	 * @return A list of animal types that produce a given resource.
	 */
	public static List<AnimalType> getAnimalTypesProducing(final Resource resource) {
		final List<AnimalType> animalTypesProducingResource = new ArrayList<>();
		for (final AnimalType animalType : AnimalType.ALL_ANIMAL_TYPES_LIST) {
			for (final Amount animalTypeAmount : animalType.production) {
				if (resource.equals(animalTypeAmount.getResource())) {
					animalTypesProducingResource.add(animalType);
				}
			}
		}
		return animalTypesProducingResource;
	}

	/**
	 * Gets the animal types with the given keys.
	 *
	 * @param keys
	 *                 An array of animal type keys.
	 * @return The array of animal types with the given keys.
	 */
	public static AnimalType[] getAnimalTypes(final String[] keys) {
		final AnimalType[] animalTypes = new AnimalType[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			animalTypes[i] = AnimalType.getAnimalType(keys[i]);
		}
		return animalTypes;
	}

	/**
	 * Gets the animal type with the given key.
	 *
	 * @param key
	 *                A animal type key.
	 * @return The animal type with the given key.
	 */
	public static AnimalType getAnimalType(final String key) {
		return AnimalType.ALL_ANIMAL_TYPES_MAP.get(key);
	}

	/**
	 * Gets the animal types with the given ids.
	 *
	 * @param ids
	 *                An array of animal type ids.
	 * @return The array of animal types with the given ids.
	 */
	public static AnimalType[] getAnimalTypes(final int[] ids) {
		final AnimalType[] animalTypes = new AnimalType[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			animalTypes[i] = AnimalType.getAnimalType(ids[i]);
		}
		return animalTypes;
	}

	/**
	 * Gets the animal type with the given id.
	 *
	 * @param id
	 *               An animal type id.
	 * @return The animal type with the given id.
	 */
	public static AnimalType getAnimalType(final int id) {
		return AnimalType.ALL_ANIMAL_TYPES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this animal type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this animal type.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the amounts of resources this animal type eats, which affects where this
	 * animal type can spawn and production rate for a given amount of food.
	 */
	public Amount[] getFood() {
		return this.food;
	}

	/**
	 * Get the amounts of resources this animal type produces when it is exploited.
	 */
	public Amount[] getProduction() {
		return this.production;
	}

}
