//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import journeymap.config.Config;
import journeymap.config.ConfigurationHandler;
import journeymap.config.WorldConfig;
import journeymap.entities.EntityFinder;
import journeymap.forge.MwKeyHandler;
import journeymap.forge.Pokeradar;
import journeymap.gui.MwGui;
import journeymap.gui.MwGuiMarkerDialog;
import journeymap.gui.MwGuiMarkerDialogNew;
import journeymap.gui.RadarGui;
import journeymap.map.MapRenderer;
import journeymap.map.MapTexture;
import journeymap.map.MapView;
import journeymap.map.Marker;
import journeymap.map.MarkerManager;
import journeymap.map.MiniMaps;
import journeymap.map.Trail;
import journeymap.map.UndergroundTexture;
import journeymap.overlay.OverlaySlime;
import journeymap.region.BlockColours;
import journeymap.region.RegionManager;
import journeymap.tasks.CloseRegionManagerTask;
import journeymap.util.Logging;
import journeymap.util.Render;
import journeymap.util.Utils;

public class Mw {
   public Minecraft mc = null;
   private final File configDir;
   private final File saveDir;
   public File worldDir = null;
   public File imageDir = null;
   public boolean ready = false;
   public int tickCounter = 0;
   public int textureSize = 2048;
   public double playerX = 0.0;
   public double playerZ = 0.0;
   public double playerY = 0.0;
   public int playerXInt = 0;
   public int playerYInt = 0;
   public int playerZInt = 0;
   public String playerBiome = "";
   public double playerHeading = 0.0;
   public int playerDimension = 0;
   public float mapRotationDegrees = 0.0F;
   public float pkRotationDegrees = 0.0F;
   public MapTexture mapTexture = null;
   public UndergroundTexture undergroundMapTexture = null;
   public BackgroundExecutor executor = null;
   public MiniMaps miniMap = null;
   public MarkerManager markerManager = null;
   public BlockColours blockColours = null;
   public RegionManager regionManager = null;
   public ChunkManager chunkManager = null;
   public Trail playerTrail = null;
   private static Mw instance;

   public static Mw getInstance() {
      if (instance == null) {
         Class var0 = WorldConfig.class;
         synchronized(WorldConfig.class) {
            if (instance == null) {
               instance = new Mw();
            }
         }
      }

      return instance;
   }

   private Mw() {
      this.mc = Minecraft.getMinecraft();
      this.saveDir = new File(this.mc.debug, "saves");
      this.configDir = new File(this.mc.debug, "config");
      this.ready = false;
      RegionManager.logger = Pokeradar.logger;
      ConfigurationHandler.loadConfig();
   }

   public void setTextureSize() {
      if (Config.configTextureSize != this.textureSize) {
         int maxTextureSize = Render.getMaxTextureSize();

         int textureSize;
         for(textureSize = 1024; textureSize <= maxTextureSize && textureSize <= Config.configTextureSize; textureSize *= 2) {
         }

         textureSize /= 2;
         Logging.log("GL reported max texture size = %d", new Object[]{maxTextureSize});
         Logging.log("texture size from config = %d", new Object[]{Config.configTextureSize});
         Logging.log("setting map texture size to = %d", new Object[]{textureSize});
         this.textureSize = textureSize;
         if (this.ready) {
            this.reloadMapTexture();
         }
      }

   }

   public void updatePlayer() {
      this.playerX = this.mc.player.posX;
      this.playerY = this.mc.player.posY;
      this.playerZ = this.mc.player.posZ;
      this.playerXInt = (int)Math.floor(this.playerX);
      this.playerYInt = (int)Math.floor(this.playerY);
      this.playerZInt = (int)Math.floor(this.playerZ);
      if (this.mc.world != null && !this.mc.world.getChunk(new BlockPos(this.playerX, 0.0, this.playerZ)).isEmpty()) {
         this.playerBiome = this.mc.world.getBiomeForCoordsBody(new BlockPos(this.playerX, 0.0, this.playerZ)).getBiomeName();
      }

      this.playerHeading = Math.toRadians((double)this.mc.player.rotationYaw) + 1.5707963267948966;
      this.mapRotationDegrees = -this.mc.player.rotationYaw + 180.0F;
      this.pkRotationDegrees = -this.mc.player.rotationYaw - 180.0F;
      this.playerDimension = this.mc.world.provider.getDimensionType().getId();
      if (this.miniMap.view.getDimension() != this.playerDimension) {
         WorldConfig.getInstance().addDimension(this.playerDimension);
         this.miniMap.view.setDimension(this.playerDimension);
      }

   }

