package com.aluminium.calculator.ui.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aluminium.calculator.data.model.CalculationInput
import com.aluminium.calculator.data.model.WindowDoorType
import com.aluminium.calculator.engine.AluminiumCalculator

class InputViewModel : ViewModel() {

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    fun buildInput(
        widthStr: String,
        heightStr: String,
        type: WindowDoorType,
        panels: Int,
        profileType: String,
        clearanceStr: String,
        overlapStr: String,
        useSw: Boolean
    ): CalculationInput? {
        val error = AluminiumCalculator.validate(widthStr, heightStr, clearanceStr, useSw)
        if (error != null) {
            _validationError.value = error
            return null
        }
        _validationError.value = null
        return CalculationInput(
            openingWidth  = widthStr.trim().toDouble(),
            openingHeight = heightStr.trim().toDouble(),
            type          = type,
            panels        = panels,
            profileType   = profileType.trim(),
            clearance     = clearanceStr.toDoubleOrNull() ?: 3.0,
            overlap       = overlapStr.toDoubleOrNull()   ?: 25.0
        )
    }

    fun clearError() {
        _validationError.value = null
    }
}
