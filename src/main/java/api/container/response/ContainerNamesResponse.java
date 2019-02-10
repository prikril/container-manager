package api.container.response;

import api.base.BaseResponse;
import api.container.element.ContainerElement;

import java.util.ArrayList;
import java.util.List;

public class ContainerNamesResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": [
            "/1.0/containers/container1",
            "/1.0/containers/container2"
        ]
    }


    */

    private List<String> metadata; // pointers to containers

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
        return "Found containers: \n" + containersBuilder.toString();
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        for (String url : metadata) {
            names.add(url.substring( url.lastIndexOf("/") + 1 ));
        }

        return names;
    }

}
