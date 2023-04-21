package com.example.nfcreader.domain.functionsUseCases

import android.app.Application
import android.util.Log
import com.example.nfcreader.core.utils.constants.FunctionConstants
import com.example.nfcreader.data.model.IncreaseCardLevelResponse
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.KinposConnectLT.Model.KEMV_CONFIG_TERM
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.InitPosParameters
import com.kinpos.KinposMobileSDK.Utiles.HexUtil.byteArrayToHexString
import com.kinpos.KinposMobileSDK.Utiles.HexUtil.hexStringToByteArray
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class IncreaseCardLevelUseCase @Inject constructor(
    private val application: Application
) : CallbackMpos {
    private lateinit var cardKey: ByteArray
    private lateinit var posConnector: IPOSServiceConnector
    private var vector = FunctionConstants.VECTOR
    private var customerName = FunctionConstants.CUSTOMER_NAME

    fun increaseLevel(): IncreaseCardLevelResponse {
        val result: StringBuilder = StringBuilder()
        try {
            val btObj = BlueToothKMPP(
                application,
                10000,
                10000
            )
            btObj.setBluetoothParameters(FunctionConstants.CARD_READER_MAC_ADDRESS) //linea para exponer en el plugin
            posConnector = BTPOSServiceConnector(btObj, this)
            val initParameters = InitPosParameters(customerName, vector)
            initParameters.btName = "D135"
            initParameters.idleOne = "One"
            initParameters.idleTwo = "Two"
            initParameters.language = KEMV_CONFIG_TERM().LANGUAGE_SPANISH
            posConnector.initPOSService(initParameters)
            cardKey = hexStringToByteArray("9EC5663586F84D80AF70140AFE63BBFA")
            result.append(
                getDate(
                    System.currentTimeMillis(),
                    "dd/MM/yyyy hh:mm:ss.SSS a"
                ) + " -> " + "Comenzando proceso de inicializacion."
            )
            result.append(System.getProperty("line.separator"))
//            binding.transactionLogTextView.setText(result)
            val initialDetection: Int = posConnector.detectMifareSl3()
            if (initialDetection == 0) {
                result.append(
                    getDate(
                        System.currentTimeMillis(),
                        "dd/MM/yyyy hh:mm:ss.SSS a"
                    ) + " -> " + "La tarjeta se detecto correctamente."
                )
            } else {
                result.append(
                    getDate(
                        System.currentTimeMillis(),
                        "dd/MM/yyyy hh:mm:ss.SSS a"
                    ) + " -> " + "No fue posible detectar la tarjeta."
                )
            }
            result.append(System.getProperty("line.separator"))
//            binding.transactionLogTextView.setText(result)
            val b: Int =
                posConnector.writePersoMifare(hexStringToByteArray("0090"), cardKey)
            val c: Int =
                posConnector.writePersoMifare(hexStringToByteArray("0190"), cardKey)
            val d: Int =
                posConnector.writePersoMifare(hexStringToByteArray("0290"), cardKey)
            val e: Int =
                posConnector.writePersoMifare(hexStringToByteArray("0390"), cardKey)
            for (blockIndex in 0..127) {
                val blockHexa: String =
                    byteArrayToHexString(byteArrayOf(blockIndex.toByte()))
                        .uppercase(Locale.ROOT) + "40"
                val s: Int =
                    posConnector.writePersoMifare(hexStringToByteArray(blockHexa), cardKey)
            }
            val commitSuccess: Int = posConnector.commitPersoMifare()
            if (commitSuccess == 0) {
                result.append(
                    getDate(
                        System.currentTimeMillis(),
                        "dd/MM/yyyy hh:mm:ss.SSS a"
                    ) + " -> " + "Commit Result: SL0"
                )
                result.append(System.getProperty("line.separator"))
            } else {
                result.append(
                    getDate(
                        System.currentTimeMillis(),
                        "dd/MM/yyyy hh:mm:ss.SSS a"
                    ) + " -> " + "Commit Result: SL3"
                )
                result.append(System.getProperty("line.separator"))
            }
            posConnector.detectMifareSl3()
            val increaseStatus: Int =
                posConnector.authMifareSl3(hexStringToByteArray("0390"), cardKey)
            if (increaseStatus == 0) {
                result.append(
                    getDate(
                        System.currentTimeMillis(),
                        "dd/MM/yyyy hh:mm:ss.SSS a"
                    ) + " -> " + "Nivel 3 de seguridad exitoso."
                )
                result.append(System.getProperty("line.separator"))
//                binding.transactionLogTextView.setText(result)
            }
            posConnector.detectMifareSl3()

            //int autenticacionBloque4000 = posConnector.authMifareSl3(hexStringToByteArray("0040"), cardKey);
            var otherIndex = 0
            for (blockIndex in 0..127) {
                val blockHexa: String =
                    byteArrayToHexString(byteArrayOf(blockIndex.toByte()))
                        .uppercase(Locale.ROOT) + "00"
                if (blockIndex != (otherIndex + 1) * 4 - 1) {
                    val autenticacionBloque4000: Int =
                        posConnector.authMifareSl3(hexStringToByteArray("0040"), cardKey)
                    when (blockIndex) {
                        12 -> {
                            val writingBlockTwelve: Int = posConnector.writeMifareSl3(
                                hexStringToByteArray(blockHexa),
                                hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                            )
                            if (writingBlockTwelve == 0) {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 0: Ok."
                                )
                            } else {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 0: Fallida."
                                )
                            }
                        }
                        13 -> {
                            val writingBlockThirteen: Int = posConnector.writeMifareSl3(
                                hexStringToByteArray(blockHexa),
                                hexStringToByteArray("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                            )
                            if (writingBlockThirteen == 0) {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 1: Ok."
                                )
                            } else {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 1: Fallida."
                                )
                            }
                        }
                        14 -> {
                            val writingBlockFourteen: Int = posConnector.writeMifareSl3(
                                hexStringToByteArray(blockHexa),
                                hexStringToByteArray("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
                            )
                            if (writingBlockFourteen == 0) {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 2: Ok."
                                )
                            } else {
                                result.append(
                                    getDate(
                                        System.currentTimeMillis(),
                                        "dd/MM/yyyy hh:mm:ss.SSS a"
                                    ) + " -> " + "Inicializacion data 2: Fallida."
                                )
                            }
                        }
                        else -> {
                            val otherBlocksWriting: Int = posConnector.writeMifareSl3(
                                hexStringToByteArray(blockHexa),
                                hexStringToByteArray("00000000000000000000000000000000")
                            )
                        }
                    }
                } else {
                    otherIndex++
                }
            }
            posConnector.detectMifareSl3()
            val block4000Authentication: Int =
                posConnector.authMifareSl3(hexStringToByteArray("0040"), cardKey)
            val byteArrayTest: ByteArray? =
                posConnector.readMifareSl3(hexStringToByteArray("0C00"), 0x01.toByte())
            Log.i("IncreaseLevelTest", "$byteArrayTest")
            result.append(System.getProperty("line.separator"))
//            binding.transactionLogTextView.text = result
            return IncreaseCardLevelResponse(result)
        } catch (e: Exception) {
            Log.e("ErrorIncreasingCardLevel", "$e")
            return IncreaseCardLevelResponse()
        }
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
    }
}