package com.sovcom.authCode.app.hilt.network

import com.sovcom.authCode.BuildConfig
import com.sovcom.data.net.common.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun isDebugEnvironment(): Boolean = BuildConfig.DEBUG

    @Singleton
    @Provides
    fun provideJson() = Network.appJson

    @Provides
    @Singleton
    @BaseUrl
    fun getBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideJsonFactory(json: Json): Converter.Factory = Network.getJsonFactory(json)

    @Provides
    @Singleton
    fun provideOkhttpCache() = Network.okHttpCache

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideLoggingInterceptor(
        isDebugEnvironment: Boolean
    ): Interceptor? = Network.getLoggingInterceptor(isDebugEnvironment)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        @LoggingInterceptor loggingInterceptor: Interceptor?,
    ): OkHttpClient = Network.getHttpClient(
        interceptors = listOfNotNull(
            loggingInterceptor,
        ),
        authenticator = null,
        cache = cache,
    )

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        @BaseUrl
        baseUrl: String,
        converter: Converter.Factory,
    ): Retrofit = Network.getRetrofit(
        client = client,
        baseUrl = baseUrl,
        converterFactory = converter,
    )
}