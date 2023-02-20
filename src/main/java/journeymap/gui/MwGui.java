//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import journeymap.Mw;
import journeymap.api.ILabelInfo;
import journeymap.api.IMwDataProvider;
import journeymap.api.MwAPI;
import journeymap.config.Config;
import journeymap.config.WorldConfig;
import journeymap.entities.EntityFinder;
import journeymap.forge.MwKeyHandler;
import journeymap.map.MapRenderer;
import journeymap.map.MapView;
import journeymap.map.Marker;
import journeymap.map.PKMarker;
import journeymap.map.mapmode.MapMode;
import journeymap.tasks.MergeTask;
import journeymap.tasks.RebuildRegionsTask;
import journeymap.util.Logging;
import journeymap.util.Utils;
import journeymap.util.VersionCheck;

@SideOnly(Side.CLIENT)
public class MwGui extends GuiScreen {
   private Mw mw;
   public MapMode mapMode;
   private MapView mapView;
   private MapRenderer map;
   private String[] HelpText1;
   private String[] HelpText2;
   private static final double PAN_FACTOR = 0.3;
   private static final int menuY = 5;
   private static final int menuX = 5;
   private int mouseLeftHeld;
   private int mouseLeftDragStartX;
   private int mouseLeftDragStartY;
   private double viewXStart;
   private double viewZStart;
   private Marker movingMarker;
   private int movingMarkerXStart;
   private int movingMarkerZStart;
   private int mouseBlockX;
   private int mouseBlockY;
   private int mouseBlockZ;
   private MwGuiLabel helpLabel;
   private MwGuiLabel optionsLabel;
   private MwGuiLabel dimensionLabel;
   private MwGuiLabel groupLabel;
   private MwGuiLabel overlayLabel;
   private final MwGuiMarkerListOverlay MarkerOverlay;
   private MwGuiLabel helpTooltipLabel;
   private MwGuiLabel updateTooltipLabel;
   private MwGuiLabel statusLabel;
   private MwGuiLabel markerLabel;
   public static MwGui instance;
   private URI clickedLinkURI;

   public MwGui(Mw mw) {
      this.HelpText1 = new String[]{"Key Controls", "", "  Space", "  Delete", "  C", "  Home", "  End", "  N", "  T", "  P", "  R", "  U", "  L"};
      this.HelpText2 = new String[]{"", "", "Next Marker Group", "Delete Marker", "Cycle Colour", "Center Map", "Center Map to Player", "Select next Marker", "Teleport", "Save png", "Regenerate", "Underground Map", "Marker List"};
      this.mouseLeftHeld = 0;
      this.mouseLeftDragStartX = 0;
      this.mouseLeftDragStartY = 0;
      this.movingMarker = null;
      this.movingMarkerXStart = 0;
      this.movingMarkerZStart = 0;
      this.mouseBlockX = 0;
      this.mouseBlockY = 0;
      this.mouseBlockZ = 0;
      this.mw = mw;
      this.mapMode = new MapMode(Config.fullScreenMap);
      this.mapView = new MapView(this.mw, true);
      this.map = new MapRenderer(this.mw, this.mapMode, this.mapView);
      this.mapView.setDimension(this.mw.miniMap.view.getDimension());
      this.mapView.setViewCentreScaled(this.mw.playerX, this.mw.playerZ, this.mw.playerDimension);
      this.mapView.setZoomLevel(Config.fullScreenZoomLevel);
      this.initLabels();
      this.MarkerOverlay = new MwGuiMarkerListOverlay(this, this.mw.markerManager);
      instance = this;
   }

   public MwGui(Mw mw, int dim, int x, int z) {
      this(mw);
      this.mapView.setDimension(dim);
      this.mapView.setViewCentreScaled((double)x, (double)z, dim);
      this.mapView.setZoomLevel(Config.fullScreenZoomLevel);
   }

