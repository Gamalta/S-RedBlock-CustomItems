package fr.gamalta.redblock.customitems.utils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.Gson;

import fr.gamalta.redblock.customitems.CustomItems;

public class Utils {
	
	private CustomItems main;
	
	public Utils(CustomItems main) {
		this.main = main;
	}
	
	public void sendRessourcePackRequest(Player player, String url) {
		
		Bukkit.getScheduler().runTaskLater(main, () -> {
			
			if (player.isValid()) {
				
				player.setResourcePack(url);
				
			} else {
				
				sendRessourcePackRequest(player, url);
			}
			
		}, 20L);
	}
	
	public boolean hasAvailableSlot(Player player, ItemStack item, int amount) {
		
		int available = 0;
		int emptyslot = 0;
		int maxStackSize = item.getType().getMaxStackSize();
		
		if (player != null && amount > 0) {
			
			for (ItemStack itemStack : player.getInventory().getStorageContents()) {
				
				if (itemStack == null) {
					emptyslot++;
					
				} else if (itemStack.isSimilar(item)) {
					
					available += 64 - itemStack.getAmount();
				}
			}
			
			if (item.getType().getMaxDurability() > 0) {
				
				return emptyslot >= amount;
				
			} else {
				
				return emptyslot * maxStackSize + Math.min(available, maxStackSize) >= amount;
				
			}
		} else {
			
			return false;
		}
	}
	
	public String getType(ItemStack item) {
		
		if (isCustomItem(item)) {
			
			return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING);
			
		} else {
			
			return item == null ? null : item.getType().toString().toLowerCase();
			
		}
	}
	
	public BlockFace getDirection(Player player) {
		
		double rotation = player.getLocation().getYaw() % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (rotation >= 315 && rotation < 45) {
			
			return BlockFace.NORTH;
			
		} else if (rotation >= 45 && rotation < 135) {
			
			return BlockFace.EAST;
			
		} else if (rotation >= 135 && rotation < 225) {
			
			return BlockFace.SOUTH;
			
		} else if (rotation >= 225 && rotation < 315) {
			
			return BlockFace.WEST;
			
		} else {
			return BlockFace.NORTH;
		}
	}
	
	public boolean isCustomItem(ItemStack item) {
		
		return hasCustomAttribue(item, CustomAttribute.UNLOCALIZED_NAME);
	}
	
	public boolean hasCustomAttribue(ItemStack item, CustomAttribute attribute) {
		
		if (item != null) {
			
			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
			
			return persistentDataContainer == null ? false : persistentDataContainer.has(attribute.getKey(), attribute.getType());
		}
		return false;
	}
	
	public Object getCustomAttribue(ItemStack item, CustomAttribute attribute) {
		
		if (item != null) {
			
			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
			
			return persistentDataContainer.get(attribute.getKey(), attribute.getType());
		}
		
		return null;
	}
	
	public void setCustomAttribue(ItemStack item, CustomAttribute attribute, Object object) {
		
		if (item != null) {
			
			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
			
			switch (attribute) {
				
				case UNLOCALIZED_NAME:
				case BLOCK_FACING:
				case BLOCK_MATERIAL:
					persistentDataContainer.set(attribute.getKey(), PersistentDataType.STRING, (String) object);
					break;
				case FUEL_CONSUMPTION:
				case FUEL_CAPACITY:
				case FUEL_LEVEL:
					persistentDataContainer.set(attribute.getKey(), PersistentDataType.DOUBLE, (Double) object);
					break;
				case DURABILITY:
				case MAX_DURABILITY:
					persistentDataContainer.set(attribute.getKey(), PersistentDataType.INTEGER, (Integer) object);
					break;
				
			}
		}
	}
	
	public enum CustomAttribute {
		
		UNLOCALIZED_NAME(new NamespacedKey(CustomItems.getInstance(), "name"), PersistentDataType.STRING), FUEL_CAPACITY(new NamespacedKey(CustomItems.getInstance(), "fuelcapacity"), PersistentDataType.DOUBLE), FUEL_LEVEL(new NamespacedKey(CustomItems.getInstance(), "fuellevel"), PersistentDataType.DOUBLE), DURABILITY(new NamespacedKey(CustomItems.getInstance(), "durability"), PersistentDataType.INTEGER), MAX_DURABILITY(new NamespacedKey(CustomItems.getInstance(), "maxdurability"), PersistentDataType.INTEGER), FUEL_CONSUMPTION(new NamespacedKey(CustomItems.getInstance(), "consumption"), PersistentDataType.DOUBLE), BLOCK_FACING(new NamespacedKey(CustomItems.getInstance(), "blockfacing"), PersistentDataType.STRING), BLOCK_MATERIAL(new NamespacedKey(CustomItems.getInstance(), "blocktype"), PersistentDataType.STRING);
		
		NamespacedKey key;
		PersistentDataType<?, ?> type;
		
		CustomAttribute(NamespacedKey key, PersistentDataType<?, ?> type) {
			
			this.key = key;
			this.type = type;
		}
		
		public NamespacedKey getKey() {
			return key;
		}
		
		public PersistentDataType<?, ?> getType() {
			return type;
		}
	}
	
	public String serializeItemStack(ItemStack item) {
		
		if (item != null) {
			
			Gson gson = new Gson();
			return gson.toJson(item.serialize());
		}
		
		return "null";
	}
	
	@SuppressWarnings("unchecked")
	public ItemStack deserializeItemStack(String json) {

		if (!json.equals("null")) {

			Gson gson = new Gson();
			Map<String, Object> serialdata = gson.fromJson(json, Map.class);
			return ItemStack.deserialize(serialdata);
		}
		return null;
	}
}
