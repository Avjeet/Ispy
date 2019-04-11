package com.loremipsum.eyespy.client

import com.loremipsum.eyespy.REST_SERVER_URL
import com.loremipsum.eyespy.model.Dataset
import com.loremipsum.eyespy.model.Phrases
import com.loremipsum.eyespy.model.Room
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class RestApiClient {

    interface APIService {
        @GET("/")
        fun getRoomData(): Call<Room>

        @GET("/get/phrases")
        fun getPhrases(): Call<ArrayList<Phrases>>

        @POST("/add/labels")
        fun putDataSetItem(@Body dataset: Dataset)
    }

    companion object {
        val restClientService by lazy {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(REST_SERVER_URL)
                    .build()
                    .create(APIService::class.java)
        }
    }

}