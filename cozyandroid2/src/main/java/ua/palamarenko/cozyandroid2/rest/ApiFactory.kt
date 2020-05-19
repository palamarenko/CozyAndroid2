package ua.palamarenko.cozyandroid2.rest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


class Rest<T>(private val apiFactory: ApiFactory<T>) {


    val api get() = apiFactory.apiService

    fun restartRest() {
        this.apiFactory.resentClient()
    }

}


class MockApiFactory<T : Any>(mockClass: T) : ApiFactory<T>("", apiGet = mockClass::class.java) {
    override val apiService = mockClass
}


open class ApiFactory<T>(private val BASE_URL: String, open val apiGet: Class<out T>) {

    private val connectTimeOut = 30L
    private val writeTimeOut = 30L
    private val timeOut = 30L

    private var client: OkHttpClient? = null

    open val apiService get() = getRest(this.BASE_URL).create(apiGet) as T


    fun getClient(): OkHttpClient {

        if (this.client == null) {
            val builder = OkHttpClient.Builder()
            this.addInterceptors(builder)
            this.addDebagInterseptor(builder)
            builder.connectTimeout(connectTimeOut, TimeUnit.SECONDS)
            builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS)
            builder.readTimeout(timeOut, TimeUnit.SECONDS)
            builder.hostnameVerifier { hostname, session -> true }


            this.client = builder.build()
        }

        return this.client!!
    }

    fun resentClient() {
        this.client = null
    }


    open fun addInterceptors(builder: OkHttpClient.Builder) {

        builder.addInterceptor { chain ->
            val original = chain.request()
            val contentType = original.headers().get("Content-Type")
            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", contentType ?: "application/json")
                .header("Cache-Control", "no-cache")
                .header("Language", Locale.getDefault().language)
                .method(original.method(), original.body())
            val request = requestBuilder
                .cacheControl(okhttp3.CacheControl.Builder().noCache().build())
                .build()
            val response = chain.proceed(request)

            if(response.code() == 401){

            }



           return@addInterceptor response
        }
    }


    fun addDebagInterseptor(builder: OkHttpClient.Builder) {
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }


    open fun getRest(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(this.getClient())
            .build()
    }
}