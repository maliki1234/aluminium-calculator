package com.aluminium.calculator.data.repository

import androidx.lifecycle.LiveData
import com.aluminium.calculator.data.db.JobDao
import com.aluminium.calculator.data.model.SavedJob

class JobRepository(private val jobDao: JobDao) {
    val allJobs: LiveData<List<SavedJob>> = jobDao.getAllJobs()

    suspend fun saveJob(job: SavedJob): Long = jobDao.insertJob(job)

    suspend fun deleteJob(job: SavedJob) = jobDao.deleteJob(job)
}
