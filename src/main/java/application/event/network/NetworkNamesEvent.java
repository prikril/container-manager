package application.event.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkNamesEvent {

    private List<String> names = new ArrayList<>();

    public NetworkNamesEvent(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

}
