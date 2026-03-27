package com.aluminium.calculator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_jobs")
data class SavedJob(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val jobName: String,
    val openingWidth: Double,
    val openingHeight: Double,
    val typeName: String,
    val panels: Int,
    val profileType: String,
    val clearance: Double,
    val overlap: Double,
    val frameWidth: Double,
    val frameHeight: Double,
    val panelWidth: Double,
    val panelHeight: Double,
    val glassWidth: Double,
    val glassHeight: Double,
    val savedAt: Long = System.currentTimeMillis()
) {
    fun toCalculationResult(): CalculationResult {
        val type = WindowDoorType.valueOf(typeName)
        return CalculationResult(
            input = CalculationInput(
                openingWidth = openingWidth,
                openingHeight = openingHeight,
                type = type,
                panels = panels,
                profileType = profileType,
                clearance = clearance,
                overlap = overlap
            ),
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            panelWidth = panelWidth,
            panelHeight = panelHeight,
            glassWidth = glassWidth,
            glassHeight = glassHeight
        )
    }
}
