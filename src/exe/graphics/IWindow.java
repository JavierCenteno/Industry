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

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import exe.Industry;
import exe.io.FileHandler;

/**
 * This class handles graphics.
 *
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class IWindow extends JFrame {

	////////////////////////////////////////////////////////////////////////////////
	// Class fields

	/**
	 *
	 */
	private static final long serialVersionUID = 6767259032222608458L;

	private static final int ABSOLUTE_MINIMUM_SIZE_X, ABSOLUTE_MINIMUM_SIZE_Y;
	private static final int ABSOLUTE_MAXIMUM_SIZE_X, ABSOLUTE_MAXIMUM_SIZE_Y;

	////////////////////////////////////////////////////////////////////////////////
	// Instance fields

	////////////////////////////////////////////////////////////////////////////////
	// Class initializer

	static {
		// TODO: put actual value here
		ABSOLUTE_MINIMUM_SIZE_X = 512;
		// TODO: put actual value here
		ABSOLUTE_MINIMUM_SIZE_Y = 512;
		// TODO: put actual value here
		ABSOLUTE_MAXIMUM_SIZE_X = 1024;
		// TODO: put actual value here
		ABSOLUTE_MAXIMUM_SIZE_Y = 1024;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Instance initializers

	public IWindow() {
		super("Industry " + Industry.VERSION);
		// TODO: load from settings
		final int defaultWidth = 0;
		// TODO: load from settings
		final int defaultHeight = 0;
		final BufferedImage logo = FileHandler.loadImage(FileHandler.GRAPHIC_PATH, "industry", "industry.logo.64.png");
		this.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
		this.setMinimumSize(new Dimension(IWindow.ABSOLUTE_MINIMUM_SIZE_X, IWindow.ABSOLUTE_MINIMUM_SIZE_Y));
		this.setMaximumSize(new Dimension(IWindow.ABSOLUTE_MAXIMUM_SIZE_X, IWindow.ABSOLUTE_MAXIMUM_SIZE_Y));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setIconImage(logo);
		this.setContentPane(new IPanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	public static void main(final String[] args) {
		new IWindow();
	}

}
