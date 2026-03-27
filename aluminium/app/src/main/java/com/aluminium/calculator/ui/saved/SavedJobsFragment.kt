package com.aluminium.calculator.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aluminium.calculator.R
import com.aluminium.calculator.databinding.FragmentSavedJobsBinding
import com.aluminium.calculator.ui.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SavedJobsFragment : Fragment() {

    private var _binding: FragmentSavedJobsBinding? = null
    private val binding get() = _binding!!

    private val sharedVm: SharedViewModel by activityViewModels()
    private lateinit var adapter: SavedJobsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        val useSw = sharedVm.useSw.value ?: false
        binding.toolbar.title = if (useSw) "Kazi Zilizohifadhiwa" else "Saved Jobs"

        adapter = SavedJobsAdapter(
            onView = { job ->
                sharedVm.setResult(job.toCalculationResult())
                findNavController().navigate(R.id.action_saved_to_result)
            },
            onDelete = { job ->
                val confirmMsg = if (useSw) "Futa kazi hii?" else "Delete this job?"
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(if (useSw) "Futa" else "Delete")
                    .setMessage(confirmMsg)
                    .setPositiveButton(if (useSw) "Futa" else "Delete") { _, _ -> sharedVm.deleteJob(job) }
                    .setNegativeButton(if (useSw) "Ghairi" else "Cancel", null)
                    .show()
            }
        )

        binding.recyclerView.adapter = adapter

        sharedVm.savedJobs.observe(viewLifecycleOwner) { jobs ->
            adapter.submitList(jobs)
            binding.tvEmpty.visibility = if (jobs.isEmpty()) View.VISIBLE else View.GONE
            binding.tvEmpty.text = if (useSw) "Hakuna kazi zilizohifadhiwa" else "No saved jobs yet"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
