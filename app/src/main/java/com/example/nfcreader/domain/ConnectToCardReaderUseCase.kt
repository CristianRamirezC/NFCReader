package com.example.nfcreader.domain

import android.app.Application
import android.util.Log
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import javax.inject.Inject

class ConnectToCardReaderUseCase @Inject constructor(
    private val application: Application
) : CallbackMpos {
    private lateinit var posConnector: IPOSServiceConnector
    private var vector =
        "7FDC5D1EFE92EBB3B89B0307158F2C29A264EBB4599242E96F27A60801F0EF442050C1E1CCA9DCAFA3B098FE5381380C055D3A13E603B63318A8088825D595B9" //release vector
    private var customerName = "KINPOS"

    fun connectToCardReader() {
        try {
            val btObj = BlueToothKMPP(
                application,
                10000,
                10000
            )
            btObj.setBluetoothParameters("54:81:2D:7E:0B:8C") //linea para exponer en el plugin
            posConnector = BTPOSServiceConnector(btObj, this)

            val initParameters = InitPosParameters(customerName, vector)
            initParameters.btName = "D135"
            initParameters.idleOne = "One"
            initParameters.idleTwo = "Two"
            initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
            posConnector.initPOSService(initParameters)
            posConnector.beep()
        } catch (e: Exception) {
            Log.e("ErrorConnectingToCardReader", "$e")
        }
    }

    override fun showMposMessage(codMsg: Int, msg: String) {
        TODO("Not yet implemented")
    }

}