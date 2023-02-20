//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import journeymap.config.Config;
import journeymap.region.MwChunk;
import journeymap.tasks.SaveChunkTask;
import journeymap.tasks.UpdateSurfaceChunksTask;
import journeymap.util.Utils;

public class ChunkManager {
   public Mw mw;
   private boolean closed = false;
   private CircularHashMap<Chunk, Integer> chunkMap = new CircularHashMap();
   private static final int VISIBLE_FLAG = 1;
   private static final int VIEWED_FLAG = 2;

   public ChunkManager(Mw mw) {
      this.mw = mw;
   }

   public synchronized void close() {
      this.closed = true;
      this.saveChunks();
      this.chunkMap.clear();
   }

   public static MwChunk copyToMwChunk(Chunk chunk) {
      Map<BlockPos, TileEntity> TileEntityMap = Maps.newHashMap();
      TileEntityMap = Utils.checkedMapByCopy(chunk.getTileEntityMap(), BlockPos.class, TileEntity.class, false);
      byte[] biomeArray = Arrays.copyOf(chunk.getBiomeArray(), chunk.getBiomeArray().length);
      ExtendedBlockStorage[] dataArray = (ExtendedBlockStorage[])Arrays.copyOf(chunk.getBlockStorageArray(), chunk.getBlockStorageArray().length);
      return new MwChunk(chunk.getPos().x, chunk.getPos().z, chunk.getWorld().provider.getDimensionType().getId(), dataArray, biomeArray, TileEntityMap);
   }

   public synchronized void addChunk(Chunk chunk) {
      if (!this.closed && chunk != null) {
         this.chunkMap.put(chunk, 0);
      }

   }

   public synchronized void removeChunk(Chunk chunk) {
      if (!this.closed && chunk != null) {
         if (!this.chunkMap.containsKey(chunk)) {
            return;
         }

         int flags = (Integer)this.chunkMap.get(chunk);
         if ((flags & 2) != 0) {
            this.addSaveChunkTask(chunk);
         }

         this.chunkMap.remove(chunk);
      }

   }

   public synchronized void saveChunks() {
      Iterator var1 = this.chunkMap.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry<Chunk, Integer> entry = (Map.Entry)var1.next();
         int flags = (Integer)entry.getValue();
         if ((flags & 2) != 0) {
            this.addSaveChunkTask((Chunk)entry.getKey());
         }
      }

   }

   public void updateUndergroundChunks() {
      int chunkArrayX = (this.mw.playerXInt >> 4) - 1;
      int chunkArrayZ = (this.mw.playerZInt >> 4) - 1;
      MwChunk[] chunkArray = new MwChunk[9];

      for(int z = 0; z < 3; ++z) {
         for(int x = 0; x < 3; ++x) {
            Chunk chunk = this.mw.mc.world.getChunk(chunkArrayX + x, chunkArrayZ + z);
            if (!chunk.isEmpty()) {
               chunkArray[z * 3 + x] = copyToMwChunk(chunk);
            }
         }
      }

   }

   public void updateSurfaceChunks() {
      int chunksToUpdate = Math.min(this.chunkMap.size(), Config.chunksPerTick);
      MwChunk[] chunkArray = new MwChunk[chunksToUpdate];

      for(int i = 0; i < chunksToUpdate; ++i) {
         Map.Entry<Chunk, Integer> entry = this.chunkMap.getNextEntry();
         if (entry != null) {
            Chunk chunk = (Chunk)entry.getKey();
            int flags = (Integer)entry.getValue();
            if (Utils.distToChunkSq(this.mw.playerXInt, this.mw.playerZInt, chunk) <= Config.maxChunkSaveDistSq) {
               flags |= 3;
            } else {
               flags &= -2;
            }

            entry.setValue(flags);
            if ((flags & 1) != 0) {
               chunkArray[i] = copyToMwChunk(chunk);
               this.mw.executor.addTask(new UpdateSurfaceChunksTask(this.mw, chunkArray[i]));
            } else {
               chunkArray[i] = null;
            }
         }
      }

   }

   public void onTick() {
      if (!this.closed) {
         if ((this.mw.tickCounter & 15) == 0) {
            this.updateUndergroundChunks();
         } else {
            this.updateSurfaceChunks();
         }
      }

   }

   private void addSaveChunkTask(Chunk chunk) {
      if ((Minecraft.getMinecraft().isSingleplayer() && Config.regionFileOutputEnabledMP || !Minecraft.getMinecraft().isSingleplayer() && Config.regionFileOutputEnabledSP) && !chunk.isEmpty()) {
         this.mw.executor.addTask(new SaveChunkTask(copyToMwChunk(chunk), this.mw.regionManager));
      }

   }
}
