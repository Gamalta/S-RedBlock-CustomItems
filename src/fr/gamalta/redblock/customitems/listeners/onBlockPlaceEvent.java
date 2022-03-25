package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockPlaceEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;
import fr.gamalta.redblock.customitems.utils.Utils;
import fr.gamalta.redblock.customitems.utils.Utils.CustomAttribute;

public class onBlockPlaceEvent implements Listener {
	
	private BlockManager blockManager;
	private Utils utils;
	
	public onBlockPlaceEvent(CustomItems main) {
		
		blockManager = main.blockManager;
		utils = new Utils(main);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		
		boolean finded = false;
		Block block = event.getBlock();
		ItemStack item = event.getItemInHand();
		
		if (utils.isCustomItem(item) && utils.hasCustomAttribue(item, CustomAttribute.BLOCK_MATERIAL)) {
			
			CustomBlockPlaceEvent customBlockPlaceEvent = new CustomBlockPlaceEvent(event, event.getPlayer(), block, item);
			Bukkit.getPluginManager().callEvent(customBlockPlaceEvent);
			
			if (!customBlockPlaceEvent.isCancelled()) {
				
				BlockFacing blockFacing = new BlockFacing((String) utils.getCustomAttribue(item, CustomAttribute.BLOCK_FACING));
				
				for (BlockManagerMap blockManagerMap : blockManager.customBlocks.values()) {
					
					for (BlockFacing blockFacing1 : blockManagerMap.getBlockFacings().values()) {
						
						if (blockFacing1.getFaces().equals(blockFacing.getFaces())) {
							
							blockManagerMap.getBaseBlock().onBlockPlaceEvent(customBlockPlaceEvent, blockFacing);
							
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