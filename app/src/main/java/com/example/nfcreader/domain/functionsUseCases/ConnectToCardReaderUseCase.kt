package com.example.nfcreader.domain.functionsUseCases

import android.app.Application
import android.util.Log
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import javax.inject.Inject

class ConnectToCardReaderUseCase @Inject constructor(
    private val posConnector: BTPOSServiceConnector
) {
    fun connectToCardReader() {
        try {
            posConnector.beep()
        } catch (e: Exception) {
            Log.e("ErrorConnectingToCardReader", "$e")
        }
    }
}