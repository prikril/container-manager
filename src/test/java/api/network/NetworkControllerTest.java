package api.network;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NetworkControllerTest {

    /**
     * Countdown latch
     * https://stackoverflow.com/questions/631598/how-to-use-junit-to-test-asynchronous-processes
     */
    private CountDownLatch lock = new CountDownLatch(1);


    @Test
    public void testFetchNetworks() {
        NetworkController networkController = new NetworkController();
        networkController.fetchNetworks();

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }

    @Test
    public void testFetchNetwork() {
        NetworkController networkController = new NetworkController();
        networkController.fetchNetwork("lxdbr0");

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }

    @Test
    public void testCreateNetwork() {
        NetworkController networkController = new NetworkController();
        networkController.createNetwork("test-network");

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }

    @Test
    public void testDeleteNetwork() {
        NetworkController networkController = new NetworkController();
        networkController.deleteNetwork("test-network");

        try {
            lock.await(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waited for callback!");
    }



}
