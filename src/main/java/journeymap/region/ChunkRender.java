//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import net.minecraft.block.state.IBlockState;
import journeymap.config.Config;

public class ChunkRender {
   public static final byte FLAG_UNPROCESSED = 0;
   public static final byte FLAG_NON_OPAQUE = 1;
   public static final byte FLAG_OPAQUE = 2;
   public static final double brightenExponent = 0.35;
   public static final double darkenExponent = 0.35;
   public static final double brightenAmplitude = 0.7;
   public static final double darkenAmplitude = 1.4;

   public ChunkRender() {
   }

   public static double getHeightShading(int height, int heightW, int heightN) {
      int samples = 0;
      int heightDiff = 0;
      if (heightW > 0 && heightW < 255) {
         heightDiff += height - heightW;
         ++samples;
      }

      if (heightN > 0 && heightN < 255) {
         heightDiff += height - heightN;
         ++samples;
      }

      double heightDiffFactor = 0.0;
      if (samples > 0) {
         heightDiffFactor = (double)heightDiff / (double)samples;
      }

      if (Config.moreRealisticMap) {
         return Math.atan(heightDiffFactor) * 0.3;
      } else {
         return heightDiffFactor >= 0.0 ? Math.pow(heightDiffFactor * 0.00392156862745098, 0.35) * 0.7 : -Math.pow(-(heightDiffFactor * 0.00392156862745098), 0.35) * 1.4;
      }
   }

   public static int getColumnColour(BlockColours bc, IChunk chunk, int x, int y, int z, int heightW, int heightN) {
      double a = 1.0;
      double r = 0.0;
      double g = 0.0;

      double b;
      int alpha;
      double c1A;
      for(b = 0.0; y > 0; --y) {
         IBlockState blockState = chunk.getBlockState(x, y, z);
         int c1 = bc.getColour(blockState);
         alpha = c1 >> 24 & 255;
         if (c1 == -8650628) {
            alpha = 0;
         }

         if (alpha > 0) {
            int biome = chunk.getBiome(x, y, z);
            int c2 = bc.getBiomeColour(blockState, biome);
            c1A = (double)alpha / 255.0;
            double c1R = (double)(c1 >> 16 & 255) / 255.0;
            double c1G = (double)(c1 >> 8 & 255) / 255.0;
            double c1B = (double)(c1 >> 0 & 255) / 255.0;
            double c2R = (double)(c2 >> 16 & 255) / 255.0;
            double c2G = (double)(c2 >> 8 & 255) / 255.0;
            double c2B = (double)(c2 >> 0 & 255) / 255.0;
            r += a * c1A * c1R * c2R;
            g += a * c1A * c1G * c2G;
            b += a * c1A * c1B * c2B;
            a *= 1.0 - c1A;
         }

         if (alpha == 255) {
            break;
         }
      }

      double heightShading = getHeightShading(y, heightW, heightN);
      alpha = chunk.getLightValue(x, y + 1, z);
      double lightShading = (double)alpha / 15.0;
      c1A = (heightShading + 1.0) * lightShading;
      r = Math.min(Math.max(0.0, r * c1A), 1.0);
      g = Math.min(Math.max(0.0, g * c1A), 1.0);
      b = Math.min(Math.max(0.0, b * c1A), 1.0);
      return (y & 255) << 24 | ((int)(r * 255.0) & 255) << 16 | ((int)(g * 255.0) & 255) << 8 | (int)(b * 255.0) & 255;
   }

   static int getPixelHeightN(int[] pixels, int offset, int scanSize) {
      return offset >= scanSize ? pixels[offset - scanSize] >> 24 & 255 : -1;
   }

   static int getPixelHeightW(int[] pixels, int offset, int scanSize) {
      return (offset & scanSize - 1) >= 1 ? pixels[offset - 1] >> 24 & 255 : -1;
   }

   public static void renderSurface(BlockColours bc, IChunk chunk, int[] pixels, int offset, int scanSize, boolean dimensionHasCeiling) {
      int chunkMaxY = chunk.getMaxY();

      for(int z = 0; z < 16; ++z) {
         for(int x = 0; x < 16; ++x) {
            int y;
            if (dimensionHasCeiling) {
               for(y = 127; y >= 0; --y) {
                  IBlockState blockState = chunk.getBlockState(x, y, z);
                  int color = bc.getColour(blockState);
                  int alpha = color >> 24 & 255;
                  if (color == -8650628) {
                     alpha = 0;
                  }

                  if (alpha != 255) {
                     break;
                  }
               }
            } else {
               y = chunkMaxY - 1;
            }

            int pixelOffset = offset + z * scanSize + x;
            pixels[pixelOffset] = getColumnColour(bc, chunk, x, y, z, getPixelHeightW(pixels, pixelOffset, scanSize), getPixelHeightN(pixels, pixelOffset, scanSize));
         }
      }

   }

   public static void renderUnderground(BlockColours bc, IChunk chunk, int[] pixels, int offset, int scanSize, int startY, byte[] mask) {
      startY = Math.min(Math.max(0, startY), 255);

      for(int z = 0; z < 16; ++z) {
         for(int x = 0; x < 16; ++x) {
            if (mask == null || mask[z * 16 + x] == 1) {
               int lastNonTransparentY = startY;

               int y;
               for(y = startY; y < chunk.getMaxY(); ++y) {
                  IBlockState blockState = chunk.getBlockState(x, y, z);
                  int color = bc.getColour(blockState);
                  int alpha = color >> 24 & 255;
                  if (color == -8650628) {
                     alpha = 0;
                  }

                  if (alpha == 255) {
                     break;
                  }

                  if (alpha > 0) {
                     lastNonTransparentY = y;
                  }
               }

               y = offset + z * scanSize + x;
               pixels[y] = getColumnColour(bc, chunk, x, lastNonTransparentY, z, getPixelHeightW(pixels, y, scanSize), getPixelHeightN(pixels, y, scanSize));
            }
         }
      }

   }
}
