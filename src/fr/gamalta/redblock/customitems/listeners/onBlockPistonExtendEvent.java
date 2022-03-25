package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockMoveEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;

public class onBlockPistonExtendEvent implements Listener {
	
	private BlockManager blockManager;
	
	public onBlockPistonExtendEvent(CustomItems main) {
		blockManager = main.blockManager;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPistonExtend(BlockPistonExtendEvent event) {

		boolean finded = false;

		for (Block block : event.getBlocks()) {
			
			if (block.getType() == Material.RED_MUSHROOM_BLOCK || block.getType() == Material.BROWN_MUSHROOM_BLOCK || block.getType() == Material.MUSHROOM_STEM) {
				
				CustomBlockMoveEvent customBlockMoveEvent = new CustomBlockMoveEvent(event, block, block.getBlockData(), event.getDirection());
				Bukkit.getPluginManager().callEvent(customBlockMoveEvent);
				
				if (!customBlockMoveEvent.isCancelled()) {
					
					MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();
					
					for (BlockManagerMap blockManagerMap : blockManager.customBlocks.values()) {
						
						for (BlockFacing blockFacing : blockManagerMap.getBlockFacings().values()) {
							
							if (blockFacing.getFaces().equals(multiFacing.getFaces())) {
								
								blockManagerMap.getBaseBlock().onBlockMove(customBlockMoveEvent);
								
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
}