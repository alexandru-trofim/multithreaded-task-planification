import java.util.Comparator;
import java.util.List;

public class ShortestQueueScheduler implements Scheduler{
    @Override
    public Host getAvailableHost(MyDispatcher dispatcher, List<Host> hosts) {


        return hosts.stream().
                min(Comparator
                        .comparingInt(Host::getQueueSize)
                        .thenComparingLong(Host::getId))
                .orElse(null);
    }

}
