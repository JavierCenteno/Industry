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
public class SmallIntegerMatrix implements IntegerMatrix {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Size parameters of this matrix.
	 */
	private final int sizeX, sizeY;
	/**
	 * Data contained in this matrix.
	 */
	private Object data;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public SmallIntegerMatrix(final int x, final int y) {
		this.sizeX = x;
		this.sizeY = y;
		this.data = new byte[this.sizeX * this.sizeY];
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

	private void check_range(final int x, final int y) {
		if ((x < 0) || (x >= this.sizeX) || (y < 0) || (y >= this.sizeY)) {
			throw new ArrayIndexOutOfBoundsException("x:" + x + "; y:" + y);
		}
	}

	@Override
	public int get(final int x, final int y) {
		this.check_range(x, y);
		if (this.data instanceof byte[]) {
			return ((byte[]) this.data)[(this.sizeX * y) + x];
		} else if (this.data instanceof short[]) {
			return ((short[]) this.data)[(this.sizeX * y) + x];
		} else if (this.data instanceof int[]) {
			return ((int[]) this.data)[(this.sizeX * y) + x];
		}
		return 0;
	}

	@Override
	public void set(final int x, final int y, final int i) {
		if (this.data instanceof byte[]) {
			if ((i < Short.MIN_VALUE) || (i > Short.MAX_VALUE)) {
				this.toInteger();
				this.set(x, y, i);
			} else if ((i < Byte.MIN_VALUE) || (i > Byte.MAX_VALUE)) {
				this.toShort();
				this.set(x, y, i);
			} else {
				((byte[]) this.data)[(this.sizeX * y) + x] = (byte) i;
			}
		} else if (this.data instanceof short[]) {
			if ((i < Short.MIN_VALUE) || (i > Short.MAX_VALUE)) {
				this.toInteger();
				this.set(x, y, i);
			} else {
				((short[]) this.data)[(this.sizeX * y) + x] = (short) i;
			}
		} else if (this.data instanceof int[]) {
			((int[]) this.data)[(this.sizeX * y) + x] = i;
		}
	}

	private void toShort() {
		if (this.data instanceof byte[]) {
			final byte[] oldData = (byte[]) this.data;
			final short[] newData = new short[oldData.length];
			for (int i = 0; i < oldData.length; ++i) {
				newData[i] = oldData[i];
			}
			this.data = newData;
		} else if (this.data instanceof int[]) {
			final int[] oldData = (int[]) this.data;
			final short[] newData = new short[oldData.length];
			for (int i = 0; i < oldData.length; ++i) {
				newData[i] = (short) oldData[i];
			}
			this.data = newData;
		}
	}

	private void toInteger() {
		if (this.data instanceof byte[]) {
			final byte[] oldData = (byte[]) this.data;
			final int[] newData = new int[oldData.length];
			for (int i = 0; i < oldData.length; ++i) {
				newData[i] = oldData[i];
			}
			this.data = newData;
		} else if (this.data instanceof short[]) {
			final short[] oldData = (short[]) this.data;
			final int[] newData = new int[oldData.length];
			for (int i = 0; i < oldData.length; ++i) {
				newData[i] = oldData[i];
			}
			this.data = newData;
		}
	}

}
