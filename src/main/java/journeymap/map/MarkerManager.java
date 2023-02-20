//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.opengl.GL11;
import journeymap.config.WorldConfig;
import journeymap.config.pokeradarConfig;
import journeymap.entities.EntityFinder;
import journeymap.entities.EntityType;
import journeymap.map.mapmode.MapMode;
import journeymap.util.Logging;
import journeymap.util.TwoDimensionalArrayList;
import journeymap.util.Utils;

public class MarkerManager {
   public List<Marker> markerList = new ArrayList();
   public List<String> groupList = new ArrayList();
   public List<Marker> visibleMarkerList = new ArrayList();
   private String visibleGroupName = "none";
   public Marker selectedMarker = null;

   public MarkerManager() {
   }

   public void load(Configuration config, String category) {
      this.markerList.clear();
      if (config.hasCategory(category)) {
         int markerCount = config.get(category, "markerCount", 0).getInt();
         this.visibleGroupName = config.get(category, "visibleGroup", "").getString();
         if (markerCount > 0) {
            for(int i = 0; i < markerCount; ++i) {
               String key = "marker" + i;
               String value = config.get(category, key, "").getString();
               Marker marker = this.stringToMarker(value);
               if (marker != null) {
                  this.addMarker(marker);
               } else {
                  Logging.log("error: could not load " + key + " from config file", new Object[0]);
               }
            }
         }
      }

      this.update();
   }

   public void save(Configuration config, String category) {
      config.removeCategory(config.getCategory(category));
      config.get(category, "markerCount", 0).set(this.markerList.size());
      config.get(category, "visibleGroup", "").set(this.visibleGroupName);
      int i = 0;

      for(Iterator var4 = this.markerList.iterator(); var4.hasNext(); ++i) {
         Marker marker = (Marker)var4.next();
         String key = "marker" + i;
         String value = this.markerToString(marker);
         config.get(category, key, "").set(value);
      }

      if (config.hasChanged()) {
         config.save();
      }

   }

   public void setVisibleGroupName(String groupName) {
      if (groupName != null) {
         this.visibleGroupName = Utils.mungeStringForConfig(groupName);
      } else {
         this.visibleGroupName = "none";
      }

   }

   public String getVisibleGroupName() {
      return this.visibleGroupName;
   }

   public void clear() {
      this.markerList.clear();
      this.groupList.clear();
      this.visibleMarkerList.clear();
      this.visibleGroupName = "none";
   }

   public String markerToString(Marker marker) {
      return String.format("%s:%d:%d:%d:%d:%06x:%s", marker.name, marker.x, marker.y, marker.z, marker.dimension, marker.colour & 16777215, marker.groupName);
   }

   public Marker stringToMarker(String s) {
      String[] split = s.split(":");
      if (split.length != 7) {
         split = s.split(" ");
      }

      Marker marker = null;
      if (split.length == 7) {
         try {
            int x = Integer.parseInt(split[1]);
            int y = Integer.parseInt(split[2]);
            int z = Integer.parseInt(split[3]);
            int dimension = Integer.parseInt(split[4]);
            int colour = -16777216 | Integer.parseInt(split[5], 16);
            marker = new Marker(split[0], split[6], x, y, z, dimension, colour);
         } catch (NumberFormatException var9) {
            marker = null;
         }
      } else {
         Logging.log("PKMarker.stringToMarker: invalid marker '%s'", new Object[]{s});
      }

      return marker;
   }

   public void addMarker(Marker marker) {
      this.markerList.add(marker);
   }

   public void addMarker(String name, String groupName, int x, int y, int z, int dimension, int colour) {
      this.addMarker(new Marker(name, groupName, x, y, z, dimension, colour));
      this.save(WorldConfig.getInstance().worldConfiguration, "markers");
   }

   public boolean delMarker(Marker markerToDelete) {
      if (this.selectedMarker == markerToDelete) {
         this.selectedMarker = null;
      }

      boolean result = this.markerList.remove(markerToDelete);
      this.save(WorldConfig.getInstance().worldConfiguration, "markers");
      return result;
   }

