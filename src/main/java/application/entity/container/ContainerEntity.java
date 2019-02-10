package application.entity.container;

import ui.node.ContainerNode;

import java.util.HashMap;
import java.util.Map;

public class ContainerEntity {

    private String name;

    private ContainerNode node;

    private ContainerStatus status;

    private String description;

    private Map<String, String> networks = new HashMap<>(); // adapterName -> networkName


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContainerNode getNode() {
        return node;
    }

    public void setNode(ContainerNode node) {
        this.node = node;
    }

    public ContainerStatus getStatus() {
        return status;
    }

    public void setStatus(ContainerStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, String> networks) {
        this.networks = networks;
    }


    public ContainerEntity(String containerName) {
        this.name = containerName;
    }

    public ContainerEntity(String containerName, ContainerNode node) {
        this.name = containerName;
        this.node = node;
    }

}
