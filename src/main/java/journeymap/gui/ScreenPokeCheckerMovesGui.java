package journeymap.gui;

import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerMoves;
import net.minecraft.client.gui.GuiScreen;

public class ScreenPokeCheckerMovesGui extends GuiScreenPokeCheckerMoves {
  public ScreenPokeCheckerMovesGui(PokemonStorage selected, StoragePosition b, GuiScreen box) {
    super(selected, b, box);
  }
  
  public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    boolean isEgg = this.pokemon.isEgg();
    isEgg = false;
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
