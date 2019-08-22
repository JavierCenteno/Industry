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

package entity.element.knot;

import java.util.List;
import java.util.Set;

import api.Json;
import entity.city.City;
import entity.element.building.Building;
import entity.world.Terrain;
import entity.world.World;
import exe.Industry;
import exe.io.Externalized;
import exe.io.Internationalized;
import type.Resource;
import type.Resource.ResourceAttribute;
import type.Technology;
import util.amount.Amount;

/**
 * This class abstracts the idea of a knot that can establish connections to
 * other knots, forming a network which can be traversed.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Pipe extends Knot {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of the knots of this class.
	 */
	@Internationalized
	private static String NAME;
	/**
	 * Description of the knots of this class.
	 */
	@Internationalized
	private static String DESCRIPTION;
	/**
	 * Maximum slope the connections between knots of this class can have.
	 */
	@Externalized
	private static int MAX_SLOPE;
	/**
	 * Maximum deviation of height connections between knots of this class can have.
	 */
	@Externalized
	private static int MAX_ROUGHNESS;
	/**
	 * Technology required to build knots of this class.
	 */
	@Externalized
	private static Technology REQUIRED_TECHNOLOGY;
	/**
	 * Cost of building a tile long connection of this class.
	 */
	@Externalized
	private static Amount[] COST;
	/**
	 * Liquid resource attribute.
	 */
	@Externalized
	private static ResourceAttribute LIQUID = Resource.getResourceAttribute("liquid");

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of pipes.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see KnotFactory
	 */
	public static class PipeFactory extends KnotFactory<Pipe> {

		////////////////////////////////////////////////////////////////////////////////
		// Class initializer

		static {
			final PipeFactory pipeFactory = new PipeFactory();
			Knot.FACTORIES.add(pipeFactory);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public Technology getRequiredTechnology() {
			return Pipe.REQUIRED_TECHNOLOGY;
		}

		@Override
		public Amount[] getCost() {
			return Pipe.COST;
		}

		@Override
		public boolean check(final Terrain terrain, final int fromX, final int fromY, final int toX, final int toY,
				final City city) {
			// TODO: check if no tile in the edges of the fromX, fromY, toX, toY rectangle
			// is occupied, underwater or under magma, and that the edges don't exceed max
			// slope
			return true;
		}

		@Override
		public void make(final Terrain terrain, final int fromX, final int fromY, final int toX, final int toY,
				final City city) {
			// TODO: create knots in the corners of the [from, to] rectangle if there aren't
			// TODO: connect the knots with connections if there aren't
		}

	}

	/**
	 * Connection of pipes.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see Connection
	 */
	public static class PipeConnection extends Connection {

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public PipeConnection(final World world, final int coordinateX, final int coordinateY, final int sizeX,
				final int sizeY, final int health, final City city) {
			super(world, coordinateX, coordinateY, sizeX, sizeY, health, city);
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		@Override
		public String getName() {
			return Pipe.NAME;
		}

		@Override
		public String getDescription() {
			return Pipe.DESCRIPTION;
		}

		@Override
		public void heal(final int health) {
			// TODO Auto-generated method stub
		}

	}

	/**
	 * Connection of pipes.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @see Network
	 */
	public static class PipeNetwork extends Network {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Resource that passes through this network.
		 */
		private Resource resource;
		/**
		 * Buildings that supply this network with the resource that they transport.
		 */
		private List<Building> suppliers;

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Get the resource that passes through this network.
		 *
		 * @return The resource that passes through this network.
		 */
		public Resource getResource() {
			return this.resource;
		}

		/**
		 * Sets the resource that passes through this network.
		 *
		 * @param resource
		 *                     A resource.
		 */
		public void setResource(final Resource resource) {
			if (resource.is(Pipe.LIQUID)) {
				this.resource = resource;
			} else {
				// TODO: error: resource must be liquid
			}
		}

		/**
		 * Get the list of buildings that supply this network with resources.
		 */
		public List<Building> getSuppliers() {
			return this.suppliers;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// i18n
		final Json i18n = Industry.I18N.get("entity", "element", "knot", "list", "pipe");
		Pipe.NAME = i18n.get("name").as(String.class);
		Pipe.DESCRIPTION = i18n.get("description").as(String.class);
		// data
		final Json data = Industry.DATA.get("entity", "element", "knot", "list", "pipe");
		Pipe.MAX_SLOPE = data.get("maxSlope").as(int.class);
		Pipe.REQUIRED_TECHNOLOGY = Technology.getTechnology(data.get("requiredTechnology").as(String.class));
		final Set<String> costKeys = data.get("cost").keys();
		Pipe.COST = new Amount[costKeys.size()];
		int costIndex = 0;
		for (final String key : costKeys) {
			final int quantity = data.get("cost", key).as(int.class);
			Pipe.COST[costIndex] = new Amount(Resource.getResource(key), quantity);
			++costIndex;
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Pipe(final World world, final int coordinateX, final int coordinateY, final int health, final City city) {
		super(world, coordinateX, coordinateY, health, city);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public String getName() {
		return Pipe.NAME;
	}

	@Override
	public String getDescription() {
		return Pipe.DESCRIPTION;
	}

	@Override
	public void heal(final int health) {
		// TODO
	}

}
