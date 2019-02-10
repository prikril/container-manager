package api.network.element;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class NetworkElement {

    /*
    {
        "config": {
            "ipv4.address": "10.71.10.1/24",
            "ipv4.nat": "true",
            "ipv6.address": "fd42:5a33:c9ef:1cdf::1/64",
            "ipv6.nat": "true"
        },
        "description": "",
        "name": "lxdbr0",
        "type": "bridge",
        "used_by": [],
        "managed": true,
        "status": "Created",
        "locations": [
            "none"
        ]
    }
     */

    private Map<String, String> config;

    private String description;

    private String name;

    private String type;

    @SerializedName("used_by")
    private List<String> usedBy;

    private Boolean managed;

    private String status;

    private List<String> locations;


    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(List<String> usedBy) {
        this.usedBy = usedBy;
    }

    public Boolean getManaged() {
        return managed;
    }

    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Network: ").append(name).append("\n")
                .append("Status: ").append(status).append("\n")
                .append("Type: ").append(type).append("\n");

        return builder.toString();
    }

}
