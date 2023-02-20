//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import journeymap.util.Logging;

import java.io.File;

public class ConfigurationHandler {
   public static Configuration configuration;

   public ConfigurationHandler() {
   }

   public static void init(File configFile) {
      if (configuration == null) {
         configuration = new Configuration(configFile);
         setMapModeDefaults();
         loadConfig();
         Config.fullScreenMap.loadConfig();
         Config.largeMap.loadConfig();
         Config.smallMap.loadConfig();
         Config.pokeradar.loadConfig();
         if (configuration.hasChanged()) {
            configuration.save();
         }

         configuration.get("mw.configgui.ctgy.general", "overlayModeIndex", Config.overlayModeIndexDef).setShowInGui(false);
         configuration.get("mw.configgui.ctgy.general", "overlayZoomLevel", Config.zoomInLevelsDef).setShowInGui(false);
      }

   }

   public static void loadConfig() {
      Config.linearTextureScaling = configuration.getBoolean("linearTextureScaling", "mw.configgui.ctgy.general", Config.linearTextureScalingDef, "", "mw.config.linearTextureScaling");
      Config.useSavedBlockColours = configuration.getBoolean("useSavedBlockColours", "mw.configgui.ctgy.general", Config.useSavedBlockColoursDef, "", "mw.config.useSavedBlockColours");
      Config.teleportEnabled = configuration.getBoolean("teleportEnabled", "mw.configgui.ctgy.general", Config.teleportEnabledDef, "", "mw.config.teleportEnabled");
      Config.teleportCommand = configuration.getString("teleportCommand", "mw.configgui.ctgy.general", Config.teleportCommandDef, "", "mw.config.teleportCommand");
      Config.maxChunkSaveDistSq = configuration.getInt("maxChunkSaveDistSq", "mw.configgui.ctgy.general", Config.maxChunkSaveDistSqDef, 1, 65536, "", "mw.config.maxChunkSaveDistSq");
      Config.mapPixelSnapEnabled = configuration.getBoolean("mapPixelSnapEnabled", "mw.configgui.ctgy.general", Config.mapPixelSnapEnabledDef, "", "mw.config.mapPixelSnapEnabled");
      Config.maxDeathMarkers = configuration.getInt("maxDeathMarkers", "mw.configgui.ctgy.general", Config.maxDeathMarkersDef, 0, 1000, "", "mw.config.maxDeathMarkers");
      Config.chunksPerTick = configuration.getInt("chunksPerTick", "mw.configgui.ctgy.general", Config.chunksPerTickDef, 1, 500, "", "mw.config.chunksPerTick");
      Config.saveDirOverride = configuration.getString("saveDirOverride", "mw.configgui.ctgy.general", Config.saveDirOverrideDef, "", "mw.config.saveDirOverride");
      Config.portNumberInWorldNameEnabled = configuration.getBoolean("portNumberInWorldNameEnabled", "mw.configgui.ctgy.general", Config.portNumberInWorldNameEnabledDef, "", "mw.config.portNumberInWorldNameEnabled");
      Config.undergroundMode = configuration.getBoolean("undergroundMode", "mw.configgui.ctgy.general", Config.undergroundModeDef, "", "mw.config.undergroundMode");
      Config.regionFileOutputEnabledSP = configuration.getBoolean("regionFileOutputEnabledSP", "mw.configgui.ctgy.general", Config.regionFileOutputEnabledSPDef, "", "mw.config.regionFileOutputEnabledSP");
      Config.regionFileOutputEnabledMP = configuration.getBoolean("regionFileOutputEnabledMP", "mw.configgui.ctgy.general", Config.regionFileOutputEnabledMPDef, "", "mw.config.regionFileOutputEnabledMP");
      Config.backgroundTextureMode = configuration.getString("backgroundTextureMode", "mw.configgui.ctgy.general", Config.backgroundTextureModeDef, "", Config.backgroundModeStringArray, "mw.config.backgroundTextureMode");
      Config.zoomOutLevels = configuration.getInt("zoomOutLevels", "mw.configgui.ctgy.general", Config.zoomOutLevelsDef, 1, 256, "", "mw.config.zoomOutLevels");
      Config.zoomInLevels = -configuration.getInt("zoomInLevels", "mw.configgui.ctgy.general", -Config.zoomInLevelsDef, 1, 256, "", "mw.config.zoomInLevels");
      Config.configTextureSize = configuration.getInt("textureSize", "mw.configgui.ctgy.general", Config.configTextureSizeDef, 1024, 4096, "", "mw.config.textureSize");
      Config.overlayModeIndex = configuration.getInt("overlayModeIndex", "mw.configgui.ctgy.general", Config.overlayModeIndexDef, 0, 1000, "", "mw.config.overlayModeIndex");
      Config.overlayZoomLevel = configuration.getInt("overlayZoomLevel", "mw.configgui.ctgy.general", Config.overlayZoomLevelDef, Config.zoomInLevels, Config.zoomOutLevels, "", "mw.config.overlayZoomLevel");
      Config.moreRealisticMap = configuration.getBoolean("moreRealisticMap", "mw.configgui.ctgy.general", Config.moreRealisticMapDef, "", "mw.config.moreRealisticMap");
      Config.newMarkerDialog = configuration.getBoolean("newMarkerDialog", "mw.configgui.ctgy.general", Config.newMarkerDialogDef, "", "mw.config.newMarkerDialog");
      Config.drawMarkersDistanceInWorld = configuration.getBoolean("drawMarkersDistanceInWorld", "mw.configgui.ctgy.general", Config.drawMarkersDistanceInWorldDef, "", "mw.config.drawMarkersDistanceInWorld");
      configuration.getCategory("mw.configgui.ctgy.general").remove("drawMarkersInWorld");
      configuration.getCategory("mw.configgui.ctgy.general").remove("drawMarkersNameInWorld");
   }

   @SubscribeEvent
   public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
      if (event.getModID().equalsIgnoreCase("pixelradar")) {
         if (event.getConfigID().equals("mw.configgui.ctgy.general")) {
            loadConfig();
         } else if (event.getConfigID().equals("fullscreenmap")) {
            Config.fullScreenMap.loadConfig();
         } else if (event.getConfigID().equals("largemap")) {
            Config.largeMap.loadConfig();
         } else if (event.getConfigID().equals("smallmap")) {
            Config.smallMap.loadConfig();
         } else if (event.getConfigID().equals("pixelradar")) {
            Config.pokeradar.loadConfig();
         } else {
            Logging.logError("Unknown config id: %s", new Object[]{event.getConfigID()});
         }

         if (configuration.hasChanged()) {
            configuration.save();
         }
      }

   }

   public static void setMapModeDefaults() {
      Config.fullScreenMap.setDefaults();
      Config.largeMap.setDefaults();
      Config.smallMap.setDefaults();
      Config.pokeradar.setDefaults();
   }
}
