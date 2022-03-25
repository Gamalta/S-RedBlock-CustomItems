package fr.gamalta.redblock.customitems.api;

import org.bukkit.inventory.ItemStack;

import fr.gamalta.lib.item.RedItem;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.utils.Utils;

public class CustomItemAPI {

	private static CustomItems main = CustomItems.getInstance();
	private static Utils utils = new Utils(main);

	public static RedItem getRedItemById(String id) {

		return main.itemManager.redItems.get(id.toLowerCase());
	}

	public static boolean isCustomItem(ItemStack item) {

		return utils.isCustomItem(item);
	}

	public static String getType(ItemStack item) {

		return utils.getType(item);
	}
}
