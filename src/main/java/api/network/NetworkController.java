package api.network;

import api.base.BaseController;
import api.network.element.CreateNetworkElement;
import api.network.element.NetworkElement;
import api.network.response.NetworkListResponse;
import api.network.response.NetworkNamesResponse;
import api.network.response.NetworkResponse;
import api.operation.response.OperationResponse;
import application.entity.network.NetworkEntity;
import application.entity.network.NetworkType;
import application.event.network.NetworkCreatedEvent;
import application.event.network.NetworkDeletedEvent;
import application.event.network.NetworkNamesEvent;
import application.logging.LoggingFacade;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class NetworkController extends BaseController {
    // idea from http://www.vogella.com/tutorials/Retrofit/article.html
    // https://futurestud.io/tutorials/retrofit-synchronous-and-asynchronous-requests

    private LoggingFacade logger = LoggingFacade.getInstance();


    public void fetchNetworks() {

        NetworkApi networkApi = getRetrofit().create(NetworkApi.class);
        Call<NetworkListResponse> call = networkApi.listNetworks();
        call.enqueue(new Callback<NetworkListResponse>() {
            @Override
            public void onResponse(Call<NetworkListResponse> call, Response<NetworkListResponse> response) {
                if(response.isSuccessful()) {
                    NetworkListResponse networkListResponse = response.body();
                    System.out.println(networkListResponse);
                    logger.log(networkListResponse.toString());
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NetworkListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchNetworkNames() {

        NetworkApi networkApi = getRetrofit().create(NetworkApi.class);
        Call<NetworkNamesResponse> call = networkApi.listNetworkNames();
        call.enqueue(new Callback<NetworkNamesResponse>() {
            @Override
            public void onResponse(Call<NetworkNamesResponse> call, Response<NetworkNamesResponse> response) {
                if(response.isSuccessful()) {
                    NetworkNamesResponse networkNamesResponse = response.body();
                    System.out.println(networkNamesResponse);
                    logger.log(networkNamesResponse.toString());
                    EventBus.getDefault().post(new NetworkNamesEvent( networkNamesResponse.getNames() ));
                } else {
                    logger.log("Error during getting network names.");
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NetworkNamesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchNetwork(String networkName) {

        NetworkApi networkApi = getRetrofit().create(NetworkApi.class);
        Call<NetworkResponse> call = networkApi.getNetwork(networkName);
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(Call<NetworkResponse> call, Response<NetworkResponse> response) {
                if(response.isSuccessful()) {
                    NetworkResponse networkResponse = response.body();
                    System.out.println(networkResponse);
                    logger.log(networkResponse.toString());
                    NetworkElement networkElement = networkResponse.getMetadata();
                    NetworkEntity networkEntity = new NetworkEntity();
                    networkEntity.setName(networkElement.getName());
                    networkEntity.setType(NetworkType.UNKNOWN); //TODO: implement helper
                    networkEntity.setIpAddress(networkElement.getConfig().get("ipv4.address"));

                    EventBus.getDefault().post(new NetworkCreatedEvent(networkName, true, networkResponse.toString()));
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NetworkResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createNetwork(String networkName) {

        NetworkApi networkApi = getRetrofit().create(NetworkApi.class);

        CreateNetworkElement element = new CreateNetworkElement(networkName);

        Call<OperationResponse> call = networkApi.createNetwork(element);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    logger.log("Created network: " + networkName);
                    // TODO: check error of operation
                    EventBus.getDefault().post(new NetworkCreatedEvent(networkName, true, operationResponse.toString()));
                } else {
                    logger.log("Error during creating network: " + networkName);
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteNetwork(String networkName) {

        NetworkApi networkApi = getRetrofit().create(NetworkApi.class);

        Call<OperationResponse> call = networkApi.deleteNetwork(networkName);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    //logger.log(operationResponse.toString());
                    // TODO: check error of operation
                    logger.log("Deleted network: " + networkName);
                    EventBus.getDefault().post(new NetworkDeletedEvent(networkName, true, operationResponse.toString()));
                } else {
                    try {
                        logger.log("Error during deleting network: " + networkName);
                        System.out.println("Error during deleting network: " + networkName);
                        System.out.println(response.errorBody().string());
                        EventBus.getDefault().post(new NetworkDeletedEvent(networkName, false, response.errorBody().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OperationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
