//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

import journeymap.gui.ModGuiConfig;
import journeymap.gui.ModGuiConfigHUD;

public class largeMapModeConfig extends MapModeConfig {
   public largeMapModeConfig(String configCategory) {
      super(configCategory);
   }

   public void loadConfig() {
      super.loadConfig();
      this.enabled = ConfigurationHandler.configuration.getBoolean("enabled", this.configCategory, this.enabledDef, "", "mw.config.map.enabled");
      this.rotate = ConfigurationHandler.configuration.getBoolean("rotate", this.configCategory, this.rotateDef, "", "mw.config.map.rotate");
      this.circular = ConfigurationHandler.configuration.getBoolean("circular", this.configCategory, this.circularDef, "", "mw.config.map.circular");
      this.coordsMode = ConfigurationHandler.configuration.getString("coordsMode", this.configCategory, this.coordsModeDef, "", coordsModeStringArray, "mw.config.map.coordsMode");
      this.borderMode = ConfigurationHandler.configuration.getBoolean("borderMode", this.configCategory, this.borderModeDef, "", "mw.config.map.borderMode");
      this.biomeMode = ConfigurationHandler.configuration.getString("biomeMode", this.configCategory, this.biomeModeDef, "", coordsModeStringArray, "mw.config.map.biomeMode");
   }

   public void setDefaults() {
      this.rotateDef = true;
      this.circularDef = true;
      this.coordsModeDef = coordsModeStringArray[1];
      this.borderModeDef = true;
      this.heightPercentDef = -1.0;
      this.xPosDef = 50.0;
      this.yPosDef = 5.0;
      this.heightPercentDef = 88.0;
      this.widthPercentDef = 91.0;
      ConfigurationHandler.configuration.get(this.configCategory, "enabled", this.enabled).setRequiresWorldRestart(true);
      ConfigurationHandler.configuration.get(this.configCategory, "rotate", this.rotate).setConfigEntryClass(ModGuiConfig.ModBooleanEntry.class);
      ConfigurationHandler.configuration.getCategory(this.mapPosCategory).setConfigEntryClass(ModGuiConfigHUD.MapPosConfigEntry.class).setLanguageKey("mw.config.map.ctgy.position").setShowInGui(true);
   }
}
