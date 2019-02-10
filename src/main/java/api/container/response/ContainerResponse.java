package api.container.response;

import api.base.BaseResponse;
import api.container.element.ContainerElement;

public class ContainerResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": {
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
    }
     */



    private ContainerElement metadata;

    @Override
    public ContainerElement getMetadata() {
        return metadata;
    }

    public void setMetadata(ContainerElement metadata) {
        this.metadata = metadata;
    }

}
