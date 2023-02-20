//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.inject;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

public class ColorData {
   public static final int POS_X = 5;
   public static final int POS_Y = 5;
   public static int buttonWidth = 50;
   public static int buttonHeight = 20;
   public static final int PADDING = 3;
   public static final int OUTLINE = 2;
   public static final String FORMAT_BUTTON_STR = "Insert Format Symbol";
   public static final int FORMAT_BUTTON_ID = 10;
   public static final String FORMAT_SYMBOL = String.valueOf('$');

   public ColorData() {
   }

   public static void initGui(GuiScreen gui) {
      int height = gui.height;
      int width = gui.width;
      int count = 2000;
      int colorLen = EnumColor.values().length;
      int buttonHeight = Math.min((int)Math.floor((double)(height / colorLen)), 20);
      int interval = 0;
      List<GuiButton> buttonList = (List)ReflectionHelper.getPrivateValue(GuiScreen.class, gui, new String[]{"field_146292_n", "buttonList"});
      if (buttonList != null) {
         EnumColor[] var8 = EnumColor.values();
         int var9 = var8.length;

         int var10;
         GuiButton button;
         for(var10 = 0; var10 < var9; ++var10) {
            EnumColor color = var8[var10];
            button = new GuiButton(count, 0, interval, 80, buttonHeight, color.getCode() + color.getName());
            buttonList.add(button);
            ++count;
            interval += buttonHeight;
         }

         count = 3000;
         interval = 0;
         EnumFormat[] var13 = EnumFormat.values();
         var9 = var13.length;

         for(var10 = 0; var10 < var9; ++var10) {
            EnumFormat format = var13[var10];
            button = new GuiButton(count, width - 80, interval, 80, buttonHeight, format.getCode() + format.getName());
            buttonList.add(button);
            ++count;
            interval += buttonHeight;
         }

         ReflectionHelper.setPrivateValue(GuiScreen.class, gui, buttonList, new String[]{"field_146292_n", "buttonList"});
      }

   }
}
