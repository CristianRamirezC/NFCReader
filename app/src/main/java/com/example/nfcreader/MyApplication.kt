package com.example.nfcreader

import android.app.Application

import dagger.hilt.android.HiltAndroidApp

//class MyApplication : Application() {
//
//    private lateinit var serviceComponent: ServiceComponent
//
//    override fun onCreate() {
//        super.onCreate()
//        // https://restcountries.eu/rest/v2/all -> It will list all the country details
//        serviceComponent = DaggerServiceComponent.builder()
//            .applicationModule(ApplicationModule(mApplication = this))
//            .serviceModule(ServiceModule(mBaseUrl = BASE_URL))
//            .build()
//    }
//}

@HiltAndroidApp
class MyApplication: Application()