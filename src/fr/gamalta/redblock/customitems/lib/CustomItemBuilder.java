package fr.gamalta.redblock.customitems.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.lib.item.RedItem;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;
import fr.gamalta.redblock.customitems.utils.Utils.CustomAttribute;

public class CustomItemBuilder extends RedItem {

	private JavaPlugin main;
	private FileConfiguration config;
	private String path;
	private String unlocalizedName;
	private int MaxCustomDurability = -1;
	private int CustomDurability = -1;
	private double fuelCapacity = 0D;
	private double fuelLevel = 0D;
	private double fuelConsumption = 0D;
	private int customModelData = -1;
	private Material customBlock = Material.AIR;
	private BlockFacing blockFacing;

	public CustomItemBuilder(JavaPlugin main, Configuration config, String path) {

		this(main, config.getConfig(), path);
	}

	public CustomItemBuilder(JavaPlugin main, FileConfiguration config, String path) {

		super(main, config, path);
		this.main = main;
		this.config = config;
		this.path = path;
		load();
	}

	public CustomItemBuilder(JavaPlugin main, FileConfiguration config, String path, String name, int amount, int durability, String owningPlayer, Material material, List<String> lores, List<ItemFlag> itemFlags, HashMap<Enchantment, Integer> enchantments, Boolean isUnbreakable, Boolean isStackable, String unlocalizedName, int MaxCustomDurability, int CustomDurability, double fuelCapacity, double fuelLevel, double fuelConsumption, int customModelData, Material customBlock, BlockFacing blockFacing) {

		super(main, config, path, name, amount, durability, owningPlayer, material, lores, itemFlags, enchantments, isUnbreakable, isStackable);
		this.main = main;
		this.config = config;
		this.path = path;
		this.unlocalizedName = unlocalizedName;
		this.MaxCustomDurability = MaxCustomDurability;
		this.CustomDurability = CustomDurability;
		this.fuelCapacity = fuelCapacity;
		this.fuelLevel = fuelLevel;
		this.fuelConsumption = fuelConsumption;
		this.customModelData = customModelData;
		this.customBlock = customBlock;
		this.blockFacing = blockFacing;

	}

	public void load() {

		ConfigurationSection section = config.getConfigurationSection(path);

		if (section != null) {

			unlocalizedName = section.getName();

			if (section.contains("CustomModelData")) {

				customModelData = section.getInt("CustomModelData");
			}

			if (section.contains("Block")) {

				customBlock = Material.getMaterial(section.getString("Block"));
			}

			if (section.contains("Facing")) {

				blockFacing = new BlockFacing(config.getConfigurationSection(path + ".Facing"));
			}

			if (section.contains("MaxCustomDurability")) {

				MaxCustomDurability = section.getInt("MaxCustomDurability");

			}

			if (section.contains("CustomDurability")) {

				CustomDurability = section.getInt("CustomDurability");

			}

			if (section.contains("FuelCapacity")) {

				fuelCapacity = section.getDouble("FuelCapacity");

			}

			if (section.contains("FuelLevel")) {

				fuelLevel = section.getDouble("FuelLevel");

			}

			if (section.contains("FuelConsumption")) {

				fuelConsumption = section.getDouble("FuelConsumption");

			}
		}
	}

	public CustomItemBuilder setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		return this;
	}

	@Override
	public ItemStack create() {

		ItemStack itemStack = super.create();
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (customModelData != -1) {

			itemMeta.setCustomModelData(customModelData);

		}

		if (unlocalizedName != null) {

			itemMeta.getPersistentDataContainer().set(CustomAttribute.UNLOCALIZED_NAME.getKey(), PersistentDataType.STRING, unlocalizedName);

		}

		if (!customBlock.equals(Material.AIR)) {

			itemMeta.getPersistentDataContainer().set(CustomAttribute.BLOCK_MATERIAL.getKey(), PersistentDataType.STRING, customBlock + "");
			itemMeta.getPersistentDataContainer().set(CustomAttribute.BLOCK_FACING.getKey(), PersistentDataType.STRING, blockFacing.toString());

		}

		if (MaxCustomDurability != -1) {

			itemMeta.getPersistentDataContainer().set(CustomAttribute.MAX_DURABILITY.getKey(), PersistentDataType.INTEGER, MaxCustomDurability);

			if (CustomDurability != -1) {

				itemMeta.getPersistentDataContainer().set(CustomAttribute.DURABILITY.getKey(), PersistentDataType.INTEGER, CustomDurability);

			}
		}

		if (fuelCapacity != -1) {

			itemMeta.getPersistentDataContainer().set(CustomAttribute.FUEL_CAPACITY.getKey(), PersistentDataType.DOUBLE, fuelCapacity);

			if (fuelLevel != -1) {

				itemMeta.getPersistentDataContainer().set(CustomAttribute.FUEL_LEVEL.getKey(), PersistentDataType.DOUBLE, fuelLevel);

			}

			if (fuelConsumption != -1) {

				itemMeta.getPersistentDataContainer().set(CustomAttribute.FUEL_CONSUMPTION.getKey(), PersistentDataType.DOUBLE, fuelConsumption);

			}
		}

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	@Override
	public CustomItemBuilder clone() {

		return new CustomItemBuilder(main, config, path, super.getName() == null ? null : new String(super.getName()), new Integer(super.getAmount()), new Integer(super.getDurability()), super.getOwningPlayer() == null ? null : new String(super.getOwningPlayer()), Material.getMaterial(super.getMaterial().name()), new ArrayList<String>(super.getLores()), new ArrayList<ItemFlag>(super.getItemFlags()), new HashMap<Enchantment, Integer>(super.getEnchantments()), new Boolean(super.isUnbreakable()), new Boolean(super.isStackable()), unlocalizedName == null ? null : new String(unlocalizedName), new Integer(MaxCustomDurability), new Integer(CustomDurability), new Double(fuelCapacity), new Double(fuelLevel), new Double(fuelConsumption), new Integer(customModelData), Material.getMaterial(customBlock.name()), blockFacing == null ? null : blockFacing.clone());
	}
}