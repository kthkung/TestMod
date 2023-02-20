//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonTab extends GuiButton {
   public static final ResourceLocation texture = new ResourceLocation("mapwriter", "textures/pokeradar/gui_textures.png");

   public GuiButtonTab(int buttonId, int x, int y, String buttonText) {
      super(buttonId, x, y, buttonText);
   }

   public GuiButtonTab(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         FontRenderer fontrenderer = mc.fontRenderer;
         mc.getTextureManager().bindTexture(texture);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
         GL11.glBlendFunc(770, 771);
         this.drawTexturedModalRect(this.x, this.y, 0, 26, 26, 12);
         this.mouseDragged(mc, mouseX, mouseY);
         int j = 14737632;
         if (this.packedFGColour != 0) {
            j = this.packedFGColour;
         } else if (!this.enabled) {
            j = 10526880;
         } else if (this.hovered) {
            j = 16777120;
         }

         this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 3) / 2, j);
      }

   }
}
