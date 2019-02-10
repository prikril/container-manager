package application.event.container;

import java.util.ArrayList;
import java.util.List;

public class ContainerNamesEvent {

    private List<String> names = new ArrayList<>();

    public ContainerNamesEvent(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

}
