//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import journeymap.Mw;
import journeymap.api.IMapMode;
import journeymap.api.IMapView;
import journeymap.api.MwAPI;
import journeymap.config.Config;

import java.util.List;

public class MapView implements IMapView {
   private int zoomLevel = 0;
   private int dimension = 0;
   private int textureSize = 2048;
   private double x = 0.0;
   private double z = 0.0;
   private int mapW = 0;
   private int mapH = 0;
   private int baseW = 1;
   private int baseH = 1;
   private double w = 1.0;
   private double h = 1.0;
   private int minZoom;
   private int maxZoom;
   private boolean undergroundMode;
   private boolean fullscreenMap;

   public MapView(Mw mw, boolean FullscreenMap) {
      this.minZoom = Config.zoomInLevels;
      this.maxZoom = Config.zoomOutLevels;
      this.undergroundMode = Config.undergroundMode;
      this.fullscreenMap = FullscreenMap;
      if (this.fullscreenMap) {
         this.setZoomLevel(Config.fullScreenZoomLevel);
      }

      this.setZoomLevel(Config.overlayZoomLevel);
      this.setViewCentre(mw.playerX, mw.playerZ);
   }

   public void setViewCentre(double vX, double vZ) {
      this.x = vX;
      this.z = vZ;
      if (MwAPI.getCurrentDataProvider() != null) {
         MwAPI.getCurrentDataProvider().onMapCenterChanged(vX, vZ, this);
      }

   }

   public double getX() {
      return this.x;
   }

   public double getZ() {
      return this.z;
   }

   public double getWidth() {
      return this.w;
   }

   public double getHeight() {
      return this.h;
   }

   public void panView(double relX, double relZ) {
      this.setViewCentre(this.x + relX * this.w, this.z + relZ * this.h);
   }

   public int setZoomLevel(int zoomLevel) {
      int prevZoomLevel = this.zoomLevel;
      if (this.undergroundMode) {
         this.zoomLevel = Math.min(Math.max(this.minZoom, zoomLevel), 0);
      } else {
         this.zoomLevel = Math.min(Math.max(this.minZoom, zoomLevel), this.maxZoom);
      }

      if (prevZoomLevel != this.zoomLevel) {
         this.updateZoom();
      }

      if (this.fullscreenMap) {
         Config.fullScreenZoomLevel = this.zoomLevel;
      }

      Config.overlayZoomLevel = this.zoomLevel;
      return this.zoomLevel;
   }

   private void updateZoom() {
      if (this.zoomLevel >= 0) {
         this.w = (double)(this.baseW << this.zoomLevel);
         this.h = (double)(this.baseH << this.zoomLevel);
      } else {
         this.w = (double)(this.baseW >> -this.zoomLevel);
         this.h = (double)(this.baseH >> -this.zoomLevel);
      }

      if (MwAPI.getCurrentDataProvider() != null) {
         MwAPI.getCurrentDataProvider().onZoomChanged(this.getZoomLevel(), this);
      }

   }

   public int adjustZoomLevel(int n) {
      return this.setZoomLevel(this.zoomLevel + n);
   }

   public int getZoomLevel() {
      return this.zoomLevel;
   }

   public int getRegionZoomLevel() {
      return Math.max(0, this.zoomLevel);
   }

   public void zoomToPoint(int newZoomLevel, double bX, double bZ) {
      int prevZoomLevel = this.zoomLevel;
      newZoomLevel = this.setZoomLevel(newZoomLevel);
      double zF = Math.pow(2.0, (double)(newZoomLevel - prevZoomLevel));
      this.setViewCentre(bX - (bX - this.x) * zF, bZ - (bZ - this.z) * zF);
   }

   public void setDimension(int dimension) {
      double scale = 1.0;
      if (dimension != this.dimension) {
         if (this.dimension != -1 && dimension == -1) {
            scale = 0.125;
         } else if (this.dimension == -1 && dimension != -1) {
            scale = 8.0;
         }

         this.dimension = dimension;
         this.setViewCentre(this.x * scale, this.z * scale);
      }

      if (MwAPI.getCurrentDataProvider() != null) {
         MwAPI.getCurrentDataProvider().onDimensionChanged(this.dimension, this);
      }

   }

   public void setDimensionAndAdjustZoom(int dimension) {
      int zoomLevelChange = 0;
      if (this.dimension != -1 && dimension == -1) {
         zoomLevelChange = -3;
      } else if (this.dimension == -1 && dimension != -1) {
         zoomLevelChange = 3;
      }

      this.setZoomLevel(this.getZoomLevel() + zoomLevelChange);
      this.setDimension(dimension);
   }

   public void nextDimension(List<Integer> dimensionList, int n) {
      int i = dimensionList.indexOf(this.dimension);
      i = Math.max(0, i);
      int size = dimensionList.size();
      int dimension = (Integer)dimensionList.get((i + size + n) % size);
      this.setDimensionAndAdjustZoom(dimension);
   }

   public int getDimension() {
      return this.dimension;
   }

   public void setMapWH(int w, int h) {
      if (this.mapW != w || this.mapH != h) {
         this.mapW = w;
         this.mapH = h;
         this.updateBaseWH();
      }

   }

   public void setMapWH(IMapMode mapMode) {
      this.setMapWH(mapMode.getWPixels(), mapMode.getHPixels());
   }

   public double getMinX() {
      return this.x - this.w / 2.0;
   }

   public double getMaxX() {
      return this.x + this.w / 2.0;
   }

   public double getMinZ() {
      return this.z - this.h / 2.0;
   }

   public double getMaxZ() {
      return this.z + this.h / 2.0;
   }

   public double getDimensionScaling(int playerDimension) {
      double scale;
      if (this.dimension != -1 && playerDimension == -1) {
         scale = 8.0;
      } else if (this.dimension == -1 && playerDimension != -1) {
         scale = 0.125;
      } else {
         scale = 1.0;
      }

      return scale;
   }

   public void setViewCentreScaled(double vX, double vZ, int playerDimension) {
      double scale = this.getDimensionScaling(playerDimension);
      this.setViewCentre(vX * scale, vZ * scale);
   }

   public void setTextureSize(int n) {
      if (this.textureSize != n) {
         this.textureSize = n;
         this.updateBaseWH();
      }

   }

   private void updateBaseWH() {
      int w = this.mapW;
      int h = this.mapH;

      for(int halfTextureSize = this.textureSize / 2; w > halfTextureSize || h > halfTextureSize; h /= 2) {
         w /= 2;
      }

      this.baseW = w;
      this.baseH = h;
      this.updateZoom();
   }

   public int getPixelsPerBlock() {
      return this.mapW / this.baseW;
   }

   public boolean isBlockWithinView(double bX, double bZ, boolean circular) {
      boolean inside;
      if (!circular) {
         inside = bX > this.getMinX() || bX < this.getMaxX() || bZ > this.getMinZ() || bZ < this.getMaxZ();
      } else {
         double x = bX - this.x;
         double z = bZ - this.z;
         double r = this.getHeight() / 2.0;
         inside = x * x + z * z < r * r;
      }

      return inside;
   }

   public boolean getUndergroundMode() {
      return this.undergroundMode;
   }

   public void setUndergroundMode(boolean enabled) {
      if (enabled && this.zoomLevel >= 0) {
         this.setZoomLevel(-1);
      }

      this.undergroundMode = enabled;
   }
}
