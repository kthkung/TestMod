//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.tasks;

import journeymap.map.MapTexture;
import journeymap.map.MapViewRequest;
import journeymap.region.RegionManager;

public class MapUpdateViewTask extends Task {
   final MapViewRequest req;
   RegionManager regionManager;
   MapTexture mapTexture;

   public MapUpdateViewTask(MapTexture mapTexture, RegionManager regionManager, MapViewRequest req) {
      this.mapTexture = mapTexture;
      this.regionManager = regionManager;
      this.req = req;
   }

   public void run() {
      this.mapTexture.loadRegions(this.regionManager, this.req);
   }

   public void onComplete() {
      this.mapTexture.setLoaded(this.req);
   }

   public boolean CheckForDuplicate() {
      return false;
   }
}
