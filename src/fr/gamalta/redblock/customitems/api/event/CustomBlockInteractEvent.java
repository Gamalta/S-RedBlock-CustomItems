package fr.gamalta.redblock.customitems.api.event;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.sun.istack.internal.NotNull;

public class CustomBlockInteractEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private PlayerInteractEvent event;
	private boolean cancelled = false;
	private Player player;
	private Action action;
	private EquipmentSlot equipmentSlot;
	private Block block;
	private BlockData blockData;

	public CustomBlockInteractEvent(PlayerInteractEvent event, Player player, Action action, BlockData blockData, EquipmentSlot equipmentSlot, Block block) {

		this.player = player;
		this.action = action;
		this.equipmentSlot = equipmentSlot;
		this.block = block;
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
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {

		return handlers;
	}

	public EquipmentSlot getEquipmentSlot() {
		return equipmentSlot;
	}

	public Action getAction() {
		return action;
	}

	public Player getPlayer() {
		return player;
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
}