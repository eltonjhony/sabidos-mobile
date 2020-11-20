package com.sabidos.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    fun getRetrofit(authToken: String): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://private-620e9-sabidos1.apiary-mock.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .client(getClient(authToken))
            .build()

    private fun getClient(authToken: String): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            //.authenticator()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer $authToken")
                    .method(original.method, original.body)
                    .build()
                it.proceed(request)
            }

        return builder.build()
    }
}