package org.infernalstudios.sizableslimes;

import org.infernalstudios.config.annotation.Configurable;
import org.infernalstudios.config.annotation.IntegerRange;

import net.minecraft.world.entity.monster.Slime;

public class SizableSlimesConfig {
  @Configurable(description = "Determines if the slime will reset its health when it grows.")
  public static boolean shouldResetHealth = false;

  @Configurable(description = "Determines the maximum size the slime camn grow to.")
  @IntegerRange(min = Slime.MIN_SIZE, max = Slime.MAX_SIZE)
  public static int maxSize = Slime.MAX_SIZE;
}
