//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.region;

import net.minecraft.block.state.IBlockState;

public interface IChunk {
   IBlockState getBlockState(int var1, int var2, int var3);

   int getBiome(int var1, int var2, int var3);

   int getLightValue(int var1, int var2, int var3);

   int getMaxY();
}
