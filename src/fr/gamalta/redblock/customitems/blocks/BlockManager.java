package fr.gamalta.redblock.customitems.blocks;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.blocks.containers.Drawer;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;
import fr.gamalta.redblock.customitems.utils.DataDrawer;

public class BlockManager {

	private CustomItems main;
	private String parentFileName;
	public HashMap<String, BlockManagerMap> customBlocks;
	public HashMap<Location, DataDrawer> drawers;
	public Configuration blocksCFG;
	public Drawer drawer;

	public BlockManager(CustomItems main) {

		this.main = main;
		drawer = new Drawer(main, this);
	}

	public void init() {

		parentFileName = main.getParentFileName() + "/Blocks";

		customBlocks = new HashMap<>();
		drawers = new HashMap<>();

		drawer.init();
	}

	public void disable() {

		drawer.disable();

	}

	public String getParentFileName() {
		return parentFileName;
	}

	public static class BlockManagerMap {

		private CustomBlock baseBlock;
		private HashMap<BlockFace, BlockFacing> blockFacings;
		private CustomItemBuilder customItemBuilder;
		private ItemStack item;

		public BlockManagerMap(CustomBlock baseBlock, HashMap<BlockFace, BlockFacing> blockFacings, CustomItemBuilder customItemBuilder, ItemStack item) {

			this.baseBlock = baseBlock;
			this.blockFacings = blockFacings;
			this.customItemBuilder = customItemBuilder;
			this.item = item;

		}

		public CustomBlock getBaseBlock() {
			return baseBlock;
		}

		public void setBaseBlock(CustomBlock baseBlock) {
			this.baseBlock = baseBlock;
		}

		public CustomItemBuilder getCustomItemBuilder() {
			return customItemBuilder;
		}

		public void setCustomItemBuilder(CustomItemBuilder customItemBuilder) {
			this.customItemBuilder = customItemBuilder;
		}

		public ItemStack getItem() {
			return item;
		}

		public void setItem(ItemStack item) {
			this.item = item;
		}

		public HashMap<BlockFace, BlockFacing> getBlockFacings() {
			return blockFacings;
		}

		public void setBlockFacings(HashMap<BlockFace, BlockFacing> blockFacings) {
			this.blockFacings = blockFacings;
		}
	}
}