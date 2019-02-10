package application.task.network;

import application.task.TaskType;

public class CreateNetworkTask implements NetworkTask {

    private final String networkName;

    public CreateNetworkTask(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkName() {
        return networkName;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.CREATE_NETWORK;
    }

    @Override
    public String getName() {
        return networkName;
    }

}
