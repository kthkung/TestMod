//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.tasks;

import net.minecraft.client.resources.I18n;
import journeymap.Mw;
import journeymap.region.BlockColours;
import journeymap.region.RegionManager;
import journeymap.util.Utils;

public class RebuildRegionsTask extends Task {
   final RegionManager regionManager;
   final BlockColours blockColours;
   final int x;
   final int z;
   final int w;
   final int h;
   final int dimension;
   String msg = "";

   public RebuildRegionsTask(Mw mw, int x, int z, int w, int h, int dimension) {
      this.regionManager = mw.regionManager;
      this.blockColours = mw.blockColours;
      this.x = x;
      this.z = z;
      this.w = w;
      this.h = h;
      this.dimension = dimension;
   }

   public void run() {
      this.regionManager.blockColours = this.blockColours;
      this.regionManager.rebuildRegions(this.x, this.z, this.w, this.h, this.dimension);
   }

   public void onComplete() {
      Utils.printBoth(I18n.format("mw.task.rebuildregionstask.chatmsg.rebuild.compleet", new Object[0]));
   }

   public boolean CheckForDuplicate() {
      return false;
   }
}
