package com.aci.constructionequipment.networking


import com.aci.constructionequipment.networking.service_request_list.ServiceRequest
import com.aci.constructionequipment.networking.technician_list.TechnicianList
import com.acibd.serviceautonomous.networking.all_data.AllData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


object ApiClient {

    private val API_BASE_URL = "http://116.68.205.71:9204/construction-equipment/api/"

    private var servicesApiInterface: ServiceApiInterface? = null

    @Synchronized
    fun build(): ServiceApiInterface? {

        if (servicesApiInterface != null)
            return servicesApiInterface

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build())
            .build()
        servicesApiInterface = retrofit.create(
            ServiceApiInterface::class.java
        )

        return servicesApiInterface as ServiceApiInterface
    }


    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    interface ServiceApiInterface {


        @POST("auth/login")
        fun login(
            @Body body: LoginPostBody
        ): Call<LoginResponse>

        @POST("auth/login")
        fun loginAsTechnician(
            @Body body: LoginTechPostBody
        ): Call<LoginResponse>

        @POST("service-request")
        fun customerRequest(
            @Header("Authorization") token: String,
            @Body body: CustomerRequestCreateBody
        ): Call<CustomerRequestResponse>


        @GET("get-all")
        fun allData(
            @Header("Authorization") token: String
        ): Call<AllData>

        @GET("user-info")
        fun userInfo(
            @Header("Authorization") token: String
        ): Call<UserInfo>


        @GET("service-request")
        fun serviceRequest(
            @Header("Authorization") token: String
        ): Call<ServiceRequest>


        @GET("get-technician-of-engineer")
        fun technicianList(
            @Header("Authorization") token: String
        ): Call<TechnicianList>

        @POST("service-request-engineer-update")
        fun engineerUpdate(
            @Header("Authorization") token: String,
            @Body body: EnginnerUpdate
        ): Call<CustomerRequestResponse>

        @POST("service-request-technitian-update")
        fun technicianUpdate(
            @Header("Authorization") token: String,
            @Body body: TechnicianUpdate
        ): Call<CustomerRequestResponse>

        @Headers("Content-Type: application/json")
        @GET
        fun apkVersion(@Url url: String): Call<ApkVersion>

        @POST("auth/set-firebase-token")
        fun deviceToken(
            @Header("Authorization") token: String,
            @Body deviceToken: DeviceToken
        ): Call<CommonResponse>
    }
}