package journeymap.gui;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import net.minecraft.client.gui.GuiScreen;

public class ScreenPokeCheckerGui extends GuiScreenPokeChecker {

    public ScreenPokeCheckerGui(PokemonStorage selected, StoragePosition b, GuiScreen box) {
        super(selected, b, box);
        selected = this.pokemon.getStorage();
      }

    @Override
     
  public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    boolean isEgg = this.pokemon.isEgg();
    isEgg = false;
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
