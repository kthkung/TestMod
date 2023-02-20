package journeymap.config;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;

public class pokeradarConfig {
   public final String configCategory;
   public static boolean drawMarkersInWorldDef = true;
   public static boolean drawMarkersInWorld;
   public static boolean drawMarkersNameInWorldDef;
   public static boolean drawMarkersNameInWorld;
   public static boolean allowLinesInWorldDef;
   public static boolean allowLinesInWorld;
   public static boolean drawLootMarkersDef;
   public static boolean drawLootMarkers;
   public static boolean drawShinyMarkersDef;
   public static boolean drawShinyMarkers;
   public static boolean drawSearchedMarkersDef;
   public static boolean drawSearchedMarkers;
   public static boolean drawBossMarkersDef;
   public static boolean drawBossMarkers;
   public static boolean drawSpecialMarkersDef;
   public static boolean drawSpecialMarkers;
   public static boolean drawDittoMarkersDef;
   public static boolean drawDittoMarkers;
   public static boolean drawDexMarkersDef;
   public static boolean drawDexMarkers;
   public static boolean drawNPCMarkersDef;
   public static boolean drawNPCMarkers;
   public static boolean drawWormMarkersDef;
   public static boolean drawWormMarkers;
   public static boolean useTexturesDef;
   public static boolean useTextures;
   public static double textureSizeDef;
   public static double textureSize;
   public static double dotSizeDef;
   public static double dotSize;
   public static boolean drawLootLinesDef;
   public static boolean drawLootLines;
   public static boolean drawShinyLinesDef;
   public static boolean drawShinyLines;
   public static boolean drawDittoLinesDef;
   public static boolean drawDittoLines;
   public static boolean drawDexLinesDef;
   public static boolean drawDexLines;
   public static boolean drawWormLinesDef;
   public static boolean drawWormLines;
   public static boolean drawNPCLinesDef;
   public static boolean drawNPCLines;
   public static boolean drawSearchedLinesDef;
   public static boolean drawSearchedLines;
   public static boolean drawBossLinesDef;
   public static boolean drawBossLines;
   public static boolean drawSpecialLinesDef;
   public static boolean drawSpecialLines;
   public static boolean enableMapDef;
   public static boolean enableMap;

   public pokeradarConfig(String configCategory) {
      this.configCategory = configCategory;
   }

   public void loadConfig() {
      enableMap = ConfigurationHandler.configuration.getBoolean("enableMap", "pixelradar", enableMapDef, "", "mw.config.pixelradar.enableMap");
      drawMarkersInWorld = ConfigurationHandler.configuration.getBoolean("drawMarkersInWorld", "pixelradar", drawMarkersInWorldDef, "", "mw.config.pixelradar.drawMarkersInWorld");
      drawMarkersNameInWorld = ConfigurationHandler.configuration.getBoolean("drawMarkersNameInWorld", "pixelradar", drawMarkersNameInWorldDef, "", "mw.config.pixelradar.drawMarkersNameInWorld");
      allowLinesInWorld = ConfigurationHandler.configuration.getBoolean("allowLinesInWorld", "pixelradar", allowLinesInWorldDef, "", "mw.config.pixelradar.allowLinesInWorld");
      drawLootMarkers = ConfigurationHandler.configuration.getBoolean("drawLootMarkers", "pixelradar", drawLootMarkersDef, "", "mw.config.pixelradar.drawLootMarkers");
      drawShinyMarkers = ConfigurationHandler.configuration.getBoolean("drawShinyMarkers", "pixelradar", drawShinyMarkersDef, "", "mw.config.pixelradar.drawShinyMarkers");
      drawDittoMarkers = ConfigurationHandler.configuration.getBoolean("drawDittoMarkers", "pixelradar", drawDittoMarkersDef, "", "mw.config.pixelradar.drawDittoMarkers");
      drawDexMarkers = ConfigurationHandler.configuration.getBoolean("drawDexMarkers", "pixelradar", drawDexMarkersDef, "", "mw.config.pixelradar.drawDexMarkers");
      drawNPCMarkers = ConfigurationHandler.configuration.getBoolean("drawNPCMarkers", "pixelradar", drawNPCMarkersDef, "", "mw.config.pixelradar.drawNPCMarkers");
      drawWormMarkers = ConfigurationHandler.configuration.getBoolean("drawWormMarkers", "pixelradar", drawWormMarkersDef, "", "mw.config.pixelradar.drawWormMarkers");
      drawLootLines = ConfigurationHandler.configuration.getBoolean("drawLootLines", "pixelradar", drawLootLinesDef, "", "mw.config.pixelradar.drawLootLines");
      drawShinyLines = ConfigurationHandler.configuration.getBoolean("drawShinyLines", "pixelradar", drawShinyLinesDef, "", "mw.config.pixelradar.drawShinyLines");
      drawDittoLines = ConfigurationHandler.configuration.getBoolean("drawDittoLines", "pixelradar", drawDittoLinesDef, "", "mw.config.pixelradar.drawDittoLines");
      drawDexLines = ConfigurationHandler.configuration.getBoolean("drawDexLines", "pixelradar", drawDexLinesDef, "", "mw.config.pixelradar.drawDexLines");
      drawNPCLines = ConfigurationHandler.configuration.getBoolean("drawNPCLines", "pixelradar", drawNPCLinesDef, "", "mw.config.pixelradar.drawNPCLines");
      drawWormLines = ConfigurationHandler.configuration.getBoolean("drawWormLines", "pixelradar", drawWormLinesDef, "", "mw.config.pixelradar.drawWormLines");
      drawSearchedLines = ConfigurationHandler.configuration.getBoolean("drawSearchedLines", "pixelradar", drawSearchedLinesDef, "", "mw.config.pixelradar.drawSearchedLines");
      drawBossLines = ConfigurationHandler.configuration.getBoolean("drawBossLines", "pixelradar", drawBossLinesDef, "", "mw.config.pixelradar.drawBossLines");
      drawSpecialLines = ConfigurationHandler.configuration.getBoolean("drawSpecialLines", "pixelradar", drawSpecialLinesDef, "", "mw.config.pixelradar.drawSpecialLines");
      drawSearchedMarkers = ConfigurationHandler.configuration.getBoolean("drawSearchedMarkers", "pixelradar", drawSearchedMarkersDef, "", "mw.config.pixelradar.drawSearchedMarkers");
      drawBossMarkers = ConfigurationHandler.configuration.getBoolean("drawBossMarkers", "pixelradar", drawBossMarkersDef, "", "mw.config.pixelradar.drawBossMarkers");
      drawSpecialMarkers = ConfigurationHandler.configuration.getBoolean("drawSpecialMarkers", "pixelradar", drawSpecialMarkersDef, "", "mw.config.pixelradar.drawSpecialMarkers");
      useTextures = ConfigurationHandler.configuration.getBoolean("useTextures", "pixelradar", useTexturesDef, "", "mw.config.pixelradar.useTextures");
      Property textureSize = ConfigurationHandler.configuration.get("pixelradar", "sizeTexture", 1, "Change the size of texture markers on your minimap", 1, 30);
      textureSize.setConfigEntryClass(this.getSliderClass());
      textureSize.setLanguageKey("mw.config.pixelradar.textureSize");
      pokeradarConfig.textureSize = textureSize.getDouble();
      Property dotSize = ConfigurationHandler.configuration.get("pixelradar", "sizeDot", 1, "Change the size of dot markers on your minimap", 1, 30);
      dotSize.setConfigEntryClass(this.getSliderClass());
      dotSize.setLanguageKey("mw.config.pixelradar.dotSize");
      pokeradarConfig.dotSize = dotSize.getDouble();
   }

