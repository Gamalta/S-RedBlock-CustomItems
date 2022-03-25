package fr.gamalta.redblock.customitems.blocks.containers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.api.event.CustomBlockBreakEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockExplodeEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockInteractEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockMoveEvent;
import fr.gamalta.redblock.customitems.api.event.CustomBlockPlaceEvent;
import fr.gamalta.redblock.customitems.blocks.BlockManager;
import fr.gamalta.redblock.customitems.blocks.BlockManager.BlockManagerMap;
import fr.gamalta.redblock.customitems.blocks.CustomBlock;
import fr.gamalta.redblock.customitems.lib.CustomBlockBuilder.BlockFacing;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;
import fr.gamalta.redblock.customitems.utils.DataDrawer;
import fr.gamalta.redblock.customitems.utils.Utils;
import fr.gamalta.redblock.customitems.utils.Utils.CustomAttribute;

public class Drawer extends CustomBlock {

	public static Drawer drawer;

	public static Drawer getInstance() {
		return drawer;
	}

	public HashMap<BlockFace, Location> armorStandLocation;
	public Configuration drawersCFG;
	public HashMap<BlockFace, EulerAngle> eulerAngle;

	public HashMap<BlockFace, BlockFacing> facings;

	public HashMap<Player, Long> lastInteract;

	public Drawer(CustomItems main, BlockManager blockManager) {

		this.main = main;
		this.blockManager = blockManager;
		utils = new Utils(main);
	}

	public void disable() {

		for (DataDrawer dataDrawer : blockManager.drawers.values()) {

			dataDrawer.save();
			dataDrawer.getArmorStand().remove();

		}
	}

