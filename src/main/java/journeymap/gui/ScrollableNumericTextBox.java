package journeymap.gui;

import net.minecraft.client.gui.FontRenderer;

class ScrollableNumericTextBox extends ScrollableTextBox {
   public int maxValue = -1;
   public int minValue = -1;

   public ScrollableNumericTextBox(int x, int y, int width, String label, FontRenderer fontrendererObj) {
      super(x, y, width, label, fontrendererObj);
   }

   public void textFieldScroll(int direction) {
      int newValue = 0;
      if (this.validateTextFieldData()) {
         newValue = this.getTextFieldIntValue();
         if (direction > 0) {
            if (this.maxValue < 0 || newValue + 1 <= this.maxValue) {
               ++newValue;
            }
         } else if (direction < 0 && (this.minValue < 0 || newValue - 1 >= this.minValue)) {
            --newValue;
         }
      }

      this.setText(newValue);
   }

   public int getTextFieldIntValue() {
      try {
         return Integer.parseInt(this.getText());
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   public void setText(int num) {
      if (this.maxValue < 0 || num <= this.maxValue || num >= this.minValue) {
         this.setText(Integer.toString(num));
      }

   }

   public void KeyTyped(char c, int key) {
      if (c >= '0' && c <= '9' || key == 14 || key == 203 || key == 205 || c == '-' && this.getCursorPosition() == 0) {
         if (Character.isDigit(c) && this.maxValue > -1 && Integer.parseInt(this.getText() + c) > this.maxValue) {
            return;
         }

         super.KeyTyped(c, key);
      }

   }

   public void setMaxValue(int max) {
      this.maxValue = max;
      this.textField.setMaxStringLength(Integer.toString(max).length());
   }

   public void setMinValue(int min) {
      this.minValue = min;
   }
}
