package api.base;

import application.GlobalConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseController {

    private static final String LXD_VERSION = "1.0";
    private static final int DEFAULT_TIMEOUT_IN_SEC = 5;

    public Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(DEFAULT_TIMEOUT_IN_SEC))
                .build();

        return retrofit;
    }

    public Retrofit getRetrofit(int timeoutInSeconds) {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(timeoutInSeconds))
                .build();

        return retrofit;
    }

    private String getBaseUrl() {
        return GlobalConfiguration.getInstance().getServerUrl() + "/" + LXD_VERSION + "/";
    }


}
