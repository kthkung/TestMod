//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.util;

import journeymap.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
   public static String RealmsWorldName = "";
   private static int[] colours = new int[]{16711680, 65280, 255, 16776960, 16711935, 65535, 16744448, 8388863};
   public static int colourIndex = 0;

   public Utils() {
   }

   public static int[] integerListToIntArray(List<Integer> list) {
      int size = list.size();
      int[] array = new int[size];

      for(int i = 0; i < size; ++i) {
         array[i] = (Integer)list.get(i);
      }

      return array;
   }

   public static String mungeString(String s) {
      s = s.replace('.', '_');
      s = s.replace('-', '_');
      s = s.replace(' ', '_');
      s = s.replace('/', '_');
      s = s.replace('\\', '_');
      return Reference.patternInvalidChars.matcher(s).replaceAll("");
   }

   public static String mungeStringForConfig(String s) {
      return Reference.patternInvalidChars2.matcher(s).replaceAll("");
   }

   public static File getFreeFilename(File dir, String baseName, String ext) {
      int i = 0;
      File outputFile;
      if (dir != null) {
         outputFile = new File(dir, baseName + "." + ext);
      } else {
         outputFile = new File(baseName + "." + ext);
      }

      for(; outputFile.exists() && i < 1000; ++i) {
         if (dir != null) {
            outputFile = new File(dir, baseName + "." + i + "." + ext);
         } else {
            outputFile = new File(baseName + "." + i + "." + ext);
         }
      }

      return i < 1000 ? outputFile : null;
   }

   public static void printBoth(String msg) {
      EntityPlayerSP player = Minecraft.getMinecraft().player;
      if (player != null) {
         player.sendStatusMessage(new TextComponentString(msg), false);
      }

      Logging.log("%s", new Object[]{msg});
   }

   public static File getDimensionDir(File worldDir, int dimension) {
      File dimDir;
      if (dimension != 0) {
         dimDir = new File(worldDir, "DIM" + dimension);
      } else {
         dimDir = worldDir;
      }

      return dimDir;
   }

   public static IntBuffer allocateDirectIntBuffer(int size) {
      if (size < 1) {
         int NewSize = Minecraft.getGLMaximumTextureSize();
         return ByteBuffer.allocateDirect(NewSize * NewSize * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      } else {
         return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      }
   }

   public static int nextHighestPowerOf2(int v) {
      --v;
      v |= v >> 1;
      v |= v >> 2;
      v |= v >> 4;
      v |= v >> 8;
      v |= v >> 16;
      return v + 1;
   }

   public static String getCurrentDateString() {
      DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
      return dateFormat.format(new Date());
   }

   public static int distToChunkSq(int x, int z, Chunk chunk) {
      int dx = (chunk.x << 4) + 8 - x;
      int dz = (chunk.z << 4) + 8 - z;
      return dx * dx + dz * dz;
   }

   public static String getWorldName() {
      String worldName;
      if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
         IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
         worldName = server != null ? server.getFolderName() : "sp_world";
      } else if (Minecraft.getMinecraft().isConnectedToRealms()) {
         if (RealmsWorldName != "") {
            worldName = RealmsWorldName;
         } else {
            worldName = "Realms";
         }
      } else {
         worldName = Minecraft.getMinecraft().getCurrentServerData().serverIP;
         if (!Config.portNumberInWorldNameEnabled) {
            worldName = worldName.substring(0, worldName.indexOf(":"));
         } else if (worldName.indexOf(":") == -1) {
            worldName = worldName + "_25565";
         } else {
            worldName = worldName.replace(":", "_");
         }
      }

      worldName = mungeString(worldName);
      if (worldName == "") {
         worldName = "default";
      }

      return worldName;
   }

   public static void openWebLink(URI p_175282_1_) {
      try {
         Class<?> oclass = Class.forName("java.awt.Desktop");
         Object object = oclass.getMethod("getDesktop").invoke((Object)null);
         oclass.getMethod("browse", URI.class).invoke(object, p_175282_1_);
      } catch (Throwable var3) {
         Logging.logError("Couldn't open link %s", new Object[]{var3.getStackTrace().toString()});
      }

   }

   public static String stringArrayToString(String[] arr) {
      StringBuilder builder = new StringBuilder();
      String[] var2 = arr;
      int var3 = arr.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String s = var2[var4];
         builder.append(I18n.format(s, new Object[0]));
         builder.append("\n");
      }

      return builder.toString();
   }

   public static int getMaxWidth(String[] arr, String[] arr2) {
      FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
      int Width = 1;

      for(int i = 0; i < arr.length; ++i) {
         int w1 = 0;
         int w2 = 0;
         String s;
         if (i < arr.length) {
            s = I18n.format(arr[i], new Object[0]);
            w1 = fontRendererObj.getStringWidth(s);
         }

         if (arr2 != null && i < arr2.length) {
            s = I18n.format(arr2[i], new Object[0]);
            w2 = fontRendererObj.getStringWidth(s);
            w2 += 65;
         }

         int wTot = w1 > w2 ? w1 : w2;
         Width = Width > wTot ? Width : wTot;
      }

      return Width;
   }

   private static int getColoursLengt() {
      return colours.length;
   }

   public static int getCurrentColour() {
      return -16777216 | colours[colourIndex];
   }

   public static int getNextColour() {
      colourIndex = (colourIndex + 1) % getColoursLengt();
      return getCurrentColour();
   }

   public static int getPrevColour() {
      colourIndex = (colourIndex + getColoursLengt() - 1) % getColoursLengt();
      return getCurrentColour();
   }

   public static <K, V> Map<K, V> checkedMapByCopy(Map rawMap, Class<K> keyType, Class<V> valueType, boolean strict) throws ClassCastException {
      Map<K, V> m2 = new HashMap(rawMap.size() * 4 / 3 + 1);
      Iterator it = rawMap.entrySet().iterator();

      while(it.hasNext()) {
         Map.Entry e = (Map.Entry)it.next();

         try {
            m2.put(keyType.cast(e.getKey()), valueType.cast(e.getValue()));
         } catch (ClassCastException var8) {
            if (strict) {
               throw var8;
            }

            System.out.println("not assignable");
         }
      }

      return m2;
   }

   public static Field findUnderlying(Class<?> clazz, String fieldName) {
      Class<?> current = clazz;

      while(true) {
         try {
            return current.getDeclaredField(fieldName);
         } catch (Exception var4) {
            if ((current = current.getSuperclass()) == null) {
               return null;
            }
         }
      }
   }
}
