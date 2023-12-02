import java.util.List;

public class SizeIntervalTaskScheduler implements Scheduler{
    @Override
    public Host getAvailableHost(MyDispatcher dispatcher, List<Host> hosts) {
        return switch (dispatcher.getTaskToAssign().getType()) {
            case SHORT -> hosts.get(0);
            case MEDIUM -> hosts.get(1);
            case LONG -> hosts.get(2);
        };
    }



}
