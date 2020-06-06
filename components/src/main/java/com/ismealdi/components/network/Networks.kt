package com.ismealdi.components.network

class Networks {

    /*fun bridge(baseUrl: String? = null): Retrofit {

        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Logs.http(message)
            }
        })

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val osVersion = Build.VERSION.RELEASE.toString()
                val appVersion = BuildConfig.VERSION_NAME

                val requestBuilder = original.newBuilder()
                    .header("Accept", "application/json;versions=1")
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("Cache-Control", "no-store")

                *//*val token = AmApplication.mContext?.let {
                    Preferences(it, Constants.Preference.UserToken).getString()
                    requestBuilder.header("Authorization", it)
                }*//*

                return chain.proceed(requestBuilder.build())
            }
        })

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.callTimeout(120, TimeUnit.SECONDS)
        httpClient.writeTimeout(50, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
    }*/
}