   public void setDefaults() {
      allowLinesInWorld = true;
      drawMarkersInWorld = true;
      drawMarkersNameInWorld = true;
      drawLootMarkers = true;
      drawShinyMarkers = true;
      drawDittoMarkers = true;
      drawDexMarkers = false;
      drawSearchedMarkers = true;
      drawBossMarkers = true;
      drawSpecialMarkers = true;
      useTextures = true;
      drawLootLines = false;
      drawShinyLines = true;
      drawDittoLines = true;
      drawDexLines = false;
      drawSearchedLines = true;
      drawBossLines = true;
      drawSpecialLines = true;
      enableMap = true;
   }

   public Class<? extends IConfigEntry> getSliderClass() {
      return NumberSliderEntry.class;
   }

   public String getConfigCategory() {
      return this.configCategory;
   }

   static {
      drawMarkersInWorld = drawMarkersInWorldDef;
      drawMarkersNameInWorldDef = true;
      drawMarkersNameInWorld = drawMarkersNameInWorldDef;
      allowLinesInWorldDef = true;
      allowLinesInWorld = allowLinesInWorldDef;
      drawLootMarkersDef = true;
      drawLootMarkers = drawLootMarkersDef;
      drawShinyMarkersDef = true;
      drawShinyMarkers = drawShinyMarkersDef;
      drawSearchedMarkersDef = true;
      drawSearchedMarkers = drawSearchedMarkersDef;
      drawBossMarkersDef = true;
      drawBossMarkers = drawBossMarkersDef;
      drawSpecialMarkersDef = true;
      drawSpecialMarkers = drawSpecialMarkersDef;
      drawDittoMarkersDef = true;
      drawDittoMarkers = drawDittoMarkersDef;
      drawDexMarkersDef = false;
      drawDexMarkers = drawDittoMarkersDef;
      drawNPCMarkersDef = false;
      drawNPCMarkers = drawNPCMarkersDef;
      drawWormMarkersDef = false;
      drawWormMarkers = drawWormMarkersDef;
      useTexturesDef = true;
      useTextures = useTexturesDef;
      textureSizeDef = 3.0D;
      textureSize = textureSizeDef;
      dotSizeDef = 1.0D;
      dotSize = dotSizeDef;
      drawLootLinesDef = false;
      drawLootLines = drawLootLinesDef;
      drawShinyLinesDef = true;
      drawShinyLines = drawShinyLinesDef;
      drawDittoLinesDef = true;
      drawDittoLines = drawDittoLinesDef;
      drawDexLinesDef = false;
      drawDexLines = drawDittoLinesDef;
      drawWormLinesDef = true;
      drawWormLines = drawWormLinesDef;
      drawNPCLinesDef = false;
      drawNPCLines = drawNPCLinesDef;
      drawSearchedLinesDef = true;
      drawSearchedLines = drawSearchedLinesDef;
      drawBossLinesDef = true;
      drawBossLines = drawBossLinesDef;
      drawSpecialLinesDef = true;
      drawSpecialLines = drawSpecialLinesDef;
      enableMapDef = true;
      enableMap = enableMapDef;
   }
}
