//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.forge;

import journeymap.Mw;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;

public class MwKeyHandler {
   public static KeyBinding keyRadar = new KeyBinding("Open Radar", 43, "PixelRadar");
   public static KeyBinding keyMapGui = new KeyBinding("Open Map GUI", 50, "PixelRadar");
   public static KeyBinding keyNewMarker = new KeyBinding("New Marker", 210, "PixelRadar");
   public static KeyBinding keyMapMode = new KeyBinding("Next Map Mode", 49, "PixelRadar");
   public static KeyBinding keyNextGroup = new KeyBinding("Next Marker Group", 51, "PixelRadar");
   public static KeyBinding keyTeleport = new KeyBinding("Teleport", 52, "PixelRadar");
   public static KeyBinding keyZoomIn = new KeyBinding("Zoom In", 201, "PixelRadar");
   public static KeyBinding keyZoomOut = new KeyBinding("Zoom Out", 209, "PixelRadar");
   public static KeyBinding keyUndergroundMode = new KeyBinding("Underground Mode", 22, "PixelRadar");
   public final KeyBinding[] keys;

   public MwKeyHandler() {
      this.keys = new KeyBinding[]{keyMapGui, keyNewMarker, keyMapMode, keyNextGroup, keyTeleport, keyZoomIn, keyZoomOut, keyUndergroundMode, keyRadar};
      ArrayList<String> listKeyDescs = new ArrayList();
      KeyBinding[] var2 = this.keys;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         KeyBinding key = var2[var4];
         if (key != null) {
            ClientRegistry.registerKeyBinding(key);
         }

         assert key != null;
         listKeyDescs.add(key.getKeyDescription());
      }

   }

   @SubscribeEvent
   public void keyEvent(InputEvent.KeyInputEvent event) {
      this.checkKeys();
   }

   private void checkKeys() {
      KeyBinding[] var1 = this.keys;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         KeyBinding key = var1[var3];
         if (key != null && key.isPressed()) {
            Mw.getInstance().onKeyDown(key);
         }
      }

   }
}
