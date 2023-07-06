// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisDoor;
import com.swdteam.common.tileentity.DMTileEntityBase;
import com.swdteam.common.tileentity.tardis.DoubleDoorsTileEntity;

import net.minecraft.tileentity.TileEntityType;

@Mixin(DoubleDoorsTileEntity.class)
public abstract class DoubleDoorsTileEntityMixin extends DMTileEntityBase  {

	public DoubleDoorsTileEntityMixin(TileEntityType<?> type) { super(type); }
	private DoubleDoorsTileEntity _this = ((DoubleDoorsTileEntity)(Object)this);

	public boolean open = false;
	
	@Inject(at = @At("TAIL"), method = "tick")
	public void tick(CallbackInfo ci) {	
		if (!this.level.isClientSide() && _this.isMainDoor()) {
			boolean doorsOpen = _this.isOpen(TardisDoor.BOTH);
			TardisData tardisData = DMTardis.getTardisFromInteriorPos(getBlockPos());

			if (tardisData != null && doorsOpen != open) tardisData.setDoorOpen(doorsOpen);
		}
	}

}
