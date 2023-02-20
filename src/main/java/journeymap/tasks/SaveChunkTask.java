//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.tasks;

import journeymap.region.MwChunk;
import journeymap.region.RegionManager;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaveChunkTask extends Task {
   private MwChunk chunk;
   private RegionManager regionManager;
   private AtomicBoolean Running = new AtomicBoolean();
   private static HashMap<Long, SaveChunkTask> chunksUpdating = new HashMap();

   public SaveChunkTask(MwChunk chunk, RegionManager regionManager) {
      this.chunk = chunk;
      this.regionManager = regionManager;
   }

   public void run() {
      this.Running.set(true);
      this.chunk.write(this.regionManager.regionFileCache);
   }

   public void onComplete() {
      Long coords = this.chunk.getCoordIntPair();
      chunksUpdating.remove(coords);
      this.Running.set(false);
   }

   public boolean CheckForDuplicate() {
      Long coords = this.chunk.getCoordIntPair();
      if (!chunksUpdating.containsKey(coords)) {
         chunksUpdating.put(coords, this);
         return false;
      } else {
         SaveChunkTask task2 = (SaveChunkTask)chunksUpdating.get(coords);
         if (!task2.Running.get()) {
            task2.UpdateChunkData(this.chunk, this.regionManager);
            return true;
         } else {
            chunksUpdating.put(coords, this);
            return false;
         }
      }
   }

   public void UpdateChunkData(MwChunk chunk, RegionManager regionManager) {
      this.chunk = chunk;
      this.regionManager = regionManager;
   }
}
