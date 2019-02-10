package application;

import application.entity.container.ContainerEntity;
import application.entity.network.NetworkEntity;

import java.util.HashMap;
import java.util.Map;

public class GlobalInventory {

    private static GlobalInventory inventory = new GlobalInventory();

    public static GlobalInventory getInstance() {
        return inventory;
    }

    private GlobalInventory() {
    }


    public Map<String, ContainerEntity> containers = new HashMap<>(); // containerName, entity

    public Map<String, NetworkEntity> networks = new HashMap<>(); // networkName, entity

    public void clear() {
        containers.clear();
        networks.clear();
    }


}
