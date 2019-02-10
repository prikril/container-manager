package api.container;

import api.container.response.ContainerListResponse;
import api.container.response.ContainerResponse;
import org.junit.jupiter.api.Test;


public class ContainerApiTest {

    @Test
    public void testListContainers() {
        ContainerController containerController = new ContainerController();

        ContainerApi service = containerController.getRetrofit().create(ContainerApi.class);
        try {
            ContainerListResponse containerListResponse;
            containerListResponse = service.listContainers().execute().body();
            System.out.println(containerListResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testGetContainer() {
        ContainerController containerController = new ContainerController();

        ContainerApi service = containerController.getRetrofit().create(ContainerApi.class);
        try {
            ContainerResponse containerResponse;
            containerResponse = service.getContainer("container1").execute().body();
            System.out.println(containerResponse.getMetadata());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
