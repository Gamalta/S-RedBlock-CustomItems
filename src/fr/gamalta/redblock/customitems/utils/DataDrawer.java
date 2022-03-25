package fr.gamalta.redblock.customitems.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.blocks.containers.Drawer;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;

public class DataDrawer {

	private int amount;
	private ArmorStand armorStand;
	private Drawer drawer;
	private ItemStack itemStack;
	private Location location;
	private Utils utils;

	public DataDrawer(Location location) {

		this(location, null, 0);
	}

	public DataDrawer(Location location, ItemStack itemStack, int amount) {

		utils = new Utils(CustomItems.getInstance());
		drawer = CustomItems.getInstance().blockManager.drawer;
		this.location = location;

		if (hasItemStack()) {
			itemStack.setAmount(1);
		}

		this.itemStack = itemStack;
		this.amount = amount;

		update();

	}

	public int getAmount() {
		return amount;
	}

	public ArmorStand getArmorStand() {
		return armorStand;

	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Location getLocation() {
		return location;
	}

	public boolean hasItemStack() {

		return itemStack != null;
	}

	public void remove() {
		
		if (armorStand != null) {
			armorStand.remove();
		}

		String world = location.getWorld().getName();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		Statement statement = null;

		try {
			
			statement = CustomItems.getConnection().createStatement();
			statement.executeUpdate("DELETE FROM `Drawers` WHERE `World`='" + world + "' AND `X`='" + x + "' AND `Y`='" + y + "' AND `Z`='" + z + "';");

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public void save() {

		String world = location.getWorld().getName();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		Statement statement = null;
		ResultSet resultSet = null;

		try {

			statement = CustomItems.getConnection().createStatement();

			resultSet = statement.executeQuery("SELECT * FROM `Drawers` WHERE `World`='" + world + "' AND `X`=" + x + " AND `Y`=" + y + " AND `Z`=" + z + ";");

			if (resultSet.next()) {

				String request = "UPDATE `Drawers` SET `ItemStack`='" + (itemStack == null ? "null" : utils.serializeItemStack(itemStack)) + "', `Amount`='" + amount + "' WHERE `id`='" + resultSet.getInt("id") + "';";
				statement.executeUpdate(request);

			} else {

				statement.executeUpdate("INSERT INTO `Drawers` (`World`, `X`, `Y`, `Z`, `ItemStack`, `Amount`) VALUES ('" + world + "', '" + x + "', '" + y + "', '" + z + "', " + (itemStack == null && amount != 0 ? "`null`, 0" : "'" + utils.serializeItemStack(itemStack) + "', '" + amount + "'") + ");");

			}

			statement.close();
			resultSet.close();

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

	public void setAmount(int amount) {

		if (amount == 0) {
			setItemStack(null);
		}

		this.amount = amount;

		update();
	}

	public void setItemStack(ItemStack itemStack) {

		if (itemStack != null) {

			itemStack.setAmount(1);
		}

		this.itemStack = itemStack;

		update();
	}

	public void setLocation(Location location) {

		this.location = location;
		update();
	}

	public void update() {
		
		Location armorLocation = location;
		EulerAngle eulerAngle = null;
		Block block = location.getBlock();

		if (block.getType() == Material.RED_MUSHROOM_BLOCK || block.getType() == Material.BROWN_MUSHROOM_BLOCK || block.getType() == Material.MUSHROOM_STEM) {
			
			MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();
			
			for (Entry<BlockFace, BlockFacing> entry : drawer.facings.entrySet()) {
				
				if (entry.getValue().getFaces().equals(multiFacing.getFaces())) {
					
					armorLocation = drawer.armorStandLocation.get(entry.getKey());
					eulerAngle = drawer.eulerAngle.get(entry.getKey());
				}
			}
			
			if (armorStand == null) {
				armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(armorLocation), EntityType.ARMOR_STAND);
			} else {
				armorStand.teleport(location.clone().add(armorLocation));
			}

			armorStand.setHeadPose(eulerAngle);
			armorStand.setCollidable(false);
			armorStand.setInvulnerable(true);
			armorStand.setVisible(false);
			armorStand.setSmall(true);
			armorStand.setGravity(false);

			if (itemStack != null && amount != 0) {
				
				armorStand.getEquipment().setHelmet(itemStack);
				armorStand.setCustomNameVisible(true);
				armorStand.setCustomName(amount + "");
				
			} else {
				
				armorStand.getEquipment().setHelmet(null);
				armorStand.setCustomNameVisible(false);
				armorStand.setCustomName("");
			}
		}
	}
}