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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

import javax.imageio.ImageIO;

import entity.world.World;
import entity.world.TectonicPlate.Tile;
import exe.io.FileHandler;
import generator.BasicGenerator;
import type.Era;
import type.TerrainShape;
import util.math.IMath;

/**
 * @author Javier Centeno Vega <jacenve@telefonica.net>
 * @version 0.1
 * @since 0.1
 *
 */
public class PrintWorld {

	////////////////////////////////////////////////////////////////////////////////
	// Class methods

	public static void main(final String[] args) throws IOException {
		FileHandler.loadClass("exe.Industry");
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Input a seed or press enter to generate a random seed.");
		BigInteger seed;
		long longSeed;
		final String seedString = bufferedReader.readLine();
		if (seedString.isEmpty()) {
			// We generate a weird seed by applying one iteration of a LCR to nanoTime
			longSeed = (System.nanoTime() * 6364136223846793005L) + 1442695040888963407L;
		} else {
			longSeed = Long.parseLong(seedString);
		}
		seed = BigInteger.valueOf(longSeed);
		System.out.println("Input a terrain shape or press enter to pick a default value.");
		TerrainShape terrainShape;
		final String terrainShapeString = bufferedReader.readLine();
		terrainShape = TerrainShape.getTerrainShape(terrainShapeString);
		if (terrainShape == null) {
			terrainShape = TerrainShape.getTerrainShape("flat");
		}
		System.out.println("Input an era or press enter to pick a default value.");
		Era era;
		final String eraString = bufferedReader.readLine();
		era = Era.getEra(eraString);
		if (era == null) {
			era = Era.getEra("1700");
		}
		System.out.println("Input a tectonic plate size exponent or press enter to pick a default value.");
		int tectonicPlateSizeExponent;
		final String tectonicPlateSizeExponentString = bufferedReader.readLine();
		if (tectonicPlateSizeExponentString.isEmpty()) {
			tectonicPlateSizeExponent = 5;
		} else {
			tectonicPlateSizeExponent = Integer.parseInt(tectonicPlateSizeExponentString);
		}
		System.out.println("Input a world size exponent or press enter to pick a default value.");
		int worldSizeExponent;
		final String worldSizeExponentString = bufferedReader.readLine();
		if (worldSizeExponentString.isEmpty()) {
			worldSizeExponent = 10;
		} else {
			worldSizeExponent = Integer.parseInt(worldSizeExponentString);
		}
		System.out.println("Input a crust thickness factor or press enter to pick a default value.");
		double crustThicknessFactor;
		final String crustThicknessFactorString = bufferedReader.readLine();
		if (crustThicknessFactorString.isEmpty()) {
			crustThicknessFactor = 1.0d;
		} else {
			crustThicknessFactor = Double.parseDouble(crustThicknessFactorString);
		}
		System.out.println("Input a sea level factor or press enter to pick a default value.");
		double seaLevelFactor;
		final String seaLevelFactorString = bufferedReader.readLine();
		if (seaLevelFactorString.isEmpty()) {
			seaLevelFactor = 1.0d;
		} else {
			seaLevelFactor = Double.parseDouble(seaLevelFactorString);
		}
		System.out.println("Input a roughness factor or press enter to pick a default value.");
		double roughnessFactor;
		final String roughnessFactorString = bufferedReader.readLine();
		if (roughnessFactorString.isEmpty()) {
			roughnessFactor = 8.0d;
		} else {
			roughnessFactor = Double.parseDouble(roughnessFactorString);
		}
		System.out.println("Input an axis tilt or press enter to pick a default value.");
		double axisTilt;
		final String axisTiltString = bufferedReader.readLine();
		if (axisTiltString.isEmpty()) {
			axisTilt = 0.15d;
		} else {
			axisTilt = Double.parseDouble(axisTiltString);
		}
		System.out.println("Input a distance to sun factor or press enter to pick a default value.");
		double distanceToSunFactor;
		final String distanceToSunFactorString = bufferedReader.readLine();
		if (distanceToSunFactorString.isEmpty()) {
			distanceToSunFactor = 1.0d;
		} else {
			distanceToSunFactor = Double.parseDouble(distanceToSunFactorString);
		}
		System.out.println("Input a playable area size exponent or press enter to pick a default value.");
		int playableSizeExponent;
		final String playableSizeExponentString = bufferedReader.readLine();
		if (playableSizeExponentString.isEmpty()) {
			playableSizeExponent = 8;
		} else {
			playableSizeExponent = Integer.parseInt(playableSizeExponentString);
		}
		final World world = new World(seed.toByteArray(), terrainShape, era, tectonicPlateSizeExponent,
				worldSizeExponent, crustThicknessFactor, seaLevelFactor, roughnessFactor, axisTilt, distanceToSunFactor,
				playableSizeExponent);
		final BasicGenerator generator = new BasicGenerator(world);
		generator.generate();
		PrintWorld.print(world, "world");
	}

