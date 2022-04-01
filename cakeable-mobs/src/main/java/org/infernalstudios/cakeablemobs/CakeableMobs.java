package org.infernalstudios.cakeablemobs;

import org.infernalstudios.clusterfuck.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CakeableMobs.MOD_ID)
public class CakeableMobs {
  public static final String MOD_ID = "cakeablemobs";

  public CakeableMobs() {
    Util.ignoreServerOnly();
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onEntityDamage(LivingAttackEvent event) {
    LivingEntity entity = event.getEntityLiving();
    Level level = entity.getLevel();
    Entity attackingEntity = event.getSource().getEntity();
    if (!level.isClientSide() && attackingEntity != null && attackingEntity instanceof Player) {
      level.setBlock(new BlockPos(entity.getX(), entity.getY(), entity.getZ()), Blocks.CAKE.defaultBlockState(), 2);
      entity.remove(RemovalReason.DISCARDED);
    }
  }
}
