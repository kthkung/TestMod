//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import journeymap.util.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class RegionFile {
   private final File file;
   private int lengthInSectors = 0;
   private RandomAccessFile fin = null;
   private Section[] chunkSectionsArray = new Section[4096];
   private int[] timestampArray = new int[4096];
   private List<Boolean> filledSectorArray = null;

   public RegionFile(File file) {
      this.file = file;
   }

   public String toString() {
      return String.format("%s", this.file);
   }

   public boolean exists() {
      return this.file.isFile();
   }

   public boolean isOpen() {
      return this.fin != null;
   }

   private void setFilledSectorArray(Section section, boolean filled) {
      int endSector = section.startSector + section.length;
      int sectorsToAppend = endSector + 1 - this.filledSectorArray.size();

      int i;
      for(i = 0; i < sectorsToAppend; ++i) {
         this.filledSectorArray.add(false);
      }

      for(i = section.startSector; i < endSector; ++i) {
         if (filled && (Boolean)this.filledSectorArray.get(i)) {
            Logging.logError("sector %d already filled, possible chunk overlap", new Object[]{i});
         }

         this.filledSectorArray.set(i, filled);
      }

   }

   private boolean checkSectionOverlaps(Section section) {
      int endSector = Math.min(section.startSector + section.length, this.filledSectorArray.size());
      boolean overlaps = false;

      for(int i = section.startSector; i < endSector; ++i) {
         if ((Boolean)this.filledSectorArray.get(i)) {
            overlaps = true;
         }
      }

      return overlaps;
   }

   private Section getFreeSection(int requiredLength) {
      int start = 0;
      int length = 0;
      int closestStart = 0;
      int closestLength = Integer.MAX_VALUE;

      int i;
      for(i = 2; i < this.filledSectorArray.size(); ++i) {
         if ((Boolean)this.filledSectorArray.get(i)) {
            if (length >= requiredLength && length < closestLength) {
               closestLength = length;
               closestStart = start;
               if (length == requiredLength) {
                  break;
               }
            }

            length = 0;
         } else {
            if (length == 0) {
               start = i;
            }

            ++length;
         }
      }

      if (closestStart <= 0) {
         closestStart = i;
      }

      return new Section(closestStart, requiredLength);
   }

   public void printInfo() {
      int freeCount = 0;
      int filledCount = 0;

      for(int i = 2; i < this.filledSectorArray.size(); ++i) {
         if ((Boolean)this.filledSectorArray.get(i)) {
            ++filledCount;
         } else {
            ++freeCount;
         }
      }

      Logging.logInfo("Region File %s: filled sectors = %d, free sectors = %d", new Object[]{this, filledCount, freeCount});
      String s = "";

      int i;
      for(i = 0; i < this.filledSectorArray.size(); ++i) {
         if ((i & 31) == 0) {
            s = String.format("%04x:", i);
         }

         s = s + ((Boolean)this.filledSectorArray.get(i) ? '1' : '0');
         if ((i & 31) == 31) {
            Logging.logInfo("%s", new Object[]{s});
         }
      }

      if ((i & 31) != 31) {
         Logging.logInfo("%s", new Object[]{s});
      }

   }

   private Section getChunkSection(int x, int z) {
      return this.chunkSectionsArray[(z & 31) << 5 | x & 31];
   }

   private void updateChunkSection(int x, int z, Section newSection) throws IOException {
      int chunkIndex = (z & 31) << 5 | x & 31;
      this.fin.seek((long)(chunkIndex * 4));
      if (newSection != null && newSection.length > 0) {
         this.fin.writeInt(newSection.getSectorAndSize());
      } else {
         this.fin.writeInt(0);
      }

      this.chunkSectionsArray[chunkIndex] = newSection;
   }

   public boolean open() {
      File dir = this.file.getParentFile();
      if (dir.exists()) {
         if (!dir.isDirectory()) {
            Logging.logError("path %s exists and is not a directory", new Object[]{dir});
            return true;
         }
      } else if (!dir.mkdirs()) {
         Logging.logError("could not create directory %s", new Object[]{dir});
         return true;
      }

      try {
         this.fin = new RandomAccessFile(this.file, "rw");
         this.fin.seek(0L);
         this.lengthInSectors = (int)((this.fin.length() + 4095L) / 4096L);
         this.filledSectorArray = new ArrayList();
         Arrays.fill(this.chunkSectionsArray, (Object)null);
         Arrays.fill(this.timestampArray, 0);
         int i;
         if (this.lengthInSectors < 3) {
            for(i = 0; i < 2048; ++i) {
               this.fin.writeInt(0);
            }
         } else {
            for(i = 0; i < 1024; ++i) {
               Section section = new Section(this.fin.readInt());
               if (section.length > 0) {
                  if (!this.checkSectionOverlaps(section)) {
                     this.chunkSectionsArray[i] = section;
                     this.setFilledSectorArray(section, true);
                  } else {
                     Logging.logError("chunk %d overlaps another chunk, file may be corrupt", new Object[]{i});
                  }
               }
            }

            for(i = 0; i < 1024; ++i) {
               this.timestampArray[i] = this.fin.readInt();
            }
         }
      } catch (Exception var4) {
         this.fin = null;
         Logging.logError("exception when opening region file '%s': %s", new Object[]{this.file, var4});
      }

      return this.fin == null;
   }

   public void close() {
      if (this.fin != null) {
         try {
            this.fin.close();
         } catch (IOException var2) {
         }
      }

   }

   public DataInputStream getChunkDataInputStream(int x, int z) {
      DataInputStream dis = null;
      if (this.fin != null) {
         Section section = this.getChunkSection(x, z);
         if (section != null && section.length > 0) {
            int offset = section.startSector * 4096;

            try {
               this.fin.seek((long)offset);
               int length = this.fin.readInt();
               byte version = this.fin.readByte();
               if (length > 1 && length + 4 < section.length * 4096 && version == 2) {
                  byte[] compressedChunkData = new byte[length - 1];
                  this.fin.read(compressedChunkData);
                  dis = new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(compressedChunkData))));
               } else {
                  Logging.logError("data length (%d) or version (%d) invalid for chunk (%d, %d)", new Object[]{length, version, x, z});
               }
            } catch (Exception var9) {
               Logging.logError("exception while reading chunk (%d, %d): %s", new Object[]{x, z, var9});
               dis = null;
            }
         }
      }

      return dis;
   }

   public DataOutputStream getChunkDataOutputStream(int x, int z) {
      return new DataOutputStream(new DeflaterOutputStream(new RegionFileChunkBuffer(this, x, z)));
   }

   private void writeChunkDataToSection(Section section, byte[] compressedChunkData, int length) throws IOException {
      this.fin.seek((long)section.startSector * 4096L);
      this.fin.writeInt(length + 1);
      this.fin.writeByte(2);
      this.fin.write(compressedChunkData, 0, length);
      int endSector = section.startSector + section.length;
      if (endSector + 1 > this.lengthInSectors) {
         this.lengthInSectors = endSector + 1;
      }

   }

   private boolean writeCompressedChunk(int x, int z, byte[] compressedChunkData, int length) {
      if (length <= 0) {
         Logging.logWarning("not writing chunk (%d, %d) with length %d", new Object[]{x, z, length});
         return true;
      } else {
         Section currentSection = this.getChunkSection(x, z);
         if (currentSection != null) {
            this.setFilledSectorArray(currentSection, false);
         }

         int requiredSectors = (length + 5 + 4095) / 4096;
         Section newSection;
         if (currentSection != null && requiredSectors <= currentSection.length) {
            newSection = new Section(currentSection.startSector, requiredSectors);
         } else {
            newSection = this.getFreeSection(requiredSectors);
         }

         this.setFilledSectorArray(newSection, true);
         boolean error = true;

         try {
            this.writeChunkDataToSection(newSection, compressedChunkData, length);
            this.updateChunkSection(x, z, newSection);
            error = false;
         } catch (IOException var10) {
            Logging.logError("could not write chunk (%d, %d) to region file: %s", new Object[]{x, z, var10});
         }

         return error;
      }
   }

   private class RegionFileChunkBuffer extends ByteArrayOutputStream {
      private final int x;
      private final int z;
      private final RegionFile regionFile;

      public RegionFileChunkBuffer(RegionFile regionFile, int x, int z) {
         super(8096);
         this.regionFile = regionFile;
         this.x = x;
         this.z = z;
      }

      public void close() {
         this.regionFile.writeCompressedChunk(this.x, this.z, this.buf, this.count);
      }
   }

   private class Section {
      final int startSector;
      final int length;

      Section(int startSector, int length) {
         this.startSector = startSector;
         this.length = length;
      }

      Section(int sectorAndSize) {
         this(sectorAndSize >> 8 & 16777215, sectorAndSize & 255);
      }

      int getSectorAndSize() {
         return this.startSector << 8 | this.length & 255;
      }
   }
}
