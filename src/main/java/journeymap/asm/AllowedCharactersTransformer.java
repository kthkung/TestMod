//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AllowedCharactersTransformer implements IClassTransformer {
   public AllowedCharactersTransformer() {
   }

   public byte[] transform(String name, String transformedName, byte[] bytes) {
      if (transformedName.equals("net.minecraft.util.ChatAllowedCharacters")) {
         System.out.println("> Pixelradar AllowedCharactersTransformer");
         bytes = this.transformClass(transformedName, bytes);
      }

      return bytes;
   }

   private byte[] transformClass(String name, byte[] bytes) {
      System.out.println("Using class " + name);
      ClassNode classNode = new ClassNode();
      ClassReader classReader = new ClassReader(bytes);
      classReader.accept(classNode, 0);

      for (MethodNode method : classNode.methods) {
         this.transformMethod(method);
      }

      ClassWriter writer = new ClassWriter(3);
      classNode.accept(writer);
      return writer.toByteArray();
   }

   private void transformMethod(MethodNode method) {
      for(int i = 0; i < method.instructions.size(); ++i) {
         AbstractInsnNode ins = method.instructions.get(i);
         if (ins.getOpcode() == 17) {
            IntInsnNode node = (IntInsnNode)ins;
            if (node.operand == 167) {
               System.out.println("Found value 167, assuming it to be the formatting symbol.");
               node.operand = 0;
            }
         }
      }

   }
}
