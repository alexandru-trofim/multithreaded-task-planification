import java.util.List;

public class RoundRobinScheduler implements Scheduler{

    @Override
    public Host getAvailableHost(MyDispatcher dispatcher, List<Host> hosts) {
        int n = hosts.size();
        int i = (int)dispatcher.getLastAssignedNode();
        return hosts.get((i + 1) % n);
    }
}
