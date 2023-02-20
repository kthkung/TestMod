package journeymap.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.util.math.ChunkPos;
import journeymap.Mw;
import journeymap.map.MapTexture;
import journeymap.region.MwChunk;
import journeymap.region.RegionManager;

public class UpdateSurfaceChunksTask extends Task {
   private MwChunk chunk;
   private RegionManager regionManager;
   private MapTexture mapTexture;
   private AtomicBoolean Running = new AtomicBoolean();
   private static Map chunksUpdating = new HashMap();

   public UpdateSurfaceChunksTask(Mw mw, MwChunk chunk) {
      this.mapTexture = mw.mapTexture;
      this.regionManager = mw.regionManager;
      this.chunk = chunk;
   }

   public void run() {
      this.Running.set(true);
      if (this.chunk != null) {
         this.regionManager.updateChunk(this.chunk);
         this.mapTexture.updateArea(this.regionManager, this.chunk.x << 4, this.chunk.z << 4, 16, 16, this.chunk.dimension);
      }

   }

   public void onComplete() {
      Long coords = this.chunk.getCoordIntPair();
      chunksUpdating.remove(coords);
      this.Running.set(false);
   }

   public void UpdateChunkData(MwChunk chunk) {
      this.chunk = chunk;
   }

   public boolean CheckForDuplicate() {
      Long coords = ChunkPos.asLong(this.chunk.x, this.chunk.z);
      if (!chunksUpdating.containsKey(coords)) {
         chunksUpdating.put(coords, this);
         return false;
      } else {
         UpdateSurfaceChunksTask task2 = (UpdateSurfaceChunksTask)chunksUpdating.get(coords);
         if (!task2.Running.get()) {
            task2.UpdateChunkData(this.chunk);
            return true;
         } else {
            chunksUpdating.put(coords, this);
            return false;
         }
      }
   }
}
