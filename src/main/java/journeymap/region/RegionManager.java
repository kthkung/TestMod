//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import org.apache.logging.log4j.Logger;
import journeymap.util.Logging;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegionManager {
   private final LruCache regionMap;
   public final File worldDir;
   public final File imageDir;
   public BlockColours blockColours;
   public static Logger logger;
   public final RegionFileCache regionFileCache;
   public int maxZoom;
   public int minZoom;

   public RegionManager(File worldDir, File imageDir, BlockColours blockColours, int minZoom, int maxZoom) {
      this.worldDir = worldDir;
      this.imageDir = imageDir;
      this.blockColours = blockColours;
      this.regionMap = new LruCache();
      this.regionFileCache = new RegionFileCache(worldDir);
      this.minZoom = minZoom;
      this.maxZoom = maxZoom;
   }

   public void close() {
      Iterator var1 = this.regionMap.values().iterator();

      while(var1.hasNext()) {
         Region region = (Region)var1.next();
         if (region != null) {
            region.close();
         }
      }

      this.regionMap.clear();
      this.regionFileCache.close();
   }

   private static int incrStatsCounter(Map<String, Integer> h, String key) {
      int n = 1;
      if (h.containsKey(key)) {
         n = (Integer)h.get(key) + 1;
      }

      h.put(key, n);
      return n;
   }

   public void printLoadedRegionStats() {
      Logging.logInfo("loaded region listing:", new Object[0]);
      Map<String, Integer> stats = new HashMap();
      Iterator var2 = this.regionMap.values().iterator();

      while(var2.hasNext()) {
         Region region = (Region)var2.next();
         Logging.logInfo("  %s", new Object[]{region});
         incrStatsCounter(stats, String.format("dim%d", region.dimension));
         incrStatsCounter(stats, String.format("zoom%d", region.zoomLevel));
         incrStatsCounter(stats, "total");
      }

      Logging.logInfo("loaded region stats:", new Object[0]);
      var2 = stats.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<String, Integer> e = (Map.Entry)var2.next();
         Logging.logInfo("  %s: %d", new Object[]{e.getKey(), e.getValue()});
      }

   }

   public Region getRegion(int x, int z, int zoomLevel, int dimension) {
      Region region = (Region)this.regionMap.get(Region.getKey(x, z, zoomLevel, dimension));
      if (region == null) {
         region = new Region(this, x, z, zoomLevel, dimension);
         this.regionMap.put(region.key, region);
      }

      return region;
   }

   public void updateChunk(MwChunk chunk) {
      Region region = this.getRegion(chunk.x << 4, chunk.z << 4, 0, chunk.dimension);
      region.updateChunk(chunk);
   }

   public void rebuildRegions(int xStart, int zStart, int w, int h, int dimension) {
      xStart &= -512;
      zStart &= -512;
      w = w + 512 & -512;
      h = h + 512 & -512;
      Logging.logInfo("rebuilding regions from (%d, %d) to (%d, %d)", new Object[]{xStart, zStart, xStart + w, zStart + h});

      for(int rX = xStart; rX < xStart + w; rX += 512) {
         for(int rZ = zStart; rZ < zStart + h; rZ += 512) {
            Region region = this.getRegion(rX, rZ, 0, dimension);
            if (this.regionFileCache.regionFileExists(rX, rZ, dimension)) {
               region.clear();

               for(int cz = 0; cz < 32; ++cz) {
                  for(int cx = 0; cx < 32; ++cx) {
                     MwChunk chunk = MwChunk.read((region.x >> 4) + cx, (region.z >> 4) + cz, region.dimension, this.regionFileCache);
                     region.updateChunk(chunk);
                  }
               }
            }

            region.updateZoomLevels();
         }
      }

   }

   class LruCache extends LinkedHashMap<Long, Region> {
      private static final long serialVersionUID = 1L;
      private static final int MAX_LOADED_REGIONS = 64;

      public LruCache() {
         super(128, 0.5F, true);
      }

      protected boolean removeEldestEntry(Map.Entry<Long, Region> entry) {
         boolean ret = false;
         if (this.size() > 64) {
            Region region = (Region)entry.getValue();
            region.close();
            ret = true;
         }

         return ret;
      }
   }
}
