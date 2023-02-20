//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.inject;

public enum EnumFormat {
   RESET("r", "Reset"),
   BOLD("l", "Bold"),
   ITALIC("o", "Italic"),
   UNDERLINE("n", "Underline"),
   STRIKE("m", "Strike"),
   RANDOM("k", "Random");

   private String code;
   private String name;

   private EnumFormat(String code, String name) {
      this.code = code;
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String getCode() {
      return "ï¿½" + this.code;
   }

   public static EnumFormat get(int index) {
      return values()[index];
   }
}