	public static void print(final World world, final String fileName) {
		final BufferedImage bufferedImage = new BufferedImage(world.getPlayableSizeX(), world.getPlayableSizeY(),
				BufferedImage.TYPE_INT_ARGB);
		for (int tileY = world.getPlayableMaxY(), pixelY = 0; tileY >= world.getPlayableMinY(); --tileY, ++pixelY) {
			for (int tileX = world.getPlayableMinX(), pixelX = 0; tileX <= world.getPlayableMaxX(); ++tileX, ++pixelX) {
				int color = 0;
				final Tile tile = world.getTile(tileX, tileY);
				final int localMagma = tile.getMagma();
				final int localLand = tile.getLand();
				final int localWater = tile.getWater();
				final int localPollution = tile.getPollution();
				final int localTemperature = tile.getCurrentTemperature();
				if ((localMagma >= localLand) && (localMagma >= localWater) && (localMagma >= localPollution)) {
					color |= 0xFF << 24;// alpha
					color |= (0xFF - PrintWorld
							.toUnsignedByteWithoutOverflow(localMagma - IMath.maximum(localLand, localWater))) << 16;// red
					color |= 0x00 << 8;// green
					color |= 0x00 << 0;// blue
				} else if ((localLand >= localMagma) && (localLand >= localWater) && (localLand >= localPollution)) {
					color |= 0xFF << 24;// alpha
					color |= 0x00 << 16;// red
					color |= (0xFF - PrintWorld.toUnsignedByteWithoutOverflow(localLand)) << 8;// green
					color |= 0x00 << 0;// blue
				} else if ((localWater >= localMagma) && (localWater >= localLand) && (localWater >= localPollution)) {
					if (localTemperature < 0) {
						color |= 0xFF << 24;// alpha
						color |= 0xFF << 16;// red
						color |= 0xFF << 8;// green
						color |= 0xFF << 0;// blue
					} else {
						color |= 0xFF << 24;// alpha
						color |= 0x00 << 16;// red
						color |= 0x00 << 8;// green
						color |= (0xFF - PrintWorld
								.toUnsignedByteWithoutOverflow(localWater - IMath.maximum(localMagma, localLand))) << 0;// blue
					}
				} else if ((localPollution >= localMagma) && (localPollution >= localLand)
						&& (localPollution >= localWater)) {
					color |= 0xFF << 24;// alpha
					color |= 0x00 << 16;// red
					color |= 0x00 << 8;// green
					color |= 0x00 << 0;// blue
				}
				bufferedImage.setRGB(pixelX, pixelY, color);
			}
		}
		final File file = new File("C:\\Users\\jacen\\Desktop\\" + fileName + ".png");
		try {
			ImageIO.write(bufferedImage, "PNG", file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static int toUnsignedByteWithoutOverflow(final int x) {
		if (x > 0xFF) {
			return 0xFF;
		}
		if (x < 0) {
			return 0;
		}
		return x;
	}

}