   public void initGui() {
      this.helpLabel.setParentWidthAndHeight(this.width, this.height);
      this.optionsLabel.setParentWidthAndHeight(this.width, this.height);
      this.dimensionLabel.setParentWidthAndHeight(this.width, this.height);
      this.groupLabel.setParentWidthAndHeight(this.width, this.height);
      this.overlayLabel.setParentWidthAndHeight(this.width, this.height);
      this.helpTooltipLabel.setParentWidthAndHeight(this.width, this.height);
      this.updateTooltipLabel.setParentWidthAndHeight(this.width, this.height);
      this.statusLabel.setParentWidthAndHeight(this.width, this.height);
      this.markerLabel.setParentWidthAndHeight(this.width, this.height);
      this.MarkerOverlay.setDimensions(MwGuiMarkerListOverlay.listWidth, this.height - 20, MwGuiMarkerListOverlay.ListY, 10 + this.height - 20, this.width - 110);
   }

   public void initLabels() {
      this.helpLabel = new MwGuiLabel(new String[]{"[" + I18n.format("Help", new Object[0]) + "]"}, (String[])null, 5, 5, true, false, this.width, this.height);
      this.optionsLabel = new MwGuiLabel(new String[]{"[" + I18n.format("Options", new Object[0]) + "]"}, (String[])null, 0, 0, true, false, this.width, this.height);
      this.dimensionLabel = new MwGuiLabel((String[])null, (String[])null, 0, 0, true, false, this.width, this.height);
      this.groupLabel = new MwGuiLabel((String[])null, (String[])null, 0, 0, true, false, this.width, this.height);
      this.overlayLabel = new MwGuiLabel((String[])null, (String[])null, 0, 0, true, false, this.width, this.height);
      this.helpTooltipLabel = new MwGuiLabel(this.HelpText1, this.HelpText2, 0, 0, true, false, this.width, this.height);
      this.updateTooltipLabel = new MwGuiLabel(new String[]{VersionCheck.getUpdateURL()}, (String[])null, 0, 0, true, false, this.width, this.height);
      this.statusLabel = new MwGuiLabel((String[])null, (String[])null, 0, 0, true, false, this.width, this.height);
      this.markerLabel = new MwGuiLabel((String[])null, (String[])null, 0, 0, true, true, this.width, this.height);
      this.optionsLabel.drawToRightOf(this.helpLabel);
      this.dimensionLabel.drawToRightOf(this.optionsLabel);
      this.groupLabel.drawToRightOf(this.dimensionLabel);
      this.overlayLabel.drawToRightOf(this.groupLabel);
      this.helpTooltipLabel.drawToBelowOf(this.helpLabel);
      this.updateTooltipLabel.drawToBelowOf(this.helpLabel);
   }

   protected void actionPerformed(GuiButton button) {
   }

   public Marker getMarkerNearScreenPos(int x, int y) {
      Marker nearMarker = null;
      Iterator var4 = this.mw.markerManager.visibleMarkerList.iterator();

      while(var4.hasNext()) {
         Marker marker = (Marker)var4.next();
         if (marker.screenPos != null && marker.screenPos.distanceSq((double)x, (double)y) < 6.0) {
            nearMarker = marker;
         }
      }

      return nearMarker;
   }

   public PKMarker getMarkerNearScreenPosPK(int x, int y) {
      PKMarker nearMarker = null;
      if (EntityFinder.pkMarkers != null) {
         Iterator var4 = EntityFinder.pkMarkers.iterator();

         while(var4.hasNext()) {
            PKMarker marker = (PKMarker)var4.next();
            if (marker != null && marker.screenPos != null) {
               if (marker.desc != null) {
                  if (this.calculateInside(x, y, marker)) {
                     nearMarker = marker;
                  }
               } else if (marker.screenPos.distanceSq((double)x, (double)y) < 9.0) {
                  nearMarker = marker;
               }
            }
         }
      }

      return nearMarker;
   }

   public boolean calculateInside(int x, int y, PKMarker m) {
      return Math.sqrt(Math.pow(m.screenPos.x - (double)x, 2.0) + Math.pow(m.screenPos.y - (double)y, 2.0)) <= m.r;
   }

   public int getHeightAtBlockPos(int bX, int bZ) {
      int bY = 0;
      int worldDimension = this.mw.mc.world.provider.getDimensionType().getId();
      if ((worldDimension == this.mapView.getDimension()) && (worldDimension != -1))
      {
         bY = this.mw.mc.world.getHeight(new BlockPos(bX, 0, bZ)).getY();
      }

      return bY;
   }

   public boolean isPlayerNearScreenPos(int x, int y) {
      Point2D.Double p = this.map.playerArrowScreenPos;
      return p.distanceSq((double)x, (double)y) < 9.0;
   }

