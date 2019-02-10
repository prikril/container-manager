package application.task;

import api.container.ContainerController;
import api.network.NetworkController;
import application.event.container.ContainerDeletedEvent;
import application.event.container.ContainerNamesEvent;
import application.event.container.ContainerStoppedEvent;
import application.event.network.NetworkDeletedEvent;
import application.event.network.NetworkNamesEvent;
import application.event.task.CleanTaskFinishedEvent;
import application.logging.LoggingFacade;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LxdTaskExecutorServiceHelper {

    private LoggingFacade loggingFacade = LoggingFacade.getInstance();

    private List<String> containerNames;
    private List<String> networkNames;

    private List<String> stoppedContainerNames = new ArrayList<>();

    private List<String> deletedContainerNames = new ArrayList<>();
    private List<String> deletedNetworkNames = new ArrayList<>();

    public LxdTaskExecutorServiceHelper() {
        EventBus.getDefault().register(this);
    }


    public void cleanHost() {
        loggingFacade.log("Cleanup host...");
        cleanHostAsync();
    }

    private void cleanHostAsync() {

        if (containerNames == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fetchAllContainerNames();
            return;
        }

        if (stoppedContainerNames.size() < containerNames.size()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stopAllContainers();
            return;
        }

        if (deletedContainerNames.size() < containerNames.size()) {
            deleteAllContainers();
            return;
        }

        if (networkNames == null) {
            fetchAllNetworkNames();
            return;
        }

        if (deletedNetworkNames.size() < networkNames.size()) {
            deleteAllNetworks();
            return;
        }

        loggingFacade.log("Host cleanup finished!");
        EventBus.getDefault().post(new CleanTaskFinishedEvent());
        EventBus.getDefault().unregister(this);
    }

    private void fetchAllContainerNames() {
        loggingFacade.log("Fetch all containers.");
        ContainerController containerController = new ContainerController();
        containerController.fetchContainerNames();
    }

    @Subscribe
    public void onContainerNames(ContainerNamesEvent event) {
        containerNames = new ArrayList<>(event.getNames());

        cleanHostAsync();
    }

    private void fetchAllNetworkNames() {
        loggingFacade.log("Fetch all networks.");
        NetworkController networkController = new NetworkController();
        networkController.fetchNetworkNames();
    }

    @Subscribe
    public void onNetworkNames(NetworkNamesEvent event) {
        networkNames = new ArrayList<>(event.getNames());

        cleanHostAsync();
    }


    private void deleteAllNetworks() {
        for(String name : networkNames) {
            if (name.equals("lxdbr0")) {
                deletedNetworkNames.add("lxdbr0");
                continue; //default bridge should not be deleted
            }

            if (name.equals("lo")) {
                deletedNetworkNames.add("lo");
                continue; //skip localhost
            }

            if (name.equals("enp0s3")) {
                deletedNetworkNames.add("enp0s3");
                continue; //skip "eth0"
            }

            deleteNetwork(name);
        }

        // if no networks from lxd (only system networks) available, program can continue
        if (deletedNetworkNames.size() == networkNames.size()) {
            cleanHostAsync();
        }

    }

    private void deleteAllContainers() {
        for(String name : containerNames) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deleteContainer(name);
        }

    }

    private void stopAllContainers() {
        for(String name : containerNames) {
            stopContainer(name);
        }

    }

    private void stopContainer(String name) {
        loggingFacade.log("Stop container: " + name);
        ContainerController containerController = new ContainerController();
        containerController.stopContainer(name);
    }

    private void deleteContainer(String name) {
        loggingFacade.log("Delete container: " + name);
        ContainerController containerController = new ContainerController();
        containerController.deleteContainer(name);
    }

    private void deleteNetwork(String name) {
        loggingFacade.log("Delete network: " + name);
        NetworkController networkController = new NetworkController();
        networkController.deleteNetwork(name);
    }

    @Subscribe
    public void onContainerStopped(ContainerStoppedEvent event) {
        stoppedContainerNames.add( event.getContainerName() );

        if (stoppedContainerNames.size() == containerNames.size()) {
            cleanHostAsync();
        }

    }

    @Subscribe
    public void onContainerDeleted(ContainerDeletedEvent event) {
        deletedContainerNames.add( event.getContainerName() );

        if (deletedContainerNames.size() == containerNames.size()) {
            cleanHostAsync();
        }

        if (event.isSuccessful()) {

        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onNetworkDeleted(NetworkDeletedEvent event) {

        if (event.isSuccessful()) {
            deletedNetworkNames.add( event.getNetworkName() );

            if (deletedNetworkNames.size() == networkNames.size()) {
                cleanHostAsync();
            }

        } else {
            EventBus.getDefault().unregister(this);
        }
    }

}
