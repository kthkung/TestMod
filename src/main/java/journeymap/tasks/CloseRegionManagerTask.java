//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.tasks;

import journeymap.region.RegionManager;

public class CloseRegionManagerTask extends Task {
   private final RegionManager regionManager;

   public CloseRegionManagerTask(RegionManager regionManager) {
      this.regionManager = regionManager;
   }

   public void run() {
      this.regionManager.close();
   }

   public void onComplete() {
   }

   public boolean CheckForDuplicate() {
      return false;
   }
}
