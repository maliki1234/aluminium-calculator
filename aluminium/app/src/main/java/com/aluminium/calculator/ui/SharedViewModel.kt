package com.aluminium.calculator.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aluminium.calculator.data.db.AppDatabase
import com.aluminium.calculator.data.model.CalculationResult
import com.aluminium.calculator.data.model.SavedJob
import com.aluminium.calculator.data.repository.JobRepository
import kotlinx.coroutines.launch

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = JobRepository(AppDatabase.getDatabase(app).jobDao())

    // Language toggle: false = English, true = Kiswahili
    private val _useSw = MutableLiveData(false)
    val useSw: LiveData<Boolean> = _useSw

    // Holds the last calculation result to pass from Input -> Result screen
    private val _result = MutableLiveData<CalculationResult?>()
    val result: LiveData<CalculationResult?> = _result

    // All saved jobs from Room
    val savedJobs: LiveData<List<SavedJob>> = repository.allJobs

    // Save operation feedback
    private val _saveMessage = MutableLiveData<String?>()
    val saveMessage: LiveData<String?> = _saveMessage

    fun toggleLanguage() {
        _useSw.value = !(_useSw.value ?: false)
    }

    fun setResult(result: CalculationResult) {
        _result.value = result
    }

    fun saveJob(result: CalculationResult, jobName: String) {
        val job = SavedJob(
            jobName     = jobName.ifBlank { "${result.input.type.displayName} ${result.input.openingWidth.toInt()}×${result.input.openingHeight.toInt()}" },
            openingWidth  = result.input.openingWidth,
            openingHeight = result.input.openingHeight,
            typeName      = result.input.type.name,
            panels        = result.input.panels,
            profileType   = result.input.profileType,
            clearance     = result.input.clearance,
            overlap       = result.input.overlap,
            frameWidth    = result.frameWidth,
            frameHeight   = result.frameHeight,
            panelWidth    = result.panelWidth,
            panelHeight   = result.panelHeight,
            glassWidth    = result.glassWidth,
            glassHeight   = result.glassHeight
        )
        viewModelScope.launch {
            repository.saveJob(job)
            _saveMessage.postValue("Job saved!")
        }
    }

    fun deleteJob(job: SavedJob) {
        viewModelScope.launch { repository.deleteJob(job) }
    }

    fun clearSaveMessage() {
        _saveMessage.value = null
    }
}
