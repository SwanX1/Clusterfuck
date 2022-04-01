package org.infernalstudios.boom;

import org.infernalstudios.clusterfuck.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Boom.MOD_ID)
public class Boom {
  public static final String MOD_ID = "boom";

  public Boom() {
    Util.ignoreServerOnly();
    MinecraftForge.EVENT_BUS.register(this);
    BoomSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  @SuppressWarnings("resource")
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onChatMessage(ClientChatEvent event) {
    // Minecraft minecraft = Minecraft.getInstance();
    Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(BoomSounds.THUD.get().getLocation(),
        SoundSource.MASTER, 0.25F, 1.0F, false, 0, Attenuation.NONE, 0.0D, 0.0D, 0.0D, true));
  }
}