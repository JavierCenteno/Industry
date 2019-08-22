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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import api.Json;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;

/**
 * This class represents a resource that can be generated as part of a world or
 * produced by its citizens.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Resource extends Type {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all resources.
	 */
	private static final Map<String, Resource> ALL_RESOURCES_MAP;
	/**
	 * Global list of all resources.
	 */
	private static final List<Resource> ALL_RESOURCES_LIST;
	/**
	 * Global map of all attributes.
	 */
	private static final Map<String, ResourceAttribute> ALL_ATTRIBUTES_MAP;
	/**
	 * Global list of all attributes.
	 */
	private static final List<ResourceAttribute> ALL_ATTRIBUTES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this resource.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this resource.
	 */
	@Internationalized
	private String description;
	/**
	 * Technology required to detect this resource.
	 */
	@Externalized
	private Technology requiredTechnology;
	/**
	 * Attributes this resource has.
	 */
	@Externalized
	private final Set<ResourceAttribute> resourceAttributes;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	public static class ResourceAttribute extends Type {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Name of this resource attribute.
		 */
		@Internationalized
		private String name;
		/**
		 * Description of this resource attribute.
		 */
		@Internationalized
		private String description;
		/**
		 * Set of resources that have this resource attribute.
		 */
		private final Set<Resource> resources;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Creates a new resource attribute and adds it to the map of all resource
		 * attributes under the given key and to the list of all resource attributes.
		 *
		 * @param key
		 *                Key under which this resource attribute will be added to the
		 *                map of all resource attributes.
		 */
		private ResourceAttribute(final String key) {
			super(Resource.ALL_ATTRIBUTES_LIST.size());
			Resource.ALL_ATTRIBUTES_MAP.put(key, this);
			Resource.ALL_ATTRIBUTES_LIST.add(this);
			this.resources = new HashSet<Resource>();
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the name of this resource attribute.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Get the description of this resource attribute.
		 */
		public String getDescription() {
			return this.description;
		}

		/**
		 * Get the set of all resources with this resource attribute.
		 */
		public Set<Resource> getResources() {
			return this.resources;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all resources and resource attributes.
	 */
	static {
		// Load all resource attributes
		ALL_ATTRIBUTES_MAP = new HashMap<>();
		ALL_ATTRIBUTES_LIST = new ArrayList<>();
		final Json resourceAttributeI18n = Industry.I18N.get("type", "resource", "attribute", "list");
		final Json resourceAttributeData = Industry.DATA.get("type", "resource", "attribute", "list");
		for (final String key : resourceAttributeData.keys()) {
			final ResourceAttribute resourceAttribute = new ResourceAttribute(key);
			resourceAttribute.name = resourceAttributeI18n.get(key, "name").as(String.class);
			resourceAttribute.description = resourceAttributeI18n.get(key, "description").as(String.class);
		}
		// Load all resources
		ALL_RESOURCES_MAP = new HashMap<>();
		ALL_RESOURCES_LIST = new ArrayList<>();
		final Json resourceI18n = Industry.I18N.get("type", "resource", "list");
		final Json resourceData = Industry.DATA.get("type", "resource", "list");
		for (final String resourceKey : resourceData.keys()) {
			final Resource resource = new Resource(resourceKey);
			resource.name = resourceI18n.get(resourceKey, "name").as(String.class);
			resource.description = resourceI18n.get(resourceKey, "description").as(String.class);
			resource.requiredTechnology = Technology
					.getTechnology(resourceData.get(resourceKey, "requiredTechnology").as(String.class));
			final ResourceAttribute[] attributes = Resource
					.getResourceAttributes(resourceData.get(resourceKey, "attributes").as(String[].class));
			for (final ResourceAttribute attribute : attributes) {
				resource.resourceAttributes.add(attribute);
				attribute.resources.add(resource);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new resource and adds it to the map of all resources under the
	 * given key and to the list of all resources.
	 *
	 * @param key
	 *                Key under which this resource will be added to the map of all
	 *                resources.
	 */
	private Resource(final String key) {
		super(Resource.ALL_RESOURCES_LIST.size());
		Resource.ALL_RESOURCES_MAP.put(key, this);
		Resource.ALL_RESOURCES_LIST.add(this);
		this.resourceAttributes = new HashSet<>();
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the resources.
	 *
	 * @return A list of all the resources.
	 */
	public static List<Resource> getAllResources() {
		return Resource.ALL_RESOURCES_LIST;
	}

	/**
	 * Gets the resources with the given keys.
	 *
	 * @param keys
	 *                 An array of resource keys.
	 * @return The array of resources with the given keys.
	 */
	public static Resource[] getResources(final String[] keys) {
		final Resource[] resources = new Resource[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			resources[i] = Resource.getResource(keys[i]);
		}
		return resources;
	}

	/**
	 * Gets the resource with the given key.
	 *
	 * @param key
	 *                A resource key.
	 * @return The resource with the given key.
	 */
	public static Resource getResource(final String key) {
		return Resource.ALL_RESOURCES_MAP.get(key);
	}

	/**
	 * Gets the resources with the given ids.
	 *
	 * @param ids
	 *                An array of resource ids.
	 * @return The array of resources with the given ids.
	 */
	public static Resource[] getResources(final int[] ids) {
		final Resource[] resources = new Resource[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			resources[i] = Resource.getResource(ids[i]);
		}
		return resources;
	}

	/**
	 * Gets the resource with the given id.
	 *
	 * @param id
	 *               A resource id.
	 * @return The resource with the given id.
	 */
	public static Resource getResource(final int id) {
		return Resource.ALL_RESOURCES_LIST.get(id);
	}

	/**
	 * Gets all the resource attributes.
	 *
	 * @return A list of all the resource attributes.
	 */
	public static List<ResourceAttribute> getAllAttributes() {
		return Resource.ALL_ATTRIBUTES_LIST;
	}

	/**
	 * Gets the resource attributes with the given keys.
	 *
	 * @param keys
	 *                 An array of resource attribute keys.
	 * @return The array of resource attributes with the given keys.
	 */
	public static ResourceAttribute[] getResourceAttributes(final String[] keys) {
		final ResourceAttribute[] resourceAttributes = new ResourceAttribute[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			resourceAttributes[i] = Resource.getResourceAttribute(keys[i]);
		}
		return resourceAttributes;
	}

	/**
	 * Gets the resource attribute with the given key.
	 *
	 * @param key
	 *                A resource attribute key.
	 * @return The resource attribute with the given key.
	 */
	public static ResourceAttribute getResourceAttribute(final String key) {
		return Resource.ALL_ATTRIBUTES_MAP.get(key);
	}

	/**
	 * Gets the resource attributes with the given ids.
	 *
	 * @param ids
	 *                An array of resource attribute ids.
	 * @return The array of resource attributes with the given ids.
	 */
	public static ResourceAttribute[] getResourceAttributes(final int[] ids) {
		final ResourceAttribute[] resourceAttributes = new ResourceAttribute[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			resourceAttributes[i] = Resource.getResourceAttribute(ids[i]);
		}
		return resourceAttributes;
	}

	/**
	 * Gets the resource attribute with the given id.
	 *
	 * @param id
	 *               A resource attribute id.
	 * @return The resource attribute with the given id.
	 */
	public static ResourceAttribute getResourceAttribute(final int id) {
		return Resource.ALL_ATTRIBUTES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this resource.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this resource.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the list of attributes of this resource.
	 */
	public Set<ResourceAttribute> getAttributes() {
		return this.resourceAttributes;
	}

	/**
	 * Checks if the resource has a specific resource attribute.
	 *
	 * @param attribute
	 *                      An attribute.
	 */
	public boolean is(final ResourceAttribute resourceAttribute) {
		return this.getAttributes().contains(resourceAttribute);
	}

}
