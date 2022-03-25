package fr.gamalta.redblock.customitems.listeners;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockExplodeEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;

public class onEntityExplodeEvent implements Listener {
	
	private BlockManager blockManager;
	
	public onEntityExplodeEvent(CustomItems main) {
		blockManager = main.blockManager;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event) {

		boolean finded = false;

		for (Block block : event.blockList()) {

			if (block.getType() == Material.RED_MUSHROOM_BLOCK || block.getType() == Material.BROWN_MUSHROOM_BLOCK || block.getType() == Material.MUSHROOM_STEM) {

				CustomBlockExplodeEvent customBlockExplodeEvent = new CustomBlockExplodeEvent(event, block, block.getBlockData());
				Bukkit.getPluginManager().callEvent(customBlockExplodeEvent);

				if (!customBlockExplodeEvent.isCancelled()) {

					MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();

					for (BlockManagerMap blockManagerMap : blockManager.customBlocks.values()) {

						for (BlockFacing blockFacing : blockManagerMap.getBlockFacings().values()) {

							if (blockFacing.getFaces().equals(multiFacing.getFaces())) {

								Collection<ItemStack> dropItems = blockManagerMap.getBaseBlock().getDropItem(block);
								blockManagerMap.getBaseBlock().onBlockExplodeEvent(customBlockExplodeEvent);
								Location location = block.getLocation();

								for (ItemStack item : dropItems) {

									location.getWorld().dropItemNaturally(location, item);
								}
								block.setType(Material.AIR);
								
								finded = true;
								break;
							}

							if (finded) {
								break;
							}
						}
					}
				}
			}
		}
	}
}