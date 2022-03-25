package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.gamalta.redblock.customitems.CustomItems;
import fr.gamalta.redblock.customitems.utils.Utils;

public class onPlayerJoinEvent implements Listener {

	private CustomItems main;
	private Utils utils;

	public onPlayerJoinEvent(CustomItems main) {

		this.main = main;
		utils = new Utils(main);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();

		if (main.settingsCFG.getBoolean("CustomItems.ResourcePack.Enabled")) {

			utils.sendRessourcePackRequest(player, main.settingsCFG.getString("CustomItems.ResourcePack.Link"));
		}
	}
}
