package com.example.adobe_exp_batch_status_android;

import com.example.adobe_exp_batch_status_android.ParameterClasses.AuthToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Abstract API.API class to be used with the API.API calls
 */
public abstract class API {

    static final String AUTH_URL = "https://ims-na1.adobelogin.com/ims/";
    static final String CATALOG_URL = "https://platform.adobe.io/data/foundation/catalog/";

    public interface AuthService {
        //Exchange JWT for Auth Token
        @FormUrlEncoded
        @POST("exchange/jwt/")
        Call<AuthToken> getAuthToken(@Field("client_id") String id, @Field("client_secret") String secret, @Field("jwt_token") String jwt);
    }

    public interface DatasetsService {
        @GET("dataSets")
        Call<JsonElement> retrieveDatasets(@HeaderMap Map<String, String> headers);

        @GET("batches?{DATASET_ID}")
        Call<JsonElement> retrieveBatches(@HeaderMap Map<String, String> headers, @Path("DATASET_ID") String datasetId);

        @GET("batches/{BATCH_ID}")
        Call<JsonElement> getBatchDetails(@HeaderMap Map<String, String> headers,
                                          @Path("BATCH_ID") String batchId);
    }

    public static AuthService getAuthService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create()) //assuming JSON
                .build();
        return retrofit.create(AuthService.class);
    }

    public static DatasetsService getDatasetService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CATALOG_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(DatasetsService.class);
    }
}