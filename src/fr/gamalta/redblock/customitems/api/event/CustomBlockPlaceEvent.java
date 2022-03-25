package fr.gamalta.redblock.customitems.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockPlaceEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private BlockPlaceEvent event;
	private boolean cancelled = false;
	private Player player;
	private Block block;
	private ItemStack item;

	public CustomBlockPlaceEvent(BlockPlaceEvent event, Player player, Block block, ItemStack item) {

		this.player = player;
		this.block = block;
		this.item = item;
		this.event = event;
	}

	@Override
	public boolean isCancelled() {

		return cancelled;
	}

	public void setSuperCancelled(boolean cancelled) {

		event.setCancelled(cancelled);
		this.cancelled = cancelled;

	}

	@Override
	public void setCancelled(boolean cancelled) {

		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {

		return handlers;
	}

	public static HandlerList getHandlerList() {

		return handlers;
	}

	public Player getPlayer() {

		return player;
	}

	public void setPlayer(Player player) {

		this.player = player;
	}

	public Block getBlock() {

		return block;
	}

	public void setBlock(Block block) {

		this.block = block;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

}
