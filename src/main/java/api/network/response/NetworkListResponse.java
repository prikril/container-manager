package api.network.response;

import api.base.BaseResponse;
import api.network.element.NetworkElement;

import java.util.List;

public class NetworkListResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": [
            "/1.0/networks/lxdbr0",
            "/1.0/networks/lo",
            "/1.0/networks/enp0s3"
        ]
    }

    or with recursion=1
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": [
            { network1 data },
            { network2 data }
        ]
    }
    */

    private List<NetworkElement> metadata;

    @Override
    public List<NetworkElement> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<NetworkElement>  metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        StringBuilder networksBuilder = new StringBuilder();
        metadata.forEach(element -> networksBuilder.append(element).append("\n"));
        return "Networks: \n" + networksBuilder.toString();
    }

}
