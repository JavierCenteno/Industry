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

package exe.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * This class handles graphics.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class IPanel extends JPanel {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 *
	 */
	private static final long serialVersionUID = 103030817126573410L;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public IPanel() {
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		final Graphics2D graphics2D = (Graphics2D) graphics;

		/*
		 * TODO
		 *
		 * tectonic plate, aside from having a Tile[x][y] array, should have an int[x +
		 * 1][y + 1] array representing the exact height of the corners of each tile
		 *
		 * the height of a tile should be a double resulting from the average of each 4
		 * corners
		 */

		// TODO: In order to draw an image, use the method:
		// graphics.drawImage(img, x, y, null);
	}

}
