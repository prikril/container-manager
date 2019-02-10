package application.entity.network;

import ui.node.NetworkNode;

public class NetworkEntity {

    private String name;

    private NetworkType type;

    private NetworkNode node;

    private String ipAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkType getType() {
        return type;
    }

    public void setType(NetworkType type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public NetworkNode getNode() {
        return node;
    }

    public void setNode(NetworkNode node) {
        this.node = node;
    }

    public NetworkEntity() {
    }

    public NetworkEntity(String name, NetworkType type, NetworkNode node) {
        this.name = name;
        this.type = type;
        this.node = node;
    }

}
