// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.common.init;

import com.bug1312.dm_door_panel.ModMain;
import com.bug1312.dm_door_panel.common.tileentity.DoorPanelTileEntity;
import com.swdteam.common.RegistryHandler;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntities {
	public static final RegistryObject<TileEntityType<DoorPanelTileEntity>> TILE_DOOR_PANEL = RegistryHandler.TILE_ENTITY_TYPES.register("door_panel", () -> TileEntityType.Builder.of(DoorPanelTileEntity::new, Blocks.DOOR_PANEL.get()).build(null));
}
