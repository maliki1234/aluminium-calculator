package com.aluminium.calculator.data.model

data class CalculationInput(
    val openingWidth: Double,
    val openingHeight: Double,
    val type: WindowDoorType,
    val panels: Int,
    val profileType: String = "",
    val clearance: Double = 3.0,
    val overlap: Double = 25.0
)
