package fr.gamalta.redblock.customitems.api.event;

import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockMoveEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private BlockPistonExtendEvent event;
	private boolean cancelled = false;
	private Block block;
	private BlockData blockData;
	private Collection<ItemStack> dropItems;
	private BlockFace direction;

	public CustomBlockMoveEvent(BlockPistonExtendEvent event, Block block, BlockData blockData, BlockFace direction) {
		
		this.block = block;
		this.blockData = blockData;
		this.event = event;
		this.direction = direction;
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

		return dropItems;
	}

	public void setDropItem(Collection<ItemStack> dropItems) {

		this.dropItems = dropItems;
	}

	public void setDirection(BlockFace direction) {
		this.direction = direction;
	}
	
	public BlockFace getDirection() {
		return direction;
	}
}