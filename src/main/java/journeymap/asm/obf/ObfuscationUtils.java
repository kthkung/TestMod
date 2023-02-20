//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.asm.obf;

import java.io.BufferedReader;

public class ObfuscationUtils {
   private static boolean runtimeDeobf = true;
   private static final String UNKNOWN = "*";
   private static BufferedReader buffer;

   public ObfuscationUtils() {
   }

   public static String asmify(String name) {
      return name.replace('.', '/');
   }

   public static boolean isRuntimeDeobfuscated() {
      return runtimeDeobf;
   }

   public static void setRuntimeDeobfuscated(boolean theDeobf) {
      System.out.println("Setting runtime deobfuscation to " + theDeobf);
      runtimeDeobf = theDeobf;
   }
}
