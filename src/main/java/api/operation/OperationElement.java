package api.operation;


import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class OperationElement {

    /*
    {
        "id": "4aa91686-8aca-4d87-bc34-edbd6b31e82f",
        "class": "task",
        "description": "Creating container",
        "created_at": "2018-07-29T21:41:34.258817866+02:00",
        "updated_at": "2018-07-29T21:41:34.258817866+02:00",
        "status": "Running",
        "status_code": 103,
        "resources": {
            "containers": [
                "/1.0/containers/my-new-container"
            ]
        },
        "metadata": null,
        "may_cancel": false,
        "err": ""
    }
     */

    private String id;

    @SerializedName("class")
    private String operationClass;

    private String description;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    private String status;

    @SerializedName("status_code")
    private Integer statusCode;

    private Object resources;

    private Object metadata;

    @SerializedName("may_cancel")
    private Boolean mayCancel;

    private String err;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationClass() {
        return operationClass;
    }

    public void setOperationClass(String operationClass) {
        this.operationClass = operationClass;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public Object getResources() {
        return resources;
    }

    public void setResources(Object resources) {
        this.resources = resources;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Boolean getMayCancel() {
        return mayCancel;
    }

    public void setMayCancel(Boolean mayCancel) {
        this.mayCancel = mayCancel;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Operation: ").append(id).append("\n")
                .append("Class: ").append(operationClass).append("\n")
                .append("Description: ").append(description).append("\n")
                .append("Status: ").append(status).append("\n")
                .append("Created at: ").append(createdAt);

        return builder.toString();
    }

}
