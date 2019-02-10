package api.container;

import api.base.BaseController;
import api.container.element.ContainerActionElement;
import api.container.element.ContainerElement;
import api.container.element.CreateContainerElement;
import api.container.response.ContainerListResponse;
import api.container.response.ContainerNamesResponse;
import api.container.response.ContainerResponse;
import api.operation.OperationCallback;
import api.operation.OperationController;
import api.operation.OperationElement;
import api.operation.response.OperationResponse;
import application.entity.container.ContainerEntity;
import application.entity.container.ContainerStatusHelper;
import application.event.container.ContainerCreatedEvent;
import application.event.container.ContainerDeletedEvent;
import application.event.container.ContainerNamesEvent;
import application.event.container.ContainerStoppedEvent;
import application.logging.LoggingFacade;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

public class ContainerController extends BaseController {
    // idea from http://www.vogella.com/tutorials/Retrofit/article.html
    // https://futurestud.io/tutorials/retrofit-synchronous-and-asynchronous-requests

    private LoggingFacade logger = LoggingFacade.getInstance();

    public ContainerController() {
        //EventBus.getDefault().register(this);
    }

    public void fetchContainers() {

        Retrofit retrofit = getRetrofit();

        ContainerApi containerApi = retrofit.create(ContainerApi.class);
        Call<ContainerListResponse> call = containerApi.listContainers();
        call.enqueue(new Callback<ContainerListResponse>() {
            @Override
            public void onResponse(Call<ContainerListResponse> call, Response<ContainerListResponse> response) {
                if(response.isSuccessful()) {
                    ContainerListResponse containerListResponse = response.body();
                    System.out.println(containerListResponse);
                    logger.log(containerListResponse.toString());
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContainerListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchContainerNames() {

        Retrofit retrofit = getRetrofit();

        ContainerApi containerApi = retrofit.create(ContainerApi.class);
        Call<ContainerNamesResponse> call = containerApi.listContainerNames();
        call.enqueue(new Callback<ContainerNamesResponse>() {
            @Override
            public void onResponse(Call<ContainerNamesResponse> call, Response<ContainerNamesResponse> response) {
                if(response.isSuccessful()) {
                    ContainerNamesResponse containerNamesResponse = response.body();
                    System.out.println(containerNamesResponse);
                    logger.log(containerNamesResponse.toString());
                    EventBus.getDefault().post(new ContainerNamesEvent( containerNamesResponse.getNames() ));
                } else {
                    logger.log("Error during getting container names.");
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContainerNamesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchContainer(String containerName) {

        Retrofit retrofit = getRetrofit();

        ContainerApi containerApi = retrofit.create(ContainerApi.class);
        Call<ContainerResponse> call = containerApi.getContainer(containerName);
        call.enqueue(new Callback<ContainerResponse>() {
            @Override
            public void onResponse(Call<ContainerResponse> call, Response<ContainerResponse> response) {
                if(response.isSuccessful()) {
                    ContainerResponse containerResponse = response.body();
                    System.out.println(containerResponse);
                    logger.log(containerResponse.toString());
                    ContainerElement containerElement = containerResponse.getMetadata();
                    ContainerEntity containerEntity = new ContainerEntity(containerElement.getName());
                    containerEntity.setStatus(ContainerStatusHelper.getStatusFromString(containerElement.getStatus()));
                    containerEntity.setDescription(containerElement.getConfig().get("image.description"));
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ContainerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createContainer(ContainerEntity containerEntity) {

        Retrofit retrofit = getRetrofit();

        ContainerApi containerApi = retrofit.create(ContainerApi.class);

        CreateContainerElement element = new CreateContainerElement(containerEntity);

        Call<OperationResponse> call = containerApi.createContainer(element);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    String operationUrl = operationResponse.getOperation();
                    String operationId = operationUrl.substring( operationUrl.lastIndexOf("/") + 1 );
                    new OperationController().awaitOperation(operationId, new OperationCallback() {
                        @Override
                        public void onResponse() {
                            logger.log("Created container: " + containerEntity.getName());
                            // TODO: check error of operation
                            EventBus.getDefault().post(new ContainerCreatedEvent(containerEntity.getName(), true, operationResponse.toString()));
                        }

                        @Override
                        public void onFailure(OperationElement operationElement) {
                            logger.log("Error during creating container: " + containerEntity.getName());
                        }
                    });

                } else {
                    logger.log("Error during creating container: " + containerEntity.getName());
                    try {
                        System.out.println(response.errorBody().string());
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

    public void deleteContainer(String containerName) {

        Retrofit retrofit = getRetrofit();

        ContainerApi containerApi = retrofit.create(ContainerApi.class);

        Call<OperationResponse> call = containerApi.deleteContainer(containerName);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    //logger.log(operationResponse.toString());
                    // TODO: check error of operation
                    String operationUrl = operationResponse.getOperation();
                    String operationId = operationUrl.substring( operationUrl.lastIndexOf("/") + 1 );
                    new OperationController().awaitOperation(operationId, new OperationCallback() {
                        @Override
                        public void onResponse() {
                            logger.log("Deleted container: " + containerName);
                            EventBus.getDefault().post(new ContainerDeletedEvent(containerName, true, operationResponse.toString()));
                        }

                        @Override
                        public void onFailure(OperationElement operationElement) {
                            logger.log("Error during deleting container: " + containerName + "\nError: " + operationElement.getErr());
                            System.out.println("Error during deleting container: " + containerName);
                        }
                    });


                } else {
                    try {
                        logger.log("Error during deleting container: " + containerName);
                        System.out.println("Error during deleting container: " + containerName);
                        System.out.println(response.errorBody().string());
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

    public void startContainer(String containerName) {

        ContainerApi containerApi = getRetrofit().create(ContainerApi.class);

        ContainerActionElement actionElement = new ContainerActionElement("start", 30, false, false);
        logger.log("Starting container: " + containerName);
        Call<OperationResponse> call = containerApi.changeContainerState(containerName, actionElement);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    //logger.log(operationResponse.toString());
                    String operationUrl = operationResponse.getOperation();
                    String operationId = operationUrl.substring( operationUrl.lastIndexOf("/") + 1 );
                    new OperationController().awaitOperation(operationId, new OperationCallback() {
                                @Override
                                public void onResponse() {
                                    logger.log("Started container: " + containerName);
                                }

                                @Override
                                public void onFailure(OperationElement operationElement) {
                                    logger.log("Error during starting container: " + containerName + "\nError: " + operationElement.getErr());
                                }
                    });

                } else {
                    logger.log("Error during starting container: " + containerName);
                    try {
                        System.out.println(response.errorBody().string());
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

    public void stopContainer(String containerName) {

        ContainerApi containerApi = getRetrofit().create(ContainerApi.class);

        ContainerActionElement actionElement = new ContainerActionElement("stop", 30, true, false);

        Call<OperationResponse> call = containerApi.changeContainerState(containerName, actionElement);
        call.enqueue(new Callback<OperationResponse>() {
            @Override
            public void onResponse(Call<OperationResponse> call, Response<OperationResponse> response) {
                if(response.isSuccessful()) {
                    OperationResponse operationResponse = response.body();
                    System.out.println(operationResponse);
                    logger.log("Stopped container: " + containerName);
                    // TODO: check error of operation
                    EventBus.getDefault().post(new ContainerStoppedEvent(containerName, true, operationResponse.toString()));
                } else {
                    logger.log("Error during stopping container: " + containerName);
                    try {
                        System.out.println(response.errorBody().string());
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
