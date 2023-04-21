package com.example.nfcreader.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nfcreader.data.model.IncreaseCardLevelResponse
import com.example.nfcreader.domain.functionsUseCases.ConnectToCardReaderUseCase
import com.example.nfcreader.domain.functionsUseCases.IncreaseCardLevelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FunctionsViewModel @Inject constructor(
    private val connectToCardReaderUseCase: ConnectToCardReaderUseCase,
    private val increaseCardLevelUseCase: IncreaseCardLevelUseCase
) : ViewModel() {

    private var _functionsResult = MutableLiveData<StringBuilder>()
    val functionsResult: LiveData<StringBuilder> = _functionsResult
    fun connectToCardReader() {
        connectToCardReaderUseCase.connectToCardReader()
    }

    fun increaseCardLevel() {
        val functionResult: IncreaseCardLevelResponse = increaseCardLevelUseCase.increaseLevel()
        _functionsResult.postValue(functionResult.result)
    }

}