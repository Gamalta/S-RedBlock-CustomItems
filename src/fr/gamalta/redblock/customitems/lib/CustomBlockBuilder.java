package fr.gamalta.redblock.customitems.lib;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

public class CustomBlockBuilder {

	public CustomBlockBuilder() {

	}

	public static class BlockFacing {

		private Boolean up = true;
		private Boolean down = true;
		private Boolean north = true;
		private Boolean south = true;
		private Boolean east = true;
		private Boolean west = true;

		public BlockFacing(ConfigurationSection section) {

			if (section.contains("Up")) {

				up = section.getBoolean("Up");
			}

			if (section.contains("Down")) {

				down = section.getBoolean("Down");
			}

			if (section.contains("North")) {

				north = section.getBoolean("North");
			}

			if (section.contains("South")) {

				south = section.getBoolean("South");
			}

			if (section.contains("East")) {

				east = section.getBoolean("East");
			}

			if (section.contains("West")) {

				west = section.getBoolean("West");
			}
		}

		public BlockFacing(String string) {

			char[] chars = string.toCharArray();
			up = chars[0] == '1';
			down = chars[1] == '1';
			north = chars[2] == '1';
			south = chars[3] == '1';
			west = chars[4] == '1';
			east = chars[5] == '1';

		}

		public BlockFacing(Boolean up, Boolean down, Boolean north, Boolean south, Boolean west, Boolean east) {

			up = new Boolean(up);
			down = new Boolean(down);
			north = new Boolean(north);
			south = new Boolean(south);
			west = new Boolean(west);
			east = new Boolean(east);
		}

		@Override
		public boolean equals(Object object) {

			if (this == object) {
				return true;
			}
			if (object instanceof BlockFacing) {

				BlockFacing anotherCustomBlockFacing = (BlockFacing) object;

				return anotherCustomBlockFacing.toString().equals(toString());
			}

			return false;
		}

		@Override
		public String toString() {

			String string = "";
			string += up ? 1 : 0;
			string += down ? 1 : 0;
			string += north ? 1 : 0;
			string += south ? 1 : 0;
			string += west ? 1 : 0;
			string += east ? 1 : 0;

			return string;
		}

		public Set<BlockFace> getFaces() {

			Set<BlockFace> blockFaces = new HashSet<>();

			if (north) {

				blockFaces.add(BlockFace.NORTH);
			}

			if (east) {
				blockFaces.add(BlockFace.EAST);
			}

			if (south) {
				blockFaces.add(BlockFace.SOUTH);
			}

			if (west) {
				blockFaces.add(BlockFace.WEST);
			}

			if (up) {
				blockFaces.add(BlockFace.UP);
			}

			if (down) {
				blockFaces.add(BlockFace.DOWN);
			}

			return blockFaces;
		}

		public Boolean getUp() {
			return up;
		}

		public void setUp(Boolean up) {
			this.up = up;
		}

		public Boolean getDown() {
			return down;
		}

		public void setDown(Boolean down) {
			this.down = down;
		}

		public Boolean getNorth() {
			return north;
		}

		public void setNorth(Boolean north) {
			this.north = north;
		}

		public Boolean getSouth() {
			return south;
		}

		public void setSouth(Boolean south) {
			this.south = south;
		}

		public Boolean getEast() {
			return east;
		}

		public void setEast(Boolean east) {
			this.east = east;
		}

		public Boolean getWest() {
			return west;
		}

		public void setWest(Boolean west) {
			this.west = west;
		}

		@Override
		public BlockFacing clone() {
			return new BlockFacing(up, down, north, south, west, east);
		}
	}
}