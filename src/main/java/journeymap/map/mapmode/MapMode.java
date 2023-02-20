//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map.mapmode;

import java.awt.Point;
import java.awt.geom.Point2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import journeymap.api.IMapMode;
import journeymap.api.IMapView;
import journeymap.config.MapModeConfig;

public class MapMode implements IMapMode {
   private int sw = 320;
   private int sh = 240;
   private double screenScalingFactor = 1.0;
   private int xTranslation = 0;
   private int yTranslation = 0;
   private int x = -25;
   private int y = -25;
   private int w = 50;
   private int h = 50;
   private int wPixels = 50;
   private int hPixels = 50;
   private int textX = 0;
   private int textY = 0;
   private int textColour = -1;
   private double widthPercent;
   private double heightPercent;
   private double xPos;
   private double yPos;
   private int mouseXOffset;
   private int mouseYOffset;
   private MapModeConfig config;

   public MapMode(MapModeConfig config) {
      this.config = config;
      this.updateMargin();
   }

   public void setScreenRes(int dw, int dh, int sw, int sh, double scaling) {
      if (sw != this.sw || sh != this.sh || scaling != this.screenScalingFactor) {
         this.sw = sw;
         this.sh = sh;
         this.screenScalingFactor = scaling;
         this.update();
      }

   }

   public void setScreenRes() {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution sRes = new ScaledResolution(mc);
      this.setScreenRes(mc.displayWidth, mc.displayHeight, sRes.getScaledWidth(), sRes.getScaledHeight(), (double)sRes.getScaleFactor());
   }

   public void updateMargin() {
      if (this.widthPercent != this.config.widthPercent || this.heightPercent != this.config.heightPercent || this.xPos != this.config.xPos || this.yPos != this.config.yPos) {
         this.widthPercent = this.config.widthPercent;
         this.heightPercent = this.config.heightPercent;
         this.xPos = this.config.xPos;
         this.yPos = this.config.yPos;
         this.update();
      }

   }

   private void update() {
      this.w = (int)((double)this.sw * (this.widthPercent / 100.0));
      this.h = (int)((double)this.sh * (this.heightPercent / 100.0));
      if (this.config.circular) {
         this.w = this.h;
      }

      this.w &= -2;
      this.h &= -2;
      double x = (double)(this.sw - this.w) * (this.xPos / 100.0);
      double y = (double)(this.sh - this.h) * (this.yPos / 100.0);
      this.xTranslation = (int)(x + (double)(this.w >> 1));
      this.yTranslation = (int)(y + (double)(this.h >> 1));
      this.x = -(this.w >> 1);
      this.y = -(this.h >> 1);
      this.wPixels = (int)Math.round((double)this.w * this.screenScalingFactor);
      this.hPixels = (int)Math.round((double)this.h * this.screenScalingFactor);
      this.textX = 0;
      this.textY = (this.h >> 1) + 4;
   }

   public Point screenXYtoBlockXZ(IMapView mapView, int sx, int sy) {
      double withinMapX = (double)(sx - this.xTranslation) / (double)this.w;
      double withinMapY = (double)(sy - this.yTranslation) / (double)this.h;
      int bx = (int)Math.floor(mapView.getX() + withinMapX * mapView.getWidth());
      int bz = (int)Math.floor(mapView.getZ() + withinMapY * mapView.getHeight());
      return new Point(bx, bz);
   }

   public Point2D.Double blockXZtoScreenXY(IMapView mapView, double bX, double bZ) {
      double xNorm = (bX - mapView.getX()) / mapView.getWidth();
      double zNorm = (bZ - mapView.getZ()) / mapView.getHeight();
      return new Point2D.Double((double)this.w * xNorm, (double)this.h * zNorm);
   }

   public Point2D.Double getClampedScreenXY(IMapView mapView, double bX, double bZ) {
      double xRel = (bX - mapView.getX()) / mapView.getWidth();
      double zRel = (bZ - mapView.getZ()) / mapView.getHeight();
      double limit = 0.49;
      if (!this.config.circular) {
         if (xRel < -limit) {
            zRel = -limit * zRel / xRel;
            xRel = -limit;
         }

         if (xRel > limit) {
            zRel = limit * zRel / xRel;
            xRel = limit;
         }

         if (zRel < -limit) {
            xRel = -limit * xRel / zRel;
            zRel = -limit;
         }

         if (zRel > limit) {
            xRel = limit * xRel / zRel;
            zRel = limit;
         }

         if (xRel < -limit) {
            zRel = -limit * zRel / xRel;
            xRel = -limit;
         }

         if (xRel > limit) {
            zRel = limit * zRel / xRel;
            xRel = limit;
         }
      } else {
         double dSq = xRel * xRel + zRel * zRel;
         if (dSq > limit * limit) {
            double a = Math.atan2(zRel, xRel);
            xRel = limit * Math.cos(a);
            zRel = limit * Math.sin(a);
         }
      }

      return new Point2D.Double((double)this.w * xRel, (double)this.h * zRel);
   }

   public boolean posWithin(int mouseX, int mouseY) {
      this.mouseXOffset = mouseX - this.xTranslation;
      this.mouseYOffset = mouseY - this.yTranslation;
      return this.isMouseYWithinSlotBounds(mouseY) && this.isMouseXWithinSlotBounds(mouseX);
   }

   private boolean isMouseYWithinSlotBounds(int mouseY) {
      return mouseY >= this.yTranslation + this.y && mouseY <= this.yTranslation - this.y;
   }

   private boolean isMouseXWithinSlotBounds(int mouseX) {
      return mouseX >= this.xTranslation + this.x && mouseX <= this.xTranslation - this.x;
   }

   public Point2D.Double getNewPosPoint(double mouseX, double mouseY) {
      int newX = (int)(mouseX - (double)this.mouseXOffset);
      int newY = (int)(mouseY - (double)this.mouseYOffset);
      if (newX + this.x < 0) {
         newX = -this.x;
      }

      if (newX - this.x > this.sw) {
         newX = this.sw - -this.x;
      }

      if (newY + this.y < 0) {
         newY = -this.y;
      }

      if (newY - this.y > this.sh) {
         newY = this.sh - -this.y;
      }

      double x = (double)(newX - this.w / 2) * 100.0 / (double)(this.sw - this.w);
      double y = (double)(newY - this.h / 2) * 100.0 / (double)(this.sh - this.h);
      Point2D.Double pos = new Point2D.Double(x, y);
      return pos;
   }

   public int getXTranslation() {
      return this.xTranslation;
   }

   public int getYTranslation() {
      return this.yTranslation;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getW() {
      return this.w;
   }

   public int getH() {
      return this.h;
   }

   public int getWPixels() {
      return this.wPixels;
   }

   public int getHPixels() {
      return this.hPixels;
   }

   public MapModeConfig getConfig() {
      return this.config;
   }

   public int getTextX() {
      return this.textX;
   }

   public int getTextY() {
      return this.textY;
   }

   public int getTextColour() {
      return this.textColour;
   }
}