   public void toggleMarkerMode() {
      this.markerManager.nextGroup();
      this.markerManager.update();
      this.mc.player.sendStatusMessage(new TextComponentTranslation("mw.msg.groupselected", new Object[]{this.markerManager.getVisibleGroupName()}), false);
   }

   public void teleportTo(int x, int y, int z) {
      if (Config.teleportEnabled) {
         this.mc.player.sendChatMessage(String.format("/%s %d %d %d", Config.teleportCommand, x, y, z));
      } else {
         Utils.printBoth(I18n.format("mw.msg.tpdisabled", new Object[0]));
      }

   }

   public void warpTo(String name) {
      if (Config.teleportEnabled) {
         this.mc.player.sendChatMessage(String.format("/warp %s", name));
      } else {
         Utils.printBoth(I18n.format("mw.msg.tpdisabled", new Object[0]));
      }

   }

   public void teleportToMapPos(MapView mapView, int x, int y, int z) {
      if (!Config.teleportCommand.equals("warp")) {
         double scale = mapView.getDimensionScaling(this.playerDimension);
         this.teleportTo((int)((double)x / scale), y, (int)((double)z / scale));
      } else {
         Utils.printBoth(I18n.format("mw.msg.warp.error", new Object[0]));
      }

   }

   public void teleportToMarker(Marker marker) {
      if (Config.teleportCommand.equals("warp")) {
         this.warpTo(marker.name);
      } else if (marker.dimension == this.playerDimension) {
         this.teleportTo(marker.x, marker.y, marker.z);
      } else {
         Utils.printBoth(I18n.format("mw.msg.tp.dimError", new Object[0]));
      }

   }

   public void loadBlockColourOverrides(BlockColours bc) {
      File f = new File(this.configDir, "MapWriterBlockColourOverrides.txt");
      if (f.isFile()) {
         Logging.logInfo("loading block colour overrides file %s", new Object[]{f});
         bc.loadFromFile(f);
      } else {
         Logging.logInfo("recreating block colour overrides file %s", new Object[]{f});
         BlockColours.writeOverridesFile(f);
         if (f.isFile()) {
            bc.loadFromFile(f);
         } else {
            Logging.logError("could not load block colour overrides from file %s", new Object[]{f});
         }
      }

   }

   public void saveBlockColours(BlockColours bc) {
      File f = new File(this.configDir, "MapWriterBlockColours.txt");
      Logging.logInfo("saving block colours to '%s'", new Object[]{f});
      bc.saveToFile(f);
   }

   public void reloadBlockColours() {
      BlockColours bc = new BlockColours();
      File f = new File(this.configDir, "MapWriterBlockColours.txt");
      if (Config.useSavedBlockColours && f.isFile() && bc.CheckFileVersion(f)) {
         Logging.logInfo("loading block colours from %s", new Object[]{f});
         bc.loadFromFile(f);
         this.loadBlockColourOverrides(bc);
      } else {
         Logging.logInfo("generating block colours", new Object[0]);
         BlockColourGen.genBlockColours(bc);
         this.loadBlockColourOverrides(bc);
         this.saveBlockColours(bc);
      }

      this.blockColours = bc;
   }

