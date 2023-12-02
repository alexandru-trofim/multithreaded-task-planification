/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    private long lastAssignedNode;
    private Object lock = new Object();
    private Task taskToAssign;
    Scheduler scheduler;

    public Task getTaskToAssign() {
        return taskToAssign;
    }
    public long getLastAssignedNode() {
        return lastAssignedNode;
    }
    private Scheduler getScheduler() {
       return switch (algorithm) {
            case ROUND_ROBIN ->  new RoundRobinScheduler();
            case SHORTEST_QUEUE -> new ShortestQueueScheduler();
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> new SizeIntervalTaskScheduler();
            case LEAST_WORK_LEFT -> new LeastWorkLeftScheduler();
        };
    }

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        this.scheduler = getScheduler();
        if (scheduler instanceof RoundRobinScheduler) {
            System.out.println("We have round robin scheduler");
        }
        this.lastAssignedNode = 0;
    }

    @Override
    public synchronized void addTask(Task task) {
        // aici am primit un task si trebuie sa decidem la ce host sa-l asignam
        taskToAssign = task;
        Host hostToAssignTask = scheduler.getAvailableHost(this, hosts);
//        lastAssignedNode = hostToAssignTask.getId();
        lastAssignedNode = hosts.indexOf(hostToAssignTask);
        System.out.println("=================================");
        System.out.println("Adding new task: ");
        System.out.println("id : " + task.getId());
        System.out.println("start : " + task.getStart());
        System.out.println("duration : " + task.getDuration());
        System.out.println("priority : " + task.getPriority());
        System.out.println("preemptible: " + task.isPreemptible());
        System.out.println("Task assigned to host : " + hostToAssignTask.getId());
        System.out.println("last assigned node : " + lastAssignedNode);
        System.out.println("=================================");
        hostToAssignTask.addTask(task);
    }
}
