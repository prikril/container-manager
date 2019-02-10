package api.network;

import api.base.UnsafeOkHttpClient;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkApiTest {

    @Test
    public void testListNetworks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://localhost:8443/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(5)) // TODO: remove dirty fix for missing certificate
                // https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
                .build();

        NetworkApi service = retrofit.create(NetworkApi.class);
        try {
            service.listNetworks().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testGetNetwork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://localhost:8443/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(5)) // TODO: remove dirty fix for missing certificate
                // https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
                .build();

        NetworkApi service = retrofit.create(NetworkApi.class);
        try {
            service.getNetwork("lxdbr0").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
