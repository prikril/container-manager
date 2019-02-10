package api.container;

import application.entity.container.ContainerEntity;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ContainerControllerTest {

    /**
     * Countdown latch
     * https://stackoverflow.com/questions/631598/how-to-use-junit-to-test-asynchronous-processes
     */
    private CountDownLatch lock = new CountDownLatch(1);


    @Test
    public void testCreateContainer() {
        ContainerEntity containerEntity = new ContainerEntity("test-container");
        ContainerController containerController = new ContainerController();
        containerController.createContainer(containerEntity);

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }

    @Test
    public void testFetchContainers() {
        ContainerController containerController = new ContainerController();
        containerController.fetchContainers();

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }

    @Test
    public void testFetchContainer() {
        ContainerController containerController = new ContainerController();
        containerController.fetchContainer("test-container");

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }


    @Test
    public void testDeleteContainer() {
        ContainerController containerController = new ContainerController();
        containerController.deleteContainer("test-container");

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }


}
