//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.util;

import java.util.ArrayList;

public class TwoDimensionalArrayList<T> extends ArrayList<ArrayList<T>> {
   public TwoDimensionalArrayList() {
   }

   public void addToInnerArray(int index, T element) {
      while(index >= this.size()) {
         this.add(new ArrayList());
      }

      ((ArrayList)this.get(index)).add(element);
   }

   public void addToInnerArray(int index, int index2, T element) {
      while(index >= this.size()) {
         this.add(new ArrayList());
      }

      ArrayList<T> inner = (ArrayList)this.get(index);

      while(index2 >= inner.size()) {
         inner.add((T)null);
      }

      inner.set(index2, element);
   }
}
