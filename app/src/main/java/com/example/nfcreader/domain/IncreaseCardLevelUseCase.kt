package com.example.nfcreader.domain

import android.app.Application
import android.util.Log
import com.kinpos.KinposMobileSDK.BlueTooth.BlueToothKMPP
import com.kinpos.KinposMobileSDK.Dispatchers.CallbackMpos
import com.kinpos.KinposMobileSDK.POS.BTPOSServiceConnector
import com.kinpos.KinposMobileSDK.POS.IPOSServiceConnector
import com.kinpos.KinposMobileSDK.Utiles.HexUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class IncreaseCardLevelUseCase @Inject constructor(
    private val application: Application
) : CallbackMpos {
    private lateinit var cardKey: ByteArray
    private lateinit var posConnector: IPOSServiceConnector


    fun increaseLevel() {
        try {
            val btObj = BlueToothKMPP(
                application,
                10000,
                10000
            )
            btObj.setBluetoothParameters("54:81:2D:7E:0B:8C") //linea para exponer en el plugin
            posConnector = BTPOSServiceConnector(btObj, this)
            cardKey = HexUtil.hexStringToByteArray("9EC5663586F84D80AF70140AFE63BBFA")
            val result: StringBuilder = StringBuilder()
            result.append(
                getDate(
                    System.currentTimeMillis(),
                    "dd/MM/yyyy hh:mm:ss.SSS a"
                ) + " -> " + "Comenzando proceso de inicializacion."
            )
            result.append(System.getProperty("line.separator"))
//            binding.transactionLogTextView.setText(result)
            val deteccionInicial: Int = posConnector.detectMifareSl3()
            if (deteccionInicial == 0) {
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
                posConnector.writePersoMifare(HexUtil.hexStringToByteArray("0090"), cardKey)
            val c: Int =
                posConnector.writePersoMifare(HexUtil.hexStringToByteArray("0190"), cardKey)
            val d: Int =
                posConnector.writePersoMifare(HexUtil.hexStringToByteArray("0290"), cardKey)
            val e: Int =
                posConnector.writePersoMifare(HexUtil.hexStringToByteArray("0390"), cardKey)
            for (blockIndex in 0..127) {
                val blockHexa: String =
                    HexUtil.byteArrayToHexString(byteArrayOf(blockIndex.toByte()))
                        .uppercase(Locale.ROOT) + "40"
                val s: Int =
                    posConnector.writePersoMifare(HexUtil.hexStringToByteArray(blockHexa), cardKey)
            }
            val commitSuccess: Int = posConnector.commitPersoMifare()
//            if (commitSuccess == 0) {
//                binding.cardLabelTextView.text = "SL0"
//            } else {
//                binding.cardLabelTextView.text = "SL3"
//            }
            posConnector.detectMifareSl3()
            val increaseStatus: Int =
                posConnector.authMifareSl3(HexUtil.hexStringToByteArray("0390"), cardKey)
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
                    HexUtil.byteArrayToHexString(byteArrayOf(blockIndex.toByte()))
                        .uppercase(Locale.ROOT) + "00"
                if (blockIndex != (otherIndex + 1) * 4 - 1) {
                    val autenticacionBloque4000: Int =
                        posConnector.authMifareSl3(HexUtil.hexStringToByteArray("0040"), cardKey)
                    when (blockIndex) {
                        12 -> {
                            val escrituraBloqueDoce: Int = posConnector.writeMifareSl3(
                                HexUtil.hexStringToByteArray(blockHexa),
                                HexUtil.hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                            )
                            if (escrituraBloqueDoce == 0) {
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
                            val escrituraBloqueTrece: Int = posConnector.writeMifareSl3(
                                HexUtil.hexStringToByteArray(blockHexa),
                                HexUtil.hexStringToByteArray("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                            )
                            if (escrituraBloqueTrece == 0) {
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
                            val escrituraBloqueCatorce: Int = posConnector.writeMifareSl3(
                                HexUtil.hexStringToByteArray(blockHexa),
                                HexUtil.hexStringToByteArray("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
                            )
                            if (escrituraBloqueCatorce == 0) {
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
                            val escrituraOtrosBloques: Int = posConnector.writeMifareSl3(
                                HexUtil.hexStringToByteArray(blockHexa),
                                HexUtil.hexStringToByteArray("00000000000000000000000000000000")
                            )
                        }
                    }
                } else {
                    otherIndex++
                }
            }
            posConnector.detectMifareSl3()
            val autenticacionBloque400: Int =
                posConnector.authMifareSl3(HexUtil.hexStringToByteArray("0040"), cardKey)
            val hola: ByteArray? =
                posConnector.readMifareSl3(HexUtil.hexStringToByteArray("0C00"), 0x01.toByte())
            Log.i("IncreaseLevelTest", "$hola")
            result.append(System.getProperty("line.separator"))
//            binding.transactionLogTextView.text = result
        } catch (e: Exception) {
            Log.e("ErrorIncreasingCardLevel", "$e")
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
        TODO("Not yet implemented")
    }

}