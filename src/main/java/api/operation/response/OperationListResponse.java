package api.operation.response;

import api.base.BaseResponse;

import java.util.List;

public class OperationListResponse extends BaseResponse {

    /*
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": [
            "/1.0/operations/abcd123"
        ]
    }

    or empty

    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": {}
    }
    */

    private List<String> metadata; // pointers to operations

    @Override
    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String>  metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        StringBuilder operationsBuilder = new StringBuilder();
        metadata.forEach(element -> operationsBuilder.append(element).append("\n"));
        return "Operations: \n" + operationsBuilder.toString();
    }

}
