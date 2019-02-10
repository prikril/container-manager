package api.network.response;

import api.base.BaseResponse;
import api.network.element.NetworkElement;

public class NetworkResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": {
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
    }
     */



    private NetworkElement metadata;

    @Override
    public NetworkElement getMetadata() {
        return metadata;
    }

    public void setMetadata(NetworkElement metadata) {
        this.metadata = metadata;
    }

}
