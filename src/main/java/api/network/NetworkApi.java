package api.network;

import api.network.element.CreateNetworkElement;
import api.network.response.NetworkListResponse;
import api.network.response.NetworkNamesResponse;
import api.network.response.NetworkResponse;
import api.operation.response.OperationResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface NetworkApi {

    @GET("networks?recursion=1")
    Call<NetworkListResponse> listNetworks();

    @GET("networks")
    Call<NetworkNamesResponse> listNetworkNames();

    @POST("networks")
    Call<OperationResponse> createNetwork(@Body CreateNetworkElement element);

    @GET("networks/{name}")
    Call<NetworkResponse> getNetwork(@Path("name") String networkName);

    @DELETE("networks/{name}")
    Call<OperationResponse> deleteNetwork(@Path("name") String networkName);

}
