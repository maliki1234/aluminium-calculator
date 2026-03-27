package com.aluminium.calculator.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aluminium.calculator.R
import com.aluminium.calculator.data.model.WindowDoorType
import com.aluminium.calculator.databinding.FragmentInputBinding
import com.aluminium.calculator.engine.AluminiumCalculator
import com.aluminium.calculator.ui.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    private val sharedVm: SharedViewModel by activityViewModels()
    private val vm: InputViewModel by viewModels()

    private var selectedType: WindowDoorType = WindowDoorType.SLIDING_WINDOW
    private var selectedPanels: Int = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedVm.useSw.observe(viewLifecycleOwner) { useSw -> applyLanguage(useSw) }

        binding.btnLanguage.setOnClickListener { sharedVm.toggleLanguage() }

        binding.btnCalculate.setOnClickListener { onCalculateClicked() }

        binding.btnSavedJobs.setOnClickListener {
            findNavController().navigate(R.id.action_input_to_saved)
        }

        // Clear error on text change
        listOf(
            binding.etOpeningWidth,
            binding.etOpeningHeight,
            binding.etClearance,
            binding.etOverlap
        ).forEach { et ->
            et.doAfterTextChanged { vm.clearError() }
        }

        vm.validationError.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun applyLanguage(useSw: Boolean) {
        val types = WindowDoorType.values().map { it.label(useSw) }
        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, types)
        binding.spinnerType.setAdapter(typeAdapter)
        binding.spinnerType.setText(selectedType.label(useSw), false)
        binding.spinnerType.setOnItemClickListener { _, _, pos, _ ->
            selectedType = WindowDoorType.values()[pos]
        }

        val panels = listOf("2", "3", "4")
        val panelsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, panels)
        binding.spinnerPanels.setAdapter(panelsAdapter)
        binding.spinnerPanels.setText(selectedPanels.toString(), false)
        binding.spinnerPanels.setOnItemClickListener { _, _, pos, _ ->
            selectedPanels = panels[pos].toInt()
        }

        // UI text
        binding.toolbar.title = if (useSw) "Kikokotoo cha Alumini" else "Aluminium Calculator Pro"
        binding.btnLanguage.text = if (useSw) "EN" else "SW"
        binding.btnCalculate.text = if (useSw) "KOKOTOA" else "CALCULATE"
        binding.btnSavedJobs.text = if (useSw) "Kazi Zilizohifadhiwa" else "Saved Jobs"

        binding.tilOpeningWidth.hint  = if (useSw) "Upana wa Ufunguzi (mm)" else "Opening Width (mm)"
        binding.tilOpeningHeight.hint = if (useSw) "Urefu wa Ufunguzi (mm)" else "Opening Height (mm)"
        binding.tilType.hint          = if (useSw) "Aina" else "Type"
        binding.tilPanels.hint        = if (useSw) "Idadi ya Paneli" else "Number of Panels"
        binding.tilProfileType.hint   = if (useSw) "Aina ya Profaili (hiari)" else "Profile Type (optional)"
        binding.tilClearance.hint     = if (useSw) "Clearance (mm) — kawaida 3" else "Clearance (mm) — default 3"
        binding.tilOverlap.hint       = if (useSw) "Overlap (mm) — kawaida 25" else "Overlap (mm) — default 25"
    }

    private fun onCalculateClicked() {
        val useSw = sharedVm.useSw.value ?: false

        // Set defaults if blank
        if (binding.etClearance.text.isNullOrBlank()) binding.etClearance.setText("3")
        if (binding.etOverlap.text.isNullOrBlank()) binding.etOverlap.setText("25")

        val input = vm.buildInput(
            widthStr    = binding.etOpeningWidth.text.toString(),
            heightStr   = binding.etOpeningHeight.text.toString(),
            type        = selectedType,
            panels      = selectedPanels,
            profileType = binding.etProfileType.text.toString(),
            clearanceStr = binding.etClearance.text.toString(),
            overlapStr   = binding.etOverlap.text.toString(),
            useSw       = useSw
        ) ?: return

        val result = AluminiumCalculator.calculate(input)
        sharedVm.setResult(result)
        findNavController().navigate(R.id.action_input_to_result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
