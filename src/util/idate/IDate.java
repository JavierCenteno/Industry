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

package util.idate;

import api.Json;
import exe.Industry;
import exe.io.Internationalized;

/**
 * This class represents a date composed by a month and a year.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class IDate implements Cloneable, Comparable<IDate> {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 * Name of date.
	 */
	@Internationalized
	public static final String NAME;
	/**
	 * Structure date names follow.
	 */
	@Internationalized
	private static final String VALUE;
	/**
	 * List of names of the months.
	 */
	@Internationalized
	public static final String[] MONTHS;
	/**
	 * Value of a month.
	 */
	public static final int JANUARY = 0, FEBRUARY = 1, MARCH = 2, APRIL = 3, MAY = 4, JUNE = 5, JULY = 6, AUGUST = 7,
			SEPTEMBER = 8, OCTOBER = 9, NOVEMBER = 10, DECEMBER = 11;
	/**
	 * Name of season.
	 */
	@Internationalized
	public static final String SEASON;
	/**
	 * List of names of the seasons as they appear in the southern hemisphere.
	 */
	@Internationalized
	public static final String[] SOUTH_SEASONS;
	/**
	 * List of names of the seasons as they appear in the tropics.
	 */
	@Internationalized
	public static final String[] TROPIC_SEASONS;
	/**
	 * List of names of the seasons as they appear in the northern hemisphere.
	 */
	@Internationalized
	public static final String[] NORTH_SEASONS;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	/**
	 * Year this date represents.
	 */
	private short year;
	/**
	 * Month this date represents.
	 */
	private byte month;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	/**
	 * Initializes the class fields.
	 */
	static {
		final Json i18n = Industry.I18N.get("util", "iDate");
		NAME = i18n.get("name").as(String.class);
		VALUE = i18n.get("value").as(String.class);
		MONTHS = i18n.get("months").as(String[].class);
		SEASON = i18n.get("season").as(String.class);
		SOUTH_SEASONS = i18n.get("southSeasons").as(String[].class);
		TROPIC_SEASONS = i18n.get("tropicSeasons").as(String[].class);
		NORTH_SEASONS = i18n.get("northSeasons").as(String[].class);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	/**
	 * Creates a date with the given year and month.
	 */
	public IDate(final short year, final byte month) {
		this.year = year;
		this.month = month;
	}

	/**
	 * Copies a date.
	 */
	public IDate(final IDate date) {
		this.year = date.year;
		this.month = date.month;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance methods

	/**
	 * Get the year of this date.
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Get the month of this date with 0 being January and 11 being December.
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * Obtain the current season as a number in [0, 3]. Note this method doesn't
	 * depend on hemispheres, so numbers don't really map to a particular season. 0
	 * stands for summer in the southern hemisphere and winter in the northern
	 * hemisphere; 1 stands for fall in the southern hemisphere and spring in the
	 * northern hemisphere; 2 stands for summer in the southern hemisphere and
	 * winter in the northern hemisphere; 3 stands for spring in the southern
	 * hemisphere and fall in the northern hemisphere.
	 */
	public int getSeason() {
		return this.month / 3;
	}

	/**
	 * Set the year of this date.
	 */
	public void setYear(final short year) {
		this.year = year;
	}

	/**
	 * Set the month of this date. If you pass a value that is not in [0, 11], this
	 * date will be in an illegal state.
	 */
	public void setMonth(final byte month) {
		this.month = month;
	}

	/**
	 * Decrease this date by a month.
	 */
	public void decrease() {
		if (this.month == 0) {
			this.month = 11;
		} else {
			--this.month;
		}
	}

	/**
	 * Increase this date by a month.
	 */
	public void increase() {
		if (this.month == 11) {
			this.month = 0;
		} else {
			++this.month;
		}
	}

	/**
	 * Decrease this date by a given amount of months.
	 */
	public void decrease(final int months) {
		this.increase(-months);
	}

	/**
	 * Decrease this date by a given amount of years and months.
	 */
	public void decrease(final int years, final int months) {
		this.year -= years;
		this.decrease(months);
	}

	/**
	 * Increase this date by a given amount of months.
	 */
	public void increase(final int months) {
		this.month += months;
		final int years = this.month / 12;
		this.year += years;
		this.month -= years * 12;
	}

	/**
	 * Increase this date by a given amount of years and months.
	 */
	public void increase(final int years, final int months) {
		this.year += years;
		this.increase(months);
	}

	/**
	 * Get the difference between this date and the given date in months.
	 */
	public int difference(final IDate date) {
		return (12 * (this.year - date.year)) + (this.month - date.month);
	}

	/**
	 * Returns true if this date is before the given date.
	 */
	public boolean before(final IDate date) {
		if (this.year < date.year) {
			return true;
		} else if (this.year == date.year) {
			return this.month < date.month;
		}
		return false;
	}

	/**
	 * Returns true if this date is after the given date.
	 */
	public boolean after(final IDate date) {
		if (this.year > date.year) {
			return true;
		} else if (this.year == date.year) {
			return this.month > date.month;
		}
		return false;
	}

	/**
	 * Returns true if this date is the same as the given date.
	 */
	public boolean equals(final IDate date) {
		return (this.year == date.year) && (this.month == date.month);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof IDate) {
			return this.equals((IDate) object);
		}
		return false;
	}

	@Override
	public int compareTo(final IDate date) {
		if (this.equals(date)) {
			return 0;
		} else if (this.before(date)) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public IDate clone() {
		return new IDate(this);
	}

	/**
	 * Returns this date as an internationalized string.
	 */
	public String getDateString() {
		return IDate.VALUE.replace("{year}", Integer.toString(this.year)).replace("{month}", IDate.MONTHS[this.month]);
	}

	/**
	 * Returns the current season as a string.
	 *
	 * @param region
	 *                   Current region of the globe. 0 for between the tropics, a
	 *                   negative number for south of the tropics and a positive
	 *                   number for north of the tropics.
	 */
	public String getSeasonString(final int region) {
		if (region < 0) {
			return IDate.SOUTH_SEASONS[this.getSeason()];
		} else if (region > 0) {
			return IDate.NORTH_SEASONS[this.getSeason()];
		} else {
			return IDate.TROPIC_SEASONS[this.getSeason()];
		}
	}

}
