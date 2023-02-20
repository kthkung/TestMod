//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import journeymap.util.Logging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SurfacePixels {
   protected Region region;
   protected File filename;
   protected int[] pixels = null;
   protected boolean cannotLoad = false;
   protected int updateCount = 0;

   public SurfacePixels(Region region, File filename) {
      this.region = region;
      this.filename = filename;
   }

   public void clear() {
      if (this.pixels != null) {
         Arrays.fill(this.pixels, 0);
      }

   }

   public void close() {
      if (this.updateCount > 0) {
         this.save();
      }

      this.pixels = null;
   }

   private void save() {
      if (this.pixels != null) {
         saveImage(this.filename, this.pixels, 512, 512);
         this.cannotLoad = false;
      }

      this.updateCount = 0;
   }

   private void load() {
      if (!this.cannotLoad) {
         this.pixels = loadImage(this.filename, 512, 512);
         if (this.pixels != null) {
            for(int i = 0; i < this.pixels.length; ++i) {
               int colour = this.pixels[i];
               if (colour == -16777216) {
                  this.pixels[i] = 0;
               }
            }
         } else {
            this.cannotLoad = true;
         }

         this.updateCount = 0;
      }

   }

   public int[] getPixels() {
      if (this.pixels == null) {
         this.load();
      }

      return this.pixels;
   }

   public int[] getOrAllocatePixels() {
      this.getPixels();
      if (this.pixels == null) {
         this.pixels = new int[262144];
         this.clear();
      }

      return this.pixels;
   }

   public void updateChunk(MwChunk chunk) {
      int x = chunk.x << 4;
      int z = chunk.z << 4;
      int offset = this.region.getPixelOffset(x, z);
      int[] pixels = this.getOrAllocatePixels();
      ChunkRender.renderSurface(this.region.regionManager.blockColours, chunk, pixels, offset, 512, chunk.dimension == -1);
      this.region.updateZoomLevels(x, z, 16, 16);
      ++this.updateCount;
   }

   public static int getAverageOfPixelQuad(int[] pixels, int offset, int scanSize) {
      int p00 = pixels[offset];
      int p01 = pixels[offset + 1];
      int p10 = pixels[offset + scanSize];
      int p11 = pixels[offset + scanSize + 1];
      int r = (p00 >> 16 & 255) + (p01 >> 16 & 255) + (p10 >> 16 & 255) + (p11 >> 16 & 255);
      r >>= 2;
      int g = (p00 >> 8 & 255) + (p01 >> 8 & 255) + (p10 >> 8 & 255) + (p11 >> 8 & 255);
      g >>= 2;
      int b = (p00 & 255) + (p01 & 255) + (p10 & 255) + (p11 & 255);
      b >>= 2;
      return -16777216 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
   }

   public void updateScaled(int[] srcPixels, int srcX, int srcZ, int dstX, int dstZ, int dstW, int dstH) {
      int[] dstPixels = this.getOrAllocatePixels();

      for(int j = 0; j < dstH; ++j) {
         for(int i = 0; i < dstW; ++i) {
            int srcOffset = (srcZ + j * 2 << 9) + srcX + i * 2;
            int dstPixel = getAverageOfPixelQuad(srcPixels, srcOffset, 512);
            dstPixels[(dstZ + j << 9) + dstX + i] = dstPixel;
         }
      }

      ++this.updateCount;
   }

   public static void saveImage(File filename, int[] pixels, int w, int h) {
      BufferedImage img = new BufferedImage(w, h, 2);
      img.setRGB(0, 0, w, h, pixels, 0, w);

      try {
         ImageIO.write(img, "png", filename);
      } catch (IOException var6) {
         Logging.logError("saveImage: error: could not write image to %s", new Object[]{filename});
      }

   }

   public static int[] loadImage(File filename, int w, int h) {
      BufferedImage img = null;

      try {
         img = ImageIO.read(filename);
      } catch (IOException var5) {
         img = null;
      }

      int[] pixels = null;
      if (img != null) {
         if (img.getWidth() == w && img.getHeight() == h) {
            pixels = new int[w * h];
            img.getRGB(0, 0, w, h, pixels, 0, w);
         } else {
            Logging.logWarning("loadImage: image '%s' does not match expected dimensions (got %dx%d expected %dx%d)", new Object[]{filename, img.getWidth(), img.getHeight(), w, h});
         }
      }

      return pixels;
   }
}
