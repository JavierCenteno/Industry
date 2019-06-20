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
 * This class represents the different shapes a terrain may have.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class TerrainShape extends TypeObject {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Global map of all terrain shapes.
	 */
	private static final Map<String, TerrainShape> ALL_TERRAIN_SHAPES_MAP;
	/**
	 * Global list of all terrain shapes.
	 */
	private static final List<TerrainShape> ALL_TERRAIN_SHAPES_LIST;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this terrain shape.
	 */
	@Internationalized
	private String name;
	/**
	 * Description of this terrain shape.
	 */
	@Internationalized
	private String description;
	/**
	 * Whether the edges of a terrain with this shape can be crossed in a particular
	 * dimension.
	 */
	@Externalized
	private boolean cycleX, cycleY;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Loads all terrain shapes.
	 */
	static {
		ALL_TERRAIN_SHAPES_MAP = new HashMap<>();
		ALL_TERRAIN_SHAPES_LIST = new ArrayList<>();
		final Json i18n = Industry.I18N.get("typeObject", "terrainShape");
		final Json data = Industry.DATA.get("typeObject", "terrainShape");
		for (final String key : data.keys()) {
			final TerrainShape terrainShape = new TerrainShape(key);
			terrainShape.name = i18n.get(key, "name").as(String.class);
			terrainShape.description = i18n.get(key, "description").as(String.class);
			terrainShape.cycleX = data.get(key, "cycleX").as(boolean.class);
			terrainShape.cycleY = data.get(key, "cycleY").as(boolean.class);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new terrain shape and adds it to the map of all terrain shapes
	 * under the given key and to the list of all terrain shapes.
	 *
	 * @param key
	 *                Key under which this terrain shape will be added to the map of
	 *                all terrain shapes.
	 */
	private TerrainShape(final String key) {
		super(TerrainShape.ALL_TERRAIN_SHAPES_LIST.size());
		TerrainShape.ALL_TERRAIN_SHAPES_MAP.put(key, this);
		TerrainShape.ALL_TERRAIN_SHAPES_LIST.add(this);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 * Gets all the terrain shapes.
	 *
	 * @return A list of all the terrain shapes.
	 */
	public static List<TerrainShape> getAllTerrainShapes() {
		return TerrainShape.ALL_TERRAIN_SHAPES_LIST;
	}

	/**
	 * Gets the terrain shapes with the given keys.
	 *
	 * @param keys
	 *                 An array of terrain shape keys.
	 * @return The array of terrain shapes with the given keys.
	 */
	public static TerrainShape[] getTerrainShapes(final String[] keys) {
		final TerrainShape[] terrainShapes = new TerrainShape[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			terrainShapes[i] = TerrainShape.getTerrainShape(keys[i]);
		}
		return terrainShapes;
	}

	/**
	 * Gets the terrain shape with the given key.
	 *
	 * @param key
	 *                A terrain shape key.
	 * @return The terrain shape with the given key.
	 */
	public static TerrainShape getTerrainShape(final String key) {
		return TerrainShape.ALL_TERRAIN_SHAPES_MAP.get(key);
	}

	/**
	 * Gets the terrain shapes with the given ids.
	 *
	 * @param ids
	 *                An array of terrain shape ids.
	 * @return The array of terrain shapes with the given ids.
	 */
	public static TerrainShape[] getTerrainShapes(final int[] ids) {
		final TerrainShape[] terrainShapes = new TerrainShape[ids.length];
		for (int i = 0; i < ids.length; ++i) {
			terrainShapes[i] = TerrainShape.getTerrainShape(ids[i]);
		}
		return terrainShapes;
	}

	/**
	 * Gets the terrain shape with the given id.
	 *
	 * @param id
	 *               A terrain shape id.
	 * @return The terrain shape with the given id.
	 */
	public static TerrainShape getTerrainShape(final int id) {
		return TerrainShape.ALL_TERRAIN_SHAPES_LIST.get(id);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the name of this terrain shape.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the description of this terrain shape.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get whether the edges of a terrain with this shape can be crossed in the x
	 * dimension.
	 */
	public boolean getCycleX() {
		return this.cycleX;
	}

	/**
	 * Get whether the edges of a terrain with this shape can be crossed in the y
	 * dimension.
	 */
	public boolean getCycleY() {
		return this.cycleY;
	}

}
