package fr.gamalta.redblock.customitems.items.tools;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.items.CustomItem;
import fr.gamalta.redblock.customitems.items.ItemManager;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;

public class ChainSaw extends CustomItem {

	CustomItems main;
	ItemManager itemManager;
	public Configuration config;

	public ChainSaw(CustomItems main, ItemManager itemManager) {
		this.main = main;
		this.itemManager = itemManager;

		config = new Configuration(main, itemManager.itemsFileName, "ChainSaw");
	}

	public void init() {

		if (config.getBoolean("Available")) {

			for (String string : config.getConfigurationSection("ChainSaw").getKeys(false)) {

				String unlocalizedName = string.toLowerCase();
				String baseKey = "ChainSaw." + string;

				if (string.equalsIgnoreCase("Default")) {

					String defaultName = "chainsaw";

					itemManager.items.put(defaultName, new CustomItemBuilder(main, config, baseKey).setUnlocalizedName(defaultName).create());

				} else if (config.getBoolean(baseKey + ".Available")) {

					if (config.contains(baseKey + ".Damaged") && config.contains(baseKey + ".Chain") && config.contains(baseKey + ".Chain.Damaged")) {

						String chainsawName = unlocalizedName + "_chainsaw";
						String damagedChainsawName = "damaged_" + chainsawName;

						CustomItemBuilder chainSaw = new CustomItemBuilder(main, config, baseKey).setUnlocalizedName(chainsawName);
						CustomItemBuilder damagedChainSaw = new CustomItemBuilder(main, config, baseKey + ".Damaged").setUnlocalizedName(damagedChainsawName);

						itemManager.items.put(chainsawName, chainSaw.create());
						itemManager.items.put(damagedChainsawName, damagedChainSaw.create());

					}
				}
			}
		}
	}
}
