package com.example.atlantapp.common

import android.content.Context
import com.example.atlantapp.BuildConfig
import com.example.atlantapp.R
import com.example.atlantapp.common.network.TokenAuthenticator
import com.example.atlantapp.common.network.TokenInterceptor
import com.example.atlantapp.common.network.TokenProvider
import com.example.atlantapp.socket.SocketUpdatesManager
import com.example.atlantapp.socket.UpdateHandler
import com.example.atlantapp.socket.UpdatesManager
import com.example.atlantapp.socket.model.Options
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object CommonModule {

    private const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 60L
    private const val DEFAULT_READ_TIMEOUT_SECONDS = 60L

    fun create() = module {
        single {
            GsonBuilder()
                .apply { if (BuildConfig.DEBUG) setPrettyPrinting() }
                .create()
        }

        single { AppCoroutineScope() }

        single {
            val context = get<Context>()
            val name = "${context.packageName}.prefs"
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        single { TokenProvider(get()) }

        single {
            val client = createOkHttpClient()
                .apply { if (BuildConfig.DEBUG) addInterceptor(createLoggingInterceptor()) }
                .build()

            Retrofit.Builder()
                .baseUrl(get<Context>().getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
                .client(client)
                .build()
        }

        single {
            val updateHandlers = get<List<UpdateHandler>>(named<UpdateHandler>())
            val socketUrl = get<Context>().getString(R.string.ws_url)
            val apiKey = get<Context>().getString(R.string.ws_api_key)
            val client = OkHttpClient.Builder().build()
            val options = Options(url = socketUrl, apiKey = apiKey)
            SocketUpdatesManager(get(), options, client, get(), updateHandlers)
        } bind UpdatesManager::class
    }

    private fun Scope.createOkHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(TokenInterceptor(get()))
            .readTimeout(DEFAULT_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)

    private fun Scope.createLoggingInterceptor(): HttpLoggingInterceptor {
        val gson = get<Gson>()
        val okHttpLogTag = "OkHttp"

        val okHttpLogger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (!message.startsWith('{') && !message.startsWith('[')) {
                    Timber.tag(okHttpLogTag).d(message)
                    return
                }

                try {
                    val json = JsonParser().parse(message)
                    Timber.tag(okHttpLogTag).d(gson.toJson(json))
                } catch (e: Throwable) {
                    Timber.tag(okHttpLogTag).d(message)
                }
            }
        }
        return HttpLoggingInterceptor(okHttpLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}