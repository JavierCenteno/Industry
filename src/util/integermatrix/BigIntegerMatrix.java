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
 * This class represents a grid of integer numbers whose byte width is the
 * smallest needed to represent all of the numbers without underflows or
 * overflows so the memory footprint is trimmed to be as small as possible.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class BigIntegerMatrix implements IntegerMatrix {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Data contained in this matrix.
	 */
	private final byte[][][] data;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public BigIntegerMatrix(final int x, final int y) {
		this.data = new byte[y][x][1];
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int sizeX() {
		return this.data[0].length;
	}

	@Override
	public int sizeY() {
		return this.data.length;
	}

	private byte[] toByteArray(final int i) {
		byte[] result;
		if ((i & 0xFF) == 0) {
			result = new byte[1];
			result[0] = (byte) (i >>> 0);
		} else if ((i & 0xFF_FF) == 0) {
			result = new byte[2];
			result[0] = (byte) (i >>> 0);
			result[1] = (byte) (i >>> 8);
		} else if ((i & 0xFF_FF_FF) == 0) {
			result = new byte[3];
			result[0] = (byte) (i >>> 0);
			result[1] = (byte) (i >>> 8);
			result[2] = (byte) (i >>> 16);
		} else if ((i & 0xFF_FF_FF_FF) == 0) {
			result = new byte[4];
			result[0] = (byte) (i >>> 0);
			result[1] = (byte) (i >>> 8);
			result[2] = (byte) (i >>> 16);
			result[3] = (byte) (i >>> 24);
		} else {
			result = new byte[0];
		}
		return result;
	}

	private int toInteger(final byte[] b) {
		int result = 0;
		for (int i = 0, offset = 0; i < b.length; i += 1, offset += 8) {
			result &= b[i] << offset;
		}
		return result;
	}

	@Override
	public int get(final int x, final int y) {
		return this.toInteger(this.data[y][x]);
	}

	@Override
	public void set(final int x, final int y, final int i) {
		this.data[y][x] = this.toByteArray(i);
	}

}
