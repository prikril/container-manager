package application.entity.container;

public enum ContainerStatus {

    UNKNOWN(-1), RUNNING(1000), STOPPED(2000);

    private int status;

    ContainerStatus(int status) {
        this.status = status;
    }

    public int getStatusCode() {
        return status;
    }

}
