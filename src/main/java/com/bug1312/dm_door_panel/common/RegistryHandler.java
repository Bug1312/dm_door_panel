// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.common;

import com.bug1312.dm_door_panel.ModMain;
import com.bug1312.dm_door_panel.common.init.BlockEntities;
import com.bug1312.dm_door_panel.common.init.Blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MOD_ID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MOD_ID);
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ModMain.MOD_ID);
	
	public static void init() {
		new Blocks();
		new BlockEntities();
		
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		ItemTags.createOptional(new ResourceLocation(ModMain.MOD_ID, "tardis_keys"));
	}
}
