//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
 import journeymap.asm.obf.ObfuscationUtils;

import java.util.Map;

@SortingIndex(1002)
@TransformerExclusions({"pixelradar"})
public class PokeradarLoadingPlugin implements IFMLLoadingPlugin {
   public static boolean isLoaded = false;

   public PokeradarLoadingPlugin() {
      System.out.println("> Loading Pixelradar Transformers");
      isLoaded = true;
   }

   public String[] getASMTransformerClass() {
      return new String[]{"pixelradar.asm.AllowedCharactersTransformer", "pixelradar.asm.UnlimitedCharactersTransformer"};
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return null;
   }

   public void injectData(Map<String, Object> data) {
      ObfuscationUtils.setRuntimeDeobfuscated((Boolean)data.get("runtimeDeobfuscationEnabled"));
   }

   public String getAccessTransformerClass() {
      return this.getASMTransformerClass()[0];
   }
}
