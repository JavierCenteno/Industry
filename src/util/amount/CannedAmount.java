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

import type.Resource;

/**
 * This class represents an amount that is canned and therefore does not decay.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class CannedAmount extends Amount {

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a new amount of the specified resource and quantity 0.
	 *
	 * @throws IllegalArgumentException
	 *                                      When resource is null.
	 */
	public CannedAmount(final Resource resource) {
		super(resource);
	}

	/**
	 * Creates a new amount of the specified resource and the specified quantity.
	 *
	 * @throws IllegalArgumentException
	 *                                      When resource is null or quantity is
	 *                                      negative.
	 */
	public CannedAmount(final Resource resource, final int quantity) {
		super(resource, quantity);
	}

	/**
	 * Creates a new canned amount based on the amount passed as an argument.
	 */
	public CannedAmount(final Amount amount) {
		super(amount.resource, amount.quantity);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	@Override
	public void tick() {
		// Canned resources do not perish, so do nothing
	}

}
