import java.util.Comparator;
import java.util.List;

public class LeastWorkLeftScheduler implements Scheduler{
    public Host getAvailableHost(MyDispatcher dispatcher, List<Host> hosts) {
        return hosts.stream().
                min(Comparator
                        .comparingLong(Host::getWorkLeft)
                        .thenComparingLong(Host::getId))
                .orElse(null);
    }

}
