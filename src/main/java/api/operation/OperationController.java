package api.operation;

import api.base.BaseController;
import api.operation.response.OperationResponse;
import application.logging.LoggingFacade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OperationController extends BaseController {
    // idea from http://www.vogella.com/tutorials/Retrofit/article.html
    // https://futurestud.io/tutorials/retrofit-synchronous-and-asynchronous-requests

    private LoggingFacade logger = LoggingFacade.getInstance();

    public OperationController() {
        //EventBus.getDefault().register(this);
    }


    public void awaitOperation(String uid, OperationCallback callback) {

        Retrofit retrofit = getRetrofit(30);

        OperationApi operationApi = retrofit.create(OperationApi.class);
        Call<OperationResponse> call = operationApi.waitForOperation(uid);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    //logger.log(operationResponse.toString());
                    OperationElement operationElement = operationResponse.getMetadata();
                    int statusCode = operationElement.getStatusCode();
                    if (statusCode == 200) {
                        callback.onResponse();
                    } else {
                        callback.onFailure(operationElement);
                    }
                } else {
                    OperationElement fakeOperationElement = new OperationElement();
                    fakeOperationElement.setErr("unknown operation error");
                    callback.onFailure(fakeOperationElement);
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



}
