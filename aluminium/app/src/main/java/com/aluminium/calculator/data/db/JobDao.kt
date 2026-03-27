package com.aluminium.calculator.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aluminium.calculator.data.model.SavedJob

@Dao
interface JobDao {
    @Query("SELECT * FROM saved_jobs ORDER BY savedAt DESC")
    fun getAllJobs(): LiveData<List<SavedJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: SavedJob): Long

    @Delete
    suspend fun deleteJob(job: SavedJob)

    @Query("DELETE FROM saved_jobs WHERE id = :id")
    suspend fun deleteJobById(id: Long)
}
