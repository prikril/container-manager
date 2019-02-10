package application.task;

import application.event.task.CleanTaskFinishedEvent;
import application.task.container.ContainerTask;
import application.task.network.NetworkTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private static TaskService taskService = new TaskService();

    private static List<ContainerTask> containerTasks = new ArrayList<>();

    private static List<NetworkTask> networkTasks = new ArrayList<>();


    public static TaskService getInstance() {
        return taskService;
    }

    private TaskService() {

    }

    public void addContainerTask(ContainerTask task) {
        containerTasks.add(task);
    }

    public void addNetworkTask(NetworkTask task) {
        networkTasks.add(task);
    }

    public void executeAllTasks() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

        cleanHostWithEvent();
    }

    public void clear() {
        containerTasks.clear();
        networkTasks.clear();
    }

    public void removeContainerTasksByName(String containerName) {
        containerTasks.removeIf(task -> containerName.equals(task.getName()));
    }

    public void removeNetworkTasksByName(String networkName) {
        networkTasks.removeIf(task -> networkName.equals(task.getName()));
    }

    private void cleanHostWithEvent() {
        LxdTaskExecutorServiceHelper helper = new LxdTaskExecutorServiceHelper();
        helper.cleanHost();
    }

    public void cleanHost() {
        EventBus.getDefault().unregister(this);
        cleanHostWithEvent();
    }

    @Subscribe
    public void onCleanTaskFinished(CleanTaskFinishedEvent event) {
        LxdTaskExecutorService service = new LxdTaskExecutorService(containerTasks, networkTasks);
        service.executeAllTasks();
    }

}