   public boolean delMarker(String name, String group) {
      Marker markerToDelete = null;
      Iterator var4 = this.markerList.iterator();

      while(var4.hasNext()) {
         Marker marker = (Marker)var4.next();
         if ((name == null || marker.name.equals(name)) && (group == null || marker.groupName.equals(group))) {
            markerToDelete = marker;
            break;
         }
      }

      return this.delMarker(markerToDelete);
   }

   public void update() {
      this.visibleMarkerList.clear();
      this.groupList.clear();
      this.groupList.add("none");
      this.groupList.add("all");
      Iterator var1 = this.markerList.iterator();

      while(var1.hasNext()) {
         Marker marker = (Marker)var1.next();
         if (marker.groupName.equals(this.visibleGroupName) || this.visibleGroupName.equals("all")) {
            this.visibleMarkerList.add(marker);
         }

         if (!this.groupList.contains(marker.groupName)) {
            this.groupList.add(marker.groupName);
         }
      }

      if (!this.groupList.contains(this.visibleGroupName)) {
         this.visibleGroupName = "none";
      }

   }

   public void nextGroup(int n) {
      if (this.groupList.size() > 0) {
         int i = this.groupList.indexOf(this.visibleGroupName);
         int size = this.groupList.size();
         if (i != -1) {
            i = (i + size + n) % size;
         } else {
            i = 0;
         }

         this.visibleGroupName = (String)this.groupList.get(i);
      } else {
         this.visibleGroupName = "none";
         this.groupList.add("none");
      }

   }

   public void nextGroup() {
      this.nextGroup(1);
   }

   public int countMarkersInGroup(String group) {
      int count = 0;
      if (group.equals("all")) {
         count = this.markerList.size();
      } else {
         Iterator var3 = this.markerList.iterator();

         while(var3.hasNext()) {
            Marker marker = (Marker)var3.next();
            if (marker.groupName.equals(group)) {
               ++count;
            }
         }
      }

      return count;
   }

   public void selectNextMarker() {
      if (this.visibleMarkerList.size() > 0) {
         int i = 0;
         if (this.selectedMarker != null) {
            i = this.visibleMarkerList.indexOf(this.selectedMarker);
            if (i == -1) {
               i = 0;
            }
         }

         i = (i + 1) % this.visibleMarkerList.size();
         this.selectedMarker = (Marker)this.visibleMarkerList.get(i);
      } else {
         this.selectedMarker = null;
      }

   }

   public Marker getNearestMarker(int x, int z, int maxDistance) {
      int nearestDistance = maxDistance * maxDistance;
      Marker nearestMarker = null;
      Iterator var6 = this.visibleMarkerList.iterator();

      while(var6.hasNext()) {
         Marker marker = (Marker)var6.next();
         int dx = x - marker.x;
         int dz = z - marker.z;
         int d = dx * dx + dz * dz;
         if (d < nearestDistance) {
            nearestMarker = marker;
            nearestDistance = d;
         }
      }

      return nearestMarker;
   }

   public Marker getNearestMarkerInDirection(int x, int z, double desiredAngle) {
      int nearestDistance = 100000000;
      Marker nearestMarker = null;
      Iterator var7 = this.visibleMarkerList.iterator();

      while(var7.hasNext()) {
         Marker marker = (Marker)var7.next();
         int dx = marker.x - x;
         int dz = marker.z - z;
         int d = dx * dx + dz * dz;
         double angle = Math.atan2((double)dz, (double)dx);
         if (Math.cos(desiredAngle - angle) > 0.8 && d < nearestDistance && d > 4) {
            nearestMarker = marker;
            nearestDistance = d;
         }
      }

      return nearestMarker;
   }

   public void drawMarkers(MapMode mapMode, MapView mapView) {
      Iterator var3 = this.visibleMarkerList.iterator();

      while(var3.hasNext()) {
         Marker marker = (Marker)var3.next();
         if (mapView.getDimension() == marker.dimension) {
            marker.draw(mapMode, mapView, -16777216);
         }
      }

      if (this.selectedMarker != null) {
         this.selectedMarker.draw(mapMode, mapView, -1);
      }

   }

