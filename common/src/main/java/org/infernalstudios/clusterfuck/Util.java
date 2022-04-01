package org.infernalstudios.clusterfuck;

import java.io.IOException;

import com.electronwill.nightconfig.core.io.ParsingException;

import org.infernalstudios.config.Config;
import org.infernalstudios.config.ConfigBuilder;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;

public final class Util {
	private Util() {} // Uninstantiable

  public static Config createConfig(String id, String name, Class<?> ...configClass) {
    try {
      ConfigBuilder builder = Config
          .builder(FMLPaths.CONFIGDIR.get().resolve(id + "-common.toml"));
      for (Class<?> clazz : configClass) {
        builder.loadClass(clazz);
      }
      return builder.build();
    } catch (IllegalStateException | IllegalArgumentException | IOException | ParsingException e) {
      throw new RuntimeException("Failed to load " + name + " config", e);
    }
  }

  public static void ignoreServerOnly() {
    ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
  }
}