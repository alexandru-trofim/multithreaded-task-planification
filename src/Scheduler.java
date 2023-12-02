import java.util.List;

public interface Scheduler {
    Host getAvailableHost(MyDispatcher dispatcher,List<Host> hosts);
}
