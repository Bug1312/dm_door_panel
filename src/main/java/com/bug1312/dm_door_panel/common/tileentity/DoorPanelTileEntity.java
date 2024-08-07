// Copyright 2024 Bug1312

package com.bug1312.dm_door_panel.common.tileentity;

import com.bug1312.dm_door_panel.common.block.DoorPanelBlock;
import com.bug1312.dm_door_panel.common.init.BlockEntities;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.init.DMTranslationKeys;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisDoor;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.common.tileentity.TardisTileEntity.DoorSource;
import com.swdteam.common.tileentity.tardis.TardisPanelTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class DoorPanelTileEntity extends TardisPanelTileEntity implements ITickableTileEntity {

	private static final long serialVersionUID = -6361124286370831557L;

	public DoorPanelTileEntity() {
		super(BlockEntities.TILE_DOOR_PANEL.get());
	}

	private void setDoors(TardisTileEntity tardis, boolean open) {
		tardis.setDoor(TardisDoor.BOTH, open, DoorSource.TARDIS);
		tardis.setDoor(TardisDoor.BOTH, open, DoorSource.INTERIOR);
	}
	
	public boolean toggleDoors(PlayerEntity player) {	
		if (this.level.isClientSide() || this.level.dimension() == DMDimensions.TARDIS) {		
			TardisData data = DMTardis.getTardisFromInteriorPos(this.getBlockPos());
			if (data != null) {				
				if (data.isLocked()) {
					player.displayClientMessage(new StringTextComponent(TextFormatting.YELLOW + DMTranslationKeys.TARDIS_IS_LOCKED.getString()), true);
					return this.getBlockState().getValue(DoorPanelBlock.OPENED);
				}
				
				if (data.isInFlight()) {
					player.displayClientMessage(new StringTextComponent(TextFormatting.YELLOW + DMTranslationKeys.TARDIS_IN_FLIGHT.getString()), true);
					return this.getBlockState().getValue(DoorPanelBlock.OPENED);
				}
				
				Location location = data.getCurrentLocation();
				ServerWorld serverWorld = level.getServer().getLevel(data.getCurrentLocation().dimensionWorldKey());
				TileEntity tile = serverWorld.getBlockEntity(location.getPosition().toBlockPos());
		
				if (tile != null && tile instanceof TardisTileEntity) {
					TardisTileEntity tardis = (TardisTileEntity) tile;
					boolean isOpen = tardis.doorOpenLeft || tardis.doorOpenRight;
					TranslationTextComponent text = new TranslationTextComponent(isOpen ? "notice.dm_door_panel.close" : "notice.dm_door_panel.open");
					
					setDoors(tardis, !isOpen);
					data.setDoorOpen(!isOpen);
					player.displayClientMessage(new StringTextComponent(TextFormatting.GREEN + text.getString()), true);
			
					return !isOpen;
				}
			}
		}
		
		return !this.getBlockState().getValue(DoorPanelBlock.OPENED);
	}
	
	public boolean toggleLocks(PlayerEntity player) {
		if (this.level.isClientSide() || this.level.dimension() == DMDimensions.TARDIS) {
			TardisData data = DMTardis.getTardisFromInteriorPos(this.getBlockPos());
			if (data != null) {
				data.setLocked(!data.isLocked());
				
				TranslationTextComponent text = new TranslationTextComponent(data.isLocked() ? "notice.dm_door_panel.lock" : "notice.dm_door_panel.unlock");
				player.displayClientMessage(new StringTextComponent(TextFormatting.GREEN + text.getString()), true);
				
				if (data.isLocked()) {
					Location location = data.getCurrentLocation();
					ServerWorld serverWorld = level.getServer().getLevel(data.getCurrentLocation().dimensionWorldKey());
					TileEntity tile = serverWorld.getBlockEntity(location.getPosition().toBlockPos());
		
					if (tile != null && tile instanceof TardisTileEntity) {
						TardisTileEntity tardis = (TardisTileEntity) tile;
						
						if (tardis.doorOpenLeft || tardis.doorOpenRight) setDoors(tardis, false);
					}
					
					data.setDoorOpen(false);
				}
		
				return data.isLocked();
			}
		}

		return !this.getBlockState().getValue(DoorPanelBlock.LOCKED);
	}
		
	@Override
	public void tick() {
		if (this.level.isClientSide() || this.level.dimension() != DMDimensions.TARDIS) return;
		TardisData data = DMTardis.getTardisFromInteriorPos(this.getBlockPos());
		if (data == null) return;
		
		if (data.isDoorOpen() != this.getBlockState().getValue(DoorPanelBlock.OPENED)) {
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(DoorPanelBlock.OPENED, data.isDoorOpen()));
		}
		if (data.isLocked() != this.getBlockState().getValue(DoorPanelBlock.LOCKED)) {
			this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(DoorPanelBlock.LOCKED, data.isLocked()));
			if (data.isLocked()) this.getBlockState().setValue(DoorPanelBlock.OPENED, false);
		}
	}
	
	@Override
	public ResourceLocation getGUIIcon() {
		return new ResourceLocation("dm_door_panel:textures/item/door_panel.png");
	}
}
