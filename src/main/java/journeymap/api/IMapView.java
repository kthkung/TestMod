//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.api;

import java.util.List;

public interface IMapView {
   void setViewCentre(double var1, double var3);

   double getX();

   double getZ();

   double getWidth();

   double getHeight();

   void panView(double var1, double var3);

   int setZoomLevel(int var1);

   int adjustZoomLevel(int var1);

   int getZoomLevel();

   int getRegionZoomLevel();

   void zoomToPoint(int var1, double var2, double var4);

   void setDimension(int var1);

   void setDimensionAndAdjustZoom(int var1);

   void nextDimension(List<Integer> var1, int var2);

   int getDimension();

   void setMapWH(int var1, int var2);

   void setMapWH(IMapMode var1);

   double getMinX();

   double getMaxX();

   double getMinZ();

   double getMaxZ();

   double getDimensionScaling(int var1);

   void setViewCentreScaled(double var1, double var3, int var5);

   void setTextureSize(int var1);

   int getPixelsPerBlock();

   boolean isBlockWithinView(double var1, double var3, boolean var5);

   boolean getUndergroundMode();

   void setUndergroundMode(boolean var1);
}
