//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.api;

import java.util.ArrayList;

public interface IMwDataProvider {
   ArrayList<IMwChunkOverlay> getChunksOverlay(int var1, double var2, double var4, double var6, double var8, double var10, double var12);

   String getStatusString(int var1, int var2, int var3, int var4);

   void onMiddleClick(int var1, int var2, int var3, IMapView var4);

   void onDimensionChanged(int var1, IMapView var2);

   void onMapCenterChanged(double var1, double var3, IMapView var5);

   void onZoomChanged(int var1, IMapView var2);

   void onOverlayActivated(IMapView var1);

   void onOverlayDeactivated(IMapView var1);

   void onDraw(IMapView var1, IMapMode var2);

   boolean onMouseInput(IMapView var1, IMapMode var2);

   ILabelInfo getLabelInfo(int var1, int var2);
}
