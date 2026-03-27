package com.aluminium.calculator.ui.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aluminium.calculator.data.model.SavedJob
import com.aluminium.calculator.databinding.ItemSavedJobBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedJobsAdapter(
    private val onView: (SavedJob) -> Unit,
    private val onDelete: (SavedJob) -> Unit
) : ListAdapter<SavedJob, SavedJobsAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSavedJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val b: ItemSavedJobBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(job: SavedJob) {
            b.tvJobName.text    = job.jobName
            b.tvJobType.text    = job.typeName.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
            b.tvJobDims.text    = "${job.openingWidth.toInt()} × ${job.openingHeight.toInt()} mm"
            b.tvJobDate.text    = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                                      .format(Date(job.savedAt))
            b.btnView.setOnClickListener   { onView(job) }
            b.btnDelete.setOnClickListener { onDelete(job) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SavedJob>() {
            override fun areItemsTheSame(a: SavedJob, b: SavedJob) = a.id == b.id
            override fun areContentsTheSame(a: SavedJob, b: SavedJob) = a == b
        }
    }
}