   public void reloadMapTexture() {
      this.executor.addTask(new CloseRegionManagerTask(this.regionManager));
      this.executor.close();
      MapTexture oldMapTexture = this.mapTexture;
      MapTexture newMapTexture = new MapTexture(this.textureSize, Config.linearTextureScaling);
      this.mapTexture = newMapTexture;
      if (oldMapTexture != null) {
         oldMapTexture.close();
      }

      this.executor = new BackgroundExecutor();
      this.regionManager = new RegionManager(this.worldDir, this.imageDir, this.blockColours, Config.zoomInLevels, Config.zoomOutLevels);
      UndergroundTexture oldTexture = this.undergroundMapTexture;
      UndergroundTexture newTexture = new UndergroundTexture(this, this.textureSize, Config.linearTextureScaling);
      this.undergroundMapTexture = newTexture;
      if (oldTexture != null) {
         this.undergroundMapTexture.close();
      }

   }

   public void toggleUndergroundMode() {
      Config.undergroundMode = !Config.undergroundMode;
      ConfigurationHandler.configuration.get("mw.configgui.ctgy.general", "undergroundMode", Config.undergroundModeDef).set(Config.undergroundMode);
   }

   public void load() {
      if (!this.ready) {
         if (this.mc.world != null && this.mc.player != null) {
            Logging.log("Mw.load: loading...", new Object[0]);
            File saveDir = this.saveDir;
            if (Config.saveDirOverride.length() > 0) {
               File d = new File(Config.saveDirOverride);
               if (d.isDirectory()) {
                  saveDir = d;
               } else {
                  Logging.log("error: no such directory %s", new Object[]{Config.saveDirOverride});
               }
            }

            if (!this.mc.isSingleplayer()) {
               this.worldDir = new File(new File(saveDir, "mapwriter_mp_worlds"), Utils.getWorldName());
            } else {
               saveDir = DimensionManager.getCurrentSaveRootDirectory();
               this.worldDir = new File(saveDir, "pokeradar");
            }

            this.imageDir = new File(this.worldDir, "images");
            if (!this.imageDir.exists()) {
               this.imageDir.mkdirs();
            }

            if (!this.imageDir.isDirectory()) {
               Logging.log("Mapwriter: ERROR: could not create images directory '%s'", new Object[]{this.imageDir.getPath()});
            }

            this.tickCounter = 0;
            this.markerManager = new MarkerManager();
            this.markerManager.load(WorldConfig.getInstance().worldConfiguration, "markers");
            this.playerTrail = new Trail(this, "player");
            this.executor = new BackgroundExecutor();
            this.mapTexture = new MapTexture(this.textureSize, Config.linearTextureScaling);
            this.undergroundMapTexture = new UndergroundTexture(this, this.textureSize, Config.linearTextureScaling);
            this.regionManager = new RegionManager(this.worldDir, this.imageDir, this.blockColours, Config.zoomInLevels, Config.zoomOutLevels);
            this.miniMap = new MiniMaps(this);
            this.miniMap.view.setDimension(this.mc.player.getEntityWorld().provider.getDimensionType().getId());
            this.chunkManager = new ChunkManager(this);
            this.ready = true;
         } else {
            Logging.log("Mw.load: world or player is null, cannot load yet", new Object[0]);
         }
      }
   }

   public void close() {
      Logging.log("Mw.close: closing...", new Object[0]);
      if (this.ready) {
         this.ready = false;
         this.chunkManager.close();
         this.chunkManager = null;
         this.executor.addTask(new CloseRegionManagerTask(this.regionManager));
         this.regionManager = null;
         Logging.log("waiting for %d tasks to finish...", new Object[]{this.executor.tasksRemaining()});
         if (this.executor.close()) {
            Logging.log("error: timeout waiting for tasks to finish", new Object[0]);
         }

         Logging.log("done", new Object[0]);
         this.playerTrail.close();
         this.markerManager.save(WorldConfig.getInstance().worldConfiguration, "markers");
         this.markerManager.clear();
         this.miniMap.close();
         this.miniMap = null;
         this.undergroundMapTexture.close();
         this.mapTexture.close();
         WorldConfig.getInstance().saveWorldConfig();
         this.tickCounter = 0;
         OverlaySlime.reset();
      }

   }

