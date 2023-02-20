//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import journeymap.util.Logging;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MwChunk implements IChunk {
   public static final int SIZE = 16;
   public final int x;
   public final int z;
   public final int dimension;
   public ExtendedBlockStorage[] dataArray = new ExtendedBlockStorage[16];
   public final Map<BlockPos, TileEntity> tileentityMap;
   public final byte[] biomeArray;
   public final int maxY;
   private static Method CarpenterMethod = null;
   private static Method FMPMethodParts = null;
   private static Method FMPMethodMaterial = null;
   private static Field FMPFieldBlock = null;
   private static Field FMPFieldMeta = null;

   public MwChunk(int x, int z, int dimension, ExtendedBlockStorage[] data, byte[] biomeArray, Map<BlockPos, TileEntity> TileEntityMap) {
      this.x = x;
      this.z = z;
      this.dimension = dimension;
      this.biomeArray = biomeArray;
      this.tileentityMap = TileEntityMap;
      this.dataArray = data;
      int maxY = 0;

      for(int y = 0; y < 16; ++y) {
         if (data[y] != null) {
            maxY = (y << 4) + 15;
         }
      }

      this.maxY = maxY;
   }

   public String toString() {
      return String.format("(%d, %d) dim%d", this.x, this.z, this.dimension);
   }

   public static MwChunk read(int x, int z, int dimension, RegionFileCache regionFileCache) {
      Boolean flag = true;
      byte[] biomeArray = null;
      ExtendedBlockStorage[] data = new ExtendedBlockStorage[16];
      Map<BlockPos, TileEntity> TileEntityMap = new HashMap();
      DataInputStream dis = null;
      RegionFile regionFile = regionFileCache.getRegionFile(x << 4, z << 4, dimension);
      if (!regionFile.isOpen() && regionFile.exists()) {
         regionFile.open();
      }

      if (regionFile.isOpen()) {
         dis = regionFile.getChunkDataInputStream(x & 31, z & 31);
      }

      if (dis != null) {
         try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(dis);
            NBTTagCompound level = nbttagcompound.getCompoundTag("Level");
            int xNbt = level.getInteger("xPos");
            int zNbt = level.getInteger("zPos");
            if (xNbt != x || zNbt != z) {
               Logging.logWarning("chunk (%d, %d) has NBT coords (%d, %d)", new Object[]{x, z, xNbt, zNbt});
            }

            NBTTagList sections = level.getTagList("Sections", 10);

            for(int k = 0; k < sections.tagCount(); ++k) {
               NBTTagCompound section = sections.getCompoundTagAt(k);
               int y = section.getByte("Y");
               ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(y << 4, flag);
               byte[] abyte = nbttagcompound.getByteArray("Blocks");
               NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
               NibbleArray nibblearray1 = nbttagcompound.hasKey("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
               extendedblockstorage.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
               extendedblockstorage.setBlockLight(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
               if (flag) {
                  extendedblockstorage.setBlockLight(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
               }

               data[y] = extendedblockstorage;
            }

            biomeArray = level.getByteArray("Biomes");
            NBTTagList nbttaglist2 = level.getTagList("TileEntities", 10);
            if (!nbttaglist2.isEmpty()) {
               for(int i1 = 0; i1 < nbttaglist2.tagCount(); ++i1) {
                  NBTTagCompound nbttagcompound4 = nbttaglist2.getCompoundTagAt(i1);
                  TileEntity tileentity = TileEntity.create((World)null, nbttagcompound4);
                  if (tileentity != null) {
                     TileEntityMap.put(tileentity.getPos(), tileentity);
                  }
               }
            }
         } catch (IOException var30) {
            Logging.logError("%s: could not read chunk (%d, %d) from region file\n", new Object[]{var30, x, z});
         } finally {
            try {
               dis.close();
            } catch (IOException var29) {
               Logging.logError("MwChunk.read: %s while closing input stream", new Object[]{var29});
            }

         }
      }

      return new MwChunk(x, z, dimension, data, biomeArray, TileEntityMap);
   }

   public boolean isEmpty() {
      return this.maxY <= 0;
   }

   public int getBiome(int x, int y, int z) {
      int i = x & 15;
      int j = z & 15;
      int k = this.biomeArray[j << 4 | i] & 255;
      if (k == 255) {
         Biome biome = Minecraft.getMinecraft().world.getBiomeProvider().getBiome(new BlockPos(k, k, k), Biomes.PLAINS);
         k = Biome.getIdForBiome(biome);
      }

      return k;
   }

   public int getLightValue(int x, int y, int z) {
      return 15;
   }

   public int getMaxY() {
      return this.maxY;
   }

   public static void carpenterdata() {
      try {
         Class<?> act = Class.forName("com.carpentersblocks.tileentity.TEBase");
         CarpenterMethod = act.getMethod("getAttribute", Byte.TYPE);
      } catch (SecurityException var1) {
      } catch (NoSuchMethodException var2) {
      } catch (ClassNotFoundException var3) {
      }

   }

   public static void FMPdata() {
      try {
         Class<?> act = Class.forName("codechicken.multipart.TileMultipart");
         FMPMethodParts = act.getMethod("jPartList");
         act = Class.forName("codechicken.microblock.Microblock");
         FMPMethodMaterial = act.getMethod("getIMaterial");
         act = Class.forName("codechicken.microblock.BlockMicroMaterial");
         FMPFieldBlock = act.getDeclaredField("block");
         FMPFieldBlock.setAccessible(true);
         FMPFieldMeta = act.getDeclaredField("meta");
         FMPFieldMeta.setAccessible(true);
      } catch (SecurityException var1) {
      } catch (NoSuchMethodException var2) {
      } catch (ClassNotFoundException var3) {
      } catch (NoSuchFieldException var4) {
      }

   }

   public IBlockState getBlockState(int x, int y, int z) {
      int yi = y >> 4 & 15;
      return this.dataArray != null && this.dataArray[yi] != null ? this.dataArray[yi].getData().get(x & 15, y & 15, z & 15) : Blocks.AIR.getDefaultState();
   }

   private NBTTagCompound writeChunkToNBT() {
      NBTTagCompound level = new NBTTagCompound();
      NBTTagCompound compound = new NBTTagCompound();
      level.setTag("Level", compound);
      compound.setInteger("xPos", this.x);
      compound.setInteger("zPos", this.z);
      ExtendedBlockStorage[] aextendedblockstorage = this.dataArray;
      NBTTagList nbttaglist = new NBTTagList();
      boolean flag = true;
      ExtendedBlockStorage[] var6 = aextendedblockstorage;
      int var7 = aextendedblockstorage.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         ExtendedBlockStorage extendedblockstorage = var6[var8];
         if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
            byte[] abyte = new byte[4096];
            NibbleArray nibblearray = new NibbleArray();
            NibbleArray nibblearray1 = extendedblockstorage.getData().getDataForNBT(abyte, nibblearray);
            nbttagcompound.setByteArray("Blocks", abyte);
            nbttagcompound.setByteArray("Data", nibblearray.getData());
            if (nibblearray1 != null) {
               nbttagcompound.setByteArray("Add", nibblearray1.getData());
            }

            nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlockLight().getData());
            if (extendedblockstorage.getSkyLight().getData().length != 0 && extendedblockstorage.getSkyLight().getData().length != 0) {
               nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkyLight().getData());
            } else {
               nbttagcompound.setByteArray("SkyLight", new byte[extendedblockstorage.getBlockLight().getData().length]);
            }

            nbttaglist.appendTag(nbttagcompound);
         }
      }

      compound.setTag("Sections", nbttaglist);
      compound.setByteArray("Biomes", this.biomeArray);
      NBTTagList nbttaglist2 = new NBTTagList();
      Iterator var16 = this.tileentityMap.values().iterator();

      while(var16.hasNext()) {
         TileEntity tileentity = (TileEntity)var16.next();

         try {
            NBTTagCompound nbttagcompound3 = tileentity.writeToNBT(new NBTTagCompound());
            nbttaglist2.appendTag(nbttagcompound3);
         } catch (Exception var14) {
         }
      }

      compound.setTag("TileEntities", nbttaglist2);
      return level;
   }

   public synchronized boolean write(RegionFileCache regionFileCache) {
      boolean error = false;
      RegionFile regionFile = regionFileCache.getRegionFile(this.x << 4, this.z << 4, this.dimension);
      if (!regionFile.isOpen()) {
         error = regionFile.open();
      }

      if (!error) {
         DataOutputStream dos = regionFile.getChunkDataOutputStream(this.x & 31, this.z & 31);
         if (dos != null) {
            try {
               CompressedStreamTools.write(this.writeChunkToNBT(), dos);
            } catch (IOException var14) {
               Logging.logError("%s: could not write chunk (%d, %d) to region file", new Object[]{var14, this.x, this.z});
               error = true;
            } finally {
               try {
                  dos.close();
               } catch (IOException var13) {
                  Logging.logError("%s while closing chunk data output stream", new Object[]{var13});
               }

            }
         } else {
            Logging.logError("error: could not get output stream for chunk (%d, %d)", new Object[]{this.x, this.z});
         }
      } else {
         Logging.logError("error: could not open region file for chunk (%d, %d)", new Object[]{this.x, this.z});
      }

      return error;
   }

   public Long getCoordIntPair() {
      return ChunkPos.asLong(this.x, this.z);
   }
}
