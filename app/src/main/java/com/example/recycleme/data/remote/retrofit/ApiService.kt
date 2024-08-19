package com.example.recycleme.data.remote.retrofit

import com.example.recycleme.data.SignupRequest
import com.example.recycleme.data.remote.response.HistoryResponse
import com.example.recycleme.data.remote.response.NewsItem
import com.example.recycleme.data.remote.response.PredictionResponse
import com.example.recycleme.data.remote.response.LoginResponse
import com.example.recycleme.data.remote.response.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("/news")
    fun getTopHeadlines(): Call<List<NewsItem>>

    @Multipart
    @POST("model/predict")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("model/history")
    fun getHistory(): Call<List<HistoryResponse>>

    @POST("users/signup")
    suspend fun register(
        @Body requestBody: SignupRequest
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("users/signin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}