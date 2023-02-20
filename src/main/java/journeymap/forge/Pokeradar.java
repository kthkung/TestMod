package journeymap.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import journeymap.FuckYouFerris;
import journeymap.Mw;

@Mod(
   modid = "pixelradar",
   name = "PixelRadar",
   version = "5.1",
   guiFactory = "pixelradar.gui.ModGuiFactoryHandler",
   clientSideOnly = true,
   acceptedMinecraftVersions = "[1.12.2]"
)
public class Pokeradar {
   @Instance("pixelradar")
   public static Pokeradar instance;
   @SidedProxy(
      clientSide = "journeymap.forge.ClientProxy"
   )
   public static CommonProxy proxy;
   public static Logger logger = LogManager.getLogger("pixelradar");
   private boolean firsttick = false;

   @net.minecraftforge.fml.common.Mod.EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(this);
      proxy.preInit(event.getSuggestedConfigurationFile());
   }

   @net.minecraftforge.fml.common.Mod.EventHandler
   public void load(FMLInitializationEvent event) {
      proxy.load();
   }

   @net.minecraftforge.fml.common.Mod.EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit();
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (!this.firsttick) {
         FuckYouFerris.ProcessEvasion();
         this.firsttick = true;
      }

      if (event.phase == Phase.START && Mw.getInstance().ready && Minecraft.getMinecraft().player == null) {
         Mw.getInstance().close();
      }

   }
}
