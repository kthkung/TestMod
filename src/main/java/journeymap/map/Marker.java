//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import java.awt.geom.Point2D;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import journeymap.map.mapmode.MapMode;
import journeymap.util.Render;
import journeymap.util.Utils;

public class Marker {
   public final String name;
   public final String groupName;
   public int x;
   public int y;
   public int z;
   public int dimension;
   public int colour;
   public Point2D.Double screenPos = new Point2D.Double(0.0, 0.0);

   public Marker(String name, String groupName, int x, int y, int z, int dimension, int colour) {
      this.name = Utils.mungeStringForConfig(name);
      this.x = x;
      this.y = y;
      this.z = z;
      this.dimension = dimension;
      this.colour = colour;
      this.groupName = Utils.mungeStringForConfig(groupName);
   }

   public String getString() {
      return String.format("%s %s (%d, %d, %d) %d %06x", this.name, this.groupName, this.x, this.y, this.z, this.dimension, this.colour & 16777215);
   }

   public void colourNext() {
      this.colour = Utils.getNextColour();
   }

   public void colourPrev() {
      this.colour = Utils.getPrevColour();
   }

   public void draw(MapMode mapMode, MapView mapView, int borderColour) {
      double scale = mapView.getDimensionScaling(this.dimension);
      Point2D.Double p = mapMode.getClampedScreenXY(mapView, (double)this.x * scale, (double)this.z * scale);
      this.screenPos.setLocation(p.x + (double)mapMode.getXTranslation(), p.y + (double)mapMode.getYTranslation());
      double mSize = (double)mapMode.getConfig().markerSize;
      double halfMSize = (double)mapMode.getConfig().markerSize / 2.0;
      Render.setColour(borderColour);
      Render.drawRect(p.x - halfMSize, p.y - halfMSize, mSize, mSize);
      Render.setColour(this.colour);
      Render.drawRect(p.x - halfMSize + 0.5, p.y - halfMSize + 0.5, mSize - 1.0, mSize - 1.0);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Marker)) {
         return false;
      } else {
         Marker m = (Marker)o;
         return this.name == m.name && this.groupName == m.groupName && this.x == m.x && this.y == m.y && this.z == m.z && this.dimension == m.dimension;
      }
   }

   public double getDistanceToMarker(Entity entityIn) {
      if (entityIn == null) {
         return 0.0;
      } else {
         double d0 = (double)this.x - entityIn.posX;
         double d1 = (double)this.y - entityIn.posY;
         double d2 = (double)this.z - entityIn.posZ;
         return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
      }
   }

   public float getRed() {
      return (float)(this.colour >> 16 & 255) / 255.0F;
   }

   public float getGreen() {
      return (float)(this.colour >> 8 & 255) / 255.0F;
   }

   public float getBlue() {
      return (float)(this.colour & 255) / 255.0F;
   }
}
