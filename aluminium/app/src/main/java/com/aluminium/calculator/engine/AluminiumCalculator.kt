package com.aluminium.calculator.engine

import com.aluminium.calculator.data.model.CalculationInput
import com.aluminium.calculator.data.model.CalculationResult

/**
 * Core calculation engine for aluminium windows and doors.
 *
 * Formulas:
 *   Frame Width  = Opening Width  - (2 × clearance)
 *   Frame Height = Opening Height - (2 × clearance)
 *
 *   Sliding:
 *     Panel Width  = (Frame Width ÷ panels) + overlap
 *     Panel Height = Frame Height - allowance
 *
 *   Casement:
 *     Sash Width  = Frame Width  - 5
 *     Sash Height = Frame Height - 5
 *
 *   Glass Width  = Panel/Sash Width  - 20
 *   Glass Height = Panel/Sash Height - 20
 */
object AluminiumCalculator {

    fun calculate(input: CalculationInput): CalculationResult {
        val frameWidth  = input.openingWidth  - (2.0 * input.clearance)
        val frameHeight = input.openingHeight - (2.0 * input.clearance)

        val panelWidth: Double
        val panelHeight: Double

        if (input.type.isSliding) {
            panelWidth  = (frameWidth / input.panels.toDouble()) + input.overlap
            panelHeight = frameHeight - input.type.allowance
        } else {
            // Casement: sash fits inside frame with 5mm inset each side
            panelWidth  = frameWidth  - 5.0
            panelHeight = frameHeight - 5.0
        }

        val glassWidth  = panelWidth  - 20.0
        val glassHeight = panelHeight - 20.0

        return CalculationResult(
            input       = input,
            frameWidth  = frameWidth,
            frameHeight = frameHeight,
            panelWidth  = panelWidth,
            panelHeight = panelHeight,
            glassWidth  = glassWidth,
            glassHeight = glassHeight
        )
    }

    /**
     * Returns a user-facing error message string, or null if inputs are valid.
     */
    fun validate(
        widthStr: String,
        heightStr: String,
        clearanceStr: String,
        useSw: Boolean = false
    ): String? {
        val width     = widthStr.toDoubleOrNull()
        val height    = heightStr.toDoubleOrNull()
        val clearance = clearanceStr.toDoubleOrNull()

        return when {
            widthStr.isBlank() || width == null ->
                if (useSw) "Tafadhali weka upana wa ufunguzi"
                else "Opening width is required"

            heightStr.isBlank() || height == null ->
                if (useSw) "Tafadhali weka urefu wa ufunguzi"
                else "Opening height is required"

            width <= 0 ->
                if (useSw) "Upana lazima uwe mkubwa kuliko sifuri"
                else "Width must be greater than 0"

            height <= 0 ->
                if (useSw) "Urefu lazima uwe mkubwa kuliko sifuri"
                else "Height must be greater than 0"

            clearance == null ->
                if (useSw) "Thamani ya clearance si sahihi"
                else "Clearance must be a valid number"

            clearance < 2.0 ->
                if (useSw) "Onyo: Clearance ni chini ya 2mm — itakuwa ngumu kuingiza fremu"
                else "Warning: Clearance below 2mm — frame may be difficult to fit"

            clearance > 10.0 ->
                if (useSw) "Onyo: Clearance inazidi 10mm — pengo kubwa mno"
                else "Warning: Clearance exceeds 10mm — gap will be too large"

            else -> null
        }
    }
}
