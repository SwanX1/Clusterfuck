package org.infernalstudios.spreadingmoss.mixin;

import java.util.Random;

import org.infernalstudios.spreadingmoss.SpreadingMossConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {
  @Inject(method = "randomTick", at = @At("HEAD"))
  private void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo ci) {
    if (((BlockBehaviour) (Object) this) instanceof MossBlock mossBlock
        && mossBlock.isValidBonemealTarget(level, pos, state, false)
        && random.nextDouble(100) < SpreadingMossConfig.mossSpreadChance) {
      mossBlock.performBonemeal(level, random, pos, state);
    }
  }

  @Shadow
  @Final
  @Mutable
  private boolean isRandomlyTicking;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void init(BlockBehaviour.Properties properties, CallbackInfo ci) {
    if (((BlockBehaviour) (Object) this) instanceof MossBlock) {
      this.isRandomlyTicking = true;
    }
  }
}
