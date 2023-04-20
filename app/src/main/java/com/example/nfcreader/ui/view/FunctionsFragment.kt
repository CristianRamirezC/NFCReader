package com.example.nfcreader.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nfcreader.R
import com.example.nfcreader.databinding.FragmentFunctionsBinding
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import com.kinpos.KinposMobileSDK.Utiles.ApplicationContext
import com.kinpos.KinposMobileSDK.Utiles.appContext

class FunctionsFragment : Fragment(), CallbackMpos {
    var vector =
        "7FDC5D1EFE92EBB3B89B0307158F2C29A264EBB4599242E96F27A60801F0EF442050C1E1CCA9DCAFA3B098FE5381380C055D3A13E603B63318A8088825D595B9" //release vector
    var customerName = "KINPOS"

    private var _binding: FragmentFunctionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFunctionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appContext = requireContext().applicationContext

        val btObj = BlueToothKMPP(
            activity?.applicationContext as ApplicationContext?,
            10000,
            10000
        )
        btObj.setBluetoothParameters("54:81:2D:7E:0B:8C") //linea para exponer en el plugin

        val posConnector: IPOSServiceConnector = BTPOSServiceConnector(btObj, this)
        val initParameters = InitPosParameters(customerName, vector)
        initParameters.btName = "D135"
        initParameters.idleOne = "One"
        initParameters.idleTwo = "Two"
        initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
        posConnector.initPOSService(initParameters)

        posConnector.beep()
    }

    override fun showMposMessage(codMsg: Int, msg: String) {
        TODO("Not yet implemented")
    }

}