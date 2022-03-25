package fr.gamalta.redblock.customitems.items;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.lib.item.RedItem;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.items.tools.ChainSaw;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;

public class ItemManager {

	CustomItems main;
	// public HashMap<String, CustomItemBuilder> items;
	public HashMap<String, ItemStack> items;
	public HashMap<String, RedItem> redItems;
	public String itemsFileName;
	public Configuration itemsCFG;
	private ChainSaw chainsaw;

	public ItemManager(CustomItems main) {
		this.main = main;

		items = new HashMap<>();
		redItems = new HashMap<>();
		itemsFileName = main.parentFileName + "/Items";
		itemsCFG = new Configuration(main, itemsFileName, "Items");

		chainsaw = new ChainSaw(main, this);
	}

	public void init() {

		chainsaw.init();

		for (String key : itemsCFG.getConfigurationSection("CustomItems").getKeys(false)) {

			if (itemsCFG.getBoolean("CustomItems." + key + ".Available")) {

				RedItem redItem = new CustomItemBuilder(main, itemsCFG, "CustomItems." + key);

				redItems.put(key.toLowerCase(), redItem);
				items.put(key.toLowerCase(), redItem.create());

			}
		}
	}
}
