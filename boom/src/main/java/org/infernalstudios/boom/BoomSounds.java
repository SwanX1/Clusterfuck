package org.infernalstudios.boom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BoomSounds {
  protected static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Boom.MOD_ID);
  public static final RegistryObject<SoundEvent> THUD = SOUNDS.register("thud", () -> new SoundEvent(new ResourceLocation(Boom.MOD_ID, "thud")));
}
