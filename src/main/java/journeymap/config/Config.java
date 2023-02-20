//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.config;

public class Config {
   public static final String[] backgroundModeStringArray = new String[]{"None", "Static", "Panning"};
   public static boolean linearTextureScalingDef = true;
   public static boolean linearTextureScaling;
   public static boolean undergroundModeDef;
   public static boolean undergroundMode;
   public static boolean teleportEnabledDef;
   public static boolean teleportEnabled;
   public static String teleportCommandDef;
   public static String teleportCommand;
   public static int defaultTeleportHeightDef;
   public static int defaultTeleportHeight;
   public static int zoomOutLevelsDef;
   public static int zoomOutLevels;
   public static int zoomInLevelsDef;
   public static int zoomInLevels;
   public static boolean useSavedBlockColoursDef;
   public static boolean useSavedBlockColours;
   public static int maxChunkSaveDistSqDef;
   public static int maxChunkSaveDistSq;
   public static boolean mapPixelSnapEnabledDef;
   public static boolean mapPixelSnapEnabled;
   public static int configTextureSizeDef;
   public static int configTextureSize;
   public static int maxDeathMarkersDef;
   public static int maxDeathMarkers;
   public static int chunksPerTickDef;
   public static int chunksPerTick;
   public static boolean portNumberInWorldNameEnabledDef;
   public static boolean portNumberInWorldNameEnabled;
   public static String saveDirOverrideDef;
   public static String saveDirOverride;
   public static boolean regionFileOutputEnabledSPDef;
   public static boolean regionFileOutputEnabledSP;
   public static boolean regionFileOutputEnabledMPDef;
   public static boolean regionFileOutputEnabledMP;
   public static String backgroundTextureModeDef;
   public static String backgroundTextureMode;
   public static boolean moreRealisticMapDef;
   public static boolean moreRealisticMap;
   public static boolean newMarkerDialogDef;
   public static boolean newMarkerDialog;
   public static boolean drawMarkersDistanceInWorldDef;
   public static boolean drawMarkersDistanceInWorld;
   public static int overlayModeIndexDef;
   public static int overlayModeIndex;
   public static int overlayZoomLevelDef;
   public static int overlayZoomLevel;
   public static int fullScreenZoomLevelDef;
   public static int fullScreenZoomLevel;
   public static largeMapModeConfig largeMap;
   public static smallMapModeConfig smallMap;
   public static MapModeConfig fullScreenMap;
   public static pokeradarConfig pokeradar;
   public static boolean reloadColours;

   public Config() {
   }

   static {
      linearTextureScaling = linearTextureScalingDef;
      undergroundModeDef = false;
      undergroundMode = undergroundModeDef;
      teleportEnabledDef = true;
      teleportEnabled = teleportEnabledDef;
      teleportCommandDef = "tp";
      teleportCommand = teleportCommandDef;
      defaultTeleportHeightDef = 80;
      defaultTeleportHeight = defaultTeleportHeightDef;
      zoomOutLevelsDef = 5;
      zoomOutLevels = zoomOutLevelsDef;
      zoomInLevelsDef = -5;
      zoomInLevels = zoomInLevelsDef;
      useSavedBlockColoursDef = false;
      useSavedBlockColours = useSavedBlockColoursDef;
      maxChunkSaveDistSqDef = 16384;
      maxChunkSaveDistSq = maxChunkSaveDistSqDef;
      mapPixelSnapEnabledDef = true;
      mapPixelSnapEnabled = mapPixelSnapEnabledDef;
      configTextureSizeDef = 2048;
      configTextureSize = configTextureSizeDef;
      maxDeathMarkersDef = 1;
      maxDeathMarkers = maxDeathMarkersDef;
      chunksPerTickDef = 5;
      chunksPerTick = chunksPerTickDef;
      portNumberInWorldNameEnabledDef = true;
      portNumberInWorldNameEnabled = portNumberInWorldNameEnabledDef;
      saveDirOverrideDef = "";
      saveDirOverride = saveDirOverrideDef;
      regionFileOutputEnabledSPDef = true;
      regionFileOutputEnabledSP = regionFileOutputEnabledSPDef;
      regionFileOutputEnabledMPDef = true;
      regionFileOutputEnabledMP = regionFileOutputEnabledMPDef;
      backgroundTextureModeDef = backgroundModeStringArray[0];
      backgroundTextureMode = backgroundTextureModeDef;
      moreRealisticMapDef = false;
      moreRealisticMap = moreRealisticMapDef;
      newMarkerDialogDef = true;
      newMarkerDialog = newMarkerDialogDef;
      drawMarkersDistanceInWorldDef = true;
      drawMarkersDistanceInWorld = drawMarkersDistanceInWorldDef;
      overlayModeIndexDef = 0;
      overlayModeIndex = overlayModeIndexDef;
      overlayZoomLevelDef = 0;
      overlayZoomLevel = overlayZoomLevelDef;
      fullScreenZoomLevelDef = 0;
      fullScreenZoomLevel = fullScreenZoomLevelDef;
      largeMap = new largeMapModeConfig("largemap");
      smallMap = new smallMapModeConfig("smallmap");
      fullScreenMap = new MapModeConfig("fullscreenmap");
      pokeradar = new pokeradarConfig("pixelradar");
      reloadColours = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
   }
}
