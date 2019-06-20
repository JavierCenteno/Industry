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

package generator;

import util.math.IMath;

/**
 * This class represents an interpolation function used to create a hill.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public interface Interpolation {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * The delta function is height at the center, 0 elsewhere.
	 */
	public static Interpolation DELTA = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			if (x == 0) {
				if (y == 0) {
					return height;
				}
			}
			return 0;
		}

	};

	/**
	 * The pyramid function, where height is proportional to the radius minus
	 * whichever coordinate is more distant from the center.
	 */
	public static Interpolation PYRAMID = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final int absoluteX = IMath.absolute(x);
			final int absoluteY = IMath.absolute(y);
			final double input = 1.0 - ((absoluteX > absoluteY ? absoluteX : absoluteY) / radius);
			return (int) (input * height);
		}

	};

	/**
	 * The cone function, where height is proportional to the radius minus the
	 * euclidean distance to the center.
	 */
	public static Interpolation CONE = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final double doubleX = x;
			final double doubleY = y;
			final double magnitude = Math.sqrt((doubleX * doubleX) + (doubleY * doubleY)) / radius;
			final double input = 1.0 - magnitude;
			return (int) (input * height);
		}

	};

	/**
	 * The diamond function, where height is proportional to the radius minus the
	 * manhattan distance to the center.
	 */
	public static Interpolation DIAMOND = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final double magnitude = ((double) Math.abs(x + y)) / radius;
			final double input = 1.0 - magnitude;
			return (int) (input * height);
		}

	};

	/**
	 * The smoothstep function, defined by y = -2x^3 + 3x^2
	 */
	public static Interpolation SMOOTHSTEP = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final double doubleX = x;
			final double doubleY = y;
			final double magnitude = Math.sqrt((doubleX * doubleX) + (doubleY * doubleY)) / radius;
			if (magnitude > 1.0) {
				return 0;
			}
			final double input = 1.0 - magnitude;
			final double inputSecondPower = input * input;
			final double inputThirdPower = inputSecondPower * input;
			return (int) (((-2 * inputThirdPower) + (3 * inputSecondPower)) * height);
		}

	};

	/**
	 * The smootherstep function, defined by y = 6x^5 - 15x^4 + 10x^3
	 */
	public static Interpolation SMOOTHERSTEP = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final double doubleX = x;
			final double doubleY = y;
			final double magnitude = Math.sqrt((doubleX * doubleX) + (doubleY * doubleY)) / radius;
			if (magnitude > 1.0) {
				return 0;
			}
			final double input = 1.0 - magnitude;
			final double inputThirdPower = input * input * input;
			final double inputFourthPower = inputThirdPower * input;
			final double inputFifthPower = inputFourthPower * input;
			return (int) ((((6 * inputFifthPower) - (15 * inputFourthPower)) + (10 * inputThirdPower)) * height);
		}

	};

	/**
	 * The smootheststep function, defined by y = -20x^7 + 70x^6 - 84x^5 + 35x^4
	 */
	public static Interpolation SMOOTHESTSTEP = new Interpolation() {

		@Override
		public int at(final int x, final int y, final int radius, final int height) {
			final double doubleX = x;
			final double doubleY = y;
			final double magnitude = Math.sqrt((doubleX * doubleX) + (doubleY * doubleY)) / radius;
			if (magnitude > 1.0) {
				return 0;
			}
			final double input = 1.0 - magnitude;
			final double inputSecondPower = input * input;
			final double inputFourthPower = inputSecondPower * inputSecondPower;
			final double inputFifthPower = inputFourthPower * input;
			final double inputSixthPower = inputFifthPower * input;
			final double inputSeventhPower = inputSixthPower * input;
			return (int) (((((-20 * inputSeventhPower) + (70 * inputSixthPower)) - (84 * inputFifthPower))
					+ (35 * inputFourthPower)) * height);
		}

	};

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the height of a hill plotted by this interpolation function with this
	 * radius and height at these coordinates.
	 *
	 * @param x
	 *                   X coordinate of the point where we want to obtain the
	 *                   height.
	 * @param y
	 *                   Y coordinate of the point where we want to obtain the
	 *                   height.
	 * @param radius
	 *                   Radius of the hill.
	 * @param height
	 *                   Maximum height of the hill.
	 * @return Height of the hill with the given parameters at the point x, y.
	 */
	public int at(int x, int y, int radius, int height);

}
