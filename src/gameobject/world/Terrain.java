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

package gameobject.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

import exe.Industry;
import gameobject.GameObject;
import gameobject.city.Citizen;
import gameobject.city.City;
import gameobject.element.building.Building;
import gameobject.element.building.Building.BuildingFactory;
import gameobject.element.feature.Feature;
import gameobject.element.feature.Feature.FeatureFactory;
import gameobject.element.knot.Knot.KnotFactory;
import gameobject.element.unit.Unit;
import gameobject.element.unit.Unit.UnitFactory;
import gameobject.world.TectonicPlate.Tile;
import typeobject.TerrainShape;

/**
 * This class represents a large grid of tiles divided in tectonic plates.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Terrain extends GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Shape of this terrain, which determines the behavior of its edges.
	 */
	private final TerrainShape terrainShape;
	/**
	 * Tectonic plates that make up this terrain.
	 */
	private final TectonicPlate[][] tectonicPlates;
	/**
	 * Side in tiles of the plates that make up this terrain.
	 */
	private final int tectonicPlateSize;
	/**
	 * Total size of the world in tiles.
	 */
	private final int totalSizeX, totalSizeY;
	/**
	 * Minimum coordinates a tile in the world can have.
	 */
	private final int totalMinX, totalMinY;
	/**
	 * Maximum coordinates a tile in the world can have.
	 */
	private final int totalMaxX, totalMaxY;
	/**
	 * Total size of the playable area in tiles.
	 */
	private final int playableSizeX, playableSizeY;
	/**
	 * Minimum coordinates a tile in the playable area can have.
	 */
	private final int playableMinX, playableMinY;
	/**
	 * Maximum coordinates a tile in the playable area can have.
	 */
	private final int playableMaxX, playableMaxY;
	/**
	 * Index in each dimension of the tiles at coordinate 0 for this dimension.
	 */
	protected int baseX, baseY;
	/**
	 * Base magma level.
	 */
	private final int baseMagma;
	/**
	 * Base height level.
	 */
	private final int baseHeight;
	/**
	 * Base terrain roughness level (height of geological features).
	 */
	private final int baseRoughness;
	/**
	 * Base water level.
	 */
	private final int baseWater;
	/**
	 * Determines seasonal temperature changes. Its value is in [-1, 1], where 0
	 * means the axis of this planet's rotation around itself and the sun are
	 * parallel.
	 */
	protected double axialTilt;
	/**
	 * Temperature of the map at the equator.
	 */
	protected int baseTemperature;
	/**
	 * Variation in temperature from the equator to the poles.
	 */
	protected int temperatureDifference;
	/**
	 * Variation in temperature caused by an increase in height of 1. For every 100
	 * meters going up, temperature drops by one degree Celsius.
	 */
	protected double heightCoolingFactor = -0.01d;
	/**
	 * List of all buildings in this terrain.
	 */
	private final List<Building> buildings = new ArrayList<Building>();
	/**
	 * List of all features in this terrain.
	 */
	private final List<Feature> features = new ArrayList<Feature>();
	/**
	 * List of all units in this terrain.
	 */
	private final List<Unit> units = new ArrayList<Unit>();
	/**
	 * List of all citizens in this terrain.
	 */
	private final List<Citizen> citizens = new ArrayList<Citizen>();

	////////////////////////////////////////////////////////////////////////////////
	// Nested classes

	/**
	 * Represents the direction of a variation in coordinates.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.1
	 * @since 0.1
	 */
	public enum Orientation {

		////////////////////////////////////////////////////////////////////////////////
		// Class fields

		NORTH(0), EAST(1), SOUTH(2), WEST(3);
		private static final Orientation[] ALL_ORIENTATIONS = new Orientation[] { NORTH, EAST, SOUTH, WEST };

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		private int rotation;

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		private Orientation(final int rotation) {
			this.rotation = rotation;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Class methods

		public static Orientation getOrientation(final int deltaX, final int deltaY) {
			if (deltaX == 0) {
				if (deltaY < 0) {
					return SOUTH;
				} else if (deltaY > 0) {
					return NORTH;
				}
			}
			if (deltaY == 0) {
				if (deltaX < 0) {
					return WEST;
				} else if (deltaX > 0) {
					return EAST;
				}
			}
			return null;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		public int getRotation() {
			return this.rotation;
		}

		public Orientation rotate(final int rotation) {
			return Orientation.ALL_ORIENTATIONS[(this.rotation + rotation) % Orientation.ALL_ORIENTATIONS.length];
		}

		public Orientation reverse() {
			return Orientation.ALL_ORIENTATIONS[(this.rotation + (Orientation.ALL_ORIENTATIONS.length / 2))
					% Orientation.ALL_ORIENTATIONS.length];
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Inner classes

	/**
	 * Iterable that defines a set of tiles whose coordinates are within a certain
	 * bound.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.0
	 * @since 0.0
	 *
	 */
	public class TileIterable implements Iterable<Tile> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Coordinates of the first tile of this iterable.
		 */
		public int startTileCoordinateX, startTileCoordinateY;
		/**
		 * Coordinates of the last tile of this iterable.
		 */
		public int endTileCoordinateX, endTileCoordinateY;

		////////////////////////////////////////////////////////////////////////////////
		// Inner classes

		/**
		 * Iterator that iterates over tiles in a TileIterable.
		 *
		 * @author Javier Centeno Vega <jacenve@telefonica.net>
		 * @version 0.0
		 * @since 0.0
		 *
		 */
		public class TileIterator implements Iterator<Tile>, Spliterator<Tile> {

			////////////////////////////////////////////////////////////////////////////////
			// Instance fields

			/**
			 * Whether this iterator has started iterating.
			 */
			private boolean started = false;
			/**
			 * Coordinates of the first tile of this iterator.
			 */
			private int startTileCoordinateX, startTileCoordinateY;
			/**
			 * Coordinates of the last tile of this iterator.
			 */
			private int endTileCoordinateX, endTileCoordinateY;
			/**
			 * Positional parameters of the first tile of this iterator.
			 */
			private int startPlateIndexX, startPlateIndexY, startTileSubindexX, startTileSubindexY;
			/**
			 * Positional parameters of the last tile of this iterator.
			 */
			private int endPlateIndexX, endPlateIndexY, endTileSubindexX, endTileSubindexY;
			/**
			 * Positional parameters of the current tile of this iterator.
			 */
			private int currentPlateIndexX, currentPlateIndexY, currentTileSubindexX, currentTileSubindexY;

			////////////////////////////////////////////////////////////////////////////////
			// Instance initializers

			/**
			 * Creates an iterator that goes over all the tiles of this TileIterable.
			 */
			public TileIterator(final int startTileCoordinateX, final int startTileCoordinateY,
					final int endTileCoordinateX, final int endTileCoordinateY) {
				// Set starting tile parameters
				this.setStart(startTileCoordinateX, startTileCoordinateY);
				// Set ending tile parameters
				this.setEnd(endTileCoordinateX, endTileCoordinateY);
				// Set current tile parameters
				this.currentPlateIndexX = this.startPlateIndexX;
				this.currentPlateIndexY = this.startPlateIndexY;
				this.currentTileSubindexX = this.startTileSubindexX;
				this.currentTileSubindexY = this.startTileSubindexY;
			}

			////////////////////////////////////////////////////////////////////////////////
			// Instance methods

			private void setStart(final int startTileCoordinateX, final int startTileCoordinateY) {
				// Starting tile coordinates
				this.startTileCoordinateX = startTileCoordinateX;
				this.startTileCoordinateY = startTileCoordinateY;
				// Starting tile parameters
				int startTileIndexX = Terrain.this.tileIndexX(startTileCoordinateX);
				// If we're over the edge and we can cycle, cycle over
				if (Terrain.this.terrainShape.getCycleX()) {
					if (startTileIndexX < 0) {
						startTileIndexX += Terrain.this.playableSizeX;
					}
					if (startTileIndexX >= Terrain.this.playableSizeX) {
						startTileIndexX -= Terrain.this.playableSizeX;
					}
				}
				int startTileIndexY = Terrain.this.tileIndexY(startTileCoordinateY);
				// If we're over the edge and we can cycle, cycle over
				if (Terrain.this.terrainShape.getCycleY()) {
					if (startTileIndexY < 0) {
						startTileIndexY += Terrain.this.playableSizeY;
					}
					if (startTileIndexY >= Terrain.this.playableSizeY) {
						startTileIndexY -= Terrain.this.playableSizeY;
					}
				}
				this.startPlateIndexX = Terrain.this.plateIndexX(startTileIndexX);
				this.startPlateIndexY = Terrain.this.plateIndexY(startTileIndexY);
				this.startTileSubindexX = startTileIndexX % Terrain.this.tectonicPlateSize;
				this.startTileSubindexY = startTileIndexY % Terrain.this.tectonicPlateSize;
			}

			private void setEnd(final int endTileCoordinateX, final int endTileCoordinateY) {
				// Ending tile coordinates
				this.endTileCoordinateX = endTileCoordinateX;
				this.endTileCoordinateY = endTileCoordinateY;
				// Ending tile parameters
				int endTileIndexX = Terrain.this.tileIndexX(endTileCoordinateX);
				// If we're over the edge and we can cycle, cycle over
				if (Terrain.this.terrainShape.getCycleX()) {
					if (endTileIndexX < 0) {
						endTileIndexX += Terrain.this.playableSizeX;
					}
					if (endTileIndexX >= Terrain.this.playableSizeX) {
						endTileIndexX -= Terrain.this.playableSizeX;
					}
				}
				int endTileIndexY = Terrain.this.tileIndexY(endTileCoordinateY);
				// If we're over the edge and we can cycle, cycle over
				if (Terrain.this.terrainShape.getCycleY()) {
					if (endTileIndexY < 0) {
						endTileIndexY += Terrain.this.playableSizeY;
					}
					if (endTileIndexY >= Terrain.this.playableSizeY) {
						endTileIndexY -= Terrain.this.playableSizeY;
					}
				}
				this.endPlateIndexX = Terrain.this.plateIndexX(endTileIndexX);
				this.endPlateIndexY = Terrain.this.plateIndexY(endTileIndexY);
				this.endTileSubindexX = endTileIndexX % Terrain.this.tectonicPlateSize;
				this.endTileSubindexY = endTileIndexY % Terrain.this.tectonicPlateSize;
			}

			private void resetToStartX() {
				this.currentPlateIndexX = this.startPlateIndexX;
				this.currentTileSubindexX = this.startTileSubindexX;
			}

			private void resetToStartY() {
				this.currentPlateIndexY = this.startPlateIndexY;
				this.currentTileSubindexY = this.startTileSubindexY;
			}

			private void resetToEndX() {
				this.currentPlateIndexX = this.endPlateIndexX;
				this.currentTileSubindexX = this.endTileSubindexX;
			}

			private void resetToEndY() {
				this.currentPlateIndexY = this.endPlateIndexY;
				this.currentTileSubindexY = this.endTileSubindexY;
			}

			private void decreaseX() {
				--this.currentTileSubindexX;
				if (this.currentTileSubindexX == -1) {
					this.currentTileSubindexX = Terrain.this.tectonicPlateSize - 1;
					--this.currentPlateIndexX;
					// If we're at the edge and we can cycle, cycle over
					if (this.currentPlateIndexX == -1) {
						if (Terrain.this.terrainShape.getCycleX()) {
							this.currentPlateIndexX = Terrain.this.tectonicPlates[0].length - 1;
						}
					}
				}
			}

			private void increaseX() {
				++this.currentTileSubindexX;
				if (this.currentTileSubindexX == Terrain.this.tectonicPlateSize) {
					this.currentTileSubindexX = 0;
					++this.currentPlateIndexX;
					// If we're at the edge and we can cycle, cycle over
					if (this.currentPlateIndexX == Terrain.this.tectonicPlates[0].length) {
						if (Terrain.this.terrainShape.getCycleX()) {
							this.currentPlateIndexX = 0;
						}
					}
				}
			}

			private void decreaseY() {
				--this.currentTileSubindexY;
				if (this.currentTileSubindexY == -1) {
					this.currentTileSubindexY = Terrain.this.tectonicPlateSize - 1;
					--this.currentPlateIndexY;
					// If we're at the edge and we can cycle, cycle over
					if (this.currentPlateIndexY == -1) {
						if (Terrain.this.terrainShape.getCycleY()) {
							this.currentPlateIndexY = Terrain.this.tectonicPlates.length - 1;
						}
					}
				}
			}

			private void increaseY() {
				++this.currentTileSubindexY;
				if (this.currentTileSubindexY == Terrain.this.tectonicPlateSize) {
					this.currentTileSubindexY = 0;
					++this.currentPlateIndexY;
					// If we're at the edge and we can cycle, cycle over
					if (this.currentPlateIndexY == Terrain.this.tectonicPlates.length) {
						if (Terrain.this.terrainShape.getCycleY()) {
							this.currentPlateIndexY = 0;
						}
					}
				}
			}

			private boolean atStartX() {
				return (this.currentPlateIndexX == this.startPlateIndexX)
						&& (this.currentTileSubindexX == this.startTileSubindexX);
			}

			private boolean atStartY() {
				return (this.currentPlateIndexY == this.startPlateIndexY)
						&& (this.currentTileSubindexY == this.startTileSubindexY);
			}

			private boolean atEndX() {
				return (this.currentPlateIndexX == this.endPlateIndexX)
						&& (this.currentTileSubindexX == this.endTileSubindexX);
			}

			private boolean atEndY() {
				return (this.currentPlateIndexY == this.endPlateIndexY)
						&& (this.currentTileSubindexY == this.endTileSubindexY);
			}

			public boolean atValidTile() {
				return !((this.currentPlateIndexX < 0)
						|| (this.currentPlateIndexX >= Terrain.this.tectonicPlates[0].length)
						|| (this.currentPlateIndexY < 0)
						|| (this.currentPlateIndexY >= Terrain.this.tectonicPlates.length));
			}

			/**
			 * Returns true if this iterator has a previous tile.
			 *
			 * @return true if this iterator has a previous tile.
			 */
			public boolean hasPrevious() {
				return !(this.atStartX() && this.atStartY());
			}

			/**
			 * Returns true if this iterator has a next tile.
			 *
			 * @return true if this iterator has a next tile.
			 */
			@Override
			public boolean hasNext() {
				return !(this.atEndX() && this.atEndY());
			}

			/**
			 * Moves this iterator to the previous tile and returns it.
			 */
			public Tile previous() {
				if (this.atStartX()) {
					// If the start of x has been reached
					if (this.atStartY()) {
						// If the start of y has been reached
						// we could loop over to the end.
						// resetToEndX();
						// resetToEndY();
						// but we'll just throw an exception instead
						throw new NoSuchElementException();
					} else {
						// Else, reset x and decrease y
						this.resetToEndX();
						this.decreaseY();
					}
				} else {
					// Else, increase x
					this.decreaseX();
				}
				if (Industry.DEBUG) {
					System.out.println("TileIterator " + this.hashCode() + " moved to plate x:"
							+ this.currentPlateIndexX + ", y:" + this.currentPlateIndexY + "; tile x:"
							+ this.currentTileSubindexX + ", y:" + this.currentTileSubindexY);
				}
				return this.current();
			}

			/**
			 * Returns the current tile in the iteration.
			 */
			public Tile current() {
				if (!this.atValidTile()) {
					return null;
				}
				final TectonicPlate tectonicPlate = Terrain.this.tectonicPlates[this.currentPlateIndexY][this.currentPlateIndexX];
				return tectonicPlate.getTile(this.currentTileSubindexX, this.currentTileSubindexY);
			}

			/**
			 * Moves this iterator to the next tile and returns it.
			 */
			@Override
			public Tile next() {
				if (!this.started) {
					this.started = true;
				} else {
					if (this.atEndX()) {
						// If the end of x has been reached
						if (this.atEndY()) {
							// If the end of y has been reached
							// we could loop over to the start.
							// resetToStartX();
							// resetToStartY();
							// but we'll just throw an exception instead
							throw new NoSuchElementException();
						} else {
							// Else, reset x and increase y
							this.resetToStartX();
							this.increaseY();
						}
					} else {
						// Else, increase x
						this.increaseX();
					}
					if (Industry.DEBUG) {
						System.out.println("TileIterator " + this.hashCode() + " moved to plate x:"
								+ this.currentPlateIndexX + ", y:" + this.currentPlateIndexY + "; tile x:"
								+ this.currentTileSubindexX + ", y:" + this.currentTileSubindexY);
					}
				}
				return this.current();
			}

			/**
			 * Reset this iterator to the first tile.
			 */
			public void resetToStart() {
				this.resetToStartX();
				this.resetToStartY();
			}

			/**
			 * Reset this iterator to the last tile.
			 */
			public void resetToEnd() {
				this.resetToEndX();
				this.resetToEndY();
			}

			@Override
			public int characteristics() {
				return Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.SIZED
						| Spliterator.SUBSIZED;
			}

			@Override
			public long estimateSize() {
				final long remainingTilesInRow = (this.endTileSubindexX - this.currentTileSubindexX)
						+ ((this.endPlateIndexX - this.currentPlateIndexX) * Terrain.this.tectonicPlateSize);
				final long remainingRows = (this.endTileSubindexY - this.currentTileSubindexY)
						+ ((this.endPlateIndexY - this.currentPlateIndexY) * Terrain.this.tectonicPlateSize);
				return remainingTilesInRow + (remainingRows * (this.endTileCoordinateX - this.startTileCoordinateX));
			}

			@Override
			public boolean tryAdvance(final Consumer<? super Tile> action) {
				if (this.hasNext()) {
					action.accept(this.next());
					return true;
				} else {
					return false;
				}
			}

			@Override
			public Spliterator<Tile> trySplit() {
				if (this.atStartX() && this.atStartY()) {
					final int rows = this.endTileCoordinateY - this.startTileCoordinateY;
					if (rows == 0) {
						final int columns = this.endTileCoordinateX - this.startTileCoordinateX;
						if (columns == 0) {
							// Can't split if there's only one element
							return null;
						}
						// If we only have a row of tiles, split this row in half
						final int midTileCoordinateX = this.startTileCoordinateX + (columns / 2);
						this.setEnd(midTileCoordinateX, this.endTileCoordinateY);
						return new TileIterator(midTileCoordinateX + 1, this.startTileCoordinateY,
								this.endTileCoordinateX, this.endTileCoordinateY);
					} else {
						// If we have multiple rows of tiles, split the rows
						final int midTileCoordinateY = this.startTileCoordinateY + (rows / 2);
						this.setEnd(this.endTileCoordinateX, midTileCoordinateY);
						return new TileIterator(this.startTileCoordinateX, midTileCoordinateY + 1,
								this.endTileCoordinateX, this.endTileCoordinateY);
					}
				} else {
					// it's harder to split if iteration has already started
					return null;
				}
			}

			@Override
			public void forEachRemaining(final Consumer<? super Tile> action) {
				Spliterator.super.forEachRemaining(action);
			}

		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Creates a TileIterable for all the tiles in the playable area.
		 */
		public TileIterable() {
			this(Terrain.this.playableMinX, Terrain.this.playableMinY, Terrain.this.playableMaxX,
					Terrain.this.playableMaxY);
		}

		/**
		 * Creates a TileIterable for the tiles in this terrain whose coordinates are
		 * within the given coordinates. If the coordinates exceed the coordinates of
		 * existing tiles, the space is filled with null.
		 *
		 * @param fromX
		 *                  A coordinate along the x axis.
		 * @param fromY
		 *                  A coordinate along the y axis.
		 * @param toX
		 *                  A coordinate along the x axis.
		 * @param toY
		 *                  A coordinate along the y axis.
		 */
		public TileIterable(final int fromX, final int fromY, final int toX, final int toY) {
			// To simplify code, we start from the smallest coordinate
			// So, if from_n is bigger than to_n, swap them.
			if (fromX < toX) {
				this.startTileCoordinateX = fromX;
				this.endTileCoordinateX = toX;
			} else {
				this.startTileCoordinateX = toX;
				this.endTileCoordinateX = fromX;
			}
			if (fromY < toY) {
				this.startTileCoordinateY = fromY;
				this.endTileCoordinateY = toY;
			} else {
				this.startTileCoordinateY = toY;
				this.endTileCoordinateY = fromY;
			}
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		public TileIterator tileIterator() {
			return this.new TileIterator(this.startTileCoordinateX, this.startTileCoordinateY, this.endTileCoordinateX,
					this.endTileCoordinateY);
		}

		@Override
		public Iterator<Tile> iterator() {
			return this.tileIterator();
		}

		@Override
		public Spliterator<Tile> spliterator() {
			return this.tileIterator();
		}

	}

	/**
	 * Iterable that defines a set of tectonic plates whose coordinates are within a
	 * certain bound.
	 *
	 * @author Javier Centeno Vega <jacenve@telefonica.net>
	 * @version 0.0
	 * @since 0.0
	 *
	 */
	public class TectonicPlateIterable implements Iterable<TectonicPlate> {

		////////////////////////////////////////////////////////////////////////////////
		// Instance fields

		/**
		 * Starting plate parameters.
		 */
		private final int startPlateIndexX, startPlateIndexY;
		/**
		 * Ending plate parameters.
		 */
		private final int endPlateIndexX, endPlateIndexY;

		////////////////////////////////////////////////////////////////////////////////
		// Inner classes

		/**
		 * Iterator that iterates over tectonic plates in a TectonicPlateIterable.
		 *
		 * @author Javier Centeno Vega <jacenve@telefonica.net>
		 * @version 0.0
		 * @since 0.0
		 *
		 */
		public class TectonicPlateIterator implements Iterator<TectonicPlate>, Spliterator<TectonicPlate> {

			////////////////////////////////////////////////////////////////////////////////
			// Instance fields

			/**
			 * Whether this iterator has started iterating.
			 */
			private boolean started = false;
			/**
			 * Starting plate parameters.
			 */
			private int startPlateIndexX, startPlateIndexY;
			/**
			 * Ending plate parameters.
			 */
			private int endPlateIndexX, endPlateIndexY;
			/**
			 * Current plate parameters.
			 */
			private int currentPlateIndexX, currentPlateIndexY;

			////////////////////////////////////////////////////////////////////////////////
			// Instance initializers

			/**
			 * Creates an iterator that goes over all the tiles of this TileIterable.
			 */
			public TectonicPlateIterator(final int startPlateIndexX, final int startPlateIndexY,
					final int endPlateIndexX, final int endPlateIndexY) {
				this.setStart(startPlateIndexX, startPlateIndexY);
				this.setEnd(endPlateIndexX, endPlateIndexY);
				this.currentPlateIndexX = startPlateIndexX;
				this.currentPlateIndexY = startPlateIndexY;
			}

			////////////////////////////////////////////////////////////////////////////////
			// Instance methods

			private void setStart(final int startPlateIndexX, final int startPlateIndexY) {
				this.startPlateIndexX = startPlateIndexX;
				this.startPlateIndexY = startPlateIndexY;
			}

			private void setEnd(final int endPlateIndexX, final int endPlateIndexY) {
				this.endPlateIndexX = endPlateIndexX;
				this.endPlateIndexY = endPlateIndexY;
			}

			/**
			 * Returns true if this iterator has a previous plate.
			 *
			 * @return true if this iterator has a previous plate.
			 */
			public boolean hasPrevious() {
				return !((this.currentPlateIndexX == this.startPlateIndexX)
						&& (this.currentPlateIndexY == this.startPlateIndexY));
			}

			/**
			 * Returns true if this iterator has a next plate.
			 *
			 * @return true if this iterator has a next plate.
			 */
			@Override
			public boolean hasNext() {
				return !((this.currentPlateIndexX == this.endPlateIndexX)
						&& (this.currentPlateIndexY == this.endPlateIndexY));
			}

			/**
			 * Moves this iterator to the previous plate and returns it.
			 */
			public TectonicPlate previous() {
				if (this.currentPlateIndexX == this.startPlateIndexX) {
					// If the start of x has been reached
					if (this.currentPlateIndexY == this.startPlateIndexY) {
						// If the start of y has been reached,
						// we could loop over to the end.
						// currentPlateIndexX = endPlateIndexX;
						// currentPlateIndexY = endPlateIndexY;
						// but we'll just throw an exception instead
						throw new NoSuchElementException();
					} else {
						// Else, reset x and decrease y
						this.currentPlateIndexX = this.endPlateIndexX;
						--this.currentPlateIndexY;
					}
				} else {
					// Else, decrease x
					--this.currentPlateIndexX;
				}
				return this.current();
			}

			/**
			 * Returns the current plate in the iteration.
			 */
			public TectonicPlate current() {
				return Terrain.this.tectonicPlates[this.currentPlateIndexY][this.currentPlateIndexX];
			}

			/**
			 * Moves this iterator to the next plate and returns it.
			 */
			@Override
			public TectonicPlate next() {
				if (!this.started) {
					this.started = true;
				} else {
					if (this.currentPlateIndexX == this.endPlateIndexX) {
						// If the end of x has been reached
						if (this.currentPlateIndexY == this.endPlateIndexY) {
							// If the end of y has been reached,
							// we could loop over to the start.
							// currentPlateIndexX = startPlateIndexX;
							// currentPlateIndexY = startPlateIndexY;
							// but we'll just throw an exception instead
							throw new NoSuchElementException();
						} else {
							// Else, reset x and increase y
							this.currentPlateIndexX = this.startPlateIndexX;
							++this.currentPlateIndexY;
						}
					} else {
						// Else, increase x
						++this.currentPlateIndexX;
					}
				}
				return this.current();
			}

			/**
			 * Creates a new plate in the current place of the iteration.
			 */
			public void newTectonicPlate(final int magmaFlowX, final int magmaFlowY, final City city) {
				Terrain.this.tectonicPlates[this.currentPlateIndexY][this.currentPlateIndexX] = new TectonicPlate(
						Terrain.this, this.currentPlateIndexX, this.currentPlateIndexY, magmaFlowX, magmaFlowY, city,
						Terrain.this.tectonicPlateSize);
			}

			@Override
			public int characteristics() {
				return Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.SIZED
						| Spliterator.SUBSIZED;
			}

			@Override
			public long estimateSize() {
				final long remainingPlatesInRow = this.endPlateIndexX - this.currentPlateIndexX;
				final long remainingRows = this.endPlateIndexY - this.currentPlateIndexY;
				return remainingPlatesInRow + (remainingRows * (this.endPlateIndexX - this.startPlateIndexX));
			}

			@Override
			public boolean tryAdvance(final Consumer<? super TectonicPlate> action) {
				if (this.hasNext()) {
					action.accept(this.next());
					return true;
				} else {
					return false;
				}
			}

			@Override
			public Spliterator<TectonicPlate> trySplit() {
				if ((this.currentPlateIndexX == this.startPlateIndexX)
						&& (this.currentPlateIndexY == this.startPlateIndexY)) {
					final int rows = this.endPlateIndexY - this.startPlateIndexY;
					if (rows == 0) {
						final int columns = this.endPlateIndexX - this.startPlateIndexX;
						if (columns == 0) {
							// Can't split if there's only one element
							return null;
						}
						// If we only have a row of plates, split this row in half
						final int midPlateIndexX = this.startPlateIndexX + (columns / 2);
						this.setEnd(midPlateIndexX, this.endPlateIndexY);
						return new TectonicPlateIterator(midPlateIndexX + 1, this.startPlateIndexY, this.endPlateIndexX,
								this.endPlateIndexY);
					} else {
						// If we have multiple rows of plates, split the rows
						final int midPlateIndexY = this.startPlateIndexY + (rows / 2);
						this.setEnd(this.endPlateIndexX, midPlateIndexY);
						return new TectonicPlateIterator(this.startPlateIndexX, midPlateIndexY + 1, this.endPlateIndexX,
								this.endPlateIndexY);
					}
				} else {
					// it's harder to split if iteration has already started
					return null;
				}
			}

			@Override
			public void forEachRemaining(final Consumer<? super TectonicPlate> action) {
				Spliterator.super.forEachRemaining(action);
			}

			/**
			 * Obtain the coordinate of the center of this plate along the x axis.
			 */
			public int getCenterX() {
				return ((this.currentPlateIndexX * Terrain.this.tectonicPlateSize)
						+ ((Terrain.this.tectonicPlateSize - 1) / 2)) - Terrain.this.baseX;
			}

			/**
			 * Obtain the coordinate of the center of this plate along the y axis.
			 */
			public int getCenterY() {
				return ((this.currentPlateIndexY * Terrain.this.tectonicPlateSize)
						+ ((Terrain.this.tectonicPlateSize - 1) / 2)) - Terrain.this.baseY;
			}

		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance initializers

		/**
		 * Creates a TectonicPlateIterable for all the tectonic plates in this terrain.
		 */
		public TectonicPlateIterable() {
			this.startPlateIndexX = 0;
			this.startPlateIndexY = 0;
			this.endPlateIndexX = Terrain.this.tectonicPlates[0].length - 1;
			this.endPlateIndexY = Terrain.this.tectonicPlates.length - 1;
		}

		////////////////////////////////////////////////////////////////////////////////
		// Instance methods

		public TectonicPlateIterator tectonicPlateIterator() {
			return this.new TectonicPlateIterator(this.startPlateIndexX, this.startPlateIndexY, this.endPlateIndexX,
					this.endPlateIndexY);
		}

		@Override
		public Iterator<TectonicPlate> iterator() {
			return this.tectonicPlateIterator();
		}

		@Override
		public Spliterator<TectonicPlate> spliterator() {
			return this.tectonicPlateIterator();
		}

	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Terrain(final World world, final TerrainShape terrainShape, final int totalPlatesX, final int totalPlatesY,
			final int playablePlatesX, final int playablePlatesY, final int tectonicPlateSize, final int crustThickness,
			final int seaLevel, final int terrainRoughness, final double axialTilt, final int baseTemperature,
			final int temperatureDifference) {
		super(world);
		this.terrainShape = terrainShape;
		this.tectonicPlates = new TectonicPlate[playablePlatesY][playablePlatesX];
		this.tectonicPlateSize = tectonicPlateSize;
		this.totalSizeX = totalPlatesX * tectonicPlateSize;
		this.totalSizeY = totalPlatesY * tectonicPlateSize;
		this.totalMinX = -(this.totalSizeX - 1) / 2;
		this.totalMinY = -(this.totalSizeY - 1) / 2;
		this.totalMaxX = -this.totalMinX;
		this.totalMaxY = -this.totalMinY;
		this.playableSizeX = playablePlatesX * tectonicPlateSize;
		this.playableSizeY = playablePlatesY * tectonicPlateSize;
		this.playableMinX = this.getPRNG().generateUniformInteger(this.totalMinX,
				(this.totalMaxX - this.playableSizeX) + 2);
		this.playableMinY = this.getPRNG().generateUniformInteger(this.totalMinY,
				(this.totalMaxY - this.playableSizeY) + 2);
		this.playableMaxX = (this.playableMinX + this.playableSizeX) - 1;
		this.playableMaxY = (this.playableMinY + this.playableSizeY) - 1;
		this.baseX = -this.playableMinX;
		this.baseY = -this.playableMinY;
		this.baseHeight = -seaLevel;
		this.baseMagma = this.baseHeight - crustThickness;
		this.baseRoughness = terrainRoughness;
		this.baseWater = 0;
		this.axialTilt = axialTilt;
		this.baseTemperature = baseTemperature;
		this.temperatureDifference = temperatureDifference;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * The squared distance between two tiles. Faster to compute than the euclidean
	 * distance.
	 *
	 * @param fromX
	 *                  Coordinate of one of the tiles along the x axis.
	 * @param fromY
	 *                  Coordinate of one of the tiles along the y axis.
	 * @param toX
	 *                  Coordinate of one of the tiles along the x axis.
	 * @param toY
	 *                  Coordinate of one of the tiles along the y axis.
	 * @return The square of the distance between the two tiles.
	 */
	protected int squaredDistance(final int fromX, final int fromY, final int toX, final int toY) {
		final int deltaX = toX - fromX;
		final int deltaY = toY - fromY;
		return (deltaX * deltaX) + (deltaY * deltaY);
	}

	/**
	 * The euclidean distance between two tiles as the square root of the squared
	 * distance between them.
	 *
	 * @param fromX
	 *                  Coordinate of one of the tiles along the x axis.
	 * @param fromY
	 *                  Coordinate of one of the tiles along the y axis.
	 * @param toX
	 *                  Coordinate of one of the tiles along the x axis.
	 * @param toY
	 *                  Coordinate of one of the tiles along the y axis.
	 * @return The euclidean distance between the two tiles.
	 */
	protected double distance(final int fromX, final int fromY, final int toX, final int toY) {
		return Math.sqrt((double) (this.squaredDistance(fromX, fromY, toX, toY)));
	}

	/**
	 * Obtains the index of a tile along the x axis in [0, maxX) from its coordinate
	 * along the x axis.
	 *
	 * @param tileCoordinateX
	 *                            The coordinate of a tile along the x axis.
	 * @return The index of a tile along the x axis.
	 */
	private int tileIndexX(final int tileCoordinateX) {
		return tileCoordinateX + this.baseX;
	}

	/**
	 * Obtains the index of a tile along the y axis in [0, maxY) from its coordinate
	 * along the y axis.
	 *
	 * @param tileCoordinateY
	 *                            The coordinate of a tile along the y axis.
	 * @return The index of a tile along the y axis.
	 */
	private int tileIndexY(final int tileCoordinateY) {
		return tileCoordinateY + this.baseY;
	}

	/**
	 * Obtains the index of a plate along the x axis in [0, tectonicPlates[].length)
	 * from the index of a tile along the x axis contained within the plate.
	 *
	 * @param tileIndexX
	 *                       The coordinate of a tile along the x axis.
	 * @return The index of a plate along the x axis.
	 */
	private int plateIndexX(final int tileIndexX) {
		return tileIndexX / this.tectonicPlateSize;
	}

	/**
	 * Obtains the index of a plate along the y axis in [0, tectonicPlates.length)
	 * from the index of a tile along the y axis contained within the plate.
	 *
	 * @param tileIndexY
	 *                       The coordinate of a tile along the y axis.
	 * @return The index of a plate along the y axis.
	 */
	private int plateIndexY(final int tileIndexY) {
		return tileIndexY / this.tectonicPlateSize;
	}

	public int getTectonicPlateSize() {
		return this.tectonicPlateSize;
	}

	public int getTotalSizeX() {
		return this.totalSizeX;
	}

	public int getTotalSizeY() {
		return this.totalSizeY;
	}

	public int getTotalMinX() {
		return this.totalMinX;
	}

	public int getTotalMinY() {
		return this.totalMinY;
	}

	public int getTotalMaxX() {
		return this.totalMaxX;
	}

	public int getTotalMaxY() {
		return this.totalMaxY;
	}

	public int getPlayableSizeX() {
		return this.playableSizeX;
	}

	public int getPlayableSizeY() {
		return this.playableSizeY;
	}

	public int getPlayableMinX() {
		return this.playableMinX;
	}

	public int getPlayableMinY() {
		return this.playableMinY;
	}

	public int getPlayableMaxX() {
		return this.playableMaxX;
	}

	public int getPlayableMaxY() {
		return this.playableMaxY;
	}

	public int getBaseMagma() {
		return this.baseMagma;
	}

	public int getBaseHeight() {
		return this.baseHeight;
	}

	public int getBaseRoughness() {
		return this.baseRoughness;
	}

	public int getBaseWater() {
		return this.baseWater;
	}

	public Tile getTile(final int x, final int y) {
		final int tileIndexX = this.tileIndexX(x);
		final int tileIndexY = this.tileIndexY(y);
		final int plateIndexX = this.plateIndexX(tileIndexX);
		final int plateIndexY = this.plateIndexY(tileIndexY);
		final int tileSubindexX = tileIndexX % this.tectonicPlateSize;
		final int tileSubindexY = tileIndexY % this.tectonicPlateSize;
		final TectonicPlate tectonicPlate = this.tectonicPlates[plateIndexY][plateIndexX];
		return tectonicPlate.getTile(tileSubindexX, tileSubindexY);
	}

	public Iterable<Tile> getAllTiles() {
		return new TileIterable();
	}

	public Iterable<Tile> getTileSet(final int fromX, final int fromY, final int toX, final int toY) {
		return new TileIterable(fromX, fromY, toX, toY);
	}

	public Iterable<TectonicPlate> getAllTectonicPlates() {
		return new TectonicPlateIterable();
	}

	public Iterable<Building> getAllBuildings() {
		return this.buildings;
	}

	public Iterable<Feature> getAllFeatures() {
		return this.features;
	}

	public Iterable<Unit> getAllUnits() {
		return this.units;
	}

	public Iterable<Citizen> getAllCitizens() {
		return this.citizens;
	}

	public void newBuilding(final BuildingFactory<?> buildingFactory, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city) {
		if (buildingFactory.check(this, coordinateX, coordinateY, orientation, city)) {
			final Building building = buildingFactory.make(this, coordinateX, coordinateY, orientation, health, city);
			this.buildings.add(building);
		}
	}

	public void newFeature(final FeatureFactory<?> featureFactory, final int coordinateX, final int coordinateY,
			final int sizeX, final int sizeY, final int health, final City city) {
		if (featureFactory.check(this, coordinateX, coordinateY, sizeX, sizeY, city)) {
			final Feature feature = featureFactory.make(this, coordinateX, coordinateY, sizeX, sizeY, health, city);
			this.features.add(feature);
		}
	}

	public void newKnot(final KnotFactory<?> knotFactory, final int fromX, final int fromY, final int toX,
			final int toY, final City city) {
		if (knotFactory.check(this, fromX, fromY, toX, toY, city)) {
			knotFactory.make(this, fromX, fromY, toX, toY, city);
		}
	}

	public void newUnit(final UnitFactory<?> unitFactory, final int coordinateX, final int coordinateY,
			final Orientation orientation, final int health, final City city) {
		if (unitFactory.check(this, coordinateX, coordinateY, orientation, city)) {
			final Unit unit = unitFactory.make(this, coordinateX, coordinateY, orientation, health, city);
			this.units.add(unit);
		}
	}

}
