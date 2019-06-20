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

package util;

import java.lang.reflect.Array;

/**
 * This class contains array utility methods.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class ArrayUtil {

	/**
	 * Create a copy of the given array.
	 *
	 * @param array
	 *                  An array.
	 * @return A copy of the given array.
	 */
	public static <E> E[] copy(final E[] array) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length);
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	/**
	 * Create a copy of the given array with a new length. If the length provided is
	 * shorter than the original array's, elements at the end will be deleted. Any
	 * element in the resulting copy will have the same index it had in the original
	 * array.
	 *
	 * @param array
	 *                   An array.
	 * @param length
	 *                   The copy's length.
	 * @return A copy of the given array with a new length.
	 */
	public static <E> E[] copy(final E[] array, final int length) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array.getClass().getComponentType(), length);
		System.arraycopy(array, 0, newArray, 0, length < array.length ? length : array.length);
		return newArray;
	}

	/**
	 * Returns the index of the first element that matches the given element
	 * following the criteria established by its equals method. Returns -1 if no
	 * matching elements are found.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to find in the given array.
	 * @return The index of the first element that matches the given element or -1
	 *         if no matching elements are found.
	 */
	public static <E> int firstIndexOf(final E[] array, final E element) {
		int index = -1;
		for (int i = 0; i < array.length; ++i) {
			if (array[i].equals(element)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Returns the index of the last element that matches the given element
	 * following the criteria established by its equals method. Returns -1 if no
	 * matching elements are found.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to find in the given array.
	 * @return The index of the last element that matches the given element or -1 if
	 *         no matching elements are found.
	 */
	public static <E> int lastIndexOf(final E[] array, final E element) {
		int index = -1;
		for (int i = array.length - 1; i >= 0; --i) {
			if (array[i].equals(element)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Returns whether the given array contains the given element following the
	 * criteria established by its equals method.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to find in the given array.
	 * @return Whether the given array contains the given element.
	 */
	public static <E> boolean contains(final E[] array, final E element) {
		return ArrayUtil.firstIndexOf(array, element) >= 0;
	}

	/**
	 * Create a new array based on the given array with the given element added at
	 * the end.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to add to the end of that array.
	 * @return The new array with the given element added at the end.
	 */
	public static <E> E[] add(final E[] array, final E element) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = element;
		return newArray;
	}

	/**
	 * Create a new array based on the given array with the given element added at
	 * the given index.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to add at the given index.
	 * @param index
	 *                    The index of the new element in the returned array.
	 * @return The new array with the given element added at the given index.
	 */
	public static <E> E[] addAt(final E[] array, final E element, final int index) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		System.arraycopy(array, 0, newArray, 0, index);
		newArray[index] = element;
		System.arraycopy(array, index, newArray, index + 1, array.length - index);
		return newArray;
	}

	/**
	 * Create a new array with the first element that matches the given element
	 * removed following the criteria established by its equals method. It is
	 * recommended to use this method over the remove(array, element) method
	 * whenever it is known it would yield the same result.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to remove from the given array.
	 * @return The new array with the first element that matches the given element
	 *         removed.
	 */
	public static <E> E[] removeFirst(final E[] array, final E element) {
		final int index = ArrayUtil.firstIndexOf(array, element);
		if (index >= 0) {
			return ArrayUtil.removeAt(array, index);
		} else {
			return array;
		}
	}

	/**
	 * Create a new array with the last element that matches the given element
	 * removed following the criteria established by its equals method. It is
	 * recommended to use this method over the remove(array, element) method
	 * whenever it is known it would yield the same result.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to remove from the given array.
	 * @return The new array with the last element that matches the given element
	 *         removed.
	 */
	public static <E> E[] removeLast(final E[] array, final E element) {
		final int index = ArrayUtil.lastIndexOf(array, element);
		if (index >= 0) {
			return ArrayUtil.removeAt(array, index);
		} else {
			return array;
		}
	}

	/**
	 * Creates a new array with the element at the given index removed.
	 *
	 * @param array
	 *                  An array.
	 * @param index
	 *                  The index of the element to be removed.
	 * @return A new array with the element at the given index removed.
	 */
	public static <E> E[] removeAt(final E[] array, final int index) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
		System.arraycopy(array, 0, newArray, 0, index);
		System.arraycopy(array, index + 1, newArray, index, newArray.length - index);
		return newArray;
	}

	/**
	 * Create a new array with all the elements that match the given element removed
	 * following the criteria established by its equals method.
	 *
	 * @param array
	 *                    An array.
	 * @param element
	 *                    An element to remove from the given array.
	 * @return The new array with all the elements that match the given element
	 *         removed.
	 */
	public static <E> E[] remove(final E[] array1, final E element) {
		@SuppressWarnings("unchecked")
		final E[] elementsThatAreNotElement = (E[]) Array.newInstance(array1.getClass().getComponentType(),
				array1.length);
		int counter = 0;
		for (int i = 0; i < array1.length; ++i) {
			if (!array1[i].equals(element)) {
				elementsThatAreNotElement[counter] = array1[i];
				++counter;
			}
		}
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array1.getClass().getComponentType(), counter);
		System.arraycopy(elementsThatAreNotElement, 0, newArray, 0, counter);
		return newArray;
	}

	/**
	 * Combines the given arrays into one.
	 *
	 * @param array1
	 *                   An array.
	 * @param array2
	 *                   An array.
	 * @return The two arrays combined into one
	 */
	public static <E> E[] combine(final E[] array1, final E[] array2) {
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array1.getClass().getComponentType(),
				array1.length + array2.length);
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length, array2.length);
		return newArray;
	}

	/**
	 * Creates a new array with all the elements of array1 except the elements in
	 * array2.
	 *
	 * @param array1
	 *                   An array.
	 * @param array2
	 *                   An array.
	 * @return A new array with all the elements of array1 except the elements in
	 *         array2.
	 */
	public static <E> E[] subtract(final E[] array1, final E[] array2) {
		@SuppressWarnings("unchecked")
		final E[] elementsNotInArray2 = (E[]) Array.newInstance(array1.getClass().getComponentType(), array1.length);
		int counter = 0;
		for (int i = 0; i < array1.length; ++i) {
			if (!ArrayUtil.contains(array2, array1[i])) {
				elementsNotInArray2[counter] = array1[i];
				++counter;
			}
		}
		@SuppressWarnings("unchecked")
		final E[] newArray = (E[]) Array.newInstance(array1.getClass().getComponentType(), counter);
		System.arraycopy(elementsNotInArray2, 0, newArray, 0, counter);
		return newArray;
	}

}
