import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeastWorkLeftScheduler implements Scheduler{
    public Host getAvailableHost(MyDispatcher dispatcher, List<Host> hosts) {
//        return hosts.stream().
//                min(Comparator
//                        .comparingLong(Host::getWorkLeft)
//                        .thenComparingLong(Host::getId))
//                .orElse(null);
        if (hosts == null || hosts.isEmpty()) {
            return null; // Or throw an exception, depending on your requirements
        }

        Host smallestQueueHost = hosts.get(0);

        for (int i = 1; i < hosts.size(); i++) {
            Host currentHost = hosts.get(i);
            long s1 = TimeUnit.MILLISECONDS.toSeconds(currentHost.getWorkLeft());
            long s2 = TimeUnit.MILLISECONDS.toSeconds(smallestQueueHost.getWorkLeft());
            int queueSizeComparison = Long.compare(s1, s2);

            if (queueSizeComparison < 0 || (queueSizeComparison == 0 && currentHost.getId() < smallestQueueHost.getId())) {
                // If currentHost has a smaller queue size or equal queue size with a smaller ID
                smallestQueueHost = currentHost;
            }
        }
        return smallestQueueHost;
    }

}
