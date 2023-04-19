package com.example.nfcreader.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nfcreader.R
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import com.kinpos.KinposMobileSDK.Utiles.appContext
import java.text.SimpleDateFormat
import java.util.*

class FunctionsActivity : AppCompatActivity(), CallbackMpos{

    var vector = "7FDC5D1EFE92EBB3B89B0307158F2C29A264EBB4599242E96F27A60801F0EF442050C1E1CCA9DCAFA3B098FE5381380C055D3A13E603B63318A8088825D595B9" //release vector
    var customerName = "KINPOS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_functions)

        appContext = this

        val btObj = BlueToothKMPP(application, 10000, 10000)
        btObj.setBluetoothParameters("54:81:2D:7E:0B:93") //linea para exponer en el plugin

        val posConnector: IPOSServiceConnector = BTPOSServiceConnector(btObj, this)
        val initParameters = InitPosParameters(customerName, vector)
        initParameters.btName = "D135"
        initParameters.idleOne = "One"
        initParameters.idleTwo = "Two"
        initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
        posConnector.initPOSService(initParameters)

        posConnector.beep()


    }

    private fun getDate(milliSeconds: Long, dateFormat: String): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    override fun showMposMessage(codMsg: Int, msg: String) {
        TODO("Not yet implemented")
    }
}