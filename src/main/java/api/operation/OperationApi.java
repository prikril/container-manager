package api.operation;

import api.operation.response.OperationListResponse;
import api.operation.response.OperationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OperationApi {

    @GET("operations")
    Call<OperationListResponse> listOperations();

    @GET("operations/{uid}/wait")
    Call<OperationResponse> waitForOperation(@Path("uid") String uid);

}
