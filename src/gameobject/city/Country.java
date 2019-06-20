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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import gameobject.GameObject;
import gameobject.world.World;
import typeobject.Technology;

/**
 * This class represents a country.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Country extends GameObject {

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Name of this country.
	 */
	private final String name;
	/**
	 * This country's current capital.
	 */
	private City capital;
	/**
	 * List of cities controlled by this country.
	 */
	private final List<City> cities;
	/**
	 * List of technologies this country has access to.
	 */
	private final List<Technology> technologies;

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public Country(final World world, final String name) {
		super(world);
		this.name = name;
		this.cities = new ArrayList<City>();
		this.technologies = new ArrayList<Technology>();
		this.technologies.add(Technology.getTechnology("workshop"));
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get this country's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get this country's capital.
	 */
	public City getCapital() {
		return this.capital;
	}

	/**
	 * Get the list of cities controlled by this country.
	 */
	public List<City> getCities() {
		return this.cities;
	}

	/**
	 * Get the list of technologies this country has access to.
	 */
	public List<Technology> getTechnologies() {
		return this.technologies;
	}

	public List<Technology> getTechnologiesAvailableForResearch() {
		final Set<Technology> followUps = new HashSet<Technology>();
		final List<Technology> technologiesAvailableForResearch = new ArrayList<Technology>();
		for (final Technology technology : this.technologies) {
			for (final Technology followUp : technology.getFollowups()) {
				if (!(this.hasTechnology(followUp) || followUps.contains(followUp))) {
					followUps.add(followUp);
				}
			}
		}
		for (final Technology followUp : followUps) {
			if (this.technologies.containsAll(Arrays.asList(followUp.getRequirements()))) {
				technologiesAvailableForResearch.add(followUp);
			}
		}
		return technologiesAvailableForResearch;
	}

	/**
	 * Checks if this country has access to a certain technology.
	 */
	public boolean hasTechnology(final Technology technology) {
		return this.technologies.contains(technology);
	}

	/**
	 * Adds a technology to this country's technology list. If there are any
	 * required technologies missing, adds them as well.
	 *
	 * Adding the technology "theory of everything" adds every technology this
	 * country doesn't have.
	 */
	public void addTechnology(final Technology technology) {
		final List<Technology> technologiesToBeAdded = new LinkedList<Technology>();
		technologiesToBeAdded.add(technology);
		while (technologiesToBeAdded.size() > 0) {
			final Technology current = technologiesToBeAdded.remove(0);
			for (final Technology requirement : current.getRequirements()) {
				if (!(this.hasTechnology(requirement) || technologiesToBeAdded.contains(requirement))) {
					technologiesToBeAdded.add(requirement);
				}
			}
			this.technologies.add(current);
		}
	}

}
