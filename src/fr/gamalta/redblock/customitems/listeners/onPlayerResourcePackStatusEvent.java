package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.customitems.CustomItems;

public class onPlayerResourcePackStatusEvent implements Listener {

	private CustomItems main;

	public onPlayerResourcePackStatusEvent(CustomItems main) {

		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {

		Player player = event.getPlayer();
		PlayerResourcePackStatusEvent.Status status = event.getStatus();

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF("lobby");

		switch (status) {
		case SUCCESSFULLY_LOADED:
			break;
		case DECLINED:
			player.spigot().sendMessage(new Message(main, main.messagesCFG, "CustomItems.ResourcePack.Declined").create());
			player.sendPluginMessage(main, "BungeeCord", out.toByteArray());

			break;
		case FAILED_DOWNLOAD:
			player.spigot().sendMessage(new Message(main, main.messagesCFG, "CustomItems.ResourcePack.FailedDownload").create());
			player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
			break;
		case ACCEPTED:
			break;
		}
	}
}