	public void init() {

		drawer = this;
		drawersCFG = new Configuration(main, blockManager.getParentFileName(), "Drawer");
		facings = new HashMap<>();
		armorStandLocation = new HashMap<>();
		eulerAngle = new HashMap<>();
		lastInteract = new HashMap<>();

		for (String string : drawersCFG.getConfigurationSection("Drawer.Facing").getKeys(false)) {

			facings.put(BlockFace.valueOf(string), new BlockFacing(drawersCFG.getConfigurationSection("Drawer.Facing." + string + ".Block")));
		}

		for (String string : drawersCFG.getConfigurationSection("Drawer.Facing").getKeys(false)) {

			armorStandLocation.put(BlockFace.valueOf(string), new Location(Bukkit.getWorlds().get(0), drawersCFG.getDouble("Drawer.Facing." + string + ".ArmorStandLocation.x"), drawersCFG.getDouble("Drawer.Facing." + string + ".ArmorStandLocation.y"), drawersCFG.getDouble("Drawer.Facing." + string + ".ArmorStandLocation.z"), drawersCFG.getLong("Drawer.Facing." + string + ".ArmorStandLocation.pitch"), drawersCFG.getLong("Drawer.Facing." + string + ".ArmorStandLocation.yaw")));
		}

		for (String string : drawersCFG.getConfigurationSection("Drawer.Facing").getKeys(false)) {

			eulerAngle.put(BlockFace.valueOf(string), new EulerAngle(drawersCFG.getDouble("Drawer.Facing." + string + ".EulerAngle.x"), drawersCFG.getDouble("Drawer.Facing." + string + ".EulerAngle.y"), drawersCFG.getDouble("Drawer.Facing." + string + ".EulerAngle.z")));
		}

		customItemBuilder = new CustomItemBuilder(main, drawersCFG, "Drawer.Item");

		blockManager.customBlocks.put("drawer", new BlockManagerMap(this, facings, customItemBuilder, customItemBuilder.create()));

		try {

			Statement statement = CustomItems.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM `Drawers`;");

			while (resultSet.next()) {

				Location location = new Location(Bukkit.getWorld(resultSet.getString("World")), resultSet.getInt("X"), resultSet.getInt("Y"), resultSet.getInt("Z"));
				
				blockManager.drawers.put(location, new DataDrawer(location, utils.deserializeItemStack(resultSet.getString("ItemStack")), resultSet.getInt("Amount")));
			}

			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBlockBreakEvent(CustomBlockBreakEvent event) {

		Location location = event.getBlock().getLocation();
		super.onBlockBreakEvent(event);

		if (blockManager.drawers.containsKey(location)) {

			DataDrawer dataDrawer = blockManager.drawers.get(location);

			if (dataDrawer != null) {

				dataDrawer.remove();
				blockManager.drawers.remove(location);
			}
		} else {

			main.logger.warning("DataDrawner not found at " + location + " of " + event.getPlayer().getName());

		}
	}

	@Override
	public void onBlockPlaceEvent(CustomBlockPlaceEvent event, BlockFacing arg1) {

		Block block = event.getBlock();
		ItemStack item = event.getItem();

		BlockFacing customBlockFacing = facings.get(utils.getDirection(event.getPlayer()));

		block.setType(Material.getMaterial((String) utils.getCustomAttribue(item, CustomAttribute.BLOCK_MATERIAL)));
		MultipleFacing multiFacing = (MultipleFacing) block.getBlockData();
		multiFacing.setFace(BlockFace.UP, customBlockFacing.getUp());
		multiFacing.setFace(BlockFace.DOWN, customBlockFacing.getDown());
		multiFacing.setFace(BlockFace.NORTH, customBlockFacing.getNorth());
		multiFacing.setFace(BlockFace.SOUTH, customBlockFacing.getSouth());
		multiFacing.setFace(BlockFace.EAST, customBlockFacing.getEast());
		multiFacing.setFace(BlockFace.WEST, customBlockFacing.getWest());
		block.setBlockData(multiFacing);

		Location location = event.getBlock().getLocation();
		DataDrawer dataDrawer = new DataDrawer(location);

		blockManager.drawers.put(location, dataDrawer);
	}

	@Override
	public void onPlayerInteractEvent(CustomBlockInteractEvent event) {

		Action action = event.getAction();
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Boolean doubleClick = false;

		if (blockManager.drawers.containsKey(location)) {

			if (lastInteract.containsKey(player)) {

				doubleClick = true;

			} else {

				lastInteract.put(player, System.nanoTime());

				Bukkit.getScheduler().runTaskLater(main, () -> {

					lastInteract.remove(player);

				}, 15L);
			}

			DataDrawer dataDrawer = blockManager.drawers.get(location);
			ItemStack hand = player.getInventory().getItemInMainHand();

			if (action.equals(Action.RIGHT_CLICK_BLOCK)) {

				event.setSuperCancelled(true);

				if (doubleClick && dataDrawer.getItemStack() != null) {

					for (int i = 0; i < 36; i++) {

						ItemStack item = player.getInventory().getItem(i);

						if (item != null) {

							if (dataDrawer.getItemStack().isSimilar(item)) {

								dataDrawer.setAmount(dataDrawer.getAmount() + item.getAmount());
								player.getInventory().setItem(i, null);
							}
						}
					}
				} else if (hand != null && (dataDrawer.getItemStack() == null || dataDrawer.getItemStack().isSimilar(hand))) {

					dataDrawer.setAmount(dataDrawer.getAmount() + hand.getAmount());
					dataDrawer.setItemStack(hand.clone());
					player.getInventory().setItemInMainHand(null);
				}
			} else if (action.equals(Action.LEFT_CLICK_BLOCK) && dataDrawer.hasItemStack() && dataDrawer.getAmount() != 0 && !player.isSneaking()) {

				event.setSuperCancelled(true);

				int taked = Math.min(dataDrawer.getItemStack().getType().getMaxStackSize(), dataDrawer.getAmount());
				ItemStack item = dataDrawer.getItemStack().clone();
				item.setAmount(taked);

				if (utils.hasAvailableSlot(player, dataDrawer.getItemStack(), taked)) {

					player.getInventory().addItem(item);

				} else {

					player.getWorld().dropItemNaturally(player.getLocation(), item);
				}

				dataDrawer.setAmount(dataDrawer.getAmount() - taked);
			}
		}
	}
	
	@Override
	public void onBlockExplodeEvent(CustomBlockExplodeEvent event) {

		Location location = event.getBlock().getLocation();
		
		if (blockManager.drawers.containsKey(location)) {

			DataDrawer dataDrawer = blockManager.drawers.get(location);

			if (dataDrawer != null) {

				dataDrawer.remove();
				blockManager.drawers.remove(location);
			}
		} else {

			main.logger.warning("DataDrawner not found at " + location.toString());

		}
	}

	@Override
	public void onBlockMove(CustomBlockMoveEvent event) {
		
		Location oldLocation = event.getBlock().getLocation();
		Location newLocation = event.getBlock().getRelative(event.getDirection()).getLocation();
		
		if (blockManager.drawers.containsKey(oldLocation)) {

			DataDrawer dataDrawer = blockManager.drawers.get(oldLocation);

			if (dataDrawer != null) {

				Bukkit.getScheduler().runTaskLater(main, () -> dataDrawer.setLocation(newLocation), 1L);
			}
		} else {

			main.logger.warning("DataDrawner not found at " + oldLocation);

		}
	}
	
	@Override
	public Collection<ItemStack> getDropItem(Block block) {

		Collection<ItemStack> dropItems = new ArrayList<>();
		Location location = block.getLocation();
		
		if (blockManager.drawers.containsKey(location)) {

			DataDrawer dataDrawer = blockManager.drawers.get(location);
			ItemStack item = dataDrawer.getItemStack();
			int amount = dataDrawer.getAmount();

			if (dataDrawer != null) {

				if (dataDrawer.hasItemStack() && dataDrawer.getAmount() != 0) {

					int maxStackSize = item.getType().getMaxStackSize();
					while (amount > 0) {

						if (amount > maxStackSize) {

							item.setAmount(maxStackSize);
							dropItems.add(item);
							amount -= maxStackSize;

						} else {

							item.setAmount(amount);
							dropItems.add(item);
							amount -= amount;
						}
					}
				}
			}
		}

		ItemStack item = customItemBuilder.create();
		item.setAmount(1);
		dropItems.add(item);

		return dropItems;
	}
}