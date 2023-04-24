package com.example.nfcreader.core.di

import android.app.Application
import com.example.nfcreader.core.utils.constants.FunctionConstants
import com.example.nfcreader.data.network.ApiClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import com.kinpos.KinposMobileSDK.Utiles.appContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL = "http://54.84.35.206:8080/api/"
//private const val BASE_URL = "http://54.84.35.206:8080/"

@Module
@InstallIn(SingletonComponent::class) //scope (application/viewModel/activity/fragment/view etc)
class ServiceModule() : CallbackMpos {
    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache?): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson?, okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun providePosConnector(
        blueToothKMPP: BlueToothKMPP,
        initParameters: InitPosParameters
    ): IPOSServiceConnector {
        lateinit var posConnector: IPOSServiceConnector
        blueToothKMPP.setBluetoothParameters(FunctionConstants.CARD_READER_MAC_ADDRESS)
        posConnector = BTPOSServiceConnector(blueToothKMPP, this)
        posConnector.initPOSService(initParameters)
        return posConnector
    }

    @Provides
    @Singleton
    fun provideInitPosParameters(): InitPosParameters {
        val initParameters =
            InitPosParameters(FunctionConstants.CUSTOMER_NAME, FunctionConstants.VECTOR)
        initParameters.btName = "D135"
        initParameters.idleOne = "One"
        initParameters.idleTwo = "Two"
        initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
        return initParameters
    }

    override fun showMposMessage(codMsg: Int, msg: String) {
    }

    @Provides
    @Singleton
    fun provideBlueToothKMPP(application: Application): BlueToothKMPP {
        appContext = application
        return BlueToothKMPP(
            application,
            10000,
            10000
        )
    }
}