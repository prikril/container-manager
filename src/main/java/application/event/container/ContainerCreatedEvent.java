package application.event.container;

public class ContainerCreatedEvent {


    private final String containerName;

    private final boolean successful;

    private final String message;


    public ContainerCreatedEvent(String containerName, boolean successful, String message) {
        this.containerName = containerName;
        this.successful = successful;
        this.message = message;
    }

    public String getContainerName() {
        return containerName;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

}
