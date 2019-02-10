package application.task.container;

import application.task.TaskType;

public class RenameContainerTask implements ContainerTask {

    private final String oldName;
    private final String newName;

    public RenameContainerTask(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }


    @Override
    public TaskType getTaskType() {
        return TaskType.RENAME_CONTAINER;
    }

    @Override
    public String getName() {
        return oldName;
    }

}
