//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Render {
   public static double zDepth = 0.0;
   public static final double circleSteps = 30.0;

   public Render() {
   }

   public static void setColourWithAlphaPercent(int colour, int alphaPercent) {
      setColour((alphaPercent * 255 / 100 & 255) << 24 | colour & 16777215);
   }

   public static void setColour(int colour) {
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color((float)(colour >> 16 & 255) / 255.0F, (float)(colour >> 8 & 255) / 255.0F, (float)(colour & 255) / 255.0F, (float)(colour >> 24 & 255) / 255.0F);
      GlStateManager.disableBlend();
   }

   public static void resetColour() {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static int multiplyColours(int c1, int c2) {
      float c1A = (float)(c1 >> 24 & 255);
      float c1R = (float)(c1 >> 16 & 255);
      float c1G = (float)(c1 >> 8 & 255);
      float c1B = (float)(c1 >> 0 & 255);
      float c2A = (float)(c2 >> 24 & 255);
      float c2R = (float)(c2 >> 16 & 255);
      float c2G = (float)(c2 >> 8 & 255);
      float c2B = (float)(c2 >> 0 & 255);
      int r = (int)(c1R * c2R / 255.0F) & 255;
      int g = (int)(c1G * c2G / 255.0F) & 255;
      int b = (int)(c1B * c2B / 255.0F) & 255;
      int a = (int)(c1A * c2A / 255.0F) & 255;
      return a << 24 | r << 16 | g << 8 | b;
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

   public static int getAverageColourOfArray(int[] pixels) {
      int count = 0;
      double totalA = 0.0;
      double totalR = 0.0;
      double totalG = 0.0;
      double totalB = 0.0;
      int[] var10 = pixels;
      int var11 = pixels.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         int pixel = var10[var12];
         double a = (double)(pixel >> 24 & 255);
         double r = (double)(pixel >> 16 & 255);
         double g = (double)(pixel >> 8 & 255);
         double b = (double)(pixel >> 0 & 255);
         totalA += a;
         totalR += r * a / 255.0;
         totalG += g * a / 255.0;
         totalB += b * a / 255.0;
         ++count;
      }

      totalR = totalR * 255.0 / totalA;
      totalG = totalG * 255.0 / totalA;
      totalB = totalB * 255.0 / totalA;
      totalA /= (double)count;
      return ((int)totalA & 255) << 24 | ((int)totalR & 255) << 16 | ((int)totalG & 255) << 8 | (int)totalB & 255;
   }

   public static int adjustPixelBrightness(int colour, int brightness) {
      int r = colour >> 16 & 255;
      int g = colour >> 8 & 255;
      int b = colour >> 0 & 255;
      r = Math.min(Math.max(0, r + brightness), 255);
      g = Math.min(Math.max(0, g + brightness), 255);
      b = Math.min(Math.max(0, b + brightness), 255);
      return colour & -16777216 | r << 16 | g << 8 | b;
   }

   public static int getTextureWidth() {
      return GL11.glGetTexLevelParameteri(3553, 0, 4096);
   }

   public static int getTextureHeight() {
      return GL11.glGetTexLevelParameteri(3553, 0, 4097);
   }

   public static int getBoundTextureId() {
      return GL11.glGetInteger(32873);
   }

   public static void printBoundTextureInfo(int texture) {
      int w = getTextureWidth();
      int h = getTextureHeight();
      int depth = GL11.glGetTexLevelParameteri(3553, 0, 32881);
      int format = GL11.glGetTexLevelParameteri(3553, 0, 4099);
      Logging.log("texture %d parameters: width=%d, height=%d, depth=%d, format=%08x", new Object[]{texture, w, h, depth, format});
   }

   public static int getMaxTextureSize() {
      return GL11.glGetInteger(3379);
   }

   public static void drawTexturedRect(double x, double y, double w, double h) {
      drawTexturedRect(x, y, w, h, 0.0, 0.0, 1.0, 1.0);
   }

   public static void drawTexturedRectRotate(double x, double y, double w, double h, float angle) {
      drawTexturedRectRotate(x, y, w, h, 0.0, 0.0, 1.0, 1.0, angle);
   }

   public static void drawTexturedRect(double x, double y, double w, double h, double u1, double v1, double u2, double v2) {
      try {
         GlStateManager.enableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder vertexbuffer = tessellator.getBuffer();
         vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
         vertexbuffer.pos(x + w, y, zDepth).tex(u2, v1).endVertex();
         vertexbuffer.pos(x, y, zDepth).tex(u1, v1).endVertex();
         vertexbuffer.pos(x, y + h, zDepth).tex(u1, v2).endVertex();
         vertexbuffer.pos(x + w, y + h, zDepth).tex(u2, v2).endVertex();
         tessellator.draw();
         GlStateManager.disableBlend();
      } catch (NullPointerException var18) {
         Logging.log("MwRender.drawTexturedRect: null pointer exception", new Object[0]);
      }

   }

   private static void drawTexturedRectRotate(double x, double y, double w, double h, double u1, double v1, double u2, double v2, float angle) {
      try {
         GlStateManager.enableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder vertexbuffer = tessellator.getBuffer();
         vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
         double x1 = x + Math.cos((double)angle) * w;
         double y1 = y + Math.sin((double)angle) * w;
         vertexbuffer.pos(x1 + w, y1, zDepth).tex(u2, v1).endVertex();
         vertexbuffer.pos(x1, y1, zDepth).tex(u1, v1).endVertex();
         vertexbuffer.pos(x1, y1 + h, zDepth).tex(u1, v2).endVertex();
         vertexbuffer.pos(x1 + w, y1 + h, zDepth).tex(u2, v2).endVertex();
         tessellator.draw();
         GlStateManager.disableBlend();
      } catch (NullPointerException var37) {
         Logging.log("MwRender.drawTexturedRect: null pointer exception", new Object[0]);
      }

   }

   public static void drawArrow(double x, double y, double angle, double length) {
      double arrowBackAngle = 2.356194490192345;
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(6, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(x + length * Math.cos(angle), y + length * Math.sin(angle), zDepth).endVertex();
      vertexbuffer.pos(x + length * 0.5 * Math.cos(angle - arrowBackAngle), y + length * 0.5 * Math.sin(angle - arrowBackAngle), zDepth).endVertex();
      vertexbuffer.pos(x + length * 0.3 * Math.cos(angle + Math.PI), y + length * 0.3 * Math.sin(angle + Math.PI), zDepth).endVertex();
      vertexbuffer.pos(x + length * 0.5 * Math.cos(angle + arrowBackAngle), y + length * 0.5 * Math.sin(angle + arrowBackAngle), zDepth).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(4, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(x1, y1, zDepth).endVertex();
      vertexbuffer.pos(x2, y2, zDepth).endVertex();
      vertexbuffer.pos(x3, y3, zDepth).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect(double x, double y, double w, double h) {
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(x + w, y, zDepth).endVertex();
      vertexbuffer.pos(x, y, zDepth).endVertex();
      vertexbuffer.pos(x, y + h, zDepth).endVertex();
      vertexbuffer.pos(x + w, y + h, zDepth).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawCircle(double x, double y, double r) {
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(6, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(x, y, zDepth).endVertex();
      double end = 6.283185307179586;
      double incr = end / 30.0;

      for(double theta = -incr; theta < end; theta += incr) {
         vertexbuffer.pos(x + r * Math.cos(-theta), y + r * Math.sin(-theta), zDepth).endVertex();
      }

      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawCircleSteps(double x, double y, double r, double steps) {
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(6, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(x, y, zDepth).endVertex();
      double end = 6.283185307179586;
      double incr = end / 30.0;

      for(double theta = -incr; theta < end; theta += incr) {
         vertexbuffer.pos(x + r * Math.cos(-theta), y + r * Math.sin(-theta), zDepth).endVertex();
      }

      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawCircleBorder(double x, double y, double r, double width) {
      GlStateManager.enableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(5, DefaultVertexFormats.POSITION);
      double end = 6.283185307179586;
      double incr = end / 30.0;
      double r2 = r + width;

      for(double theta = -incr; theta < end; theta += incr) {
         vertexbuffer.pos(x + r * Math.cos(-theta), y + r * Math.sin(-theta), zDepth).endVertex();
         vertexbuffer.pos(x + r2 * Math.cos(-theta), y + r2 * Math.sin(-theta), zDepth).endVertex();
      }

      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRectBorder(double x, double y, double w, double h, double bw) {
      drawRect(x - bw, y - bw, w + bw + bw, bw);
      drawRect(x - bw, y + h, w + bw + bw, bw);
      drawRect(x - bw, y, bw, h);
      drawRect(x + w, y, bw, h);
   }

   public static void drawString(int x, int y, int colour, String formatString, Object... args) {
      Minecraft mc = Minecraft.getMinecraft();
      FontRenderer fr = mc.fontRenderer;
      String s = String.format(formatString, args);
      fr.drawStringWithShadow(s, (float)x, (float)y, colour);
   }

   public static void drawCentredString(int x, int y, int colour, String formatString, Object... args) {
      Minecraft mc = Minecraft.getMinecraft();
      FontRenderer fr = mc.fontRenderer;
      String s = String.format(formatString, args);
      int w = fr.getStringWidth(s);
      fr.drawStringWithShadow(s, (float)(x - w / 2), (float)y, colour);
   }

   public static void setCircularStencil(double x, double y, double r) {
      GlStateManager.enableDepth();
      GlStateManager.colorMask(false, false, false, false);
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(519);
      setColour(-1);
      zDepth = 0.0;
      drawCircle(x, y, r);
      zDepth = -1.0;
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.depthMask(false);
      GlStateManager.depthFunc(516);
   }

   public static void disableStencil() {
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(515);
      GlStateManager.disableDepth();
      zDepth = 0.0;
   }
}
