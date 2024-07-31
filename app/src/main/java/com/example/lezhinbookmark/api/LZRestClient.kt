package com.example.lezhinbookmark.api

import com.example.lezhinbookmark.common.LZConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LZRestClient<T> {
    private var service: T? = null
    private var timeOut: Long = 20L

    fun getClient(type: Class<out T>?): T {
        if (service == null) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder()
                    builder.addHeader("Authorization", "KakaoAK ${LZConstants.KAKAO_API_KEY}")
                    chain.proceed(builder.build())
                }
                .addInterceptor { chain ->
                    chain.proceed(chain.request())
                }
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)

            val interceptorLogging = HttpLoggingInterceptor()
            interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClient.addInterceptor(interceptorLogging)

            val mBuilder = Retrofit.Builder()
            mBuilder.client(okHttpClient.build())
            mBuilder.addConverterFactory(GsonConverterFactory.create())
            mBuilder.baseUrl("https://dapi.kakao.com/v2/search/")
            val client = mBuilder.build()
            service = client.create(type)
        }
        return service!!
    }
}