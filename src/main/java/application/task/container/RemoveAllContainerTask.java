package application.task.container;

import application.task.TaskType;

public class RemoveAllContainerTask implements ContainerTask {

    public RemoveAllContainerTask() {

    }

    @Override
    public TaskType getTaskType() {
        return TaskType.REMOVE_ALL_CONTAINER;
    }

    @Override
    public String getName() {
        return null;
    }

}
