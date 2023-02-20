//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import journeymap.Mw;
import journeymap.config.WorldConfig;
import journeymap.map.MapView;

@SideOnly(Side.CLIENT)
public class MwGuiDimensionDialog extends MwGuiTextDialog {
   final Mw mw;
   final MapView mapView;
   final int dimension;

   public MwGuiDimensionDialog(GuiScreen parentScreen, Mw mw, MapView mapView, int dimension) {
		super(parentScreen, I18n.format("mw.gui.mwguidimensiondialog.title") + ":", Integer.toString(dimension), I18n.format("mw.gui.mwguidimensiondialog.error"));
		this.mw = mw;
		this.mapView = mapView;
		this.dimension = dimension;
	}

   public boolean submit() {
      boolean done = false;
      int dimension = this.getInputAsInt();
      if (this.inputValid) {
         this.mapView.setDimensionAndAdjustZoom(dimension);
         this.mw.miniMap.view.setDimension(dimension);
         WorldConfig.getInstance().addDimension(dimension);
         done = true;
      }

      return done;
   }
}