   public void deleteSelectedMarker() {
      if (this.mw.markerManager.selectedMarker != null) {
         this.mw.markerManager.delMarker(this.mw.markerManager.selectedMarker);
         this.mw.markerManager.update();
         this.mw.markerManager.selectedMarker = null;
      }

   }

   public void mergeMapViewToImage() {
      this.mw.chunkManager.saveChunks();
      this.mw.executor.addTask(new MergeTask(this.mw.regionManager, (int)this.mapView.getX(), (int)this.mapView.getZ(), (int)this.mapView.getWidth(), (int)this.mapView.getHeight(), this.mapView.getDimension(), this.mw.worldDir, this.mw.worldDir.getName()));
      Utils.printBoth(I18n.format("mw.gui.mwgui.chatmsg.merge", new Object[]{this.mw.worldDir.getAbsolutePath()}));
   }

   public void regenerateView() {
      Utils.printBoth(I18n.format("mw.gui.mwgui.chatmsg.regenmap", new Object[]{(int)this.mapView.getWidth(), (int)this.mapView.getHeight(), (int)this.mapView.getMinX(), (int)this.mapView.getMinZ()}));
      this.mw.executor.addTask(new RebuildRegionsTask(this.mw, (int)this.mapView.getMinX(), (int)this.mapView.getMinZ(), (int)this.mapView.getWidth(), (int)this.mapView.getHeight(), this.mapView.getDimension()));
   }

   protected void keyTyped(char c, int key) {
      switch (key) {
         case 1:
            this.exitGui();
            break;
         case 19:
            this.regenerateView();
            this.exitGui();
            break;
         case 20:
            if (this.mw.markerManager.selectedMarker != null) {
               this.mw.teleportToMarker(this.mw.markerManager.selectedMarker);
               this.exitGui();
            } else {
               this.mc.displayGuiScreen(new MwGuiTeleportDialog(this, this.mw, this.mapView, this.mouseBlockX, Config.defaultTeleportHeight, this.mouseBlockZ));
            }
            break;
         case 25:
            this.mergeMapViewToImage();
            this.exitGui();
            break;
         case 38:
            this.MarkerOverlay.setEnabled(!this.MarkerOverlay.getEnabled());
            break;
         case 46:
            if (this.mw.markerManager.selectedMarker != null) {
               this.mw.markerManager.selectedMarker.colourNext();
            }
            break;
         case 49:
            this.mw.markerManager.selectNextMarker();
            break;
         case 57:
            this.mw.markerManager.nextGroup();
            this.mw.markerManager.update();
            break;
         case 199:
            this.mapView.setViewCentreScaled(this.mw.playerX, this.mw.playerZ, this.mw.playerDimension);
            break;
         case 200:
            this.mapView.panView(0.0, -0.3);
            break;
         case 203:
            this.mapView.panView(-0.3, 0.0);
            break;
         case 205:
            this.mapView.panView(0.3, 0.0);
            break;
         case 207:
            this.centerOnSelectedMarker();
            break;
         case 208:
            this.mapView.panView(0.0, 0.3);
            break;
         case 211:
            this.deleteSelectedMarker();
            break;
         default:
            if (key == MwKeyHandler.keyMapGui.getKeyCode()) {
               this.exitGui();
            } else if (key == MwKeyHandler.keyZoomIn.getKeyCode()) {
               this.mapView.adjustZoomLevel(-1);
            } else if (key == MwKeyHandler.keyZoomOut.getKeyCode()) {
               this.mapView.adjustZoomLevel(1);
            } else if (key == MwKeyHandler.keyNextGroup.getKeyCode()) {
               this.mw.markerManager.nextGroup();
               this.mw.markerManager.update();
            } else if (key == MwKeyHandler.keyUndergroundMode.getKeyCode()) {
               this.mw.toggleUndergroundMode();
            }
      }

   }

   public void handleMouseInput() throws IOException {
      if (this.MarkerOverlay.isMouseInField() && this.mouseLeftHeld == 0) {
         this.MarkerOverlay.handleMouseInput();
      } else {
         if (MwAPI.getCurrentDataProvider() != null && MwAPI.getCurrentDataProvider().onMouseInput(this.mapView, this.mapMode)) {
            return;
         }

         int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
         int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
         int direction = Mouse.getEventDWheel();
         if (direction != 0) {
            this.mouseDWheelScrolled(x, y, direction);
         }
      }

      super.handleMouseInput();
   }

