package application.entity.container;

public class ContainerStatusHelper {

    public static ContainerStatus getStatusFromString(String statusString) {
        if (statusString.equalsIgnoreCase("running")) {
            return ContainerStatus.RUNNING;
        }

        if (statusString.equalsIgnoreCase("stopped")) {
            return ContainerStatus.STOPPED;
        }

        return ContainerStatus.UNKNOWN;
    }

}
