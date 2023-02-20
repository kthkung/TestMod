//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import journeymap.BackgroundExecutor;
import journeymap.region.Region;
import journeymap.region.RegionManager;
import journeymap.tasks.MapUpdateViewTask;
import journeymap.util.Texture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTexture extends Texture {
   public int textureRegions;
   public int textureSize;
   private MapViewRequest loadedView = null;
   private MapViewRequest requestedView = null;
   private Region[] regionArray;
   private List<Rect> textureUpdateQueue = new ArrayList();

   public MapTexture(int textureSize, boolean linearScaling) {
      super(textureSize, textureSize, 0, 9729, 9729, 10497);
      this.setLinearScaling(linearScaling);
      this.textureRegions = textureSize >> 9;
      this.textureSize = textureSize;
      this.regionArray = new Region[this.textureRegions * this.textureRegions];
   }

   public void requestView(MapViewRequest req, BackgroundExecutor executor, RegionManager regionManager) {
      if (this.requestedView == null || !this.requestedView.equals(req)) {
         this.requestedView = req;
         executor.addTask(new MapUpdateViewTask(this, regionManager, req));
      }

   }

   public void processTextureUpdates() {
      synchronized(this.textureUpdateQueue) {
         Iterator var2 = this.textureUpdateQueue.iterator();

         while(var2.hasNext()) {
            Rect rect = (Rect)var2.next();
            this.updateTextureArea(rect.x, rect.y, rect.w, rect.h);
         }

         this.textureUpdateQueue.clear();
      }
   }

   public void setLoaded(MapViewRequest req) {
      this.loadedView = req;
   }

   public boolean isLoaded(MapViewRequest req) {
      return this.loadedView != null && this.loadedView.mostlyEquals(req);
   }

   public synchronized void setRGBOpaque(int x, int y, int w, int h, int[] pixels, int offset, int scanSize) {
      int bufOffset = y * this.w + x;

      for(int i = 0; i < h; ++i) {
         this.setPixelBufPosition(bufOffset + i * this.w);
         int rowOffset = offset + i * scanSize;

         for(int j = 0; j < w; ++j) {
            int colour = pixels[rowOffset + j];
            if (colour != 0) {
               colour |= -16777216;
            }

            this.pixelBufPut(colour);
         }
      }

   }

   public void addTextureUpdate(int x, int z, int w, int h) {
      synchronized(this.textureUpdateQueue) {
         this.textureUpdateQueue.add(new Rect(x, z, w, h));
      }
   }

   public void updateTextureFromRegion(Region region, int x, int z, int w, int h) {
      int tx = x >> region.zoomLevel & this.w - 1;
      int ty = z >> region.zoomLevel & this.h - 1;
      int tw = w >> region.zoomLevel;
      int th = h >> region.zoomLevel;
      tw = Math.min(tw, this.w - tx);
      th = Math.min(th, this.h - th);
      int[] pixels = region.getPixels();
      if (pixels != null) {
         this.setRGBOpaque(tx, ty, tw, th, pixels, region.getPixelOffset(x, z), 512);
      } else {
         this.fillRect(tx, ty, tw, th, 0);
      }

      this.addTextureUpdate(tx, ty, tw, th);
   }

   public int getRegionIndex(int x, int z, int zoomLevel) {
      x = x >> 9 + zoomLevel & this.textureRegions - 1;
      z = z >> 9 + zoomLevel & this.textureRegions - 1;
      return z * this.textureRegions + x;
   }

   public boolean loadRegion(RegionManager regionManager, int x, int z, int zoomLevel, int dimension) {
      boolean loaded = false;
      int index = this.getRegionIndex(x, z, zoomLevel);
      Region currentRegion = this.regionArray[index];
      if (currentRegion == null || !currentRegion.equals(x, z, zoomLevel, dimension)) {
         Region newRegion = regionManager.getRegion(x, z, zoomLevel, dimension);
         this.regionArray[index] = newRegion;
         this.updateTextureFromRegion(newRegion, newRegion.x, newRegion.z, newRegion.size, newRegion.size);
         loaded = true;
      }

      return loaded;
   }

   public int loadRegions(RegionManager regionManager, MapViewRequest req) {
      int size = 512 << req.zoomLevel;
      int loadedCount = 0;

      for(int z = req.zMin; z <= req.zMax; z += size) {
         for(int x = req.xMin; x <= req.xMax; x += size) {
            if (this.loadRegion(regionManager, x, z, req.zoomLevel, req.dimension)) {
               ++loadedCount;
            }
         }
      }

      return loadedCount;
   }

   public void updateArea(RegionManager regionManager, int x, int z, int w, int h, int dimension) {
      for(int i = 0; i < this.regionArray.length; ++i) {
         Region region = this.regionArray[i];
         if (region != null && region.isAreaWithin(x, z, w, h, dimension)) {
            this.updateTextureFromRegion(region, x, z, w, h);
         }
      }

   }

   private class Rect {
      final int x;
      final int y;
      final int w;
      final int h;

      Rect(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }
   }
}
