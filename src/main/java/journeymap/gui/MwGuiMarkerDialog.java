//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import journeymap.map.Marker;
import journeymap.map.MarkerManager;
import journeymap.util.Utils;

@SideOnly(Side.CLIENT)
public class MwGuiMarkerDialog extends MwGuiTextDialog {
   private final MarkerManager markerManager;
   private Marker editingMarker;
   private String markerName = "";
   private String markerGroup = "";
   private int markerX = 0;
   private int markerY = 80;
   private int markerZ = 0;
   private int state = 0;
   private int dimension = 0;

   public MwGuiMarkerDialog(GuiScreen parentScreen, MarkerManager markerManager, String markerName, String markerGroup, int x, int y, int z, int dimension) {
      super(parentScreen, I18n.format("Marker Name", new Object[0]) + ":", markerName, I18n.format("marker must have a name", new Object[0]));
      this.markerManager = markerManager;
      this.markerName = markerName;
      this.markerGroup = markerGroup;
      this.markerX = x;
      this.markerY = y;
      this.markerZ = z;
      this.editingMarker = null;
      this.dimension = dimension;
   }

   public MwGuiMarkerDialog(GuiScreen parentScreen, MarkerManager markerManager, Marker editingMarker) {
      super(parentScreen, I18n.format("Edit Marker Name", new Object[0]) + ":", editingMarker.name, I18n.format("marker must have a name", new Object[0]));
      this.markerManager = markerManager;
      this.editingMarker = editingMarker;
      this.markerName = editingMarker.name;
      this.markerGroup = editingMarker.groupName;
      this.markerX = editingMarker.x;
      this.markerY = editingMarker.y;
      this.markerZ = editingMarker.z;
      this.dimension = editingMarker.dimension;
   }

   public boolean submit() {
      boolean done = false;
      switch (this.state) {
         case 0:
            this.markerName = this.getInputAsString();
            if (this.inputValid) {
               this.title = I18n.format("Marker Group", new Object[0]) + ":";
               this.setText(this.markerGroup);
               this.error = I18n.format("marker must have a group name", new Object[0]);
               ++this.state;
            }
            break;
         case 1:
            this.markerGroup = this.getInputAsString();
            if (this.inputValid) {
               this.title = I18n.format("Marker X", new Object[0]) + ":";
               this.setText("" + this.markerX);
               this.error = I18n.format("invalid value", new Object[0]);
               ++this.state;
            }
            break;
         case 2:
            this.markerX = this.getInputAsInt();
            if (this.inputValid) {
               this.title = I18n.format("Marker Y", new Object[0]) + ":";
               this.setText("" + this.markerY);
               this.error = I18n.format("invalid value", new Object[0]);
               ++this.state;
            }
            break;
         case 3:
            this.markerY = this.getInputAsInt();
            if (this.inputValid) {
               this.title = I18n.format("Marker Z", new Object[0]) + ":";
               this.setText("" + this.markerZ);
               this.error = I18n.format("invalid value", new Object[0]);
               ++this.state;
            }
            break;
         case 4:
            this.markerZ = this.getInputAsInt();
            if (this.inputValid) {
               done = true;
               int colour = Utils.getCurrentColour();
               if (this.editingMarker != null) {
                  colour = this.editingMarker.colour;
                  this.markerManager.delMarker(this.editingMarker);
                  this.editingMarker = null;
               }

               this.markerManager.addMarker(this.markerName, this.markerGroup, this.markerX, this.markerY, this.markerZ, this.dimension, colour);
               this.markerManager.setVisibleGroupName(this.markerGroup);
               this.markerManager.update();
            }
      }

      return done;
   }
}
