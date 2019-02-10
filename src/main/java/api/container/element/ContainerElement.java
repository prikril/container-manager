package api.container.element;


import api.base.DeviceElement;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContainerElement {

    /*
    {
        "architecture": "x86_64",
        "config": {
            "image.architecture": "amd64",
            "image.description": "ubuntu 16.04 LTS amd64 (release) (20180724)",
            "image.label": "release",
            "image.os": "ubuntu",
            "image.release": "xenial",
            "image.serial": "20180724",
            "image.version": "16.04",
            "volatile.apply_template": "create",
            "volatile.base_image": "2a7896bae0f2322559e5b9452b0adf58a5a76f7b772fa6906c825407ea6c3386",
            "volatile.eth0.hwaddr": "00:16:3e:5d:1f:0a",
            "volatile.idmap.base": "0",
            "volatile.idmap.next": "[{\"Isuid\":true,\"Isgid\":false,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536},{\"Isuid\":false,\"Isgid\":true,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536}]",
            "volatile.last_state.idmap": "[{\"Isuid\":true,\"Isgid\":false,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536},{\"Isuid\":false,\"Isgid\":true,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536}]"
        },
        "devices": {},
        "ephemeral": false,
        "profiles": [
            "default"
        ],
        "stateful": false,
        "description": "",
        "created_at": "2018-07-26T01:05:39+02:00",
        "expanded_config": {
            "image.architecture": "amd64",
            "image.description": "ubuntu 16.04 LTS amd64 (release) (20180724)",
            "image.label": "release",
            "image.os": "ubuntu",
            "image.release": "xenial",
            "image.serial": "20180724",
            "image.version": "16.04",
            "volatile.apply_template": "create",
            "volatile.base_image": "2a7896bae0f2322559e5b9452b0adf58a5a76f7b772fa6906c825407ea6c3386",
            "volatile.eth0.hwaddr": "00:16:3e:5d:1f:0a",
            "volatile.idmap.base": "0",
            "volatile.idmap.next": "[{\"Isuid\":true,\"Isgid\":false,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536},{\"Isuid\":false,\"Isgid\":true,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536}]",
            "volatile.last_state.idmap": "[{\"Isuid\":true,\"Isgid\":false,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536},{\"Isuid\":false,\"Isgid\":true,\"Hostid\":165536,\"Nsid\":0,\"Maprange\":65536}]"
        },
        "expanded_devices": {
            "eth0": {
                "name": "eth0",
                "nictype": "bridged",
                "parent": "lxdbr0",
                "type": "nic"
            },
            "root": {
                "path": "/",
                "pool": "default",
                "type": "disk"
            }
        },
        "name": "container1",
        "status": "Stopped",
        "status_code": 102,
        "last_used_at": "1970-01-01T01:00:00+01:00",
        "location": ""
    }


    or 404:
    {
        "error": "not found",
        "error_code": 404,
        "type": "error"
    }
     */

    private String architecture;

    private Map<String, String> config;

    private Map<String, DeviceElement> devices;

    private Boolean ephemeral;

    private List<String> profiles;

    private Boolean stateful;

    private String description;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("expanded_config")
    private Object expandedConfig;

    @SerializedName("expanded_devices")
    private Object expandedDevices;

    private String name;

    private String status;

    @SerializedName("status_code")
    private Integer statusCode;

    @SerializedName("last_used_at")
    private Date lastUsedAt;

    private String location;


    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, DeviceElement> getDevices() {
        return devices;
    }

    public void setDevices(Map<String, DeviceElement> devices) {
        this.devices = devices;
    }

    public Boolean getEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(Boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public Boolean getStateful() {
        return stateful;
    }

    public void setStateful(Boolean stateful) {
        this.stateful = stateful;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Object getExpandedConfig() {
        return expandedConfig;
    }

    public void setExpandedConfig(Object expandedConfig) {
        this.expandedConfig = expandedConfig;
    }

    public Object getExpandedDevices() {
        return expandedDevices;
    }

    public void setExpandedDevices(Object expandedDevices) {
        this.expandedDevices = expandedDevices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Date getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Date lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Container: ").append(name).append("\n")
                .append("Status: ").append(status).append("\n")
                .append("Created at: ").append(createdAt).append("\n");

        return builder.toString();
    }

}
