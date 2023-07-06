// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.swdteam.common.tileentity.ExtraRotationTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;

import net.minecraft.tileentity.TileEntityType;

@Mixin(TardisTileEntity.class)
public abstract class TardisTileEntityMixin extends ExtraRotationTileEntityBase {

	public TardisTileEntityMixin(TileEntityType<?> tileEntityTypeIn) { super(tileEntityTypeIn); }
	private TardisTileEntity _this = ((TardisTileEntity)(Object)this);

	public boolean open = false;

	@Inject(at = @At("TAIL"), method = "tick")
	public void tick(CallbackInfo ci) {
		if (!this.level.isClientSide()) {
			boolean exteriorOpen = (_this.doorOpenLeft || _this.doorOpenRight);
			if (_this.tardisData != null && exteriorOpen != open) {
				this.open = exteriorOpen;
				_this.tardisData.setDoorOpen(exteriorOpen);
			}
		}
	}
}
