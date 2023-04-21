package com.example.nfcreader.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.nfcreader.domain.functionsUseCases.ConnectToCardReaderUseCase
import com.example.nfcreader.domain.functionsUseCases.IncreaseCardLevelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FunctionsViewModel @Inject constructor(
    private val connectToCardReaderUseCase: ConnectToCardReaderUseCase,
    private val increaseCardLevelUseCase: IncreaseCardLevelUseCase
) : ViewModel() {

    fun connectToCardReader() {
        connectToCardReaderUseCase.connectToCardReader()
    }

    fun increaseCardLevel() {
        increaseCardLevelUseCase.increaseLevel()
    }

}