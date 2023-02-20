//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.map;

import journeymap.Mw;
import journeymap.config.ConfigurationHandler;
import journeymap.map.mapmode.MapMode;
import journeymap.util.Render;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;

public class Trail {
   private Mw mw;
   public LinkedList<TrailMarker> trailMarkerList = new LinkedList();
   public int maxLength = 30;
   public String name;
   public boolean enabled;
   public long lastMarkerTime = 0L;
   public long intervalMillis = 5000L;

   public Trail(Mw mw, String name) {
      this.mw = mw;
      this.name = name;
      this.enabled = ConfigurationHandler.configuration.getBoolean(this.name + "TrailEnabled", "mw.configgui.ctgy.general", false, "");
      this.maxLength = ConfigurationHandler.configuration.getInt(this.name + "TrailMaxLength", "mw.configgui.ctgy.general", this.maxLength, 1, 200, "");
      this.intervalMillis = (long)ConfigurationHandler.configuration.getInt(this.name + "TrailMarkerIntervalMillis", "mw.configgui.ctgy.general", (int)this.intervalMillis, 100, 360000, "");
   }

   public void close() {
      this.trailMarkerList.clear();
   }

   public void onTick() {
      long time = System.currentTimeMillis();
      if (time - this.lastMarkerTime > this.intervalMillis) {
         this.lastMarkerTime = time;
         this.addMarker(this.mw.playerX, this.mw.playerY, this.mw.playerZ, this.mw.playerHeading);
      }

   }

   public void addMarker(double x, double y, double z, double heading) {
      this.trailMarkerList.add(new TrailMarker(x, y, z, heading));

      while(this.trailMarkerList.size() > this.maxLength) {
         this.trailMarkerList.poll();
      }

      int i = this.maxLength - this.trailMarkerList.size();

      for(Iterator var10 = this.trailMarkerList.iterator(); var10.hasNext(); ++i) {
         TrailMarker marker = (TrailMarker)var10.next();
         marker.alphaPercent = i * 100 / this.maxLength;
      }

   }

   public void draw(MapMode mapMode, MapView mapView) {
      Iterator var3 = this.trailMarkerList.iterator();

      while(var3.hasNext()) {
         TrailMarker marker = (TrailMarker)var3.next();
         marker.draw(mapMode, mapView);
      }

   }

   class TrailMarker {
      double x;
      double y;
      double z;
      double heading;
      int alphaPercent;
      static final int borderColour = -16777216;
      static final int colour = -16711681;

      public TrailMarker(double x, double y, double z, double heading) {
         this.set(x, y, z, heading);
      }

      public void set(double x, double y, double z, double heading) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.heading = heading;
         this.alphaPercent = 100;
      }

      public void draw(MapMode mapMode, MapView mapView) {
         if (mapView.isBlockWithinView(this.x, this.z, mapMode.getConfig().circular)) {
            Point2D.Double p = mapMode.blockXZtoScreenXY(mapView, this.x, this.z);
            Render.setColourWithAlphaPercent(-16777216, this.alphaPercent);
            Render.drawArrow(p.x, p.y, this.heading, (double)mapMode.getConfig().trailMarkerSize);
            Render.setColourWithAlphaPercent(-16711681, this.alphaPercent);
            Render.drawArrow(p.x, p.y, this.heading, (double)mapMode.getConfig().trailMarkerSize - 1.0);
         }

      }
   }
}
