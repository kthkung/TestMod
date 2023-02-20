//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class ModGuiFactoryHandler implements IModGuiFactory {
   public ModGuiFactoryHandler() {
   }

   public void initialize(Minecraft minecraftInstance) {
   }

   public boolean hasConfigGui() {
      return true;
   }

   public GuiScreen createConfigGui(GuiScreen parentScreen) {
      return new ModGuiConfig(parentScreen);
   }

   public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories() {
      return null;
   }
}
