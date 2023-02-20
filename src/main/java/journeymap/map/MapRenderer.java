//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import journeymap.Mw;
import journeymap.api.IMwChunkOverlay;
import journeymap.api.IMwDataProvider;
import journeymap.api.MwAPI;
import journeymap.config.Config;
import journeymap.config.MapModeConfig;
import journeymap.config.pokeradarConfig;
import journeymap.entities.EntityFinder;
import journeymap.map.mapmode.MapMode;
import journeymap.util.Reference;
import journeymap.util.Render;

public class MapRenderer {
   protected Mw mw;
   private MapMode mapMode;
   private MapView mapView;
   public Point2D.Double playerArrowScreenPos = new Point2D.Double(0.0, 0.0);
   private int textOffset = 12;
   private int textY = 0;
   private int textX = 0;
   public static final double TWICE_PI = 6.283185307179586;

   public MapRenderer(Mw mw, MapMode mapMode, MapView mapView) {
      this.mw = mw;
      this.mapMode = mapMode;
      this.mapView = mapView;
   }

   private void drawMap() {
      int regionZoomLevel = Math.max(0, this.mapView.getZoomLevel());
      double tSize = (double)this.mw.textureSize;
      double zoomScale = (double)(1 << regionZoomLevel);
      double u;
      double v;
      double w;
      double h;
      if (!this.mapMode.getConfig().circular && Config.mapPixelSnapEnabled && this.mapView.getZoomLevel() >= 0) {
         u = (double)Math.round(this.mapView.getMinX() / zoomScale) / tSize % 1.0;
         v = (double)Math.round(this.mapView.getMinZ() / zoomScale) / tSize % 1.0;
         w = (double)Math.round(this.mapView.getWidth() / zoomScale) / tSize;
         h = (double)Math.round(this.mapView.getHeight() / zoomScale) / tSize;
      } else {
         double tSizeInBlocks = tSize * zoomScale;
         u = this.mapView.getMinX() / tSizeInBlocks % 1.0;
         v = this.mapView.getMinZ() / tSizeInBlocks % 1.0;
         w = this.mapView.getWidth() / tSizeInBlocks;
         h = this.mapView.getHeight() / tSizeInBlocks;
      }

      GlStateManager.pushMatrix();
      if (this.mapMode.getConfig().rotate && this.mapMode.getConfig().circular) {
         GlStateManager.rotate(this.mw.mapRotationDegrees, 0.0F, 0.0F, 1.0F);
      }

      if (this.mapMode.getConfig().circular) {
         Render.setCircularStencil(0.0, 0.0, (double)this.mapMode.getH() / 2.0);
      }

      if (this.mapView.getUndergroundMode() && regionZoomLevel == 0) {
         this.mw.undergroundMapTexture.requestView(this.mapView);
         Render.setColourWithAlphaPercent(0, this.mapMode.getConfig().alphaPercent);
         Render.drawRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH());
         Render.setColourWithAlphaPercent(16777215, this.mapMode.getConfig().alphaPercent);
         this.mw.undergroundMapTexture.bind();
         Render.drawTexturedRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH(), u, v, u + w, v + h);
      } else {
         MapViewRequest req = new MapViewRequest(this.mapView);
         this.mw.mapTexture.requestView(req, this.mw.executor, this.mw.regionManager);
         this.drawBackground(tSize, u, v, w, h);
         if (this.mw.mapTexture.isLoaded(req)) {
            this.mw.mapTexture.bind();
            Render.setColourWithAlphaPercent(16777215, this.mapMode.getConfig().alphaPercent);
            Render.drawTexturedRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH(), u, v, u + w, v + h);
         }
      }

      IMwDataProvider provider = this.drawOverlay();
      if (provider != null) {
         GlStateManager.pushMatrix();
         provider.onDraw(this.mapView, this.mapMode);
         GlStateManager.popMatrix();
      }

      if (this.mapMode.getConfig().circular) {
         Render.disableStencil();
      }

      GlStateManager.popMatrix();
   }

   private void drawBackground(double tSize, double u, double v, double w, double h) {
      if (!Config.backgroundTextureMode.equals(Config.backgroundModeStringArray[0])) {
         double bu1 = 0.0;
         double bu2 = 1.0;
         double bv1 = 0.0;
         double bv2 = 1.0;
         if (Config.backgroundTextureMode.equals(Config.backgroundModeStringArray[2])) {
            double bSize = tSize / 256.0;
            bu1 = u * bSize;
            bu2 = (u + w) * bSize;
            bv1 = v * bSize;
            bv2 = (v + h) * bSize;
         }

         this.mw.mc.renderEngine.bindTexture(Reference.backgroundTexture);
         Render.setColourWithAlphaPercent(16777215, this.mapMode.getConfig().alphaPercent);
         Render.drawTexturedRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH(), bu1, bv1, bu2, bv2);
      } else {
         Render.setColourWithAlphaPercent(0, this.mapMode.getConfig().alphaPercent);
         Render.drawRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH());
      }

   }

   private void drawBorder() {
      if (this.mapMode.getConfig().circular) {
         this.mw.mc.renderEngine.bindTexture(Reference.roundMapTexture);
      } else {
         this.mw.mc.renderEngine.bindTexture(Reference.squareMapTexture);
      }

      Render.setColour(-1);
      Render.drawTexturedRect((double)this.mapMode.getX() / 0.75, (double)this.mapMode.getY() / 0.75, (double)this.mapMode.getW() / 0.75, (double)this.mapMode.getH() / 0.75, 0.0, 0.0, 1.0, 1.0);
   }

   public void drawEntityArrow(double x, double z, double r, boolean isPlayer) {
      double scale = this.mapView.getDimensionScaling(this.mapView.getDimension());
      Point2D.Double p = this.mapMode.getClampedScreenXY(this.mapView, x * scale, z * scale);
      if (isPlayer) {
         Render.setColour(-16777216);
         Render.drawArrow(p.x, p.y, r, 6.5);
         Render.setColour(-16776961);
         Render.drawArrow(p.x, p.y, r, 5.0);
      } else {
         Render.setColour(Color.BLACK.getRGB());
         Render.drawArrow(p.x, p.y, r, 6.5);
         Render.setColour(Color.LIGHT_GRAY.getRGB());
         Render.drawArrow(p.x, p.y, r, 5.0);
      }

   }

   private void drawPlayerArrow() {
      GlStateManager.pushMatrix();
      double scale = this.mapView.getDimensionScaling(this.mw.playerDimension);
      Point2D.Double p = this.mapMode.getClampedScreenXY(this.mapView, this.mw.playerX * scale, this.mw.playerZ * scale);
      this.playerArrowScreenPos.setLocation(p.x + (double)this.mapMode.getXTranslation(), p.y + (double)this.mapMode.getYTranslation());
      GlStateManager.translate(p.x, p.y, 0.0);
      if (!this.mapMode.getConfig().rotate || !this.mapMode.getConfig().circular) {
         GlStateManager.rotate(-this.mw.mapRotationDegrees, 0.0F, 0.0F, 1.0F);
      }

      double arrowSize = (double)this.mapMode.getConfig().playerArrowSize;
      Render.setColour(-1);
      this.mw.mc.renderEngine.bindTexture(Reference.playerArrowTexture);
      Render.drawTexturedRect(-arrowSize, -arrowSize, arrowSize * 2.0, arrowSize * 2.0, 0.0, 0.0, 1.0, 1.0);
      GlStateManager.popMatrix();
      EntityFinder.getAndDraw(this, this.mw.mc);
   }

   private void drawIcons() {
      GlStateManager.pushMatrix();
      if (this.mapMode.getConfig().rotate && this.mapMode.getConfig().circular) {
         GlStateManager.rotate(this.mw.mapRotationDegrees, 0.0F, 0.0F, 1.0F);
      }

      this.mw.markerManager.drawMarkers(this.mapMode, this.mapView);
      this.mw.markerManager.drawPKMarkers(this.mapMode, this.mapView, this, this.mw.mapRotationDegrees);
      if (this.mw.playerTrail.enabled) {
         this.mw.playerTrail.draw(this.mapMode, this.mapView);
      }

      this.drawNorthArrow();
      GlStateManager.popMatrix();
      this.drawPlayerArrow();
   }

   private void drawNorthArrow() {
      if (this.mapMode.getConfig().rotate) {
         double y = (double)this.mapMode.getH() / 2.0;
         double arrowSize = (double)this.mapMode.getConfig().playerArrowSize;
         Render.setColour(-1);
         this.mw.mc.renderEngine.bindTexture(Reference.northArrowTexture);
         Render.drawTexturedRect(-arrowSize, -y - arrowSize * 2.0, arrowSize * 2.0, arrowSize * 2.0, 0.0, 0.0, 1.0, 1.0);
      }

   }

   private void drawStatusText() {
      this.textOffset = 12;
      this.textY = this.mapMode.getTextY();
      this.textX = this.mapMode.getTextX();
      this.drawCoords();
      this.drawBiomeName();
      this.drawUndergroundMode();
   }

   private void drawCoords() {
      if (!this.mapMode.getConfig().coordsMode.equals(MapModeConfig.coordsModeStringArray[0])) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)this.textX, (float)this.textY, 0.0F);
         if (this.mapMode.getConfig().coordsMode.equals(MapModeConfig.coordsModeStringArray[1])) {
            GlStateManager.scale(0.5F, 0.5F, 1.0F);
            this.textOffset = (int)((float)this.textOffset * 0.5F);
         }

         Render.drawCentredString(0, 0, this.mapMode.getTextColour(), "%d, %d, %d", new Object[]{this.mw.playerXInt, this.mw.playerYInt, this.mw.playerZInt});
         this.textY += this.textOffset;
         GlStateManager.popMatrix();
      }

   }

   private void drawBiomeName() {
      if (!this.mapMode.getConfig().biomeMode.equals(MapModeConfig.coordsModeStringArray[0])) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)this.textX, (float)this.textY, 0.0F);
         if (this.mapMode.getConfig().biomeMode.equals(MapModeConfig.coordsModeStringArray[1])) {
            GlStateManager.scale(0.5F, 0.5F, 1.0F);
            this.textOffset = (int)((float)this.textOffset * 0.5F);
         }

         Render.drawCentredString(0, 0, this.mapMode.getTextColour(), this.mw.playerBiome.equals("") ? "BiomeName" : this.mw.playerBiome, new Object[0]);
         this.textY += this.textOffset;
         GlStateManager.popMatrix();
      }

   }

   private void drawUndergroundMode() {
      if (Config.undergroundMode) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)this.textX, (float)this.textY, 0.0F);
         GlStateManager.scale(0.5F, 0.5F, 1.0F);
         this.textOffset = (int)((float)this.textOffset * 0.5F);
         Render.drawCentredString(0, 0, this.mapMode.getTextColour(), "underground mode", new Object[0]);
         this.textY += this.textOffset;
         GlStateManager.popMatrix();
      }

   }

   private IMwDataProvider drawOverlay() {
      IMwDataProvider provider = MwAPI.getCurrentDataProvider();
      if (provider != null) {
         ArrayList<IMwChunkOverlay> overlays = provider.getChunksOverlay(this.mapView.getDimension(), this.mapView.getX(), this.mapView.getZ(), this.mapView.getMinX(), this.mapView.getMinZ(), this.mapView.getMaxX(), this.mapView.getMaxZ());
         if (overlays != null) {
            Iterator var3 = overlays.iterator();

            while(var3.hasNext()) {
               IMwChunkOverlay overlay = (IMwChunkOverlay)var3.next();
               paintChunk(this.mapMode, this.mapView, overlay);
            }
         }
      }

      return provider;
   }

   public void draw() {
      this.mapMode.updateMargin();
      this.mapMode.setScreenRes();
      this.mapView.setMapWH(this.mapMode);
      this.mapView.setTextureSize(this.mw.textureSize);
      if (pokeradarConfig.enableMap) {
         GlStateManager.pushMatrix();
         GlStateManager.translate((double)this.mapMode.getXTranslation(), (double)this.mapMode.getYTranslation(), 0.0);
         this.drawMap();
         if (this.mapMode.getConfig().borderMode) {
            this.drawBorder();
         }

         this.drawIcons();
         this.drawStatusText();
         GlStateManager.enableDepth();
         GlStateManager.popMatrix();
      }

      EntityFinder.getAndDraw(this, this.mw.mc);
   }

   public void drawDummy() {
      this.mapMode.updateMargin();
      this.mapMode.setScreenRes();
      GlStateManager.pushMatrix();
      GlStateManager.translate((double)this.mapMode.getXTranslation(), (double)this.mapMode.getYTranslation(), 1000.0);
      double u;
      double v;
      double w;
      double h;
      if (!this.mapMode.getConfig().circular) {
         u = 0.0;
         v = 0.0;
         w = 1.0 * (this.mapMode.getConfig().widthPercent / 100.0);
         h = 1.0 * (this.mapMode.getConfig().heightPercent / 100.0);
      } else {
         double scale1 = this.mw.mc.displayWidth < this.mw.mc.displayHeight ? 1.0 : (double)this.mw.mc.displayHeight / (double)this.mw.mc.displayWidth;
         double scale2 = this.mw.mc.displayWidth < this.mw.mc.displayHeight ? (double)this.mw.mc.displayWidth / (double)this.mw.mc.displayHeight : 1.0;
         u = 0.0;
         v = 0.0;
         w = 1.0 * (this.mapMode.getConfig().heightPercent / 100.0) * scale1;
         h = 1.0 * (this.mapMode.getConfig().heightPercent / 100.0) * scale2;
      }

      GlStateManager.pushMatrix();
      if (this.mapMode.getConfig().rotate && this.mapMode.getConfig().circular) {
         GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
      }

      if (this.mapMode.getConfig().circular) {
         Render.setCircularStencil(0.0, 0.0, (double)this.mapMode.getH() / 2.0);
      }

      this.mw.mc.renderEngine.bindTexture(Reference.DummyMapTexture);
      Render.setColourWithAlphaPercent(16777215, 60);
      Render.drawTexturedRect((double)this.mapMode.getX(), (double)this.mapMode.getY(), (double)this.mapMode.getW(), (double)this.mapMode.getH(), u, v, u + w, v + h);
      if (this.mapMode.getConfig().circular) {
         Render.disableStencil();
      }

      GlStateManager.popMatrix();
      if (this.mapMode.getConfig().borderMode) {
         this.drawBorder();
      }

      GlStateManager.pushMatrix();
      if (this.mapMode.getConfig().rotate && this.mapMode.getConfig().circular) {
         GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
      }

      if (this.mapMode.getConfig().rotate) {
         this.drawNorthArrow();
      }

      GlStateManager.popMatrix();
      this.drawStatusText();
      GlStateManager.enableDepth();
      GlStateManager.popMatrix();
   }

   private static void paintChunk(MapMode mapMode, MapView mapView, IMwChunkOverlay overlay) {
      int chunkX = overlay.getCoordinates().x;
      int chunkZ = overlay.getCoordinates().y;
      float filling = overlay.getFilling();
      Point2D.Double topCorner = mapMode.blockXZtoScreenXY(mapView, (double)(chunkX << 4), (double)(chunkZ << 4));
      Point2D.Double botCorner = mapMode.blockXZtoScreenXY(mapView, (double)(chunkX + 1 << 4), (double)(chunkZ + 1 << 4));
      topCorner.x = Math.max((double)mapMode.getX(), topCorner.x);
      topCorner.x = Math.min((double)(mapMode.getX() + mapMode.getW()), topCorner.x);
      topCorner.y = Math.max((double)mapMode.getY(), topCorner.y);
      topCorner.y = Math.min((double)(mapMode.getY() + mapMode.getH()), topCorner.y);
      botCorner.x = Math.max((double)mapMode.getX(), botCorner.x);
      botCorner.x = Math.min((double)(mapMode.getX() + mapMode.getW()), botCorner.x);
      botCorner.y = Math.max((double)mapMode.getY(), botCorner.y);
      botCorner.y = Math.min((double)(mapMode.getY() + mapMode.getH()), botCorner.y);
      double sizeX = (botCorner.x - topCorner.x) * (double)filling;
      double sizeY = (botCorner.y - topCorner.y) * (double)filling;
      double offsetX = (botCorner.x - topCorner.x - sizeX) / 2.0;
      double offsetY = (botCorner.y - topCorner.y - sizeY) / 2.0;
      if (overlay.hasBorder()) {
         Render.setColour(overlay.getBorderColor());
         Render.drawRectBorder(topCorner.x + 1.0, topCorner.y + 1.0, botCorner.x - topCorner.x - 1.0, botCorner.y - topCorner.y - 1.0, (double)overlay.getBorderWidth());
      }

      Render.setColour(overlay.getColor());
      Render.drawRect(topCorner.x + offsetX + 1.0, topCorner.y + offsetY + 1.0, sizeX - 1.0, sizeY - 1.0);
   }

   public MapMode getMapMode() {
      return this.mapMode;
   }

   public MapView getMapView() {
      return this.mapView;
   }
}
