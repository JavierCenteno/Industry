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

package util.collection;

import java.util.AbstractList;
import java.util.Collection;

/**
 * This class represents a list with a given capacity that can hold a fixed and
 * limited number of elements.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class LimitedCapacityArrayList<E> extends AbstractList<E> {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Elements this list contains.
	 */
	private Object[] elements;
	/**
	 * Number of elements in this list.
	 */
	private int size;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Constructs an empty LimitedCapacityArrayList with a given capacity.
	 */
	public LimitedCapacityArrayList(final int capacity) {
		this.elements = new Object[capacity];
		this.size = 0;
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements of a given array.
	 */
	public LimitedCapacityArrayList(final E[] elements) {
		this(elements, elements.length);
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements of a given array and
	 * a given capacity.
	 */
	public LimitedCapacityArrayList(final E[] elements, final int capacity) {
		this.elements = new Object[capacity];
		System.arraycopy(elements, 0, this.elements, 0, elements.length);
		this.size = 0;
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements and capacity of a
	 * given LimitedCapacityArrayList.
	 */
	public LimitedCapacityArrayList(final LimitedCapacityArrayList<E> elements) {
		this.elements = new Object[elements.capacity()];
		System.arraycopy(elements.elements, 0, this.elements, 0, elements.size());
		this.size = elements.size();
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements of a given
	 * LimitedCapacityArrayList and a given capacity.
	 */
	public LimitedCapacityArrayList(final LimitedCapacityArrayList<E> elements, final int capacity) {
		this.elements = new Object[capacity];
		if (capacity >= elements.size()) {
			System.arraycopy(elements.elements, 0, this.elements, 0, elements.size());
		} else {
			System.arraycopy(elements.elements, 0, this.elements, 0, capacity);
		}
		this.size = elements.size();
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements of a given
	 * collection.
	 */
	public LimitedCapacityArrayList(final Collection<E> elements) {
		this.elements = elements.toArray();
		this.size = elements.size();
	}

	/**
	 * Constructs a LimitedCapacityArrayList with the elements of a given collection
	 * and a given capacity.
	 */
	public LimitedCapacityArrayList(final Collection<E> elements, final int capacity) {
		if (capacity >= elements.size()) {
			this.elements = elements.toArray(new Object[capacity]);
			this.size = elements.size();
		} else if (capacity < elements.size()) {
			this.elements = new Object[capacity];
			System.arraycopy(elements.toArray(), 0, this.elements, 0, this.elements.length);
			this.size = this.elements.length;
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the number of elements this list contains.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Get the number of elements this list can contain.
	 */
	public int capacity() {
		return this.elements.length;
	}

	/**
	 * Get an array that contains all the elements this list contains.
	 */
	@Override
	public Object[] toArray() {
		final Object[] trimmedElements = new Object[this.size()];
		System.arraycopy(this.elements, 0, trimmedElements, 0, trimmedElements.length);
		return trimmedElements;
	}

	/**
	 * Get the element at the given index.
	 *
	 * @throws IndexOutOfBoundsException
	 *                                       If the given index is out of bounds.
	 */
	@Override
	public E get(final int index) {
		if ((index < 0) || (index >= this.size())) {
			throw new IndexOutOfBoundsException("LimitedCapacityArrayList: Index out of bounds.");
		}
		@SuppressWarnings("unchecked")
		final E element = (E) this.elements[index];
		return element;
	}

	/**
	 * Set the element at the given index to the given element.
	 *
	 * @throws IndexOutOfBoundsException
	 *                                       If the given index is out of bounds.
	 */
	@Override
	public E set(final int index, final E element) {
		if ((index < 0) || (index >= this.size())) {
			throw new IndexOutOfBoundsException("LimitedCapacityArrayList: Index out of bounds.");
		}
		@SuppressWarnings("unchecked")
		final E oldElement = (E) this.elements[index];
		this.elements[index] = element;
		return oldElement;
	}

	/**
	 * Insert the given element into the list.
	 *
	 * @throws IndexOutOfBoundsException
	 *                                       If the list is full.
	 */
	@Override
	public boolean add(final E element) {
		if (this.size() < this.capacity()) {
			this.elements[this.size] = element;
			++this.size;
		} else {
			throw new IndexOutOfBoundsException("LimitedCapacityArrayList: Capacity exceeded.");
		}
		return true;
	}

	/**
	 * Insert the given element into the list at the given index.
	 *
	 * @throws IndexOutOfBoundsException
	 *                                       If the list is full or the given index
	 *                                       is out of bounds.
	 */
	@Override
	public void add(final int index, final E element) {
		if (this.size() < this.capacity()) {
			System.arraycopy(this.elements, index, this.elements, index + 1, this.size - index);
			this.elements[index] = element;
			++this.size;
		} else {
			throw new IndexOutOfBoundsException("LimitedCapacityArrayList: Capacity exceeded.");
		}
	}

	/**
	 * Remove the element at the given index from the list.
	 *
	 * @throws IndexOutOfBoundsException
	 *                                       If the given index is out of bounds.
	 */
	@Override
	public E remove(final int index) {
		if ((index < 0) || (index >= this.size())) {
			throw new IndexOutOfBoundsException("List: Index out of bounds.");
		}
		@SuppressWarnings("unchecked")
		final E oldElement = (E) this.elements[index];
		System.arraycopy(this.elements, index + 1, this.elements, index, this.size - index - 1);
		--this.size;
		return oldElement;
	}

}
