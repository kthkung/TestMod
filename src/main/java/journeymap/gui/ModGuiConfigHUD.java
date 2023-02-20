//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import journeymap.Mw;
import journeymap.config.MapModeConfig;
import journeymap.config.largeMapModeConfig;
import journeymap.config.smallMapModeConfig;
import journeymap.map.MapRenderer;
import journeymap.map.MapView;
import journeymap.map.mapmode.MapMode;

public class ModGuiConfigHUD extends GuiConfig {
   private Mw mw;
   public MapMode mapMode;
   private MapRenderer map;
   private Boolean DraggingMap = false;
   protected GuiButtonExt btnTopLeft;
   protected GuiButtonExt btnTopRight;
   protected GuiButtonExt btnBottomLeft;
   protected GuiButtonExt btnBottomRight;
   protected GuiButtonExt btnCenterTop;
   protected GuiButtonExt btnCenterBottom;
   protected GuiButtonExt btnCenterLeft;
   protected GuiButtonExt btnCenterRight;
   protected GuiButtonExt btnCenter;
   private MapModeConfig dummyMapConfig;

   public ModGuiConfigHUD(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String Config) {
      super(parentScreen, configElements, modID, configID, allRequireWorldRestart, allRequireMcRestart, title, "Use right click and hold to move the map");
      if (Config.equals("fullscreenmap")) {
         this.dummyMapConfig = new MapModeConfig("fullscreenmap");
      } else if (Config.equals("largemap")) {
         this.dummyMapConfig = new largeMapModeConfig("largemap");
      } else if (Config.equals("smallmap")) {
         this.dummyMapConfig = new smallMapModeConfig("smallmap");
      }

      this.dummyMapConfig.setDefaults();
      this.dummyMapConfig.loadConfig();
      this.mw = Mw.getInstance();
      this.mapMode = new MapMode(this.dummyMapConfig);
      this.map = new MapRenderer(this.mw, this.mapMode, (MapView)null);
   }

