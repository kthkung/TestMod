//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import journeymap.entities.EntityType;
import journeymap.map.mapmode.MapMode;
import journeymap.util.Render;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.awt.geom.Point2D;

public class PKMarker {
   public Point2D.Double screenPos = new Point2D.Double(0.0, 0.0);
   public String name = "";
   public String pkName = "";
   public String desc = "";
   public int form = 0;
   public boolean isShiny = false;
   public boolean isMega = false;
   public boolean isSpecial = true;
   ResourceLocation sprite;
   public EntityPixelmon entityPixelmon;
   public double r = 0.0;
   public double x = 0.0;
   public double y = 0.0;
   public double z = 0.0;
   public int coordX = 0;
   public int coordY = 0;
   public int coordZ = 0;
   public int dimension;
   public EntityType type;
   private static double borderWidth = 0.4;
   private static int borderColor = -16777216;
   public float yaw;

   public PKMarker(EntityType type, Entity entity, String name, String desc, double radius, MapRenderer mp, int dimension) {
      this.name = name;
      if (entity instanceof EntityPixelmon) {
         EntityPixelmon entityPixelmon = (EntityPixelmon)entity;
         this.form = entityPixelmon.getPokemonData().getForm();
         this.isShiny = entityPixelmon.getPokemonData().isShiny();
         this.pkName = entityPixelmon.getPokemonName();
         this.isSpecial = entityPixelmon.hasForms();
         String pkNum = String.format("%03d", Pokedex.nameToID(this.pkName));
         if (this.form > 0 && this.isShiny && entityPixelmon.getBaseStats() != null) {
            EnumSpecies enumPokemon = entityPixelmon.getBaseStats().pokemon;
            if (enumPokemon != null && enumPokemon.hasMega()) {
               this.isShiny = false;
               this.isMega = true;
               this.name = "Mega " + this.name;
            }
         }

         if (this.isShiny) {
            this.sprite = GuiResources.shinySprite(pkNum);
            this.name = this.name + " (shiny)";
         } else if (!this.pkName.contains("Sableye") && !this.pkName.contains("Hippowdon") && !this.pkName.contains("Hippopotas") && !this.pkName.contains("Wobbuffet")) {
            if (this.isSpecial && !this.isMega) {
               this.sprite = GuiResources.getPokemonSprite(entityPixelmon.getSpecies(), Minecraft.getMinecraft().world.provider.getDimensionType().getId(), entityPixelmon.getPokemonData().getGender(), this.isSpecial, this.isShiny);
            } else {
               this.sprite = GuiResources.getPokemonSprite(entityPixelmon.getSpecies(), Minecraft.getMinecraft().world.provider.getDimensionType().getId(), entityPixelmon.getPokemonData().getGender(), this.isSpecial, this.isShiny);
            }
         } else {
            this.sprite = GuiResources.shinySprite(pkNum);
         }
      }

      this.yaw = entity.rotationYaw;
      this.dimension = Minecraft.getMinecraft().world.provider.getDimensionType().getId();
      this.type = type;
      this.r = radius;
      this.desc = desc;
      this.x = entity.posX;
      this.y = entity.posY;
      this.z = entity.posZ;
      this.coordX = (int)this.x;
      this.coordY = (int)this.y;
      this.coordZ = (int)this.z;
      double scale = mp.getMapView().getDimensionScaling(Minecraft.getMinecraft().world.provider.getDimensionType().getId());
      Point2D.Double p = mp.getMapMode().getClampedScreenXY(mp.getMapView(), this.x * scale, this.z * scale);
      this.screenPos.setLocation(p.x + (double)mp.getMapMode().getXTranslation(), p.y + (double)mp.getMapMode().getYTranslation());
   }

   public PKMarker(EntityType type, TileEntity entity, String name, String desc, double radius, MapRenderer mp, int dimension) {
      this.yaw = 0.0F;
      this.dimension = Minecraft.getMinecraft().world.provider.getDimensionType().getId();
      this.type = type;
      this.r = radius;
      this.name = name;
      this.desc = desc;
      this.x = (double)entity.getPos().getX();
      this.y = (double)entity.getPos().getY();
      this.z = (double)entity.getPos().getZ();
      this.coordX = (int)this.x;
      this.coordY = (int)this.y;
      this.coordZ = (int)this.z;
      double scale = mp.getMapView().getDimensionScaling(Minecraft.getMinecraft().world.provider.getDimensionType().getId());
      Point2D.Double p = mp.getMapMode().getClampedScreenXY(mp.getMapView(), this.x * scale, this.z * scale);
      this.screenPos.setLocation(p.x + (double)mp.getMapMode().getXTranslation(), p.y + (double)mp.getMapMode().getYTranslation());
   }

   public void draw(MapMode mapMode, MapView mapView) {
      if (this.type != EntityType.LOOT && this.type != EntityType.SHRINE) {
         double scale = mapView.getDimensionScaling(mapView.getDimension());
         Point2D.Double p = mapMode.getClampedScreenXY(mapView, this.x * scale, this.z * scale);
         Render.setColour(this.type.getColor().getRGB());
         Render.drawCircle(p.x, p.y, this.type.getRadius());
         Render.setColour(borderColor);
         Render.drawCircleBorder(p.x, p.y, this.type.getRadius(), borderWidth);
      } else {
         this.drawTile(mapMode, mapView);
      }
   }

   public void drawPKTexture(MapMode mapMode, MapView mapView, float mapRotationDegrees) {
      if (this.type != EntityType.LOOT && this.type != EntityType.SHRINE) {
         double scale = mapView.getDimensionScaling(mapView.getDimension());
         Point2D.Double p = mapMode.getClampedScreenXY(mapView, this.x * scale, this.z * scale);
         double markerSize = this.type.getTextureDimension();
         Render.setColour(-1);
         if (this.sprite != null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(this.sprite);
         }

         Render.drawTexturedRect(p.x - markerSize / 2.0, p.y - markerSize / 2.0, markerSize, markerSize);
      } else {
         this.drawTile(mapMode, mapView);
      }
   }

   public void drawTile(MapMode mapMode, MapView mapView) {
      double scale = mapView.getDimensionScaling(mapView.getDimension());
      Point2D.Double p = mapMode.getClampedScreenXY(mapView, this.x * scale, this.z * scale);
      Render.setColour(this.type.getColor().getRGB());
      Render.drawRect(p.x, p.y, this.type.getRadius(), this.type.getRadius());
      Render.setColour(borderColor);
      Render.drawRectBorder(p.x, p.y, this.type.getRadius(), this.type.getRadius(), borderWidth * 2.0);
   }

   public double getDistanceToMarker(Entity entityIn) {
      if (entityIn == null) {
         return 0.0;
      } else {
         double d0 = this.x - entityIn.posX;
         double d1 = this.y - entityIn.posY;
         double d2 = this.z - entityIn.posZ;
         return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
      }
   }
}