   protected void mouseClicked(int x, int y, int button) {
      Marker marker = this.getMarkerNearScreenPos(x, y);
      Marker prevMarker = this.mw.markerManager.selectedMarker;
      if (this.MarkerOverlay.isMouseInField() && this.mouseLeftHeld == 0) {
         this.MarkerOverlay.handleMouseInput();
      } else {
         if (button == 0) {
            if (this.dimensionLabel.posWithin(x, y)) {
               this.mc.displayGuiScreen(new MwGuiDimensionDialog(this, this.mw, this.mapView, this.mapView.getDimension()));
            } else if (this.optionsLabel.posWithin(x, y)) {
               try {
                  GuiScreen newScreen = (GuiScreen)ModGuiConfig.class.getConstructor(GuiScreen.class).newInstance(this);
                  this.mc.displayGuiScreen(newScreen);
               } catch (Exception var8) {
                  Logging.logError("There was a critical issue trying to build the config GUI for %s", new Object[]{"pixelradar"});
               }
            } else {
               this.mouseLeftHeld = 1;
               this.mouseLeftDragStartX = x;
               this.mouseLeftDragStartY = y;
               this.mw.markerManager.selectedMarker = marker;
               if (marker != null && prevMarker == marker) {
                  this.movingMarker = marker;
                  this.movingMarkerXStart = marker.x;
                  this.movingMarkerZStart = marker.z;
               }
            }
         } else if (button == 1) {
            this.openMarkerGui(marker, x, y);
         } else if (button == 2) {
            Point blockPoint = this.mapMode.screenXYtoBlockXZ(this.mapView, x, y);
            IMwDataProvider provider = MwAPI.getCurrentDataProvider();
            if (provider != null) {
               provider.onMiddleClick(this.mapView.getDimension(), blockPoint.x, blockPoint.y, this.mapView);
            }
         }

         this.viewXStart = this.mapView.getX();
         this.viewZStart = this.mapView.getZ();
      }

   }

   protected void mouseReleased(int x, int y, int button) {
      if (button == 0) {
         this.mouseLeftHeld = 0;
         this.movingMarker = null;
      } else if (button == 1) {
      }

   }

   public void mouseDWheelScrolled(int x, int y, int direction) {
      Marker marker = this.getMarkerNearScreenPos(x, y);
      if (marker != null && marker == this.mw.markerManager.selectedMarker) {
         if (direction > 0) {
            marker.colourNext();
         } else {
            marker.colourPrev();
         }
      } else {
         int n;
         if (this.dimensionLabel.posWithin(x, y)) {
            n = direction > 0 ? 1 : -1;
            this.mapView.nextDimension(WorldConfig.getInstance().dimensionList, n);
         } else if (this.groupLabel.posWithin(x, y)) {
            n = direction > 0 ? 1 : -1;
            this.mw.markerManager.nextGroup(n);
            this.mw.markerManager.update();
         } else if (this.overlayLabel.posWithin(x, y)) {
            n = direction > 0 ? 1 : -1;
            if (MwAPI.getCurrentDataProvider() != null) {
               MwAPI.getCurrentDataProvider().onOverlayDeactivated(this.mapView);
            }

            if (n == 1) {
               MwAPI.setNextProvider();
            } else {
               MwAPI.setPrevProvider();
            }

            if (MwAPI.getCurrentDataProvider() != null) {
               MwAPI.getCurrentDataProvider().onOverlayActivated(this.mapView);
            }
         } else {
            n = direction > 0 ? -1 : 1;
            this.mapView.zoomToPoint(this.mapView.getZoomLevel() + n, (double)this.mouseBlockX, (double)this.mouseBlockZ);
            Config.fullScreenZoomLevel = this.mapView.getZoomLevel();
         }
      }

   }

   public void exitGui() {
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mw.miniMap.view.setDimension(this.mapView.getDimension());
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
   }