   public void initGui() {
      super.initGui();
      int topLeftWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.topleft", new Object[0])) + 20, 100);
      int topRightWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.topright", new Object[0])) + 20, 100);
      int bottomLeftWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.botleft", new Object[0])) + 20, 100);
      int bottomRightWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.botright", new Object[0])) + 20, 100);
      int CenterTopWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.centertop", new Object[0])) + 20, 100);
      int CenterBottomWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.centerbottom", new Object[0])) + 20, 100);
      int CenterWidth = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.center", new Object[0])) + 20, 100);
      int CenterLeft = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.centerleft", new Object[0])) + 20, 100);
      int CenterRight = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("mw.config.map.ctgy.position.centerright", new Object[0])) + 20, 100);
      int buttonWidthHalf1 = (bottomLeftWidth + 5 + bottomRightWidth + CenterTopWidth + 5) / 2;
      int buttonWidthHalf2 = (CenterLeft + 5 + CenterWidth + CenterRight + 5) / 2;
      int buttonWidthHalf3 = (topLeftWidth + 5 + topRightWidth + CenterBottomWidth + 5) / 2;
      int buttonHeigth1 = this.height - 29 - 29 - 29 - 29;
      int buttonHeigth2 = this.height - 29 - 29 - 29;
      int buttonHeigth3 = this.height - 29 - 29;
      this.buttonList.add(new GuiButtonExt(3000, this.width / 2 - buttonWidthHalf1, buttonHeigth1, topLeftWidth, 20, I18n.format("mw.config.map.ctgy.position.topleft", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3001, this.width / 2 - buttonWidthHalf1 + topLeftWidth + 5, buttonHeigth1, CenterTopWidth, 20, I18n.format("mw.config.map.ctgy.position.centertop", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3002, this.width / 2 - buttonWidthHalf1 + topLeftWidth + 5 + CenterTopWidth + 5, buttonHeigth1, topRightWidth, 20, I18n.format("mw.config.map.ctgy.position.topright", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3010, this.width / 2 - buttonWidthHalf2, buttonHeigth2, CenterLeft, 20, I18n.format("mw.config.map.ctgy.position.centerleft", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3011, this.width / 2 - buttonWidthHalf2 + CenterLeft + 5, buttonHeigth2, CenterWidth, 20, I18n.format("mw.config.map.ctgy.position.center", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3012, this.width / 2 - buttonWidthHalf2 + CenterLeft + 5 + CenterWidth + 5, buttonHeigth2, CenterRight, 20, I18n.format("mw.config.map.ctgy.position.centerright", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3020, this.width / 2 - buttonWidthHalf3, buttonHeigth3, bottomLeftWidth, 20, I18n.format("mw.config.map.ctgy.position.botleft", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3021, this.width / 2 - buttonWidthHalf3 + bottomLeftWidth + 5, buttonHeigth3, CenterBottomWidth, 20, I18n.format("mw.config.map.ctgy.position.centerbottom", new Object[0])));
      this.buttonList.add(new GuiButtonExt(3022, this.width / 2 - buttonWidthHalf3 + bottomLeftWidth + 5 + CenterBottomWidth + 5, buttonHeigth3, bottomRightWidth, 20, I18n.format("mw.config.map.ctgy.position.botright", new Object[0])));
      this.UpdateParrentSettings();
   }

   protected void actionPerformed(GuiButton button) {
      double bottomOffset = 0.0;
      if (!this.mapMode.getConfig().biomeMode.equals(MapModeConfig.coordsModeStringArray[0])) {
         bottomOffset = bottomOffset + (double)this.mc.fontRenderer.FONT_HEIGHT + 3.0;
      }

      if (!this.mapMode.getConfig().biomeMode.equals(MapModeConfig.coordsModeStringArray[0])) {
         bottomOffset = bottomOffset + (double)this.mc.fontRenderer.FONT_HEIGHT + 3.0;
      }

      bottomOffset = bottomOffset / (double)this.height * 100.0;
      double SmallMarginY = 10.0 / (double)(this.height - this.mapMode.getH()) * 100.0;
      double SmallMarginX = 10.0 / (double)(this.width - this.mapMode.getW()) * 100.0;
      double LargeMarginBottom = 40.0 / (double)(this.height - this.mapMode.getH()) * 100.0;
      bottomOffset = bottomOffset < SmallMarginY ? SmallMarginY : bottomOffset;
      if (button.id == 3000) {
         this.updateMap(new Point2D.Double(SmallMarginX, SmallMarginY));
      } else if (button.id == 3001) {
         this.updateMap(new Point2D.Double(50.0, SmallMarginY));
      } else if (button.id == 3002) {
         this.updateMap(new Point2D.Double(100.0 - SmallMarginX, SmallMarginY));
      } else if (button.id == 3010) {
         this.updateMap(new Point2D.Double(SmallMarginX, 50.0));
      } else if (button.id == 3011) {
         this.updateMap(new Point2D.Double(50.0, 50.0));
      } else if (button.id == 3012) {
         this.updateMap(new Point2D.Double(100.0 - SmallMarginX, 50.0));
      } else if (button.id == 3020) {
         this.updateMap(new Point2D.Double(SmallMarginX, 100.0 - bottomOffset));
      } else if (button.id == 3021) {
         this.updateMap(new Point2D.Double(50.0, 100.0 - LargeMarginBottom));
      } else if (button.id == 3022) {
         this.updateMap(new Point2D.Double(100.0 - SmallMarginX, 100.0 - bottomOffset));
      } else {
         super.actionPerformed(button);
      }

   }

   public void mouseClicked(int x, int y, int mouseEvent) throws IOException {
      if (mouseEvent == 1 && this.mapMode.posWithin(x, y)) {
         this.DraggingMap = true;
      } else {
         super.mouseClicked(x, y, mouseEvent);
      }

   }

   protected void mouseClickMove(int x, int y, int mouseEvent, long timeSinceLastClick) {
      if (this.DraggingMap) {
         this.updateMap(this.mapMode.getNewPosPoint((double)x, (double)y));
      } else {
         super.mouseClickMove(x, y, mouseEvent, timeSinceLastClick);
      }

   }

   public void mouseReleased(int x, int y, int mouseEvent) {
      if (this.DraggingMap) {
         this.DraggingMap = false;
      } else {
         super.mouseReleased(x, y, mouseEvent);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawScreen(mouseX, mouseY, partialTicks);
      this.UpdateButtonValues();
      this.map.drawDummy();
   }

   private void UpdateButtonValues() {
      Iterator var1 = this.entryList.listEntries.iterator();

      while(var1.hasNext()) {
         GuiConfigEntries.IConfigEntry entry = (GuiConfigEntries.IConfigEntry)var1.next();
         if (entry.getName().equals("xPos")) {
            this.dummyMapConfig.xPos = (Double)entry.getCurrentValue();
         } else if (entry.getName().equals("yPos")) {
            this.dummyMapConfig.yPos = (Double)entry.getCurrentValue();
         } else if (entry.getName().equals("heightPercent")) {
            this.dummyMapConfig.heightPercent = (Double)entry.getCurrentValue();
         } else if (entry.getName().equals("widthPercent")) {
            this.dummyMapConfig.widthPercent = (Double)entry.getCurrentValue();
            if (this.mapMode.getConfig().circular) {
               ((ModGuiConfig.ModNumberSliderEntry)entry).setEnabled(false);
            } else {
               ((ModGuiConfig.ModNumberSliderEntry)entry).setEnabled(true);
            }
         }
      }

   }

   private void UpdateParrentSettings() {
      if (this.parentScreen != null && this.parentScreen instanceof GuiConfig) {
         GuiConfig parrent = (GuiConfig)this.parentScreen;
         if (parrent.entryList != null && parrent.entryList.listEntries != null) {
            Iterator var2 = parrent.entryList.listEntries.iterator();

            while(var2.hasNext()) {
               GuiConfigEntries.IConfigEntry entry = (GuiConfigEntries.IConfigEntry)var2.next();
               if (entry.getName().equals("circular")) {
                  this.dummyMapConfig.circular = (Boolean)entry.getCurrentValue();
               } else if (entry.getName().equals("coordsMode")) {
                  this.dummyMapConfig.coordsMode = (String)entry.getCurrentValue();
               } else if (entry.getName().equals("borderMode")) {
                  this.dummyMapConfig.borderMode = (Boolean)entry.getCurrentValue();
               } else if (entry.getName().equals("playerArrowSize")) {
                  this.dummyMapConfig.playerArrowSize = Integer.valueOf((String)entry.getCurrentValue());
               } else if (entry.getName().equals("biomeMode")) {
                  this.dummyMapConfig.biomeMode = (String)entry.getCurrentValue();
               }
            }
         }
      }

   }

   private void updateMap(Point2D.Double point) {
      Iterator var2 = this.entryList.listEntries.iterator();

      while(var2.hasNext()) {
         GuiConfigEntries.IConfigEntry entry = (GuiConfigEntries.IConfigEntry)var2.next();
         if (entry instanceof ModGuiConfig.ModNumberSliderEntry) {
            if (entry.getName().equals("xPos")) {
               ((ModGuiConfig.ModNumberSliderEntry)entry).setValue(point.getX());
            } else if (entry.getName().equals("yPos")) {
               ((ModGuiConfig.ModNumberSliderEntry)entry).setValue(point.getY());
            }
         }
      }

   }

   public static class MapPosConfigEntry extends GuiConfigEntries.CategoryEntry {
      public MapPosConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
         super(owningScreen, owningEntryList, prop);
      }

      protected GuiScreen buildChildScreen() {
         String QualifiedName = this.configElement.getQualifiedName();
         String config = QualifiedName.substring(0, QualifiedName.indexOf(".")).replace(".", "");
         return new ModGuiConfigHUD(this.owningScreen, this.getConfigElement().getChildElements(), this.owningScreen.modID, (String)null, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, this.owningScreen.title, config);
      }
   }
}
