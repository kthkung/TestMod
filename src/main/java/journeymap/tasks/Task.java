//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package journeymap.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Task implements Runnable {
   private Future<?> future = null;

   public Task() {
   }

   public abstract void onComplete();

   public abstract void run();

   public abstract boolean CheckForDuplicate();

   public final Future<?> getFuture() {
      return this.future;
   }

   public final void setFuture(Future<?> future) {
      this.future = future;
   }

   public final boolean isDone() {
      return this.future != null ? this.future.isDone() : false;
   }

   public final void printException() {
      if (this.future != null) {
         try {
            this.future.get();
         } catch (ExecutionException var3) {
            Throwable rootException = var3.getCause();
            rootException.printStackTrace();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }
}
