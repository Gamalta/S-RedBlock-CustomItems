package fr.gamalta.redblock.customitems.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.lib.CustomItemBuilder;
import fr.gamalta.redblock.customitems.utils.Utils;

public class CustomItemsCmd implements CommandExecutor, TabCompleter {

	private CustomItems main;
	private Utils utils;

	public CustomItemsCmd(CustomItems main) {

		this.main = main;
		utils = new Utils(main);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender.hasPermission(main.settingsCFG.getString("CustomItems.Permission"))) {

			if (args.length > 0) {

				switch (args[0].toLowerCase()) {

				case "dev":

					((Player) sender).getInventory().addItem(main.blockManager.customBlocks.get("drawer").getItem());
					break;

				case "resourcepack":

					if (args.length > 1) {

						switch (args[1].toLowerCase()) {

						case "enable":

							main.settingsCFG.set("CustomItems.ResourcePack.Enabled", true);
							sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.ResourcePack.Enable").create());
							break;

						case "disable":

							main.settingsCFG.set("CustomItems.ResourcePack.Enabled", false);
							sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.ResourcePack.Disable").create());
							break;

						case "set":

							if (args.length > 2) {

								main.settingsCFG.set("CustomItems.ResourcePack.Link", args[2]);
								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.ResourcePack.Set.Successful").replace("%link%", args[2]).create());

							} else {

								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.ResourcePack.Set.Usage").create());

							}
							break;

						default:

							sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.ResourcePack.Usage").create());
							break;
						}
					} else {

						Message message = new Message(main.messagesCFG, "CustomItems.ResourcePack.Information");
						message.replace("%Link%", main.settingsCFG.getString("CustomItems.ResourcePack.Link"));
						message.replace("%Enabled%", String.valueOf(main.settingsCFG.getBoolean("CustomItems.ResourcePack.Enabled")));
						sender.spigot().sendMessage(message.create());
					}
					break;

				case "give":

					if (args.length > 1) {

						if (args[1].equalsIgnoreCase("-s")) {
							if (args.length > 2) {

								Player target = Bukkit.getPlayer(args[2]);

								if (target != null) {
									if (args.length > 3) {

										if (main.itemManager.items.containsKey(args[3].toLowerCase())) {

											int amount = 1;
											int durability = -1;

											if (args.length > 4) {

												try {
													amount = Integer.parseInt(args[4]);
												} catch (Exception e) {

													sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
													return true;
												}

												if (args.length > 5) {
													try {

														durability = Integer.parseInt(args[5]);
													} catch (Exception e) {

														sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
														return true;
													}
												}
											}

											if (amount > 0) {

												ItemStack item = main.itemManager.items.get(args[3].toLowerCase());
												String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : args[3];

												if (utils.hasAvailableSlot(target, item, amount)) {

													giveCustomItem(target, item, amount, durability);

												} else {
													sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.InventoryFull").replace("%item%", itemName).create());
												}
											} else {
												sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

											}
										} else {

											sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.ItemNotFound").replace("%item%", args[3]).create());

										}
									} else {

										sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

									}
								} else {
									if (sender instanceof ConsoleCommandSender) {

										sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

									} else {
										if (main.itemManager.items.containsKey(args[2].toLowerCase())) {

											int amount = 1;
											int durability = -1;

											if (args.length > 3) {

												try {
													amount = Integer.parseInt(args[3]);
												} catch (Exception e) {

													sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
													return true;
												}

												if (args.length > 4) {
													try {

														durability = Integer.parseInt(args[4]);
													} catch (Exception e) {

														sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
														return true;
													}
												}
											}
											Player player = (Player) sender;

											ItemStack item = main.itemManager.items.get(args[3].toLowerCase());
											String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : args[3];

											if (utils.hasAvailableSlot(player, item, amount)) {

												giveCustomItem(player, item, amount, durability);

											} else {

												sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.InventoryFull").replace("%item%", itemName).create());

											}
										} else {

											sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

										}
									}
								}
							} else {

								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

							}
						} else {

							Player target = Bukkit.getPlayer(args[1]);

							if (target != null) {
								if (args.length > 2 && main.itemManager.items.containsKey(args[2])) {

									int amount = 1;
									int durability = -1;

									if (args.length > 3) {

										try {
											amount = Integer.parseInt(args[3]);
										} catch (Exception e) {

											sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
											return true;

										}

										if (args.length > 4) {
											try {

												durability = Integer.parseInt(args[4]);
											} catch (Exception e) {

												sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
												return true;
											}
										}
									}

									ItemStack item = main.itemManager.items.get(args[2].toLowerCase());
									String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : args[2];

									if (utils.hasAvailableSlot(target, item, amount)) {

										giveCustomItem(target, item, amount, durability);
										sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Gived.Player").replace("%item%", itemName).replace("%player%", target.getName()).replace("%amout%", amount + "").create());
										target.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Gived.Received").replace("%item%", itemName).replace("%player%", target.getName()).replace("%amout%", amount + "").create());

									} else {

										sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.InventoryFull").replace("%item%", itemName).create());
									}
								} else {

									sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

								}
							} else if (main.itemManager.items.containsKey(args[1].toLowerCase())) {

								int amount = 1;
								int durability = -1;

								if (args.length > 2) {

									try {
										amount = Integer.parseInt(args[2]);
									} catch (Exception e) {

										sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
										return true;
									}

									if (args.length > 3) {
										try {

											durability = Integer.parseInt(args[3]);
										} catch (Exception e) {

											sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
											return true;
										}
									}
								}

								Player player = (Player) sender;

								ItemStack item = main.itemManager.items.get(args[1].toLowerCase());
								String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : args[1];

								if (utils.hasAvailableSlot(player, item, amount)) {

									giveCustomItem(player, item, amount, durability);
									sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Gived.Alone").replace("%item%", itemName).create());

								} else {
									sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.InventoryFull").replace("%item%", itemName).create());

								}

							} else {

								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());

							}
						}
					} else {

						sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Give.Usage").create());
					}
					break;

				case "enable":

					if (args.length > 1) {

						if (main.itemManager.itemsCFG.contains("CustomItems." + args[1].toLowerCase())) {

							if (!main.itemManager.items.containsKey(args[1].toLowerCase())) {

								main.itemManager.items.put(args[1].toLowerCase(), new CustomItemBuilder(main, main.itemManager.itemsCFG, "CustomItems." + args[1]).create());
								main.itemManager.itemsCFG.set("CustomItems." + args[1].toLowerCase() + ".Available", true);
								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Enable.Enable").replace("%item%", args[1]).create());

							} else {

								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Enable.AlreadyEnable").create());

							}
						} else {
							sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Enable.ItemNotFound").create());
						}
					} else {

						sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Enable.Usage").create());

					}
					break;
				case "disable":

					if (args.length > 1) {

						if (main.itemManager.itemsCFG.contains("CustomItems." + args[1].toLowerCase())) {

							if (main.itemManager.items.containsKey(args[1].toLowerCase())) {

								main.itemManager.items.remove(args[1].toLowerCase());
								main.itemManager.itemsCFG.set("CustomItems." + args[1].toLowerCase() + ".Available", false);
								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Disable.Disable").replace("%item%", args[1]).create());

							} else {

								sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Disable.AlreadyDisable").create());

							}
						} else {
							sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Disable.ItemNotFound").create());
						}
					} else {

						sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Disable.Usage").create());

					}
					break;

				case "reload":

					main.settingsCFG.load();
					main.messagesCFG.load();

					main.blockManager.disable();
					main.blockManager.init();

					main.itemManager.items.clear();
					main.itemManager.init();
					main.itemManager.itemsCFG.load();

					sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Reload").create());
					break;

				default:

					sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Usage").create());
					break;

				}
			} else {

				sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.Usage").create());

			}
		} else {

			sender.spigot().sendMessage(new Message(main.messagesCFG, "CustomItems.NoPermission").create());
		}

		return true;
	}

	private void giveCustomItem(Player player, ItemStack itemStack, int amount, int durability) {

		ItemStack item = itemStack.clone();
		ItemMeta itemMeta = item.getItemMeta();

		if (itemMeta instanceof Damageable) {

			if (durability != -1) {

				((Damageable) itemMeta).setDamage(item.getType().getMaxDurability() - durability);
				item.setItemMeta(itemMeta);
			}

			for (int i = 0; i < amount; i++) {

				player.getInventory().addItem(item);
			}

		} else {

			item.setItemMeta(itemMeta);
			item.setAmount(amount);
			player.getInventory().addItem(item);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {

		ArrayList<String> tabComplete = new ArrayList<>();

		if (args.length == 1) {

			String[] args1 = {
					"resourcepack", "give", "enable", "disable", "reload" };
			StringUtil.copyPartialMatches(args[0], Arrays.asList(args1), tabComplete);

		} else if (args.length == 2) {

			switch (args[0].toLowerCase()) {

			case "resourcepack":

				String[] resourcepack = {
						"enable", "disable", "set" };
				StringUtil.copyPartialMatches(args[1], Arrays.asList(resourcepack), tabComplete);
				break;

			case "give":

				List<String> players = new ArrayList<>();
				Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
				StringUtil.copyPartialMatches(args[1], players, tabComplete);
				break;

			case "enable":

				ArrayList<String> enable = new ArrayList<>();

				for (String key : main.itemManager.itemsCFG.getConfigurationSection("CustomItems").getKeys(false)) {

					if (!main.itemManager.items.containsKey(key.toLowerCase())) {
						enable.add(key);
					}
				}

				StringUtil.copyPartialMatches(args[1], enable, tabComplete);
				break;

			case "disable":

				StringUtil.copyPartialMatches(args[1], main.itemManager.items.keySet(), tabComplete);
				break;
			}
		} else if (args[0].equalsIgnoreCase("give")) {

			if (args.length == 3) {

				StringUtil.copyPartialMatches(args[2], main.itemManager.items.keySet(), tabComplete);

			} else if (args.length == 4) {

				Collection<String> list = new ArrayList<>();
				list.add("1");
				list.add("16");
				list.add("32");
				list.add("48");
				list.add("64");
				StringUtil.copyPartialMatches(args[3], list, tabComplete);

			} else if (args.length == 5) {

				Collection<String> list = new ArrayList<>();
				list.add("1");
				list.add("10");
				list.add("100");
				list.add("1000");
				StringUtil.copyPartialMatches(args[3], list, tabComplete);

			}
		}

		Collections.sort(tabComplete);

		return tabComplete;
	}
}