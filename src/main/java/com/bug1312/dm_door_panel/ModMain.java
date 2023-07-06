// Copyright 2023 Bug1312

package com.bug1312.dm_door_panel;

import com.bug1312.dm_door_panel.common.RegistryHandler;
import com.bug1312.dm_door_panel.common.init.Blocks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModMain.MOD_ID)
public class ModMain {
	public static final String MOD_ID = "dm_door_panel";

	public ModMain() {
		RegistryHandler.init();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client);
	}
	
	public void client(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(Blocks.DOOR_PANEL.get(), RenderType.cutoutMipped());
	}
}


