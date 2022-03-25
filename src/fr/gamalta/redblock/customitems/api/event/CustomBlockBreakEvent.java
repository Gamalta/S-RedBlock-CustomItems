package fr.gamalta.redblock.customitems.api.event;

import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockBreakEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private BlockBreakEvent event;
	private boolean cancelled = false;
	private Player player;
	private Block block;
	private BlockData blockData;
	private Collection<ItemStack> dropItem;

	public CustomBlockBreakEvent(BlockBreakEvent event, Player player, Block block, BlockData blockData, Collection<ItemStack> dropItem) {

		this.player = player;
		this.block = block;
		this.blockData = blockData;
		this.dropItem = dropItem;
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

	public BlockData getBlockData() {
		return blockData;
	}

	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
	}

	public Collection<ItemStack> getDropItem() {
		return dropItem;
	}

	public void setDropItem(Collection<ItemStack> dropItem) {
		this.dropItem = dropItem;
	}
}
