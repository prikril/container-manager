package api.container.element;

import api.base.DeviceElement;
import application.GlobalConfiguration;
import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.entity.network.NetworkEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateContainerElement {


    /*
    {
        "name": "my-new-container",
        "architecture": "x86_64",
        "profiles": ["default"],
        "ephemeral": false,
        "source": {
            "type": "image",
            "alias": "ubuntu14"
        },
       "devices": {
            "eth0": {
                "nictype": "bridged",
                "parent": "net123",
                "type": "nic"
                },
            "eth1": {
                "nictype": "bridged",
                "parent": "net1",
                "type": "nic"
                }
        }

    }
     */

    private String name;

    private String architecture;

    private List<String> profiles; // default profile is used, when not initialized

    private Boolean ephemeral = false; // if false, container will not be destroyed after shutdown

    private Map<String, String> source = new HashMap<>();

    private Map<String, DeviceElement> devices = new HashMap<>();


    public CreateContainerElement(ContainerEntity containerEntity) {
        this.name = containerEntity.getName();
        this.architecture = "x86_64";
        this.source.put("type", "image");
        String alias = GlobalConfiguration.getInstance().getLxdImageAlias();
        this.source.put("alias", alias);
        assignDevices(containerEntity.getNetworks());
    }

    private void assignDevices(Map<String, String> networks) {
        final String DEFAULT_NETWORK_TYPE = "bridged";
        for(String adapterName : networks.keySet()) {
            String networkName = networks.get(adapterName);
            NetworkEntity networkEntity = GlobalInventory.getInstance().networks.get(networkName);
            if (networkEntity != null) {
                String networkType = DEFAULT_NETWORK_TYPE; //TODO: get dynamically from networkEntity!
                DeviceElement deviceElement = new DeviceElement(networkType, networkName, "nic");
                devices.put(adapterName, deviceElement);
            }
        }
    }


}
