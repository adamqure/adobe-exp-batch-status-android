package com.example.adobe_exp_batch_status_android

import com.example.adobe_exp_batch_status_android.ParameterClasses.AuthToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.gson.GsonConverterFactory.*
import retrofit2.http.*

object API {
    const val AUTH_URL = "https://ims-na1.adobelogin.com/ims/"
    const val CATALOG_URL = "https://platform.adobe.io/data/foundation/catalog/"

    interface AuthService {
        //Exchange JWT for Auth Token
        @FormUrlEncoded
        @POST("exchange/jwt/")
        fun getAuthToken(
            @Field("client_id") id: String?, @Field(
                "client_secret"
            ) secret: String?, @Field("jwt_token") jwt: String?
        ): Call<AuthToken?>?
    }

    interface DatasetsService {
        @GET("dataSets")
        fun retrieveDatasets(@HeaderMap headers: Map<String, String>, @Query("orderBy") order: String, @Query("start") start: Int, @Query("limit") limit: Int): Call<JsonElement>

        @GET("batches/")
        fun retrieveBatches(@HeaderMap headers: Map<String, String>, @Query("DATASET_ID") datasetId: String?, @Query("orderBy") order: String, @Query("start") start: Int, @Query("limit") limit: Int): Call<JsonElement>

        @GET("batches/{BATCH_ID}")
        fun getBatchDetails(@HeaderMap headers: Map<String, String>, @Path("BATCH_ID") batchId: String?): Call<JsonElement>
    }

    fun getAuthService(): AuthService {
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthService::class.java)
    }

    fun getDatasetsService(): DatasetsService {
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(CATALOG_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(DatasetsService::class.java)
    }
}