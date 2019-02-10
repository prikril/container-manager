package persistence.entity.containernetwork;

import persistence.entity.container.ContainerDataElement;
import persistence.entity.network.NetworkDataElement;

import java.util.ArrayList;
import java.util.List;

public class ContainerNetworkDataElement {

    private List<NetworkDataElement> networks = new ArrayList<>();

    private List<ContainerDataElement> containers = new ArrayList<>();


    public List<NetworkDataElement> getNetworks() {
        return networks;
    }

    public void setNetworks(List<NetworkDataElement> networks) {
        this.networks = networks;
    }

    public List<ContainerDataElement> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerDataElement> containers) {
        this.containers = containers;
    }

}
