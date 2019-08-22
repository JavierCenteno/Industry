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

package type;

/**
 * This class abstracts the idea of types. The instances of this class are
 * inherent to this software itself and are loaded from memory whenever this
 * software starts.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public abstract class Type {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * String identifying the id of this type.
	 */
	protected final int id;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new type.
	 */
	public Type(final int id) {
		this.id = id;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(final Object object) {
		if (object.getClass().equals(this.getClass())) {
			return ((Type) object).getId() == this.getId();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "#" + this.getId();
	}

	/**
	 * Get the id of this type.
	 */
	public final long getId() {
		return this.id;
	}

}
