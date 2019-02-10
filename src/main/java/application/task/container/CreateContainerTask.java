package application.task.container;

import application.task.TaskType;

public class CreateContainerTask implements ContainerTask {

    private final String containerName;

    public CreateContainerTask(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerName() {
        return containerName;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.CREATE_CONTAINER;
    }

    @Override
    public String getName() {
        return containerName;
    }

}
