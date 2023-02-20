//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.api;

public interface IMapModeConfig {
   String getConfigCategory();

   String getMapPosCategory();

   String[] getCoordsModeStringArray();

   boolean getEnabled();

   boolean getRotate();

   boolean getCircular();

   String getCoordsMode();

   boolean getBorderMode();

   int getPlayerArrowSize();

   int getMarkerSize();

   int getTrailMarkerSize();

   int getAlphaPercent();

   String getBiomeMode();

   double getXPos();

   double getYPos();

   double getHeightPercent();

   double getWidthPercent();
}
