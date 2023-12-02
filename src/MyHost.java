/* Implement this class. */

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MyHost extends Host {

    PriorityBlockingQueue<Task> queue;
    ConcurrentHashMap<Task, Double> map;
    Task currentTask;
    Task highPriorityTask;
    long workLeft;
    boolean preemptCurrentTask;
    int working;
    boolean running;
    final Object queueLock = new Object();
    public MyHost() {
        super();
        preemptCurrentTask = false;
        highPriorityTask = null;
        running = true;
        working = 0;
        map = new ConcurrentHashMap<>();
        queue = new PriorityBlockingQueue<>(2, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                int priorityCompare = Integer.compare(t2.getPriority(), t1.getPriority());
                return priorityCompare == 0 ?
                        Double.compare(map.get(t1), map.get(t2)) : priorityCompare;

            }
        });
    }
    @Override
    public void run() {
        while (running) {
            Task task;
            if (highPriorityTask == null)  {
                    task = queue.poll();
            } else {
                task = highPriorityTask;
                highPriorityTask = null;
            }
            if (task == null) {
                working = 0;
                //sleep or wait
            } else {
                working = 1;
                currentTask = task;
                long remainingTime;
                if (currentTask.getLeft() == 0) {
                    System.out.println("Am intrat 1");
                    remainingTime = TimeUnit.MILLISECONDS.toNanos(currentTask.getDuration());
                } else {
                    System.out.println("Am intrat 2");
                    remainingTime = TimeUnit.MILLISECONDS.toNanos(currentTask.getLeft());
                }
                System.out.println("Am scos task-ul " + currentTask.getId() + " din coada "
                        + "si mai are " + TimeUnit.NANOSECONDS.toMillis(remainingTime));

                long startTime = System.nanoTime();
                long elapsedTime;

                while (remainingTime > 0) {
                    try {
                        Thread.sleep(50); // or use TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception");
                    }
                    elapsedTime = System.nanoTime() - startTime;
                    remainingTime -= elapsedTime;
                    workLeft = remainingTime;
                    startTime = System.nanoTime();

                    if (preemptCurrentTask) {
                        System.out.println("We stop task " + currentTask.getId()
                                + " at " + TimeUnit.NANOSECONDS.toMillis(remainingTime));
                        //set Left time for current task
                        currentTask.setLeft(TimeUnit.NANOSECONDS.toMillis(remainingTime));
                        System.out.println("We stop task " + currentTask.getId()
                                + " at " + currentTask.getLeft());
                        //add this to queue
                            queue.put(currentTask);
                        preemptCurrentTask = false;
                        break;
                    }
                }
                if (remainingTime <= 0) {
                    currentTask.finish();
                    workLeft = 0;
                }

            }
        }
    }

    @Override
    public synchronized void addTask(Task task) {
        map.put(task, Timer.getTimeDouble());
        // ceva de genul trebuie sa fie
        if (currentTask != null && currentTask.isPreemptible() &&
            currentTask.getPriority() < task.getPriority()) {
            preemptCurrentTask = true;
            highPriorityTask = task;
            System.out.println("Task " + task.getId() + " is a high priority task");
        } else {
            queue.put(task);
        }
    }

    @Override
    public int getQueueSize() {
        return queue.size() + working;
    }

    @Override
    public synchronized long getWorkLeft() {
        AtomicLong res = new AtomicLong();
        Task[] tasks;
        queue.forEach(task -> {
            if (task != null && task != currentTask) {
                res.addAndGet(task.getLeft());
            }
        });
        res.addAndGet(workLeft);
        return TimeUnit.MILLISECONDS.toSeconds(res.get());
    }

    @Override
    public void shutdown() {
        running = false;
    }
}
