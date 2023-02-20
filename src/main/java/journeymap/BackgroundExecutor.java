package journeymap;

import journeymap.tasks.Task;
import journeymap.util.Logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BackgroundExecutor {
   private ExecutorService executor = Executors.newSingleThreadExecutor();
   private LinkedList<Task> taskQueue = new LinkedList();
   public boolean closed = false;
   private boolean doDiag = true;

   public boolean addTask(Task task) {
      if (!this.closed) {
         if (!task.CheckForDuplicate()) {
            Future<?> future = this.executor.submit(task);
            task.setFuture(future);
            this.taskQueue.add(task);
         }

         if (this.tasksRemaining() > 500 && this.doDiag) {
            this.doDiag = false;
            Logging.logError("Taskque went over 500 starting diagnostic");
            this.taskLeftPerType();
            Logging.logError("End of diagnostic");
         } else {
            this.doDiag = true;
         }
      } else {
         Logging.log("MwExecutor.addTask: error: cannot add task to closed executor");
      }

      return this.closed;
   }

   public boolean processTaskQueue() {
      boolean processed = false;
      Task task = (Task)this.taskQueue.poll();
      if (task != null) {
         if (task.isDone()) {
            task.printException();
            task.onComplete();
            processed = true;
         } else {
            this.taskQueue.push(task);
         }
      }

      return !processed;
   }

   public boolean processRemainingTasks(int attempts, int delay) {
      while(this.taskQueue.size() > 0 && attempts > 0) {
         if (this.processTaskQueue()) {
            try {
               Thread.sleep((long)delay);
            } catch (Exception var4) {
            }

            --attempts;
         }
      }

      return attempts <= 0;
   }

   public int tasksRemaining() {
      return this.taskQueue.size();
   }

   public boolean close() {
      boolean error = true;

      try {
         this.taskLeftPerType();
         this.executor.shutdown();
         this.processRemainingTasks(50, 5);
         error = !this.executor.awaitTermination(10L, TimeUnit.SECONDS);
         error = false;
      } catch (InterruptedException var3) {
         Logging.log("error: IO task was interrupted during shutdown");
         var3.printStackTrace();
      }

      this.closed = true;
      return error;
   }

   private void taskLeftPerType() {
      HashMap<String, Object> tasksLeft = new HashMap();
      Iterator var2 = this.taskQueue.iterator();

      String key;
      while(var2.hasNext()) {
         Task t = (Task)var2.next();
         key = t.getClass().toString();
         if (tasksLeft.containsKey(key)) {
            tasksLeft.put(key, (Integer)tasksLeft.get(key) + 1);
         } else {
            tasksLeft.put(key, 1);
         }
      }

      var2 = tasksLeft.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, Object> entry = (Entry)var2.next();
         key = (String)entry.getKey();
         Object value = entry.getValue();
         Logging.log("waiting for %d %s to finish...", value, key);
      }

   }
}
