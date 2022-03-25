package fr.gamalta.redblock.customitems.blocks;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockBreakEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockExplodeEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockInteractEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockMoveEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockPlaceEvent;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;
import fr.gamalta.redblock.customitems.utils.Utils;
import fr.gamalta.redblock.customitems.utils.Utils.CustomAttribute;

public class CustomBlock {

	public CustomItems main;
	public BlockManager blockManager;
	public Utils utils;
	public CustomItemBuilder customItemBuilder;

	public void onPlayerInteractEvent(CustomBlockInteractEvent event) {
	}

	public void onBlockPlaceEvent(CustomBlockPlaceEvent event, CustomBlockBuilder.BlockFacing customBlockFacing) {

		ItemStack item = event.getItem();
		Block block = event.getBlock();

		block.setType(Material.getMaterial((String) utils.getCustomAttribue(item, CustomAttribute.BLOCK_MATERIAL)));
		MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();
		multiFacing.setFace(BlockFace.UP, customBlockFacing.getUp());
		multiFacing.setFace(BlockFace.DOWN, customBlockFacing.getDown());
		multiFacing.setFace(BlockFace.NORTH, customBlockFacing.getNorth());
		multiFacing.setFace(BlockFace.SOUTH, customBlockFacing.getSouth());
		multiFacing.setFace(BlockFace.EAST, customBlockFacing.getEast());
		multiFacing.setFace(BlockFace.WEST, customBlockFacing.getWest());
		block.setBlockData(multiFacing);
	}

	public void onBlockBreakEvent(CustomBlockBreakEvent event) {

		event.setDropItem(null);
		event.setDropItem(getDropItem(event.getBlock()));

	}

	public void give(Player player, int amount) {

		ItemStack item = customItemBuilder.create();
		int maxStackSize = item.getType().getMaxStackSize();

		while (amount > 0) {

			if (amount > maxStackSize) {

				item.setAmount(maxStackSize);
				amount -= maxStackSize;
				player.getInventory().addItem(item);

			} else {

				item.setAmount(amount);
				amount -= amount;
				player.getInventory().addItem(item);
			}
		}
	}

	public void onBlockExplodeEvent(CustomBlockExplodeEvent event) {
		
	}

	public Collection<ItemStack> getDropItem(Block block) {

		ItemStack item = customItemBuilder.create();
		item.setAmount(1);

		return Arrays.asList(item);
	}
	
	public void onBlockMove(CustomBlockMoveEvent event) {

	}
}