   public void drawPKMarkers(MapMode mapMode, MapView mapView, MapRenderer mr, float mapRotationDegrees) {
      TwoDimensionalArrayList<PKMarker> markerList = new TwoDimensionalArrayList();
      EntityType[] var6 = EntityType.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EntityType var10000 = var6[var8];
         markerList.add(new ArrayList());
      }

      Iterator var10 = EntityFinder.pkMarkers.iterator();

      while(var10.hasNext()) {
         PKMarker marker = (PKMarker)var10.next();
         if (mapView.getDimension() == marker.dimension) {
            markerList.addToInnerArray(marker.type.ordinal(), marker);
         }
      }

      EntityFinder.pkMarkers.clear();
      var10 = markerList.iterator();

      while(var10.hasNext()) {
         ArrayList<PKMarker> markerArray = (ArrayList)var10.next();
         Iterator var13 = markerArray.iterator();

         while(var13.hasNext()) {
            PKMarker marker = (PKMarker)var13.next();
            if (marker.type == EntityType.PLAYER) {
               mr.drawEntityArrow(marker.x, marker.z, Math.toRadians((double)marker.yaw) + 1.5707963267948966, true);
            } else if (marker.type == EntityType.NPC) {
               mr.drawEntityArrow(marker.x, marker.z, Math.toRadians((double)marker.yaw) + 1.5707963267948966, false);
            } else if (pokeradarConfig.useTextures) {
               marker.drawPKTexture(mapMode, mapView, mapRotationDegrees);
            } else {
               marker.draw(mapMode, mapView);
            }
         }
      }

   }

   public void drawMarkersWorld(float partialTicks) {
      if (pokeradarConfig.drawMarkersInWorld || pokeradarConfig.drawMarkersNameInWorld) {
         Iterator var2 = EntityFinder.pkMarkers.iterator();

         while(var2.hasNext()) {
            PKMarker m = (PKMarker)var2.next();
            Minecraft mc = Minecraft.getMinecraft();
            if (m.dimension == mc.world.provider.getDimensionType().getId()){
               boolean draw = false;
               boolean drawLine = false;
               switch (m.type) {
                  case BOSS_UNCOMMON:
                  case BOSS_RARE:
                  case BOSS_LEGENDARY:
                  case BOSS_ULTIMATE:
                     if (pokeradarConfig.drawBossMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawBossLines) {
                        drawLine = true;
                     }
                     break;
                  case SPECIAL:
                     if (pokeradarConfig.drawSpecialMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawSpecialLines) {
                        drawLine = true;
                     }
                     break;
                  case SEARCHED:
                     if (pokeradarConfig.drawSearchedMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawSearchedLines) {
                        drawLine = true;
                     }
                     break;
                  case SHINY:
                     if (pokeradarConfig.drawShinyMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawShinyLines) {
                        drawLine = true;
                     }
                     break;
                  case LOOT:
                     if (pokeradarConfig.drawLootMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawLootLines) {
                        drawLine = true;
                     }
                     break;
                  case DITTO:
                     if (pokeradarConfig.drawDittoMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawDittoLines) {
                        drawLine = true;
                     }
                     break;
                  case NPC:
                     if (pokeradarConfig.drawNPCMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawNPCLines) {
                        drawLine = true;
                     }
                     break;
                  case DEX:
                     if (pokeradarConfig.drawDexMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawDexLines) {
                        drawLine = true;
                     }
                     break;
                  case WORMHOLE:
                     if (pokeradarConfig.drawWormMarkers) {
                        draw = true;
                     }

                     if (pokeradarConfig.drawWormLines) {
                        drawLine = true;
                     }
               }

               if (drawLine && pokeradarConfig.allowLinesInWorld) {
                  this.drawBeam(m, partialTicks);
               }

               if (draw) {
                  if (pokeradarConfig.drawMarkersInWorld) {
                     this.drawBeam(m, partialTicks);
                  }

                  if (pokeradarConfig.drawMarkersNameInWorld) {
                     if (!m.desc.isEmpty()) {
                        this.drawLabel(m);
                     } else {
                        this.drawLabelNoDesc(m);
                     }
                  }
               }
            }
         }

         if (!pokeradarConfig.enableMap) {
            EntityFinder.pkMarkers.clear();
         }

         var2 = this.visibleMarkerList.iterator();

         while(var2.hasNext()) {
            Marker m = (Marker)var2.next();
            if (m.dimension == Minecraft.getMinecraft().player.dimension) {
               if (pokeradarConfig.drawMarkersInWorld) {
                  this.drawBeam(m, partialTicks);
               }

               if (pokeradarConfig.drawMarkersNameInWorld) {
                  this.drawLabel(m);
               }
            }
         }

      }
   }

   public void drawLine(PKMarker m, float partialTicks, EntityPlayerSP player) {
      RayTraceResult ray = player.rayTrace(2.0, partialTicks);
      if (ray != null) {
         GL11.glPushMatrix();
         GL11.glPushAttrib(8192);
         double playerX = player.posX + (player.posX - player.posX) * (double)partialTicks;
         double playerY = player.posY + (player.posY - player.posY) * (double)partialTicks;
         double playerZ = player.posZ + (player.posZ - player.posZ) * (double)partialTicks;
         Vec3d ma = new Vec3d(m.x, m.y, m.z);
         GL11.glTranslated(-playerX, -playerY, -playerZ);
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.disableCull();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.depthMask(false);
         GlStateManager.disableDepth();
         GL11.glEnable(34383);
         GL11.glColor4ub((byte)m.type.getColor().getRed(), (byte)m.type.getColor().getGreen(), (byte)m.type.getColor().getBlue(), (byte)-106);
         GL11.glLineWidth(10.0F);
         GL11.glHint(3154, 4354);
         GL11.glBegin(3);
         GL11.glVertex3d(ray.entityHit.posX, ray.entityHit.posY, ray.entityHit.posZ);
         GL11.glVertex3d(ma.x, ma.y + 0.5, ma.z);
         GL11.glEnd();
         GL11.glDisable(34383);
         GlStateManager.disableTexture2D();
         GL11.glPopAttrib();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
         GlStateManager.depthMask(true);
         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   public void drawBeam(Marker m, float partialTicks) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      float f2 = (float)Minecraft.getMinecraft().world.getTotalWorldTime() + partialTicks;
      double d3 = (double)f2 * 0.025 * -1.5;
      double d17 = 255.0;
      double x = (double)m.x - TileEntityRendererDispatcher.staticPlayerX;
      double y = 0.0 - TileEntityRendererDispatcher.staticPlayerY;
      double z = (double)m.z - TileEntityRendererDispatcher.staticPlayerZ;
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.depthMask(false);
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      double d4 = 0.2;
      double d5 = 0.5 + Math.cos(d3 + 2.356194490192345) * d4;
      double d6 = 0.5 + Math.sin(d3 + 2.356194490192345) * d4;
      double d7 = 0.5 + Math.cos(d3 + 0.7853981633974483) * d4;
      double d8 = 0.5 + Math.sin(d3 + 0.7853981633974483) * d4;
      double d9 = 0.5 + Math.cos(d3 + 3.9269908169872414) * d4;
      double d10 = 0.5 + Math.sin(d3 + 3.9269908169872414) * d4;
      double d11 = 0.5 + Math.cos(d3 + 5.497787143782138) * d4;
      double d12 = 0.5 + Math.sin(d3 + 5.497787143782138) * d4;
      float fRed = m.getRed();
      float fGreen = m.getGreen();
      float fBlue = m.getBlue();
      float fAlpha = 0.125F;
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      d4 = 0.5;
      d5 = 0.5 + Math.sin(d3 + 2.356194490192345) * d4;
      d6 = 0.5 + Math.cos(d3 + 2.356194490192345) * d4;
      d7 = 0.5 + Math.sin(d3 + 0.7853981633974483) * d4;
      d8 = 0.5 + Math.cos(d3 + 0.7853981633974483) * d4;
      d9 = 0.5 + Math.sin(d3 + 3.9269908169872414) * d4;
      d10 = 0.5 + Math.cos(d3 + 3.9269908169872414) * d4;
      d11 = 0.5 + Math.sin(d3 + 5.497787143782138) * d4;
      d12 = 0.5 + Math.cos(d3 + 5.497787143782138) * d4;
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public void drawBeam(PKMarker m, float partialTicks) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      float f2 = (float)Minecraft.getMinecraft().world.getTotalWorldTime() + partialTicks;
      double d3 = (double)f2 * 0.025 * -1.5;
      double d17 = 255.0;
      double x = m.x - TileEntityRendererDispatcher.staticPlayerX;
      double y = 0.0 - TileEntityRendererDispatcher.staticPlayerY;
      double z = m.z - TileEntityRendererDispatcher.staticPlayerZ;
      GlStateManager.pushMatrix();
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.depthMask(false);
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      double d4 = 0.2;
      double d5 = 0.5 + Math.cos(d3 + 2.356194490192345) * d4;
      double d6 = 0.5 + Math.sin(d3 + 2.356194490192345) * d4;
      double d7 = 0.5 + Math.cos(d3 + 0.7853981633974483) * d4;
      double d8 = 0.5 + Math.sin(d3 + 0.7853981633974483) * d4;
      double d9 = 0.5 + Math.cos(d3 + 3.9269908169872414) * d4;
      double d10 = 0.5 + Math.sin(d3 + 3.9269908169872414) * d4;
      double d11 = 0.5 + Math.cos(d3 + 5.497787143782138) * d4;
      double d12 = 0.5 + Math.sin(d3 + 5.497787143782138) * d4;
      Color color = m.type.getColor();
      float fRed = (float)color.getRed() / 255.0F;
      float fGreen = (float)color.getGreen() / 255.0F;
      float fBlue = (float)color.getBlue() / 255.0F;
      float fAlpha = 0.125F;
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      d4 = 0.5;
      d5 = 0.5 + Math.sin(d3 + 2.356194490192345) * d4;
      d6 = 0.5 + Math.cos(d3 + 2.356194490192345) * d4;
      d7 = 0.5 + Math.sin(d3 + 0.7853981633974483) * d4;
      d8 = 0.5 + Math.cos(d3 + 0.7853981633974483) * d4;
      d9 = 0.5 + Math.sin(d3 + 3.9269908169872414) * d4;
      d10 = 0.5 + Math.cos(d3 + 3.9269908169872414) * d4;
      d11 = 0.5 + Math.sin(d3 + 5.497787143782138) * d4;
      d12 = 0.5 + Math.cos(d3 + 5.497787143782138) * d4;
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y + d17, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d7, y, z + d8).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d11, y + d17, z + d12).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y + d17, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d9, y, z + d10).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos(x + d5, y + d17, z + d6).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
   }

   public void drawLabel(Marker m) {
      float growFactor = 0.17F;
      Minecraft mc = Minecraft.getMinecraft();
      RenderManager renderManager = mc.getRenderManager();
      FontRenderer fontrenderer = mc.fontRenderer;
      double x = 0.5 + (double)m.x - TileEntityRendererDispatcher.staticPlayerX;
      double y = 0.5 + (double)m.y - TileEntityRendererDispatcher.staticPlayerY;
      double z = 0.5 + (double)m.z - TileEntityRendererDispatcher.staticPlayerZ;
      float fRed = m.getRed();
      float fGreen = m.getGreen();
      float fBlue = m.getBlue();
      float fAlpha = 0.2F;
      double distance = m.getDistanceToMarker(renderManager.renderViewEntity);
      String strText = m.name;
      String strDistance = "(" + (int)distance + "m)";
      int strTextWidth = fontrenderer.getStringWidth(strText) / 2;
      int strDistanceWidth = fontrenderer.getStringWidth(strDistance) / 2;
      int offstet = 9;
      float f = (float)(1.0 + distance * (double)growFactor);
      float f1 = 0.016666668F * f;
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(-f1, -f1, f1);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(false);
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glEnable(34383);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      GlStateManager.disableTexture2D();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strTextWidth - 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strTextWidth - 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(-1 + offstet), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(8 + offstet), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(8 + offstet), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(-1 + offstet), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      fontrenderer.drawString(strText, -strTextWidth, 0, -1);
      fontrenderer.drawString(strDistance, -strDistanceWidth, offstet, -1);
      GL11.glDisable(34383);
      GlStateManager.disableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   public void drawLabel(PKMarker m) {
      float growFactor = 0.17F;
      Minecraft mc = Minecraft.getMinecraft();
      RenderManager renderManager = mc.getRenderManager();
      FontRenderer fontrenderer = mc.fontRenderer;
      double x = 0.5 + m.x - TileEntityRendererDispatcher.staticPlayerX;
      double y = 0.5 + m.y - TileEntityRendererDispatcher.staticPlayerY;
      double z = 0.5 + m.z - TileEntityRendererDispatcher.staticPlayerZ;
      Color color = m.type.getColor();
      float fRed = (float)color.getRed() / 255.0F;
      float fGreen = (float)color.getGreen() / 255.0F;
      float fBlue = (float)color.getBlue() / 255.0F;
      float fAlpha = 0.2F;
      double distance = m.getDistanceToMarker(renderManager.renderViewEntity);
      String strText = m.name;
      String strDesc = m.desc;
      String strDistance = "(" + (int)distance + "m)";
      int strTextWidth = fontrenderer.getStringWidth(strText) / 2;
      int strDescWidth = fontrenderer.getStringWidth(strDesc) / 2;
      int strDistanceWidth = fontrenderer.getStringWidth(strDistance) / 2;
      int offset = 9;
      float f = (float)(1.0 + distance * (double)growFactor);
      float f1 = 0.016666668F * f;
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(-f1, -f1, f1);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(false);
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glEnable(34383);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      GlStateManager.disableTexture2D();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strTextWidth - 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strTextWidth - 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strDescWidth - 1), (double)(-1 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strDescWidth - 1), (double)(8 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDescWidth + 1), (double)(8 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDescWidth + 1), (double)(-1 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(-1 + offset * 2), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(8 + offset * 2), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(8 + offset * 2), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(-1 + offset * 2), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      fontrenderer.drawString(strText, -strTextWidth, 0, -1);
      fontrenderer.drawString(strDesc, -strDescWidth, offset, -1);
      fontrenderer.drawString(strDistance, -strDistanceWidth, offset * 2, -1);
      GL11.glDisable(34383);
      GlStateManager.disableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   public void drawLabelNoDesc(PKMarker m) {
      float growFactor = 0.17F;
      Minecraft mc = Minecraft.getMinecraft();
      RenderManager renderManager = mc.getRenderManager();
      FontRenderer fontrenderer = mc.fontRenderer;
      double x = 0.5 + m.x - TileEntityRendererDispatcher.staticPlayerX;
      double y = 0.5 + m.y - TileEntityRendererDispatcher.staticPlayerY;
      double z = 0.5 + m.z - TileEntityRendererDispatcher.staticPlayerZ;
      Color color = m.type.getColor();
      float fRed = (float)color.getRed() / 255.0F;
      float fGreen = (float)color.getGreen() / 255.0F;
      float fBlue = (float)color.getBlue() / 255.0F;
      float fAlpha = 0.2F;
      double distance = m.getDistanceToMarker(renderManager.renderViewEntity);
      String strText = m.name;
      String strDistance = "(" + (int)distance + "m)";
      int strTextWidth = fontrenderer.getStringWidth(strText) / 2;
      int strDistanceWidth = fontrenderer.getStringWidth(strDistance) / 2;
      int offset = 9;
      float f = (float)(1.0 + distance * (double)growFactor);
      float f1 = 0.016666668F * f;
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(-f1, -f1, f1);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(false);
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GL11.glEnable(34383);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      GlStateManager.disableTexture2D();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strTextWidth - 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strTextWidth - 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), 8.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strTextWidth + 1), -1.0, 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(-1 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(-strDistanceWidth - 1), (double)(8 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(8 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      vertexbuffer.pos((double)(strDistanceWidth + 1), (double)(-1 + offset), 0.0).color(fRed, fGreen, fBlue, fAlpha).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
      fontrenderer.drawString(strText, -strTextWidth, 0, -1);
      fontrenderer.drawString(strDistance, -strDistanceWidth, offset, -1);
      GL11.glDisable(34383);
      GlStateManager.disableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }
}
