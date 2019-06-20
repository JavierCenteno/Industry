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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class offers methods of managing with a console.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class Console {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	private static final BufferedReader INPUT_READER = new BufferedReader(new InputStreamReader(System.in));

	private static boolean RUNNING = true;

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	public static void main(final String[] args) {
		Console.output("");
		Console.output("    |     | |  \\  | | |    \\   | |   | |  /    | |       | |    \\   \\ \\  / /");
		Console.output("      | |   | \\ \\ | | | | \\ \\  | |   | | | |        | |    | | \\ \\   \\ \\/ /");
		Console.output("      | |   | |\\ \\| | | |  | | | |   | |  \\   \\     | |    | | / /    \\  /");
		Console.output("      | |   | | \\ \\ | | | / /  | |   | |     | |    | |    |     \\    / /");
		Console.output("    |     | | |  \\  | |    /    \\     /  |    /     | |    | |  \\ \\  / /");
		Console.output("");
		Console.output("--------------------------------------------------------------------------------");
		Console.output(
				Industry.VERSION + "                                                                                "
						.substring(Industry.VERSION.length() + Industry.COPYRIGHT.length()) + Industry.COPYRIGHT);
		Console.output("");
		try {
			while (Console.RUNNING) {
				Console.input();
			}
		} catch (final Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}
	}

	public static void output(final String s) {
		System.out.println(s);
	}

	public static void input() throws IOException {
		String line;
		line = Console.INPUT_READER.readLine();
		// TODO
		Console.output(line);
	}

	public static void stop() {
		Console.RUNNING = false;
	}

}
