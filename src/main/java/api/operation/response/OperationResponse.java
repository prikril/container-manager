package api.operation.response;

import api.base.BaseResponse;
import api.operation.OperationElement;

public class OperationResponse extends BaseResponse {

    /*
    example deleting a container named "test2"
    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": {
            "id": "3613302d-16ae-405f-988d-fa2b76c49c29",
            "class": "task",
            "description": "Deleting container",
            "created_at": "2018-08-31T17:19:28.571889857+02:00",
            "updated_at": "2018-08-31T17:19:28.571889857+02:00",
            "status": "Success",
            "status_code": 200,
            "resources": {
                "containers": [
                    "/1.0/containers/test2"
                ]
            },
            "metadata": null,
            "may_cancel": false,
            "err": ""
        }
    }

    or with error (but http 200)

    {
        "type": "sync",
        "status": "Success",
        "status_code": 200,
        "operation": "",
        "error_code": 0,
        "error": "",
        "metadata": {
            "id": "11fe3c4e-4e60-4df1-9a8d-cb9c5d2034ab",
            "class": "task",
            "description": "Creating container",
            "created_at": "2018-07-29T23:15:32.671971156+02:00",
            "updated_at": "2018-07-29T23:15:32.671971156+02:00",
            "status": "Failure",
            "status_code": 400,
            "resources": {
                "containers": [
                    "/1.0/containers/my-new-container"
                ]
            },
            "metadata": null,
            "may_cancel": false,
            "err": "Container 'my-new-container' already exists"
        }
    }
    */

    private OperationElement metadata;

    @Override
    public OperationElement getMetadata() {
        return metadata;
    }

    public void setMetadata(OperationElement  metadata) {
        this.metadata = metadata;
    }


    @Override
    public String toString() {
        StringBuilder operationBuilder = new StringBuilder();
        if (metadata != null) {
            operationBuilder.append(metadata.toString()).append("\n");
        } else {
            operationBuilder.append("Metadata is null\n");
        }

        return "OperationResponse: \n" + operationBuilder.toString();
    }

}
