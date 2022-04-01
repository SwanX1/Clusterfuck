package org.infernalstudios.spreadingmoss;

import org.infernalstudios.clusterfuck.Util;

import net.minecraftforge.fml.common.Mod;

@Mod(SpreadingMoss.MOD_ID)
public class SpreadingMoss {
  public static final String MOD_ID = "spreadingmoss";

  public SpreadingMoss() {
    Util.ignoreServerOnly();
    Util.createConfig(MOD_ID, "Spreading Moss", SpreadingMossConfig.class);
  }
}