package com.example.nfcreader.domain.functionsUseCases

import android.app.Application
import android.util.Log
import com.example.nfcreader.core.utils.constants.FunctionConstants
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import javax.inject.Inject

class ConnectToCardReaderUseCase @Inject constructor(
    private val application: Application,
    private val posConnector: IPOSServiceConnector
) {
//    private lateinit var posConnector: IPOSServiceConnector

    fun connectToCardReader() {
        try {
//            val btObj = BlueToothKMPP(
//                application,
//                10000,
//                10000
//            )
//            //linea para exponer en el plugin
//            btObj.setBluetoothParameters(FunctionConstants.CARD_READER_MAC_ADDRESS)
//            posConnector = BTPOSServiceConnector(btObj, this)
//
//            val initParameters = InitPosParameters(FunctionConstants.CUSTOMER_NAME, FunctionConstants.VECTOR)
//            initParameters.btName = "D135"
//            initParameters.idleOne = "One"
//            initParameters.idleTwo = "Two"
//            initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
//            posConnector.initPOSService(initParameters)
            posConnector.beep()
        } catch (e: Exception) {
            Log.e("ErrorConnectingToCardReader", "$e")
        }
    }
//    override fun showMposMessage(codMsg: Int, msg: String) {
//        TODO("Not yet implemented")
//    }
}