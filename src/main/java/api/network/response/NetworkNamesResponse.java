package api.network.response;

import api.base.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class NetworkNamesResponse extends BaseResponse {

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
            "/1.0/networks/my-network"
        ]
    }


    */

    private List<String> metadata; // pointers to networks

    @Override
    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String>  metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        StringBuilder containersBuilder = new StringBuilder();
        getNames().forEach(element -> containersBuilder.append(element).append("\n"));
        return "Found networks: \n" + containersBuilder.toString();
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        for (String url : metadata) {
            names.add(url.substring( url.lastIndexOf("/") + 1 ));
        }

        return names;
    }

}
