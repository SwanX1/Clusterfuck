package org.infernalstudios.spreadingmoss;

import org.infernalstudios.config.annotation.Configurable;
import org.infernalstudios.config.annotation.DoubleRange;

public class SpreadingMossConfig {
  @Configurable(description = "The chance (in %) that a moss block will spread on a random tick.")
  @DoubleRange(min = 0.0D, max = 100.0D)
  public static double mossSpreadChance = 50.0D;
}
