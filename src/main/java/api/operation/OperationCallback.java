package api.operation;

public interface OperationCallback {

    void onResponse();

    void onFailure(OperationElement operationElement);

}
