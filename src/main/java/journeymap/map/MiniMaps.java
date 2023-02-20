//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import java.util.ArrayList;
import java.util.List;
import journeymap.Mw;
import journeymap.config.Config;
import journeymap.map.mapmode.MapMode;

public class MiniMaps {
   public MapMode smallMapMode;
   public MapMode largeMapMode;
   public MapMode guiMapMode;
   public MapView view;
   public MapRenderer smallMap;
   public MapRenderer largeMap;
   private List<MapRenderer> mapList;
   private MapRenderer currentMap = null;

   public MiniMaps(Mw mw) {
      this.view = new MapView(mw, false);
      this.view.setZoomLevel(Config.overlayZoomLevel);
      this.smallMapMode = new MapMode(Config.smallMap);
      this.smallMap = new MapRenderer(mw, this.smallMapMode, this.view);
      this.largeMapMode = new MapMode(Config.largeMap);
      this.largeMap = new MapRenderer(mw, this.largeMapMode, this.view);
      this.mapList = new ArrayList();
      if (this.smallMapMode.getConfig().enabled) {
         this.mapList.add(this.smallMap);
      }

      if (this.largeMapMode.getConfig().enabled) {
         this.mapList.add(this.largeMap);
      }

      this.mapList.add((MapRenderer) null);
      this.nextOverlayMode(0);
      this.currentMap = (MapRenderer)this.mapList.get(Config.overlayModeIndex);
   }

   public void close() {
      this.mapList.clear();
      this.currentMap = null;
   }

   public MapRenderer nextOverlayMode(int increment) {
      int size = this.mapList.size();
      Config.overlayModeIndex = (Config.overlayModeIndex + size + increment) % size;
      MapRenderer newMap = (MapRenderer)this.mapList.get(Config.overlayModeIndex);
      this.currentMap = newMap;
      return this.currentMap;
   }

   public void drawCurrentMap() {
      if (this.currentMap != null) {
         this.currentMap.draw();
      }

   }
}
