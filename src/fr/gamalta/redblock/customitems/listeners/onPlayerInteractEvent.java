package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockInteractEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;
import fr.gamalta.redblock.customitems.utils.Utils;

public class onPlayerInteractEvent implements Listener {
	
	CustomItems main;
	BlockManager blockManager;
	Utils utils;
	
	public onPlayerInteractEvent(CustomItems main) {
		
		this.main = main;
		blockManager = main.blockManager;
		utils = new Utils(main);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		boolean finded = false;
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK)) {
			
			if (block.getType() == Material.RED_MUSHROOM_BLOCK || block.getType() == Material.BROWN_MUSHROOM_BLOCK || block.getType() == Material.MUSHROOM_STEM) {
				
				CustomBlockInteractEvent customBlockInteractEvent = new CustomBlockInteractEvent(event, player, action, block.getBlockData(), event.getHand(), event.getClickedBlock());
				Bukkit.getPluginManager().callEvent(customBlockInteractEvent);
				
				if (!customBlockInteractEvent.isCancelled()) {
					
					MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();
					
					for (BlockManagerMap blockManagerMap : blockManager.customBlocks.values()) {
						
						for (BlockFacing blockFacing : blockManagerMap.getBlockFacings().values()) {
							
							if (blockFacing.getFaces().equals(multiFacing.getFaces())) {
								
								blockManagerMap.getBaseBlock().onPlayerInteractEvent(customBlockInteractEvent);
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