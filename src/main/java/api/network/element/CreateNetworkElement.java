package api.network.element;

import java.util.HashMap;
import java.util.Map;

public class CreateNetworkElement {


    /*
    {
        "name": "my-network",
        "description": "My network",
        "config": {
            "ipv4.address": "none",
            "ipv6.address": "2001:470:b368:4242::1/64",
            "ipv6.nat": "true"
        }
    }
     */

    private String name;

    private String description;

    private Map<String, String> config;


    public CreateNetworkElement(String networkName) {
        this.name = networkName;
        this.description = "test-network " + networkName;
        this.config = new HashMap<>();
        this.config.put("ipv4.address", "none");
        this.config.put("ipv6.address", "none");
    }

}
