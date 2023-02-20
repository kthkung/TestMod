//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.api;

import java.awt.Point;
import java.awt.geom.Point2D;

public interface IMapMode {
   void setScreenRes(int var1, int var2, int var3, int var4, double var5);

   void setScreenRes();

   void updateMargin();

   Point screenXYtoBlockXZ(IMapView var1, int var2, int var3);

   Point2D.Double blockXZtoScreenXY(IMapView var1, double var2, double var4);

   Point2D.Double getClampedScreenXY(IMapView var1, double var2, double var4);

   boolean posWithin(int var1, int var2);

   Point2D.Double getNewPosPoint(double var1, double var3);

   int getXTranslation();

   int getYTranslation();

   int getX();

   int getY();

   int getW();

   int getH();

   int getWPixels();

   int getHPixels();

   IMapModeConfig getConfig();

   int getTextX();

   int getTextY();

   int getTextColour();
}
