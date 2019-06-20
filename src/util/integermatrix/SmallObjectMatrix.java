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

package util.integermatrix;

/**
 * This class represents a grid of objects as a list of objects and an integer
 * grid of indices of that list.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @see SmallIntegerMatrix
 * @since 0.1
 *
 */
public class SmallObjectMatrix<T> implements ObjectMatrix<T> {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Array of the different types of object this matrix has.
	 */
	private Object[] data;
	/**
	 * Total number of different types of objects this matrix has.
	 */
	private int size;
	/**
	 * Matrix of the indices of each object in the data array.
	 */
	private final SmallIntegerMatrix indices;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public SmallObjectMatrix(final int x, final int y) {
		this.indices = new SmallIntegerMatrix(x, y);
		this.data = new Object[1];
		this.size = 0;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int sizeX() {
		return this.indices.sizeX();
	}

	@Override
	public int sizeY() {
		return this.indices.sizeY();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(final int x, final int y) {
		return (T) this.data[this.indices.get(x, y)];
	}

	@Override
	public void set(final int x, final int y, final T t) {
		// If the object is already in this map, just put its index in this tile.
		for (int index = 0; index < this.data.length; ++index) {
			if (this.data[index] == t) {
				this.indices.set(x, y, index);
				return;
			}
		}
		// If the object is not in this map, check whether it fits in the array. If not,
		// expand the array.
		if (this.size == this.data.length) {
			final Object[] newData = new Object[this.data.length << 1];
			System.arraycopy(this.data, 0, newData, 0, this.data.length);
			this.data = newData;
		}
		// Add the object to the map.
		this.indices.set(x, y, this.size);
		this.data[this.size] = t;
		++this.size;
	}

}
