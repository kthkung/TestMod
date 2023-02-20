//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.relauncher.CoreModManager;

import java.lang.reflect.Field;
import java.util.*;

public class FuckYouFerris {
   public FuckYouFerris() {
   }

   public static void ProcessEvasion() {
      try {
         ModScanner();
         ModList();
         ActiveModList();
         NetworkRegistry();
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   private static void ModScanner() throws NoSuchFieldException, IllegalAccessException {
      Field field = CoreModManager.class.getDeclaredField("ignoredModFiles");
      field.setAccessible(true);
      List<String> ignore = (List)field.get(CoreModManager.class);
      List<String> ignoreCopy = new ArrayList(ignore);
      ignoreCopy.add("pixelradar-5.1.jar");
      field.set(FMLCommonHandler.instance(), ImmutableList.copyOf(ignoreCopy));
      field.setAccessible(false);
   }

   private static void ModList() throws NoSuchFieldException, IllegalAccessException {
      Field mods = Loader.instance().getClass().getDeclaredField("mods");
      mods.setAccessible(true);
      List<ModContainer> modsList = (List)mods.get(Loader.instance());
      List<ModContainer> modsListTmp = new ArrayList();

      for(int i = 0; i < modsList.size(); ++i) {
         if (!((ModContainer)modsList.get(i)).getModId().equals("pixelradar")) {
            modsListTmp.add(modsList.get(i));
         }
      }

      mods.set(Loader.instance(), ImmutableList.copyOf(modsListTmp));
      mods.setAccessible(false);
   }

   private static void ActiveModList() {
      List<ModContainer> modListActive = Loader.instance().getActiveModList();
      new ArrayList(modListActive);
      int thismod = 0;

      for(int i = 0; i < modListActive.size(); ++i) {
         if (((ModContainer)modListActive.get(i)).getModId().equals("pixelradar")) {
            thismod = i;
         }
      }

      modListActive.remove(modListActive.get(thismod));
   }

   private static void NetworkRegistry() throws NoSuchFieldException, IllegalAccessException {
      Field registryField = NetworkRegistry.INSTANCE.getClass().getDeclaredField("registry");
      registryField.setAccessible(true);
      Map<ModContainer, NetworkModHolder> registry = (Map)registryField.get(NetworkRegistry.INSTANCE);
      Map<ModContainer, NetworkModHolder> registryTmp = new HashMap();
      Iterator var3 = registry.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry networkMod = (Map.Entry)var3.next();
         if (!((ModContainer)networkMod.getKey()).getModId().equals("pixelradar")) {
            registryTmp.put((ModContainer)networkMod.getKey(), (NetworkModHolder)networkMod.getValue());
         }
      }

      registryField.set(NetworkRegistry.INSTANCE, ImmutableMap.copyOf(registryTmp));
      registryField.setAccessible(false);
   }

   private static void AddBranding() {
      try {
         Field brdfield = FMLCommonHandler.instance().getClass().getDeclaredField("brandings");
         brdfield.setAccessible(true);
         List<String> brd = (List)brdfield.get(FMLCommonHandler.instance());
         List<String> brdCopy = new ArrayList(brd);
         brdCopy.add("PixelRadar v5.1 by RoxoR");
         brdfield.set(FMLCommonHandler.instance(), ImmutableList.copyOf(brdCopy));
         brdfield.setAccessible(false);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         var3.printStackTrace();
      }

   }
}
