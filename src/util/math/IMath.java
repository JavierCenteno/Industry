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

package util.math;

/**
 * This class contains math utility methods.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class IMath {

	////////////////////////////////////////////////////////////////////////////////
	// Number methods

	public static byte absolute(final byte value) {
		return value < 0 ? (byte) -value : value;
	}

	public static short absolute(final short value) {
		return value < 0 ? (short) -value : value;
	}

	public static int absolute(final int value) {
		return value < 0 ? -value : value;
	}

	public static long absolute(final long value) {
		return value < 0 ? -value : value;
	}

	public static float absolute(final float value) {
		return value < 0 ? -value : value;
	}

	public static double absolute(final double value) {
		return value < 0 ? -value : value;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Array methods

	public static byte[] flatten(final byte[]... arrays) {
		int totalLength = 0;
		for (final byte[] array : arrays) {
			totalLength += array.length;
		}
		final byte[] flattenedArray = new byte[totalLength];
		int start = 0;
		for (final byte[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	public static short[] flatten(final short[]... arrays) {
		int totalLength = 0;
		for (final short[] array : arrays) {
			totalLength += array.length;
		}
		final short[] flattenedArray = new short[totalLength];
		int start = 0;
		for (final short[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	public static int[] flatten(final int[]... arrays) {
		int totalLength = 0;
		for (final int[] array : arrays) {
			totalLength += array.length;
		}
		final int[] flattenedArray = new int[totalLength];
		int start = 0;
		for (final int[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	public static long[] flatten(final long[]... arrays) {
		int totalLength = 0;
		for (final long[] array : arrays) {
			totalLength += array.length;
		}
		final long[] flattenedArray = new long[totalLength];
		int start = 0;
		for (final long[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	public static float[] flatten(final float[]... arrays) {
		int totalLength = 0;
		for (final float[] array : arrays) {
			totalLength += array.length;
		}
		final float[] flattenedArray = new float[totalLength];
		int start = 0;
		for (final float[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	public static double[] flatten(final double[]... arrays) {
		int totalLength = 0;
		for (final double[] array : arrays) {
			totalLength += array.length;
		}
		final double[] flattenedArray = new double[totalLength];
		int start = 0;
		for (final double[] array : arrays) {
			System.arraycopy(array, 0, flattenedArray, start, array.length);
			start += array.length;
		}
		return flattenedArray;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Minimum methods

	public static byte minimum(final byte... values) {
		byte minimum = Byte.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
			}
		}
		return minimum;
	}

	public static short minimum(final short... values) {
		short minimum = Short.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
			}
		}
		return minimum;
	}

	public static int minimum(final int... values) {
		int minimum = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
			}
		}
		return minimum;
	}

	public static long minimum(final long... values) {
		long minimum = Long.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
			}
		}
		return minimum;
	}

	public static int minimumIndex(final byte... values) {
		int index = 0;
		byte minimum = Byte.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int minimumIndex(final short... values) {
		int index = 0;
		short minimum = Short.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int minimumIndex(final int... values) {
		int index = 0;
		int minimum = Integer.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int minimumIndex(final long... values) {
		int index = 0;
		long minimum = Long.MAX_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] < minimum) {
				minimum = values[i];
				index = i;
			}
		}
		return index;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Maximum methods

	public static byte maximum(final byte... values) {
		byte maximum = Byte.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
			}
		}
		return maximum;
	}

	public static short maximum(final short... values) {
		short maximum = Short.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
			}
		}
		return maximum;
	}

	public static int maximum(final int... values) {
		int maximum = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
			}
		}
		return maximum;
	}

	public static long maximum(final long... values) {
		long maximum = Long.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
			}
		}
		return maximum;
	}

	public static int maximumIndex(final byte... values) {
		int index = 0;
		byte maximum = Byte.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int maximumIndex(final short... values) {
		int index = 0;
		short maximum = Short.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int maximumIndex(final int... values) {
		int index = 0;
		int maximum = Integer.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
				index = i;
			}
		}
		return index;
	}

	public static int maximumIndex(final long... values) {
		int index = 0;
		long maximum = Long.MIN_VALUE;
		for (int i = 0; i < values.length; ++i) {
			if (values[i] > maximum) {
				maximum = values[i];
				index = i;
			}
		}
		return index;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Sum methods

	public static long sum(final byte... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	public static long sum(final short... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	public static long sum(final int... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	public static long sum(final long... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	public static double sum(final float... values) {
		double sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	public static double sum(final double... values) {
		double sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum += values[i];
		}
		return sum;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Sum methods

	public static long product(final byte... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	public static long product(final short... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	public static long product(final int... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	public static long product(final long... values) {
		long sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	public static double product(final float... values) {
		double sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	public static double product(final double... values) {
		double sum = 0;
		for (int i = 0; i < values.length; ++i) {
			sum *= values[i];
		}
		return sum;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Average methods

	public static double mean(final byte... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	public static double mean(final short... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	public static double mean(final int... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	public static double mean(final long... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	public static double mean(final float... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	public static double mean(final double... values) {
		return ((double) IMath.sum(values)) / ((double) values.length);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Average absolute deviation methods

	public static double absoluteDeviation(final byte value, final byte... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	public static double absoluteDeviation(final short value, final short... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	public static double absoluteDeviation(final int value, final int... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	public static double absoluteDeviation(final long value, final long... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	public static double absoluteDeviation(final float value, final float... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	public static double absoluteDeviation(final double value, final double... values) {
		final double[] deviations = new double[values.length];
		for (int i = 0; i < values.length; ++i) {
			deviations[i] = IMath.absolute(((double) values[i]) - value);
		}
		return IMath.mean(deviations);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Variance methods

	public static double variance(final byte... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	public static double variance(final short... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	public static double variance(final int... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	public static double variance(final long... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	public static double variance(final float... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	public static double variance(final double... values) {
		final double mean = IMath.mean(values);
		double sum = 0.0d;
		for (int i = 0; i < values.length; ++i) {
			final double square = ((double) values[i]) - mean;
			sum += square * square;
		}
		return sum / values.length;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Standard deviation methods

	public static double standardDeviation(final byte... values) {
		return Math.sqrt(IMath.variance(values));
	}

	public static double standardDeviation(final short... values) {
		return Math.sqrt(IMath.variance(values));
	}

	public static double standardDeviation(final int... values) {
		return Math.sqrt(IMath.variance(values));
	}

	public static double standardDeviation(final long... values) {
		return Math.sqrt(IMath.variance(values));
	}

	public static double standardDeviation(final float... values) {
		return Math.sqrt(IMath.variance(values));
	}

	public static double standardDeviation(final double... values) {
		return Math.sqrt(IMath.variance(values));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Quantile methods

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final byte... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final short... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final int... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final long... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final float... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

	/**
	 * Divides a sorted distribution into a number of quantiles.
	 *
	 * @param distribution
	 *                         A sorted distribution.
	 * @param quantiles
	 *                         A number of quantiles.
	 * @return An array with a lenght of quantiles - 1 showing the values that
	 *         separate each adjacent pair of quantiles.
	 */
	public static double[] quantiles(final int quantiles, final double... values) {
		final double portion = (double) (values.length) / (double) (quantiles);
		double iteration = portion;
		final double[] result = new double[quantiles - 1];
		for (int i = 0; i < result.length; ++i) {
			final int index = (int) (iteration);
			result[i] = values[index];
			iteration += portion;
		}
		return result;
	}

}
