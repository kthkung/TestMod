//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegionFileCache {
   private LruCache regionFileCache = new LruCache();
   private File worldDir;

   public RegionFileCache(File worldDir) {
      this.worldDir = worldDir;
   }

   public void close() {
      Iterator var1 = this.regionFileCache.values().iterator();

      while(var1.hasNext()) {
         RegionFile regionFile = (RegionFile)var1.next();
         regionFile.close();
      }

      this.regionFileCache.clear();
   }

   public File getRegionFilePath(int x, int z, int dimension) {
      File dir = this.worldDir;
      if (dimension != 0) {
         dir = new File(dir, "DIM" + dimension);
      }

      dir = new File(dir, "region");
      String filename = String.format("r.%d.%d.mca", x >> 9, z >> 9);
      return new File(dir, filename);
   }

   public boolean regionFileExists(int x, int z, int dimension) {
      File regionFilePath = this.getRegionFilePath(x, z, dimension);
      return regionFilePath.isFile();
   }

   public RegionFile getRegionFile(int x, int z, int dimension) {
      File regionFilePath = this.getRegionFilePath(x, z, dimension);
      String key = regionFilePath.toString();
      RegionFile regionFile = (RegionFile)this.regionFileCache.get(key);
      if (regionFile == null) {
         regionFile = new RegionFile(regionFilePath);
         this.regionFileCache.put(key, regionFile);
      }

      return regionFile;
   }

   class LruCache extends LinkedHashMap<String, RegionFile> {
      private static final long serialVersionUID = 1L;
      static final int MAX_REGION_FILES_OPEN = 8;

      public LruCache() {
         super(16, 0.5F, true);
      }

      protected boolean removeEldestEntry(Map.Entry<String, RegionFile> entry) {
         boolean ret = false;
         if (this.size() > 8) {
            RegionFile regionFile = (RegionFile)entry.getValue();
            regionFile.close();
            ret = true;
         }

         return ret;
      }
   }
}
