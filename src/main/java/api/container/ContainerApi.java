package api.container;

import api.container.element.ContainerActionElement;
import api.container.element.CreateContainerElement;
import api.container.response.ContainerListResponse;
import api.container.response.ContainerNamesResponse;
import api.container.response.ContainerResponse;
import api.operation.response.OperationResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ContainerApi {

    @GET("containers?recursion=1")
    Call<ContainerListResponse> listContainers();

    @GET("containers")
    Call<ContainerNamesResponse> listContainerNames();

    @POST("containers")
    Call<OperationResponse> createContainer(@Body CreateContainerElement element);

    @DELETE("containers/{name}")
    Call<OperationResponse> deleteContainer(@Path("name") String containerName);


    @GET("containers/{name}")
    Call<ContainerResponse> getContainer(@Path("name") String containerName);

    @PUT("containers/{name}/state")
    Call<OperationResponse> changeContainerState(@Path("name") String containerName, @Body ContainerActionElement actionElement);

}
