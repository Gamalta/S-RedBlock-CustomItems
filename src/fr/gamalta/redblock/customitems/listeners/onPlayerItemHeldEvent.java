package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.gamalta.redblock.customitems.CustomItems;

public class onPlayerItemHeldEvent implements Listener {

	private CustomItems main;

	public onPlayerItemHeldEvent(CustomItems main) {

		this.main = main;
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item != null && item.getItemMeta() != null) {

			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
			NamespacedKey key = new NamespacedKey(main, "name");

			if (persistentDataContainer.has(key, PersistentDataType.STRING)) {

				if (persistentDataContainer.get(key, PersistentDataType.STRING).contains("chainsaw")) {

					if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

						event.setCancelled(true);
						player.playSound(player.getLocation(), "custom.chainsaw", SoundCategory.PLAYERS, 1F, 1F);

					} else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

						event.setCancelled(true);
					}
				}
			}
		}
	}
}