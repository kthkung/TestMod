//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import journeymap.Mw;
import journeymap.api.MwAPI;
import journeymap.config.ConfigurationHandler;
import journeymap.overlay.OverlayGrid;
import journeymap.overlay.OverlaySlime;
import journeymap.region.MwChunk;
import journeymap.util.VersionCheck;

import java.io.File;

public class ClientProxy extends CommonProxy {
   public ClientProxy() {
   }

   public void preInit(File configFile) {
      ConfigurationHandler.init(configFile);
      MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
   }

   public void load() {
      EventHandler eventHandler = new EventHandler(Mw.getInstance());
      MinecraftForge.EVENT_BUS.register(eventHandler);
      MwKeyHandler keyEventHandler = new MwKeyHandler();
      MinecraftForge.EVENT_BUS.register(keyEventHandler);
   }

   public void postInit() {
      if (Loader.isModLoaded("VersionChecker")) {
         FMLInterModComms.sendRuntimeMessage("pixelradar", "VersionChecker", "addVersionCheck", "https://goo.gl/T20VFb");
      } else {
         VersionCheck versionCheck = new VersionCheck();
         Thread versionCheckThread = new Thread(versionCheck, "Version Check");
         versionCheckThread.start();
      }

      if (Loader.isModLoaded("CarpentersBlocks")) {
         MwChunk.carpenterdata();
      }

      if (Loader.isModLoaded("ForgeMultipart")) {
         MwChunk.FMPdata();
      }

      MwAPI.registerDataProvider("Slime", new OverlaySlime());
      MwAPI.registerDataProvider("Grid", new OverlayGrid());
   }
}
