package application.task;

import api.container.ContainerController;
import api.network.NetworkController;
import application.GlobalInventory;
import application.entity.container.ContainerEntity;
import application.event.container.ContainerCreatedEvent;
import application.event.network.NetworkCreatedEvent;
import application.event.task.AllTasksExecutedEvent;
import application.logging.LoggingFacade;
import application.task.base.BaseTask;
import application.task.container.ContainerTask;
import application.task.container.CreateContainerTask;
import application.task.network.CreateNetworkTask;
import application.task.network.NetworkTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LxdTaskExecutorService {

    private LoggingFacade loggingFacade = LoggingFacade.getInstance();

    private List<ContainerTask> containerTaskList;
    private List<NetworkTask> networkTaskList;

    private int finishedContainerTasks;
    private int finishedNetworkTasks;

    public LxdTaskExecutorService(List<ContainerTask> containerTasks, List<NetworkTask> networkTasks) {
        this.containerTaskList = containerTasks;
        this.networkTaskList = networkTasks;
        EventBus.getDefault().register(this);
    }


    public void executeAllTasks() {
        loggingFacade.log("Executing all LXD tasks...");
        finishedContainerTasks = 0;
        finishedNetworkTasks = 0;
        executeAllTasksAsync();
    }

    private void executeAllTasksAsync() {

        if (finishedNetworkTasks < networkTaskList.size()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loggingFacade.log("Executing network tasks...");
            executeAllNetworkTasks();
            return;
        }

        //TODO: wait until all networks are created

        if (finishedContainerTasks < containerTaskList.size()) {
            /*try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            loggingFacade.log("Executing container tasks...");
            executeAllContainerTasks();
            return;
        }


        loggingFacade.log("Executed all LXD tasks.");
        EventBus.getDefault().post(new AllTasksExecutedEvent());
        EventBus.getDefault().unregister(this);
    }

    private void executeAllNetworkTasks() {
        for(NetworkTask task : networkTaskList) {
            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            switch (task.getTaskType()) {
                case CREATE_NETWORK:
                    createNetwork(task);
                    break;

                case REMOVE_NETWORK:
                    break;

                default:
                    break;
            }
        }

        //networkTaskList.clear(); // comment out ONLY for debugging
    }

    private void executeAllContainerTasks() {
        for(BaseTask task : containerTaskList) {
            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            switch (task.getTaskType()) {
                case CREATE_CONTAINER:
                    createContainer(task);
                    break;

                case RENAME_CONTAINER:
                    break;

                default:
                    break;
            }
        }

        //containerTaskList.clear(); // comment out ONLY for debugging
    }

    private void createContainer(BaseTask task) {
        CreateContainerTask createContainerTask = (CreateContainerTask) task;
        String containerName = createContainerTask.getContainerName();
        ContainerEntity containerEntity = GlobalInventory.getInstance().containers.get(containerName);

        loggingFacade.log("Creating container: " + containerName);
        ContainerController containerController = new ContainerController();
        containerController.createContainer(containerEntity);

    }

    private void createNetwork(BaseTask task) {
        CreateNetworkTask createNetworkTask = (CreateNetworkTask) task;
        String networkName = createNetworkTask.getNetworkName();

        loggingFacade.log("Creating network: " + networkName);
        NetworkController networkController = new NetworkController();
        networkController.createNetwork(networkName);

    }

    @Subscribe
    public void onContainerCreated(ContainerCreatedEvent event) {
        finishedContainerTasks++;
        if (finishedContainerTasks == containerTaskList.size()) {
            executeAllTasksAsync();
        }


        if (event.isSuccessful()) {
            /*loggingFacade.log("created container");
            loggingFacade.log(event.getContainerName());
            loggingFacade.log(event.getMessage());*/
        } else {
            EventBus.getDefault().unregister(this);
            /*loggingFacade.log("error creating container");
            loggingFacade.log(event.getContainerName());
            loggingFacade.log(event.getMessage());*/
        }
    }

    @Subscribe
    public void onNetworkCreated(NetworkCreatedEvent event) {
        finishedNetworkTasks++;
        if (finishedNetworkTasks == networkTaskList.size()) {
            executeAllTasksAsync();
        }

        if (event.isSuccessful()) {
            /*loggingFacade.log("created network");
            loggingFacade.log(event.getNetworkName());
            loggingFacade.log(event.getMessage());*/
        } else {
            EventBus.getDefault().unregister(this);
            /*loggingFacade.log("error creating network");
            loggingFacade.log(event.getNetworkName());
            loggingFacade.log(event.getMessage());*/
        }
    }

}