   public void drawStatus(int bX, int bY, int bZ) {
      StringBuilder builder = new StringBuilder();
      int xPos = Integer.valueOf(bX);
      int yPos = Integer.valueOf(bY);
      int zPos = Integer.valueOf(bZ);
      if (bY != 0) {
         builder.append(I18n.format("(" + xPos + ", " + yPos + ", " + zPos + ")", new Object[]{bX, bY, bZ}));
      } else {
         builder.append(I18n.format("(" + xPos + ", ?, " + zPos + ")", new Object[]{bX, bZ}));
      }

      String s;
      if (this.mc.world != null) {
         String biome = "Biome: %s";
         s = this.mc.world.getBiomeForCoordsBody(new BlockPos(bX, 0, bZ)).getBiomeName();
         if (!this.mc.world.getChunk(new BlockPos(bX, 0, bZ)).isEmpty()) {
            builder.append(", ");
            builder.append(I18n.format("Biome: " + s, new Object[]{this.mc.world.getBiomeForCoordsBody(new BlockPos(bX, 0, bZ)).getBiomeName()}));
         }
      }

      IMwDataProvider provider = MwAPI.getCurrentDataProvider();
      if (provider != null) {
         builder.append(provider.getStatusString(this.mapView.getDimension(), bX, bY, bZ));
      }

      s = builder.toString();
      int x = this.width / 2 - 10 - this.fontRenderer.getStringWidth(s) / 2;
      this.statusLabel.setCoords(x, this.height - 21);
      this.statusLabel.setText(new String[]{builder.toString()}, (String[])null);
      this.statusLabel.draw();
   }

   public void drawScreen(int mouseX, int mouseY, float f) {
      this.mapView.setUndergroundMode(Config.undergroundMode);
      this.drawDefaultBackground();
      double xOffset = 0.0;
      double yOffset = 0.0;
      if (this.mouseLeftHeld > 2) {
         xOffset = (double)(this.mouseLeftDragStartX - mouseX) * this.mapView.getWidth() / (double)this.mapMode.getW();
         yOffset = (double)(this.mouseLeftDragStartY - mouseY) * this.mapView.getHeight() / (double)this.mapMode.getH();
         if (this.movingMarker != null) {
            double scale = this.mapView.getDimensionScaling(this.movingMarker.dimension);
            this.movingMarker.x = this.movingMarkerXStart - (int)(xOffset / scale);
            this.movingMarker.z = this.movingMarkerZStart - (int)(yOffset / scale);
         } else {
            this.mapView.setViewCentre(this.viewXStart + xOffset, this.viewZStart + yOffset);
         }
      }

      if (this.mouseLeftHeld > 0) {
         ++this.mouseLeftHeld;
      }

      this.map.draw();
      Point p = this.mapMode.screenXYtoBlockXZ(this.mapView, mouseX, mouseY);
      this.mouseBlockX = p.x;
      this.mouseBlockZ = p.y;
      this.mouseBlockY = this.getHeightAtBlockPos(this.mouseBlockX, this.mouseBlockZ);
      this.drawMarkerLabel(mouseX, mouseY, f);
      this.drawStatus(this.mouseBlockX, this.mouseBlockY, this.mouseBlockZ);
      this.drawLabel(mouseX, mouseY, f);
      this.MarkerOverlay.drawScreen(mouseX, mouseY, f);
      super.drawScreen(mouseX, mouseY, f);
   }

