package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockBreakEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;

public class onBlockBreakEvent implements Listener {

	CustomItems main;
	BlockManager blockManager;

	public onBlockBreakEvent(CustomItems main) {
		this.main = main;
		blockManager = main.blockManager;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		
		Block block = event.getBlock();
		boolean finded = false;

		if (block.getType() == Material.RED_MUSHROOM_BLOCK || block.getType() == Material.BROWN_MUSHROOM_BLOCK || block.getType() == Material.MUSHROOM_STEM) {
			
			CustomBlockBreakEvent customBlockBreakEvent = new CustomBlockBreakEvent(event, event.getPlayer(), block, block.getBlockData(), block.getDrops());
			Bukkit.getPluginManager().callEvent(customBlockBreakEvent);

			if (!customBlockBreakEvent.isCancelled()) {

				MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();

				for (BlockManagerMap blockManagerMap : blockManager.customBlocks.values()) {

					for (BlockFacing blockFacing : blockManagerMap.getBlockFacings().values()) {

						if (blockFacing.getFaces().equals(multiFacing.getFaces())) {

							blockManagerMap.getBaseBlock().onBlockBreakEvent(customBlockBreakEvent);
							event.setDropItems(false);
							Location location = event.getBlock().getLocation();

							for (ItemStack item : customBlockBreakEvent.getDropItem()) {

								location.getWorld().dropItemNaturally(location, item);
							}
							finded = true;
							break;
						}
					}
					if (finded) {
						break;
					}
				}
			}
		}
	}
}