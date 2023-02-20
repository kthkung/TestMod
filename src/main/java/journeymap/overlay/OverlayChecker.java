//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.overlay;

import journeymap.api.*;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;

public class OverlayChecker implements IMwDataProvider {
   public OverlayChecker() {
   }

   public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
      int minChunkX = (MathHelper.ceil(minX) >> 4) - 1;
      int minChunkZ = (MathHelper.ceil(minZ) >> 4) - 1;
      int maxChunkX = (MathHelper.ceil(maxX) >> 4) + 1;
      int maxChunkZ = (MathHelper.ceil(maxZ) >> 4) + 1;
      int cX = (MathHelper.ceil(centerX) >> 4) + 1;
      int cZ = (MathHelper.ceil(centerZ) >> 4) + 1;
      int limitMinX = Math.max(minChunkX, cX - 100);
      int limitMaxX = Math.min(maxChunkX, cX + 100);
      int limitMinZ = Math.max(minChunkZ, cZ - 100);
      int limitMaxZ = Math.min(maxChunkZ, cZ + 100);
      ArrayList<IMwChunkOverlay> chunks = new ArrayList();

      for(int x = limitMinX; x <= limitMaxX; ++x) {
         for(int z = limitMinZ; z <= limitMaxZ; ++z) {
            if ((x + z) % 2 == 0) {
               chunks.add(new ChunkOverlay(x, z));
            }
         }
      }

      return chunks;
   }

   public String getStatusString(int dim, int bX, int bY, int bZ) {
      return "";
   }

   public void onMiddleClick(int dim, int bX, int bZ, IMapView mapview) {
   }

   public void onDimensionChanged(int dimension, IMapView mapview) {
   }

   public void onMapCenterChanged(double vX, double vZ, IMapView mapview) {
   }

   public void onZoomChanged(int level, IMapView mapview) {
   }

   public void onOverlayActivated(IMapView mapview) {
   }

   public void onOverlayDeactivated(IMapView mapview) {
   }

   public void onDraw(IMapView mapview, IMapMode mapmode) {
   }

   public boolean onMouseInput(IMapView mapview, IMapMode mapmode) {
      return false;
   }

   public ILabelInfo getLabelInfo(int mouseX, int mouseY) {
      return null;
   }

   public class ChunkOverlay implements IMwChunkOverlay {
      Point coord;

      public ChunkOverlay(int x, int z) {
         this.coord = new Point(x, z);
      }

      public Point getCoordinates() {
         return this.coord;
      }

      public int getColor() {
         return -1862270977;
      }

      public float getFilling() {
         return 1.0F;
      }

      public boolean hasBorder() {
         return true;
      }

      public float getBorderWidth() {
         return 0.5F;
      }

      public int getBorderColor() {
         return -16777216;
      }
   }
}