   public void onTick() {
      this.load();
      if (this.ready && this.mc.player != null) {
         this.setTextureSize();
         this.updatePlayer();
         this.miniMap.view.setUndergroundMode(Config.undergroundMode);
         if (Config.undergroundMode && this.tickCounter % 30 == 0) {
            this.undergroundMapTexture.update();
         }

         if (!(this.mc.currentScreen instanceof MwGui)) {
            this.miniMap.view.setViewCentreScaled(this.playerX, this.playerZ, this.playerDimension);
            this.miniMap.drawCurrentMap();
         }

         for(int maxTasks = 50; !this.executor.processTaskQueue() && maxTasks > 0; --maxTasks) {
         }

         this.chunkManager.onTick();
         this.mapTexture.processTextureUpdates();
         this.playerTrail.onTick();
         ++this.tickCounter;
      }

   }

   public void onChunkLoad(Chunk chunk) {
      this.load();
      if (chunk != null && chunk.getWorld() instanceof WorldClient) {
         if (this.ready) {
            this.chunkManager.addChunk(chunk);
         } else {
            Logging.logInfo("missed chunk (%d, %d)", new Object[]{chunk.getPos().x, chunk.getPos().z});
         }
      }

   }

   public void onChunkUnload(Chunk chunk) {
      if (this.ready && chunk != null && chunk.getWorld() instanceof WorldClient) {
         this.chunkManager.removeChunk(chunk);
      }

   }

   public void onPlayerDeath() {
      if (this.ready && Config.maxDeathMarkers > 0) {
         this.updatePlayer();
         int deleteCount = this.markerManager.countMarkersInGroup("playerDeaths") - Config.maxDeathMarkers + 1;

         for(int i = 0; i < deleteCount; ++i) {
            this.markerManager.delMarker((String)null, "playerDeaths");
         }

         this.markerManager.addMarker(Utils.getCurrentDateString(), "playerDeaths", this.playerXInt, this.playerYInt, this.playerZInt, this.playerDimension, -65536);
         this.markerManager.setVisibleGroupName("playerDeaths");
         this.markerManager.update();
      }

   }

   public void onKeyDown(KeyBinding kb) {
      if (this.mc.currentScreen == null && this.ready) {
         if (kb == MwKeyHandler.keyRadar) {
            this.mc.displayGuiScreen(new RadarGui());
         } else if (kb == MwKeyHandler.keyMapMode) {
            MapRenderer newMap = this.miniMap.nextOverlayMode(1);
            if (newMap == null) {
               EntityFinder.pkMarkers.clear();
               GlStateManager.pushMatrix();
               GlStateManager.popMatrix();
            }
         } else if (kb == MwKeyHandler.keyMapGui) {
            this.mc.displayGuiScreen(new MwGui(this));
         } else if (kb == MwKeyHandler.keyNewMarker) {
            String group = this.markerManager.getVisibleGroupName();
            if (group.equals("none")) {
               group = "group";
            }

            if (Config.newMarkerDialog) {
               this.mc.displayGuiScreen(new MwGuiMarkerDialogNew((GuiScreen)null, this.markerManager, "", group, this.playerXInt, this.playerYInt, this.playerZInt, this.playerDimension));
            } else {
               this.mc.displayGuiScreen(new MwGuiMarkerDialog((GuiScreen)null, this.markerManager, "", group, this.playerXInt, this.playerYInt, this.playerZInt, this.playerDimension));
            }
         } else if (kb == MwKeyHandler.keyNextGroup) {
            this.markerManager.nextGroup();
            this.markerManager.update();
            this.mc.player.sendStatusMessage(new TextComponentTranslation("mw.msg.groupselected", new Object[]{this.markerManager.getVisibleGroupName()}), false);
         } else if (kb == MwKeyHandler.keyTeleport) {
            Marker marker = this.markerManager.getNearestMarkerInDirection(this.playerXInt, this.playerZInt, this.playerHeading);
            if (marker != null) {
               this.teleportToMarker(marker);
            }
         } else if (kb == MwKeyHandler.keyZoomIn) {
            this.miniMap.view.adjustZoomLevel(-1);
         } else if (kb == MwKeyHandler.keyZoomOut) {
            this.miniMap.view.adjustZoomLevel(1);
         } else if (kb == MwKeyHandler.keyUndergroundMode) {
            this.toggleUndergroundMode();
         }
      }

   }
}
