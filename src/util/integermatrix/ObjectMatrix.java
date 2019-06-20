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
 * This interface offers methods to interact with a data structure that
 * represents a two dimensional grid of objects.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public interface ObjectMatrix<T> {

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Obtain the size of the matrix across the x axis.
	 */
	public int sizeX();

	/**
	 * Obtain the size of the matrix across the y axis.
	 */
	public int sizeY();

	/**
	 * Gets an object from the indices x, y of a grid.
	 *
	 * @param x
	 *              x index of an integer in a grid.
	 * @param y
	 *              y index of an integer in a grid.
	 * @return Object at the indices x, y of a grid.
	 */
	public T get(int x, int y);

	/**
	 * Puts an object at the indices x, y of a grid.
	 *
	 * @param x
	 *              x index of an integer in a grid.
	 * @param y
	 *              y index of an integer in a grid.
	 * @param i
	 *              Object to be put at the indices x, y of a grid.
	 */
	public void set(int x, int y, T t);

}
