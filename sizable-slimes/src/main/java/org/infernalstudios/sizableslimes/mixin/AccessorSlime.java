package org.infernalstudios.sizableslimes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.monster.Slime;

@Mixin(Slime.class)
public interface AccessorSlime {
  @Invoker
  void callSetSize(int size, boolean resetHealth);
}
