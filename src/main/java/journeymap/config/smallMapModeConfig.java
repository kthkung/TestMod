//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

import journeymap.gui.ModGuiConfig;
import journeymap.gui.ModGuiConfigHUD;

public class smallMapModeConfig extends largeMapModeConfig {
   public smallMapModeConfig(String configCategory) {
      super(configCategory);
   }

   public void loadConfig() {
      super.loadConfig();
      ConfigurationHandler.configuration.getCategory(this.configCategory).remove("Position");
      ConfigurationHandler.configuration.getCategory(this.configCategory).remove("heightPercent");
   }

   public void setDefaults() {
      this.rotateDef = true;
      this.circularDef = true;
      this.coordsModeDef = coordsModeStringArray[1];
      this.biomeModeDef = coordsModeStringArray[1];
      this.borderModeDef = true;
      this.playerArrowSizeDef = 4;
      this.markerSizeDef = 3;
      this.heightPercentDef = 30.0;
      this.xPosDef = 97.5;
      this.yPosDef = 17.0;
      this.heightPercentDef = 30.0;
      this.widthPercentDef = 30.0;
      ConfigurationHandler.configuration.get(this.configCategory, "enabled", this.enabledDef).setRequiresWorldRestart(true);
      ConfigurationHandler.configuration.get(this.configCategory, "rotate", this.rotate).setConfigEntryClass(ModGuiConfig.ModBooleanEntry.class);
      ConfigurationHandler.configuration.getCategory(this.mapPosCategory).setLanguageKey("mw.config.map.ctgy.position").setConfigEntryClass(ModGuiConfigHUD.MapPosConfigEntry.class).setShowInGui(true);
   }
}
