package org.infernalstudios.sizableslimes.mixin;

import org.infernalstudios.sizableslimes.SizableSlimesConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.monster.Slime;

@Mixin(JumpControl.class)
public class MixinJumpControl {
  @Shadow
  @Final
  private Mob mob;

  @Inject(method = "jump", at = @At("HEAD"))
  private void onJump(CallbackInfo ci) {
    if (!this.mob.level.isClientSide() && this.mob instanceof Slime slime) {
      ((AccessorSlime) slime).callSetSize(Math.min(SizableSlimesConfig.maxSize, slime.getSize() + 1), false);
    }
  }
}
