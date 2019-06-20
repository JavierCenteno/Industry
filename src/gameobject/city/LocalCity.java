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

package gameobject.city;

import java.util.ArrayList;
import java.util.Arrays;

import gameobject.element.building.Government;
import gameobject.world.World;
import typeobject.Policy;
import typeobject.Technology;
import util.math.IMath;

/**
 * This class represents a city which is present in a terrain and may be
 * governed by a player.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class LocalCity extends City {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Government building of this city.
	 */
	protected Government government;
	/**
	 * Technologies this city has access to.
	 */
	protected ArrayList<Technology> technologies;
	/**
	 * Active policies in this city.
	 */
	protected ArrayList<Policy> policies;
	/**
	 * Population of this city.
	 */
	protected ArrayList<Citizen> population;
	/*
	 * TODO: Cities can issue and sell bonds. Cities need to pay interests for each
	 * bond sold until it is bought back. Other cities may decide to buy bonds
	 * depending on economic profits and growth of the issuer.
	 */
	/*
	 * TODO: When generating the initial population, we use a pareto distribution.
	 * Generate pareto double with shape between 0.2 and 0.5 and scale equal to 1.0;
	 * Substract 1 from the number generated; Then apply the chance of someone dying
	 * of old age given the age.
	 */

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public LocalCity(final World world, final String name, final Country country) {
		super(world, name, country);
		// This list is predicted to grow a lot, so we start it off with a lot of space.
		this.population = new ArrayList<Citizen>(10000);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Calculates the statistics of this city.
	 */
	public void statistics() {
		final int populationSize = this.population.size();
		final double popuationSizeFloat = (double) populationSize;
		// Declare distributions of parameters
		final int[] ageDistribution = new int[populationSize];
		final int[] wealthDistribution = new int[populationSize];
		// Populate the distributions
		for (int i = 0; i < populationSize; ++i) {
			final Citizen citizen = this.population.get(i);
			/*
			 * TODO: Add citizen parameters to the arrays.
			 *
			 * ageDistribution[i] = citizen.getAge(); wealthDistribution[i] =
			 * citizen.getWealth();
			 */
		}
		// Sort the distributions
		Arrays.sort(ageDistribution);
		Arrays.sort(wealthDistribution);
		// Get wealth statistics
		final double wealthTotal = (double) IMath.sum(wealthDistribution);
		final double wealthMean = wealthTotal / popuationSizeFloat;
		final double[] wealthDeciles = IMath.quantiles(10, wealthDistribution);
		// Get age statistics
		final double ageTotal = (double) IMath.sum(ageDistribution);
		final double ageMean = ageTotal / popuationSizeFloat;
		final double[] ageMedian = IMath.quantiles(2, ageDistribution);
	}

	public Government getGovernment() {
		return this.government;
	}

	public void setGovernment(final Government government) {
		this.government = government;
	}

}
