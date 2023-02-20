//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

import journeymap.api.IMapModeConfig;
import journeymap.gui.ModGuiConfig;
import journeymap.gui.ModGuiConfigHUD;

public class MapModeConfig implements IMapModeConfig {
   public final String configCategory;
   public final String mapPosCategory;
   public static final String[] coordsModeStringArray = new String[]{"mw.config.map.coordsMode.disabled", "mw.config.map.coordsMode.small", "mw.config.map.coordsMode.large"};
   public boolean enabledDef = true;
   public boolean enabled;
   public boolean rotateDef;
   public boolean rotate;
   public boolean circularDef;
   public boolean circular;
   public String coordsModeDef;
   public String coordsMode;
   public boolean borderModeDef;
   public boolean borderMode;
   public int playerArrowSizeDef;
   public int playerArrowSize;
   public int markerSizeDef;
   public int markerSize;
   public int trailMarkerSizeDef;
   public int trailMarkerSize;
   public int alphaPercentDef;
   public int alphaPercent;
   public String biomeModeDef;
   public String biomeMode;
   public double xPosDef;
   public double xPos;
   public double yPosDef;
   public double yPos;
   public double heightPercentDef;
   public double heightPercent;
   public double widthPercentDef;
   public double widthPercent;

   public MapModeConfig(String configCategory) {
      this.enabled = this.enabledDef;
      this.rotateDef = false;
      this.rotate = false;
      this.circularDef = false;
      this.circular = this.circularDef;
      this.coordsModeDef = coordsModeStringArray[1];
      this.coordsMode = this.coordsModeDef;
      this.borderModeDef = false;
      this.borderMode = this.borderModeDef;
      this.playerArrowSizeDef = 5;
      this.playerArrowSize = this.playerArrowSizeDef;
      this.markerSizeDef = 5;
      this.markerSize = this.markerSizeDef;
      this.trailMarkerSizeDef = 3;
      this.trailMarkerSize = this.trailMarkerSizeDef;
      this.alphaPercentDef = 100;
      this.alphaPercent = this.alphaPercentDef;
      this.biomeModeDef = coordsModeStringArray[2];
      this.biomeMode = this.biomeModeDef;
      this.xPosDef = 0.0;
      this.xPos = this.xPosDef;
      this.yPosDef = 0.0;
      this.yPos = this.yPosDef;
      this.heightPercentDef = 100.0;
      this.heightPercent = this.heightPercentDef;
      this.widthPercentDef = 100.0;
      this.widthPercent = this.widthPercentDef;
      this.configCategory = configCategory;
      this.mapPosCategory = configCategory + "." + "mappos";
   }

   public void loadConfig() {
      this.playerArrowSize = ConfigurationHandler.configuration.getInt("playerArrowSize", this.configCategory, this.playerArrowSizeDef, 1, 20, "", "mw.config.map.playerArrowSize");
      this.markerSize = ConfigurationHandler.configuration.getInt("markerSize", this.configCategory, this.markerSizeDef, 1, 20, "", "mw.config.map.markerSize");
      this.alphaPercent = ConfigurationHandler.configuration.getInt("alphaPercent", this.configCategory, this.alphaPercentDef, 0, 100, "", "mw.config.map.alphaPercent");
      this.trailMarkerSize = Math.max(1, this.markerSize - 1);
      this.xPos = ConfigurationHandler.configuration.get(this.mapPosCategory, "xPos", this.xPosDef, " [range: 0.0 ~ 100.0, default: " + this.xPosDef + "]", 0.0, 100.0).setLanguageKey("mw.config.map.xPos").setConfigEntryClass(ModGuiConfig.ModNumberSliderEntry.class).getDouble();
      this.yPos = ConfigurationHandler.configuration.get(this.mapPosCategory, "yPos", this.yPosDef, " [range: 0.0 ~ 100.0, default: " + this.yPosDef + "]", 0.0, 100.0).setLanguageKey("mw.config.map.yPos").setConfigEntryClass(ModGuiConfig.ModNumberSliderEntry.class).getDouble();
      this.heightPercent = ConfigurationHandler.configuration.get(this.mapPosCategory, "heightPercent", this.heightPercentDef, " [range: 0.0 ~ 100.0, default: " + this.heightPercentDef + "]", 0.0, 100.0).setLanguageKey("mw.config.map.heightPercent").setConfigEntryClass(ModGuiConfig.ModNumberSliderEntry.class).getDouble();
      this.widthPercent = ConfigurationHandler.configuration.get(this.mapPosCategory, "widthPercent", this.widthPercentDef, " [range: 0.0 ~ 100.0, default: " + this.widthPercentDef + "]", 0.0, 100.0).setLanguageKey("mw.config.map.widthPercent").setConfigEntryClass(ModGuiConfig.ModNumberSliderEntry.class).getDouble();
   }

   public void setDefaults() {
      ConfigurationHandler.configuration.getCategory(this.mapPosCategory).setLanguageKey("mw.config.map.ctgy.position").setConfigEntryClass(ModGuiConfigHUD.MapPosConfigEntry.class).setShowInGui(false);
   }

   public String getConfigCategory() {
      return this.configCategory;
   }

   public String getMapPosCategory() {
      return this.mapPosCategory;
   }

   public String[] getCoordsModeStringArray() {
      return coordsModeStringArray;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public boolean getRotate() {
      return this.rotate;
   }

   public boolean getCircular() {
      return this.circular;
   }

   public String getCoordsMode() {
      return this.coordsMode;
   }

   public boolean getBorderMode() {
      return this.borderMode;
   }

   public int getPlayerArrowSize() {
      return this.playerArrowSize;
   }

   public int getMarkerSize() {
      return this.markerSize;
   }

   public int getTrailMarkerSize() {
      return this.trailMarkerSize;
   }

   public int getAlphaPercent() {
      return this.alphaPercent;
   }

   public String getBiomeMode() {
      return this.biomeMode;
   }

   public double getXPos() {
      return this.xPos;
   }

   public double getYPos() {
      return this.yPos;
   }

   public double getHeightPercent() {
      return this.heightPercent;
   }

   public double getWidthPercent() {
      return this.widthPercent;
   }
}
