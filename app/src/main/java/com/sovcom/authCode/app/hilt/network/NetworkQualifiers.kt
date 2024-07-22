package com.sovcom.authCode.app.hilt.network

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class LoggingInterceptor

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class BaseUrl

