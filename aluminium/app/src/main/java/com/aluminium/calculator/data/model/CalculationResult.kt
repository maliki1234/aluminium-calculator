package com.aluminium.calculator.data.model

data class CalculationResult(
    val input: CalculationInput,
    val frameWidth: Double,
    val frameHeight: Double,
    val panelWidth: Double,
    val panelHeight: Double,
    val glassWidth: Double,
    val glassHeight: Double
) {
    val panelCount: Int get() = input.panels
    val isCasement: Boolean get() = !input.type.isSliding

    fun toCuttingListText(useSw: Boolean = false): String {
        val sb = StringBuilder()
        if (useSw) {
            sb.appendLine("=== ORODHA YA KUKATA ===")
            sb.appendLine("Aina: ${input.type.displayNameSw}")
            sb.appendLine("Upana wa Ufunguzi: ${input.openingWidth.toInt()}mm")
            sb.appendLine("Urefu wa Ufunguzi: ${input.openingHeight.toInt()}mm")
            if (input.profileType.isNotBlank()) sb.appendLine("Profaili: ${input.profileType}")
            sb.appendLine()
            sb.appendLine("--- FREMU ---")
            sb.appendLine("Juu:    ${frameWidth.toInt()}mm  (1x)")
            sb.appendLine("Chini:  ${frameWidth.toInt()}mm  (1x)")
            sb.appendLine("Pande:  ${frameHeight.toInt()}mm (2x)")
            sb.appendLine()
            if (isCasement) {
                sb.appendLine("--- SASH ---")
                sb.appendLine("Juu:    ${panelWidth.toInt()}mm  (1x)")
                sb.appendLine("Chini:  ${panelWidth.toInt()}mm  (1x)")
                sb.appendLine("Pande:  ${panelHeight.toInt()}mm (2x)")
            } else {
                sb.appendLine("--- PANELI ---")
                sb.appendLine("Upana:  ${panelWidth.toInt()}mm")
                sb.appendLine("Urefu:  ${panelHeight.toInt()}mm")
                sb.appendLine("Idadi:  ${panelCount}x")
            }
            sb.appendLine()
            sb.appendLine("--- GLASI ---")
            sb.appendLine("${glassWidth.toInt()}mm × ${glassHeight.toInt()}mm  (${panelCount}x)")
        } else {
            sb.appendLine("=== CUTTING LIST ===")
            sb.appendLine("Type:           ${input.type.displayName}")
            sb.appendLine("Opening Width:  ${input.openingWidth.toInt()}mm")
            sb.appendLine("Opening Height: ${input.openingHeight.toInt()}mm")
            if (input.profileType.isNotBlank()) sb.appendLine("Profile:        ${input.profileType}")
            sb.appendLine()
            sb.appendLine("--- FRAME ---")
            sb.appendLine("Top:    ${frameWidth.toInt()}mm  (1x)")
            sb.appendLine("Bottom: ${frameWidth.toInt()}mm  (1x)")
            sb.appendLine("Sides:  ${frameHeight.toInt()}mm (2x)")
            sb.appendLine()
            if (isCasement) {
                sb.appendLine("--- SASH ---")
                sb.appendLine("Top:    ${panelWidth.toInt()}mm  (1x)")
                sb.appendLine("Bottom: ${panelWidth.toInt()}mm  (1x)")
                sb.appendLine("Sides:  ${panelHeight.toInt()}mm (2x)")
            } else {
                sb.appendLine("--- PANELS ---")
                sb.appendLine("Width:  ${panelWidth.toInt()}mm")
                sb.appendLine("Height: ${panelHeight.toInt()}mm")
                sb.appendLine("Count:  ${panelCount}x")
            }
            sb.appendLine()
            sb.appendLine("--- GLASS ---")
            sb.appendLine("${glassWidth.toInt()}mm × ${glassHeight.toInt()}mm  (${panelCount}x)")
        }
        return sb.toString().trimEnd()
    }
}
