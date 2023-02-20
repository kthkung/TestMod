//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

import net.minecraftforge.common.config.Configuration;
import journeymap.Mw;
import journeymap.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldConfig {
   private static WorldConfig instance = null;
   public Configuration worldConfiguration = null;
   public List<Integer> dimensionList = new ArrayList();

   private WorldConfig() {
      File worldConfigFile = new File(Mw.getInstance().worldDir, "pixelradar.cfg");
      this.worldConfiguration = new Configuration(worldConfigFile);
      this.InitDimensionList();
   }

   public static WorldConfig getInstance() {
      if (instance == null) {
         Class var0 = WorldConfig.class;
         synchronized(WorldConfig.class) {
            if (instance == null) {
               instance = new WorldConfig();
            }
         }
      }

      return instance;
   }

   public void saveWorldConfig() {
      this.worldConfiguration.save();
   }

   public void InitDimensionList() {
      this.dimensionList.clear();
      this.worldConfiguration.get("world", "dimensionList", Utils.integerListToIntArray(this.dimensionList));
      this.addDimension(0);
      this.cleanDimensionList();
   }

   public void addDimension(int dimension) {
      int i = this.dimensionList.indexOf(dimension);
      if (i < 0) {
         this.dimensionList.add(dimension);
      }

   }

   public void cleanDimensionList() {
      List<Integer> dimensionListCopy = new ArrayList(this.dimensionList);
      this.dimensionList.clear();
      Iterator var2 = dimensionListCopy.iterator();

      while(var2.hasNext()) {
         int dimension = (Integer)var2.next();
         this.addDimension(dimension);
      }

   }
}
