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
 * This class stores a grid of integer numbers as the sums of the numbers of
 * every group of four tiles and uses these sums and the slopes between them to
 * approximate the original values upon calling the method get.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class TinyIntegerMatrix implements IntegerMatrix {

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

	public TinyIntegerMatrix(final int x, final int y) {
		this.sizeX = x / 2;
		this.sizeY = y / 2;
		this.data = new byte[(this.sizeX * this.sizeY)];
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
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override
	public int get(final int x, final int y) {
		this.check_range(x, y);
		if (this.data instanceof byte[]) {
			final byte[] castedData = (byte[]) this.data;
			final int indexX = x / 2;
			final int indexY = y / 2;
			/*
			 * The idea here is that since every group of 4 points is simplified to a single
			 * point, we calculate the indices of where the neighboring point would be. If
			 * an index is even, that means it's on the lower end of the pair so the
			 * neighboring point is contained in the previous pair. If it's odd, it is on
			 * the higher end of the pair, so the neighboring point is contained in the next
			 * pair.
			 */
			final int sideIndexX = (x % 2) == 0 ? indexX - 1 : indexX + 1;
			final int sideIndexY = (y % 2) == 0 ? indexY - 1 : indexY + 1;

			// Sum of the 4 values of the point that contains this one.
			int centralValue;
			// Sum of the 4 values of the point that contains the neighboring point on the x
			// axis.
			int sideValueX;
			// Sum of the 4 values of the point that contains the neighboring point on the y
			// axis.
			int sideValueY;

			centralValue = castedData[(this.sizeX * indexY) + indexX];

			/*
			 * If the indices of neighboring points are beyond the bounds of the array,
			 * approximate their values by continuing the slope of the neighboring points
			 * that are known to be within bounds.
			 */
			if (sideIndexX < 0) {
				final int oppositeSideIndexX = sideIndexX + 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else if (sideIndexX >= this.sizeX) {
				final int oppositeSideIndexX = sideIndexX - 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else {
				sideValueX = castedData[(this.sizeX * indexY) + sideIndexX];
			}
			if (sideIndexY < 0) {
				final int oppositeSideIndexY = sideIndexY + 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else if (sideIndexY >= this.sizeY) {
				final int oppositeSideIndexY = sideIndexY - 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else {
				sideValueY = castedData[(this.sizeX * sideIndexY) + indexX];
			}

			// Average the points.
			return ((2 * centralValue) + sideValueX + sideValueY) / 16;
		} else if (this.data instanceof short[]) {
			final short[] castedData = (short[]) this.data;
			final int indexX = x / 2;
			final int indexY = y / 2;
			final int sideIndexX = (x % 2) == 0 ? indexX - 1 : indexX + 1;
			final int sideIndexY = (y % 2) == 0 ? indexY - 1 : indexY + 1;

			int centralValue;
			int sideValueX;
			int sideValueY;

			centralValue = castedData[(this.sizeX * indexY) + indexX];

			if (sideIndexX < 0) {
				final int oppositeSideIndexX = sideIndexX + 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else if (sideIndexX >= this.sizeX) {
				final int oppositeSideIndexX = sideIndexX - 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else {
				sideValueX = castedData[(this.sizeX * indexY) + sideIndexX];
			}
			if (sideIndexY < 0) {
				final int oppositeSideIndexY = sideIndexY + 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else if (sideIndexY >= this.sizeY) {
				final int oppositeSideIndexY = sideIndexY - 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else {
				sideValueY = castedData[(this.sizeX * sideIndexY) + indexX];
			}

			return ((2 * centralValue) + sideValueX + sideValueY) / 16;
		} else if (this.data instanceof int[]) {
			final int[] castedData = (int[]) this.data;
			final int indexX = x / 2;
			final int indexY = y / 2;
			final int sideIndexX = (x % 2) == 0 ? indexX - 1 : indexX + 1;
			final int sideIndexY = (y % 2) == 0 ? indexY - 1 : indexY + 1;

			int centralValue;
			int sideValueX;
			int sideValueY;

			centralValue = castedData[(this.sizeX * indexY) + indexX];

			if (sideIndexX < 0) {
				final int oppositeSideIndexX = sideIndexX + 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else if (sideIndexX >= this.sizeX) {
				final int oppositeSideIndexX = sideIndexX - 2;
				sideValueX = (2 * centralValue) - castedData[(this.sizeX * indexY) + oppositeSideIndexX];
			} else {
				sideValueX = castedData[(this.sizeX * indexY) + sideIndexX];
			}
			if (sideIndexY < 0) {
				final int oppositeSideIndexY = sideIndexY + 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else if (sideIndexY >= this.sizeY) {
				final int oppositeSideIndexY = sideIndexY - 2;
				sideValueY = (2 * centralValue) - castedData[(this.sizeX * oppositeSideIndexY) + indexX];
			} else {
				sideValueY = castedData[(this.sizeX * sideIndexY) + indexX];
			}

			return ((2 * centralValue) + sideValueX + sideValueY) / 16;
		}
		return 0;
	}

	@Override
	public void set(final int x, final int y, final int i) {
		this.add(x, y, i - this.get(x, y));
	}

	@Override
	public void add(final int x, final int y, final int i) {
		final int indexX = x / 2;
		final int indexY = y / 2;
		final int flatIndex = (this.sizeX * indexY) + indexX;
		if (this.data instanceof byte[]) {
			final byte[] castedData = (byte[]) this.data;
			int value = castedData[flatIndex];
			value += i;
			if ((value < Short.MIN_VALUE) || (value > Short.MAX_VALUE)) {
				this.toInteger();
				this.set(x, y, i);
			} else if ((value < Byte.MIN_VALUE) || (value > Byte.MAX_VALUE)) {
				this.toShort();
				this.set(x, y, i);
			} else {
				castedData[flatIndex] = (byte) value;
			}
		} else if (this.data instanceof short[]) {
			final short[] castedData = (short[]) this.data;
			int value = castedData[flatIndex];
			value += i;
			if ((value < Short.MIN_VALUE) || (value > Short.MAX_VALUE)) {
				this.toInteger();
				this.set(x, y, i);
			} else {
				castedData[flatIndex] = (short) value;
			}
		} else if (this.data instanceof int[]) {
			final int[] castedData = (int[]) this.data;
			int value = castedData[flatIndex];
			value += i;
			castedData[flatIndex] = value;
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
