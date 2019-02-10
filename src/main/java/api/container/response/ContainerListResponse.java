package api.container.response;

import api.base.BaseResponse;
import api.container.element.ContainerElement;

import java.util.List;

public class ContainerListResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": [
            "/1.0/containers/container1"
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
            { container1 data },
            { container2 data }
        ]
    }

    */

    private List<ContainerElement> metadata;

    @Override
    public List<ContainerElement> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<ContainerElement>  metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        StringBuilder containersBuilder = new StringBuilder();
        metadata.forEach(element -> containersBuilder.append(element).append("\n"));
        return "Containers: \n" + containersBuilder.toString();
    }

}
