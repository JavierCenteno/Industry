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

import java.util.ArrayList;
import java.util.List;

import entity.city.City;
import entity.element.Element;
import entity.world.Terrain;
import entity.world.World;
import entity.world.Terrain.Orientation;

/**
 * This class abstracts the idea of a knot that can establish connections to
 * other knots, forming a network which can be traversed and used to transport
 * resources and units.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Knot extends Element {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * List of factories for all types of knot there are.
	 */
	public static final List<KnotFactory<?>> FACTORIES = new ArrayList<KnotFactory<?>>();

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Knots that connect to this knot.
	 *
	 * @see Knot#getConnections
	 */
	private final Knot[] connections;
	/**
	 * Network this knot belongs to.
	 */
	private Network network;

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Factory of knots that returns instances of a class that extends knot.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @param <K>
	 *            Class of the knots this factory returns.
	 */
	public static abstract class KnotFactory<K extends Knot> extends ElementFactory<K> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Checks whether connections can be built with these parameters.
		 *
		 * @param terrain
		 *                    Terrain where we're building the connections.
		 * @param fromX
		 *                    Coordinate of the starting tile along the x axis.
		 * @param fromY
		 *                    Coordinate of the starting tile along the y axis.
		 * @param toX
		 *                    Coordinate of the ending tile along the x axis.
		 * @param toY
		 *                    Coordinate of the ending tile along the y axis.
		 * @param city
		 *                    City the connections belong to.
		 * @return Whether connections can be built with these parameters.
		 */
		public abstract boolean check(Terrain terrain, int fromX, int fromY, int toX, int toY, City city);

		/**
		 * Creates connections in the rectangle determined by the given tile coordinates
		 * with knots in the corners and connections through the edges.
		 *
		 * @param terrain
		 *                    Terrain where we're building the connections.
		 * @param fromX
		 *                    Coordinate of the starting tile along the x axis.
		 * @param fromY
		 *                    Coordinate of the starting tile along the y axis.
		 * @param toX
		 *                    Coordinate of the ending tile along the x axis.
		 * @param toY
		 *                    Coordinate of the ending tile along the y axis.
		 * @param city
		 *                    City the connections belong to.
		 */
		public abstract void make(Terrain terrain, int fromX, int fromY, int toX, int toY, City city);

	}

	/**
	 * This class represents a connection between two knots.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static abstract class Connection extends Element {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Size of the connection in each dimension.
		 */
		private final int sizeX, sizeY;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		public Connection(final World world, final int coordinateX, final int coordinateY, final int sizeX,
				final int sizeY, final int health, final City city) {
			super(world, coordinateX, coordinateY, health, city);
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Gets this connection's depth.
		 *
		 * @return This connection's width.
		 */
		public int getSizeX() {
			return this.sizeX;
		}

		/**
		 * Gets this connection's depth.
		 *
		 * @return This connection's width.
		 */
		public int getSizeY() {
			return this.sizeY;
		}

	}

	/**
	 * Network that groups all knots with paths connecting them together and ensures
	 * that there are no knots connected to the knots of this network that aren't
	 * considered part of this network and that there are are no knots disconnected
	 * from the knots of this network that are considered part of this network. Used
	 * for easily checking whether any two knots are connected.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public static class Network {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * The list of all knots in this network.
		 */
		private ArrayList<Knot> knots;

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		/**
		 * Gets the list of all knots in this network.
		 *
		 * @return The list of all knots in this network.
		 */
		public List<Knot> getKnots() {
			return this.knots;
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Knot(final World world, final int coordinateX, final int coordinateY, final int health, final City city) {
		super(world, coordinateX, coordinateY, health, city);
		this.connections = new Knot[4];
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the knots that connect to this knot.
	 *
	 * @return An array of knots of length 4 determining which knot this knot
	 *         connects with in each direction (or null if there are no connections
	 *         in that direction). Each position of the array corresponds with plus
	 *         x, plus y, minus x and minus y, respectively.
	 */
	public Knot[] getConnections() {
		return this.connections;
	}

	/**
	 * Get the knot that connects to this knot at the given orientation.
	 *
	 * @param orientation
	 *                        An orientation.
	 * @return The knot that connects to this knot at the given orientation.
	 */
	public Knot getKnotAt(final Orientation orientation) {
		return this.getConnections()[orientation.getRotation()];
	}

	/**
	 * Creates a connection between this knot and a specified knot. Doesn't do
	 * anything if it's impossible to create a connection (attempting to create a
	 * connection with itself, attempting to create a connection with a knot outside
	 * of range, attempting to create a connection where there's already one).
	 *
	 * @param to
	 *               Knot to create a connection to.
	 */
	public void connect(final Knot to) {
		// Orientation of the vector going from this to to
		final Orientation orientation = Orientation.getOrientation(to.getCoordinateX() - this.getCoordinateX(),
				to.getCoordinateY() - this.getCoordinateY());
		this.connections[orientation.getRotation()] = to;
		to.connections[orientation.reverse().getRotation()] = this;
		/*
		 * If the connected knots belong to different networks, fuse the networks
		 * together to ensure that networks contain all and only all interconnected
		 * knots.
		 */
		if (!this.getNetwork().equals(to.getNetwork())) {
			for (final Knot knot : to.getNetwork().getKnots()) {
				knot.setNetwork(this.getNetwork());
			}
		}
	}

	/**
	 * Destroys a connection between this knot and a specified knot. Doesn't do
	 * anything if there's no connection between the knots.
	 *
	 * @param from
	 *                 Knot to destroy a connection from.
	 */
	public void disconnect(final Knot from) {
		// Orientation of the vector going from this to from
		Orientation orientation = Orientation.getOrientation(from.getCoordinateX() - this.getCoordinateX(),
				from.getCoordinateY() - this.getCoordinateY());
		this.connections[orientation.getRotation()] = null;
		from.connections[orientation.reverse().getRotation()] = null;

		Knot knot = from;
		while (true) {
			/*
			 * Check all directions in clockwise order starting by the direction immediately
			 * after the one we just followed.
			 *
			 * The idea is to follow the edge of the network until you bump into either from
			 * or this.
			 */
			// Orientation pointing to the previous knot
			final Orientation toPrevious = orientation.reverse();
			// Iterate over all orientations except the one pointing to the previous knot
			for (Orientation nextOrientation = toPrevious.rotate(1); !nextOrientation
					.equals(toPrevious); nextOrientation = nextOrientation.rotate(1)) {
				final Knot nextKnot = knot.getKnotAt(nextOrientation);
				if (nextKnot != null) {
					knot = nextKnot;
					orientation = nextOrientation;
					if (knot.equals(from)) {
						/*
						 * If you bump into from, it means there are no connections remaining between
						 * this and from.
						 */
						// TODO: fork the networks of this and from
						return;
					}
					if (knot.equals(this)) {
						/*
						 * If you bump into this, it means there are remaining connections between this
						 * and from.
						 */
						return;
					}
				}
			}
		}
	}

	/**
	 * Get the network this knot belongs to.
	 *
	 * @return The network this knot belongs to.
	 */
	public Network getNetwork() {
		return this.network;
	}

	/**
	 * Set the network this knot belongs to to the given network.
	 *
	 * @param network
	 *                    A network.
	 */
	private void setNetwork(final Network network) {
		if (this.network != null) {
			this.network.getKnots().remove(this);
		}
		this.network = network;
		network.getKnots().add(this);
	}

}
