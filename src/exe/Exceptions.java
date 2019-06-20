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

package exe;

import api.Json;
import exe.io.Internationalized;

/**
 * This class offers methods for creating internationalized exceptions.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Exceptions {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 *
	 */
	@Internationalized
	private static final String LOADING_EXCEPTION_MESSAGE;
	/**
	 *
	 */
	@Internationalized
	private static final String SAVING_EXCEPTION_MESSAGE;
	/**
	 *
	 */
	@Internationalized
	private static final String CLASS_NOT_FOUND_EXCEPTION_MESSAGE;

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		final Json i18n = Industry.I18N.get("exe", "exceptions");
		LOADING_EXCEPTION_MESSAGE = i18n.get("loading").as(String.class);
		SAVING_EXCEPTION_MESSAGE = i18n.get("saving").as(String.class);
		CLASS_NOT_FOUND_EXCEPTION_MESSAGE = i18n.get("classNotFound").as(String.class);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	/**
	 *
	 *
	 * @param cause
	 * @return
	 */
	public static RuntimeException loadingException(final Throwable cause) {
		return new RuntimeException(Exceptions.LOADING_EXCEPTION_MESSAGE, cause);
	}

	/**
	 *
	 *
	 * @param cause
	 * @return
	 */
	public static RuntimeException savingException(final Throwable cause) {
		return new RuntimeException(Exceptions.SAVING_EXCEPTION_MESSAGE, cause);
	}

	/**
	 *
	 *
	 * @param cause
	 * @return
	 */
	public static RuntimeException classNotFoundException(final Throwable cause) {
		return new RuntimeException(Exceptions.CLASS_NOT_FOUND_EXCEPTION_MESSAGE, cause);
	}

}
