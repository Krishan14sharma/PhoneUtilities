package kr.ifttutilities.data.remote

import android.content.Context
import io.reactivex.Single
import kr.ifttutilities.BuildConfig
import kr.ifttutilities.uber.model.ProductWrapper
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

/**
 * Created by krishan on 08/03/18.
 */
interface Api {

    @GET("https://api.uber.com/v1.2/products")
    fun getUberProducts(
            @Query("access_token") accessToken: String,
            @Query("latitude") latitude: String,
            @Query("longitude") longitude: String): Single<ProductWrapper>


    companion object {
        private const val ENDPOINT = "https://api.uber.com"
        private const val cacheSize: Long = 10 * 1024 * 1024
        private var api: Api? = null

        fun getAppApi(context: Context): Api {
            return api ?: synchronized(this) {
                return api ?: provideApi(context).also { api = it }
            }
        }

        private fun provideApi(context: Context): Api {
            val retrofit = Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .client(provideOkHttpClient(context, provideHttpLoggingInterceptor()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(Api::class.java)
        }

        private fun provideOkHttpClient(context: Context, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            val cache = Cache(File(context.cacheDir, "http-cache"), cacheSize)
            return OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(httpLoggingInterceptor)
                    .build()
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = if (BuildConfig.LOG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

    }
}