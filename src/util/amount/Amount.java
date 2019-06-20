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

package util.amount;

import typeobject.Resource;
import typeobject.Resource.ResourceAttribute;

/**
 * This class represents a certain quantity of a certain resource.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Amount {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Perishable resource attribute.
	 */
	private static final ResourceAttribute PERISHABLE = Resource.getResourceAttribute("perishable");

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Resource of this amount.
	 */
	Resource resource;
	/**
	 * Quantity of the resource this amount contains.
	 */
	int quantity;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new amount of the specified resource and quantity 0.
	 *
	 * @throws IllegalArgumentException
	 *                                      When resource is null.
	 */
	public Amount(final Resource resource) {
		this(resource, 0);
	}

	/**
	 * Creates a new amount of the specified resource and the specified quantity.
	 *
	 * @throws IllegalArgumentException
	 *                                      When resource is null or quantity is
	 *                                      negative.
	 */
	public Amount(final Resource resource, final int quantity) {
		if (resource == null) {
			throw new IllegalArgumentException("Amount constructor: Expected valid resource, got null instead.");
		}
		if (quantity < 0) {
			throw new IllegalArgumentException(
					"Amount constructor: Expected positive or zero quantity, got negative instead.");
		}
		this.resource = resource;
		this.quantity = quantity;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	public int getQuantity() {
		return this.quantity;
	}

	public Resource getResource() {
		return this.resource;
	}

	/**
	 * Attempts to retrieve a certain amount of resource from this Amount and
	 * returns a new Amount with the retrieved amount of the resource.
	 *
	 * @param quantity
	 *                     Quantity of the resource to be retrieved.
	 * @return A new Amount containing the amount of resource retrieved from the
	 *         current amount or the whole amount if attempted to retrieve more than
	 *         there is.
	 */
	public Amount fork(final int quantity) {
		final Amount newAmount = new Amount(this.resource);
		if (this.quantity <= quantity) {
			newAmount.quantity = this.quantity;
			this.quantity = 0;
		} else {
			this.quantity -= quantity;
			newAmount.quantity = quantity;
		}
		return newAmount;
	}

	/**
	 * Attempts to move a determined quantity of resources between two amounts until
	 * the minimum or maximum non negative values are reached on either side.
	 *
	 * @param amount
	 *                     Amount to be piled together with this one.
	 * @param quantity
	 *                     Quantity that will be attempted to be moved, the sign
	 *                     indicates the direction of the movement (positive, to
	 *                     this amount; negative, from this amount).
	 * @return True if the operation was successful, false if there was a mismatch
	 *         between resource types.
	 */
	public boolean pile(final Amount amount, final int quantity) {
		if (this.resource == amount.resource) {
			this.quantity += quantity;
			amount.quantity -= quantity;
			/*
			 * If quantity turns out to be less than 0, we move amounts back so both are non
			 * negative.
			 */
			if (this.quantity < 0) {
				if (quantity > 0) {
					/*
					 * If positive plus positive equals negative, there's been an overflow. We set
					 * the quantity to the maximum possible value of an integer, removing as many
					 * from the source amount as possible.
					 */
					amount.quantity = (amount.quantity + this.quantity) - Integer.MAX_VALUE;
					this.quantity = Integer.MAX_VALUE;
				} else {
					/*
					 * If there isn't an overflow, we just set the quantities necessary for neither
					 * to be negative.
					 */
					amount.quantity = amount.quantity + this.quantity;
					this.quantity = 0;
				}
			}
			/*
			 * If quantity turns out to be less than 0, we move amounts back so both are non
			 * negative.
			 */
			if (amount.quantity < 0) {
				if (quantity < 0) {
					/*
					 * If positive plus positive equals negative, there's been an overflow. We set
					 * the quantity to the maximum possible value of an integer, removing as many
					 * from the source amount as possible.
					 */
					this.quantity = ((this.quantity + amount.quantity) - Integer.MAX_VALUE);
					amount.quantity = Integer.MAX_VALUE;
				} else {
					/*
					 * If there isn't an overflow, we just set the quantities necessary for neither
					 * to be negative.
					 */
					this.quantity = (this.quantity + amount.quantity);
					amount.quantity = 0;
				}
			}
			return true;
		}
		return false;
	}

	public void tick() {
		// Perishable resources perish after a month
		if (this.getResource().is(Amount.PERISHABLE)) {
			this.quantity = 0;
		}
	}

}
