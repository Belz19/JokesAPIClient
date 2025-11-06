package ph.edu.comteq.jokesapiclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //base Url
    private const val BASE_URL = "https://programmingwizards.tech/"

    //logging interceptor - helps us see what's happening
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //OkHttp Client
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor).build()

    //Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //API Service
    val jokeAPI: JokesApiService = retrofit.create(JokesApiService::class.java)
}