package fr.gamalta.redblock.customitems.listeners;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.utils.Utils;

public class onPrepareItemCraftEvent implements Listener {

	CustomItems main;
	Utils utils;

	public onPrepareItemCraftEvent(CustomItems main) {
		this.main = main;
		utils = new Utils(main);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCraftItem(PrepareItemCraftEvent event) {

		CraftingInventory matrix = event.getInventory();
		HashMap<String, Integer> items = new HashMap<>();

		for (ItemStack itemStack : matrix.getMatrix()) {

			if (itemStack != null) {

				ItemMeta itemMeta = itemStack.getItemMeta();

				if (itemMeta.hasCustomModelData()) {
					event.getInventory().setResult(null);
					break;
				}
			}
		}

		for (int i = 0; i < 9; i++) {

			ItemStack item = matrix.getItem(i);

			if (item != null) {

				items.put(utils.getType(item), i);
			}
		}

		if (items.size() == 2) {

			if (items.containsKey("fuel")) {

				int FuelSlot = items.get("fuel");
				int ChainsawSlot;

				if (items.containsKey("chainsaw")) {

					ChainsawSlot = items.get("chainsaw");

				} else if (items.containsKey("unstable_chainsaw")) {

					ChainsawSlot = items.get("unstable_chainsaw");

				} else if (items.containsKey("diamond_chainsaw")) {

					ChainsawSlot = items.get("diamond_chainsaw");

				} else if (items.containsKey("iron_chainsaw")) {

					ChainsawSlot = items.get("iron_chainsaw");

				} else {

					// juste pour retirer le warning
					ChainsawSlot = FuelSlot;
					FuelSlot += ChainsawSlot;

				}
			}
		}
	}
}
