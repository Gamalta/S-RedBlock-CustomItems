package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import fr.gamalta.redblock.customitems.CustomItems;

public class onBlockPhysicsEvent implements Listener {

	public onBlockPhysicsEvent(CustomItems main) {

	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {

		Material type = event.getBlock().getType();

		if (type.equals(Material.RED_MUSHROOM_BLOCK) || type.equals(Material.BROWN_MUSHROOM_BLOCK) || type.equals(Material.MUSHROOM_STEM)) {

			event.setCancelled(true);
		}
	}
}