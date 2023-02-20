package journeymap.region;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import journeymap.util.Logging;

public class MergeToImage {
   public static final int MAX_WIDTH = 8192;
   public static final int MAX_HEIGHT = 8192;

   public static BufferedImage mergeRegions(RegionManager regionManager, int x, int z, int w, int h, int dimension) {
      BufferedImage mergedImage = new BufferedImage(w, h, 1);

      for(int zi = 0; zi < h; zi += 512) {
         for(int xi = 0; xi < w; xi += 512) {
            Region region = regionManager.getRegion(x + xi, z + zi, 0, dimension);
            int[] regionPixels = region.surfacePixels.getPixels();
            if (regionPixels != null) {
               mergedImage.setRGB(xi, zi, 512, 512, regionPixels, 0, 512);
            }
         }
      }

      return mergedImage;
   }

   public static boolean writeImage(BufferedImage img, File f) {
      boolean error = true;

      try {
         ImageIO.write(img, "png", f);
         error = false;
      } catch (IOException var4) {
      }

      return error;
   }

   public static int merge(RegionManager regionManager, int xCentre, int zCentre, int w, int h, int dimension, File dir, String basename) {
      w = w + 512 - 1 & -512;
      h = h + 512 - 1 & -512;
      w = Math.max(512, w);
      h = Math.max(512, h);
      int xMin = xCentre - w / 2;
      int zMin = zCentre - h / 2;
      xMin = Math.round((float)xMin / 512.0F) * 512;
      zMin = Math.round((float)zMin / 512.0F) * 512;
      int xMax = xMin + w;
      int zMax = zMin + h;
      Logging.logInfo("merging area starting at (%d,%d), %dx%d blocks", xMin, zMin, w, h);
      int countZ = 0;
      int count = 0;

      for(int z = zMin; z < zMax; z += 8192) {
         int imgH = Math.min(zMax - z, 8192);
         int countX = 0;

         for(int x = xMin; x < xMax; x += 8192) {
            int imgW = Math.min(xMax - x, 8192);
            String imgName = String.format("%s.%d.%d.png", basename, countX, countZ);
            File f = new File(dir, imgName);
            Logging.logInfo("merging regions to image %s", f);
            BufferedImage img = mergeRegions(regionManager, x, z, imgW, imgH, dimension);
            writeImage(img, f);
            ++countX;
            ++count;
         }

         ++countZ;
      }

      return count;
   }
}
