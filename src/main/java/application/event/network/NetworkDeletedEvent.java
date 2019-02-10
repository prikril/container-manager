package application.event.network;

public class NetworkDeletedEvent {


    private final String networkName;

    private final boolean successful;

    private final String message;


    public NetworkDeletedEvent(String networkName, boolean successful, String message) {
        this.networkName = networkName;
        this.successful = successful;
        this.message = message;
    }

    public String getNetworkName() {
        return networkName;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

}
