package fr.gamalta.redblock.customitems.listeners;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import fr.gamalta.redblock.customitems.CustomItems;

public class onStructureGrowEvent implements Listener {

	public onStructureGrowEvent(CustomItems main) {

	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onStructureGrow(StructureGrowEvent event) {

		TreeType treeType = event.getSpecies();

		if (treeType.equals(TreeType.RED_MUSHROOM) || treeType.equals(TreeType.BROWN_MUSHROOM)) {

			event.getBlocks().get(0).getBlockData();

			for (BlockState blockState : event.getBlocks()) {

				Material material = blockState.getType();

				if (material.equals(Material.RED_MUSHROOM_BLOCK) || material.equals(Material.BROWN_MUSHROOM_BLOCK) || material.equals(Material.MUSHROOM_STEM)) {

					MultipleFacing multiFacing = (MultipleFacing) blockState.getBlockData();

					multiFacing.setFace(BlockFace.DOWN, true);
					multiFacing.setFace(BlockFace.UP, true);
					multiFacing.setFace(BlockFace.NORTH, true);
					multiFacing.setFace(BlockFace.SOUTH, true);
					multiFacing.setFace(BlockFace.EAST, true);
					multiFacing.setFace(BlockFace.WEST, true);

					blockState.setBlockData(multiFacing);

				}
			}
		}
	}
}