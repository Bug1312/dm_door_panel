// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.common.tileentity;

import com.bug1312.dm_door_panel.common.block.DoorPanelBlock;
import com.bug1312.dm_door_panel.common.init.BlockEntities;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.init.DMTranslationKeys;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisDoor;
import com.swdteam.common.tileentity.DMTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.common.tileentity.TardisTileEntity.DoorSource;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class DoorPanelTileEntity extends DMTileEntityBase implements ITickableTileEntity {

	public DoorPanelTileEntity() {
		super(BlockEntities.TILE_DOOR_PANEL.get());
	}

	private void setDoors(TardisTileEntity tardis, boolean open) {
		tardis.setDoor(TardisDoor.BOTH, open, DoorSource.TARDIS);
		tardis.setDoor(TardisDoor.BOTH, open, DoorSource.INTERIOR);
	}
	
	
	public boolean toggleDoors(PlayerEntity player) {
		TardisData data = DMTardis.getTardisFromInteriorPos(this.getBlockPos());
		if (data == null) return !this.getBlockState().getValue(DoorPanelBlock.OPENED);
		
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

		if (tile == null || !(tile instanceof TardisTileEntity)) return this.getBlockState().getValue(DoorPanelBlock.OPENED);
		
		TardisTileEntity tardis = (TardisTileEntity) tile;
		boolean isOpen = tardis.doorOpenLeft || tardis.doorOpenRight;
		TranslationTextComponent text = new TranslationTextComponent(isOpen ? "notice.dm_door_panel.close" : "notice.dm_door_panel.open");
		
		setDoors(tardis, !isOpen);
		data.setDoorOpen(!isOpen);
		player.displayClientMessage(new StringTextComponent(TextFormatting.GREEN + text.getString()), true);

		return !isOpen;
	}
	
	public boolean toggleLocks(PlayerEntity player) {
		TardisData data = DMTardis.getTardisFromInteriorPos(this.getBlockPos());
		if (data == null) return !this.getBlockState().getValue(DoorPanelBlock.LOCKED);
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

	@Override
	public void tick() {
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
}
