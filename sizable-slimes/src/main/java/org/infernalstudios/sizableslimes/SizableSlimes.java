package org.infernalstudios.sizableslimes;

import org.infernalstudios.clusterfuck.Util;

import net.minecraftforge.fml.common.Mod;

@Mod(SizableSlimes.MOD_ID)
public class SizableSlimes {
  public static final String MOD_ID = "sizableslimes";

  public SizableSlimes() {
    Util.ignoreServerOnly();
    Util.createConfig(MOD_ID, "Sizable Slimes", SizableSlimesConfig.class);
  }
}
