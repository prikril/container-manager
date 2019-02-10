package api.base;

public class ErrorResponse {

    /*
    while deleting a container:

    {
        "error": "container is running",
        "error_code": 400,
        "type": "error"
    }

    while stopping a non existent container:

    {
        "error": "not found",
        "error_code": 404,
        "type": "error"
    }

     */


    private String error;
    private int error_code;
    private String type;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