   private void drawMarkerLabel(int mouseX, int mouseY, float f) {
      Marker marker = this.getMarkerNearScreenPos(mouseX, mouseY);
      if (marker != null) {
         this.markerLabel.setText(new String[]{marker.name, String.format("(%d, %d, %d)", marker.x, marker.y, marker.z)}, (String[])null);
         this.markerLabel.setCoords(mouseX + 8, mouseY);
         this.markerLabel.draw();
      } else {
         PKMarker pkMarker = this.getMarkerNearScreenPosPK(mouseX, mouseY);
         if (pkMarker == null) {
            if (this.isPlayerNearScreenPos(mouseX, mouseY)) {
               this.markerLabel.setText(new String[]{this.mc.player.getDisplayNameString(), String.format("(%d, %d, %d)", this.mw.playerXInt, this.mw.playerYInt, this.mw.playerZInt)}, (String[])null);
               this.markerLabel.setCoords(mouseX + 8, mouseY);
               this.markerLabel.draw();
            } else {
               IMwDataProvider provider = MwAPI.getCurrentDataProvider();
               if (provider != null) {
                  ILabelInfo info = provider.getLabelInfo(mouseX, mouseY);
                  if (info != null) {
                     this.markerLabel.setText(info.getInfoText(), (String[])null);
                     this.markerLabel.setCoords(mouseX + 8, mouseY);
                     this.markerLabel.draw();
                     return;
                  }
               }

            }
         } else {
            if (pkMarker.desc != null && !pkMarker.desc.isEmpty()) {
               this.markerLabel.setText(new String[]{pkMarker.name, pkMarker.desc, String.format("(%d, %d, %d)", pkMarker.coordX, pkMarker.coordY, pkMarker.coordZ)}, (String[])null);
            } else {
               this.markerLabel.setText(new String[]{pkMarker.name, String.format("(%d, %d, %d)", pkMarker.coordX, pkMarker.coordY, pkMarker.coordZ)}, (String[])null);
            }

            this.markerLabel.setCoords(mouseX + 8, mouseY);
            this.markerLabel.draw();
         }
      }
   }

   private void drawLabel(int mouseX, int mouseY, float f) {
      this.helpLabel.draw();
      this.optionsLabel.draw();
      String Group = this.mw.markerManager.getVisibleGroupName();
      int getDim = this.mapView.getDimension();
      String dimString = "[" + I18n.format("Dimension: " + getDim, new Object[]{this.mapView.getDimension()}) + "]";
      this.dimensionLabel.setText(new String[]{dimString}, (String[])null);
      this.dimensionLabel.draw();
      String groupString = "[" + I18n.format("Group: " + Group, new Object[]{this.mw.markerManager.getVisibleGroupName()}) + "]";
      this.groupLabel.setText(new String[]{groupString}, (String[])null);
      this.groupLabel.draw();
      String overlayString = "[" + I18n.format("Overlay", new Object[]{MwAPI.getCurrentProviderName()}) + "]";
      this.overlayLabel.setText(new String[]{overlayString}, (String[])null);
      this.overlayLabel.draw();
      if (!VersionCheck.isLatestVersion()) {
      }

      if (this.helpLabel.posWithin(mouseX, mouseY)) {
         this.helpTooltipLabel.draw();
      }

   }

   public void func_73878_a(boolean result, int id) {
      if (id == 31102009) {
         if (result) {
            Utils.openWebLink(this.clickedLinkURI);
         }

         this.clickedLinkURI = null;
         this.mc.displayGuiScreen(this);
      }

   }

   public void centerOnSelectedMarker() {
      if (this.mw.markerManager.selectedMarker != null) {
         this.mapView.setViewCentreScaled((double)this.mw.markerManager.selectedMarker.x, (double)this.mw.markerManager.selectedMarker.z, 0);
      }

   }

   public void openMarkerGui(Marker m, int mouseX, int mouseY) {
      if (m != null && this.mw.markerManager.selectedMarker == m) {
         if (Config.newMarkerDialog) {
            this.mc.displayGuiScreen(new MwGuiMarkerDialogNew(this, this.mw.markerManager, m));
         } else {
            this.mc.displayGuiScreen(new MwGuiMarkerDialog(this, this.mw.markerManager, m));
         }
      } else if (m == null) {
         String group = this.mw.markerManager.getVisibleGroupName();
         if (group.equals("none")) {
            group = I18n.format("Group 2", new Object[0]);
         }

         int mx;
         int my;
         int mz;
         if (this.isPlayerNearScreenPos(mouseX, mouseY)) {
            mx = this.mw.playerXInt;
            my = this.mw.playerYInt;
            mz = this.mw.playerZInt;
         } else {
            mx = this.mouseBlockX;
            my = this.mouseBlockY > 0 ? this.mouseBlockY : Config.defaultTeleportHeight;
            mz = this.mouseBlockZ;
         }

         if (Config.newMarkerDialog) {
            this.mc.displayGuiScreen(new MwGuiMarkerDialogNew(this, this.mw.markerManager, "", group, mx, my, mz, this.mapView.getDimension()));
         } else {
            this.mc.displayGuiScreen(new MwGuiMarkerDialog(this, this.mw.markerManager, "", group, mx, my, mz, this.mapView.getDimension()));
         }
      }

   }
}
