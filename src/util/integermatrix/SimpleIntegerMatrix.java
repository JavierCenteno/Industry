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
 * An integer matrix that is just a wrapper for an array.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class SimpleIntegerMatrix implements IntegerMatrix {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Size parameters of this matrix.
	 */
	private final int sizeX, sizeY;
	/**
	 * Data contained in this matrix.
	 */
	private final int[][] data;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public SimpleIntegerMatrix(final int x, final int y) {
		this.sizeX = x;
		this.sizeY = y;
		this.data = new int[this.sizeY][this.sizeX];
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int sizeX() {
		return this.sizeX;
	}

	@Override
	public int sizeY() {
		return this.sizeY;
	}

	@Override
	public int get(final int x, final int y) {
		return this.data[y][x];
	}

	@Override
	public void set(final int x, final int y, final int i) {
		this.data[y][x] = i;
	}

}
