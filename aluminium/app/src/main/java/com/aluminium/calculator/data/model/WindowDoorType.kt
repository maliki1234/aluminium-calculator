package com.aluminium.calculator.data.model

enum class WindowDoorType(
    val displayName: String,
    val displayNameSw: String,
    val allowance: Double,
    val isSliding: Boolean
) {
    SLIDING_WINDOW(
        displayName = "Sliding Window",
        displayNameSw = "Dirisha la Kuteleza",
        allowance = 20.0,
        isSliding = true
    ),
    SLIDING_DOOR(
        displayName = "Sliding Door",
        displayNameSw = "Mlango wa Kuteleza",
        allowance = 25.0,
        isSliding = true
    ),
    CASEMENT_WINDOW(
        displayName = "Casement Window",
        displayNameSw = "Dirisha la Kufungua",
        allowance = 5.0,
        isSliding = false
    ),
    CASEMENT_DOOR(
        displayName = "Casement Door",
        displayNameSw = "Mlango wa Kufungua",
        allowance = 5.0,
        isSliding = false
    );

    fun label(useSw: Boolean) = if (useSw) displayNameSw else displayName
}
