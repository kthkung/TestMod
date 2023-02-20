package journeymap.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.client.SplashProgress;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class Texture {
   private int id;
   public final int w;
   public final int h;
   private final IntBuffer pixelBuf;

   public Texture(int w, int h, int fillColour, int minFilter, int maxFilter, int textureWrap) {
      this.id = GlStateManager.generateTexture();
      this.w = w;
      this.h = h;
      this.pixelBuf = Utils.allocateDirectIntBuffer(w * h);
      this.fillRect(0, 0, w, h, fillColour);
      this.pixelBuf.position(0);
      this.bind();
      GL11.glTexImage2D(3553, 0, 32856, w, h, 0, 32993, 5121, this.pixelBuf);
      this.setTexParameters(minFilter, maxFilter, textureWrap);
   }

   public Texture(int w, int h, int fillColour) {
      this(w, h, fillColour, 9729, 9728, 33071);
   }

   public Texture(int id) {
      this.id = id;
      this.bind();
      this.w = Render.getTextureWidth();
      this.h = Render.getTextureHeight();
      this.pixelBuf = Utils.allocateDirectIntBuffer(this.w * this.h);
      this.getPixelsFromExistingTexture();
      Logging.log("created new MwTexture from GL texture id %d (%dx%d) (%d pixels)", this.id, this.w, this.h, this.pixelBuf.limit());
   }

   public synchronized void close() {
      if (this.id != 0) {
         try {
            GlStateManager.deleteTexture(this.id);
         } catch (NullPointerException var2) {
            Logging.log("MwTexture.close: null pointer exception (texture %d)", this.id);
         }

         this.id = 0;
      }

   }

   public void setPixelBufPosition(int i) {
      this.pixelBuf.position(i);
   }

   public void pixelBufPut(int pixel) {
      this.pixelBuf.put(pixel);
   }

   public synchronized void fillRect(int x, int y, int w, int h, int colour) {
      int offset = y * this.w + x;

      for(int j = 0; j < h; ++j) {
         this.pixelBuf.position(offset + j * this.w);

         for(int i = 0; i < w; ++i) {
            this.pixelBuf.put(colour);
         }
      }

   }

   public synchronized void getRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize, TextureAtlasSprite icon) {
      int bufOffset = y * this.w + x;

      for(int i = 0; i < h; ++i) {
         try {
            this.pixelBuf.position(bufOffset + i * this.w);
            this.pixelBuf.get(pixels, offset + i * scanSize, w);
         } catch (IllegalArgumentException var12) {
            Logging.logWarning("MwTexture.getRGB: IllegalArgumentException (icon name: %s; height: %d; width: %d; MaxU: %f; MinU: %f; MaxV: %f; MinV: %f)", icon.getIconName(), icon.getIconHeight(), icon.getIconWidth(), icon.getMaxU(), icon.getMinU(), icon.getMaxV(), icon.getMinV());
            Logging.logWarning("MwTexture.getRGB: IllegalArgumentException (pos: %d)", bufOffset + i * this.w);
            Logging.logWarning("MwTexture.getRGB: IllegalArgumentException (buffersize: %d)", this.pixelBuf.limit());
         }
      }

   }

   public synchronized void setRGB(int x, int y, int w, int h, int[] pixels, int offset, int scanSize) {
      int bufOffset = y * this.w + x;

      for(int i = 0; i < h; ++i) {
         this.pixelBuf.position(bufOffset + i * this.w);
         this.pixelBuf.put(pixels, offset + i * scanSize, w);
      }

   }

   public synchronized void setRGB(int x, int y, int colour) {
      this.pixelBuf.put(y * this.w + x, colour);
   }

   public synchronized int getRGB(int x, int y) {
      return this.pixelBuf.get(y * this.w + x);
   }

   public void bind() {
      Class var1 = SplashProgress.class;
      synchronized(SplashProgress.class) {
         GlStateManager.bindTexture(this.id);
      }
   }

   public void setTexParameters(int minFilter, int maxFilter, int textureWrap) {
      this.bind();
      GL11.glTexParameteri(3553, 10242, textureWrap);
      GL11.glTexParameteri(3553, 10243, textureWrap);
      GL11.glTexParameteri(3553, 10241, minFilter);
      GL11.glTexParameteri(3553, 10240, maxFilter);
   }

   public void setLinearScaling(boolean enabled) {
      this.bind();
      if (enabled) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      } else {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

   }

   public synchronized void updateTextureArea(int x, int y, int w, int h) {
      try {
         this.bind();
         GL11.glPixelStorei(3314, this.w);
         this.pixelBuf.position(y * this.w + x);
         GL11.glTexSubImage2D(3553, 0, x, y, w, h, 32993, 5121, this.pixelBuf);
         GL11.glPixelStorei(3314, 0);
      } catch (NullPointerException var6) {
         Logging.log("MwTexture.updatePixels: null pointer exception (texture %d)", this.id);
      }

   }

   public synchronized void updateTexture() {
      this.bind();
      this.pixelBuf.position(0);
      GL11.glTexImage2D(3553, 0, 32856, this.w, this.h, 0, 32993, 5121, this.pixelBuf);
   }

   private synchronized void getPixelsFromExistingTexture() {
      try {
         this.bind();
         this.pixelBuf.clear();
         GL11.glGetTexImage(3553, 0, 32993, 5121, this.pixelBuf);
         this.pixelBuf.limit(this.w * this.h);
      } catch (NullPointerException var2) {
         Logging.log("MwTexture.getPixels: null pointer exception (texture %d)", this.id);
      }

   }
}
