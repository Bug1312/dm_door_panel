// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.common.init;

import com.bug1312.dm_door_panel.common.RegistryHandler;
import com.bug1312.dm_door_panel.common.block.DoorPanelBlock;
import com.bug1312.dm_door_panel.common.tileentity.DoorPanelTileEntity;
import com.google.common.base.Supplier;
import com.swdteam.common.init.DMTabs;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class Blocks {
	public static final RegistryObject<Block> DOOR_PANEL = registerBlockAndItem("door_panel", () -> new DoorPanelBlock(DoorPanelTileEntity::new, Properties.of(Material.STONE).instabreak().noOcclusion().sound(SoundType.WOOD)), new Item.Properties().tab(DMTabs.DM_TARDIS));

	/* Register Method */
	public static <B extends Block> RegistryObject<Block> registerBlockAndItem(String name, Supplier<B> block, Item.Properties properties) {
		RegistryObject<Block> blockObject = RegistryHandler.BLOCKS.register(name, block);
		RegistryHandler.ITEMS.register(name, () -> new BlockItem(blockObject.get(), properties));

		return blockObject;
	}

}