package com.aluminium.calculator.ui.result

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aluminium.calculator.R
import com.aluminium.calculator.data.model.CalculationResult
import com.aluminium.calculator.databinding.FragmentResultBinding
import com.aluminium.calculator.ui.SharedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val sharedVm: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        sharedVm.result.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            val useSw = sharedVm.useSw.value ?: false
            renderResult(result, useSw)
        }

        sharedVm.useSw.observe(viewLifecycleOwner) { useSw ->
            val result = sharedVm.result.value ?: return@observe
            renderResult(result, useSw)
        }

        sharedVm.saveMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                sharedVm.clearSaveMessage()
            }
        }

        binding.btnCopy.setOnClickListener {
            val useSw = sharedVm.useSw.value ?: false
            val text = sharedVm.result.value?.toCuttingListText(useSw) ?: return@setOnClickListener
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Cutting List", text))
            Snackbar.make(
                binding.root,
                if (useSw) "Imekopwa kwenye clipboard" else "Copied to clipboard",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.btnSave.setOnClickListener { showSaveDialog() }
    }

    private fun renderResult(result: CalculationResult, useSw: Boolean) {
        val isSw = useSw
        binding.toolbar.title = if (isSw) "Matokeo" else "Results"
        binding.btnCopy.text  = if (isSw) "NAKILI MATOKEO" else "COPY RESULTS"
        binding.btnSave.text  = if (isSw) "HIFADHI KAZI" else "SAVE JOB"

        // Input summary card
        binding.tvTypeLabel.text    = if (isSw) "Aina:" else "Type:"
        binding.tvTypeValue.text    = result.input.type.label(isSw)
        binding.tvOpeningLabel.text = if (isSw) "Ufunguzi:" else "Opening:"
        binding.tvOpeningValue.text = "${result.input.openingWidth.toInt()} × ${result.input.openingHeight.toInt()} mm"
        binding.tvPanelsLabel.text  = if (isSw) "Paneli:" else "Panels:"
        binding.tvPanelsValue.text  = result.panelCount.toString()
        if (result.input.profileType.isNotBlank()) {
            binding.tvProfileLabel.visibility = View.VISIBLE
            binding.tvProfileValue.visibility = View.VISIBLE
            binding.tvProfileLabel.text = if (isSw) "Profaili:" else "Profile:"
            binding.tvProfileValue.text = result.input.profileType
        } else {
            binding.tvProfileLabel.visibility = View.GONE
            binding.tvProfileValue.visibility = View.GONE
        }

        // Frame card
        binding.tvFrameTitle.text   = if (isSw) "FREMU" else "FRAME"
        binding.tvFrameTop.text     = if (isSw) "Juu / Chini:  ${result.frameWidth.toInt()}mm  (2×)"
                                      else       "Top / Bottom: ${result.frameWidth.toInt()}mm  (2×)"
        binding.tvFrameSides.text   = if (isSw) "Pande:        ${result.frameHeight.toInt()}mm (2×)"
                                      else       "Sides:        ${result.frameHeight.toInt()}mm (2×)"

        // Panel / Sash card
        val panelTitle = when {
            isSw && result.isCasement  -> "SASH"
            isSw && !result.isCasement -> "PANELI"
            !isSw && result.isCasement -> "SASH"
            else                       -> "PANELS"
        }
        binding.tvPanelTitle.text = panelTitle

        if (result.isCasement) {
            binding.tvPanelDim.text  = if (isSw) "Juu / Chini:  ${result.panelWidth.toInt()}mm  (2×)"
                                       else       "Top / Bottom: ${result.panelWidth.toInt()}mm  (2×)"
            binding.tvPanelCount.text = if (isSw) "Pande:        ${result.panelHeight.toInt()}mm (2×)"
                                        else       "Sides:        ${result.panelHeight.toInt()}mm (2×)"
        } else {
            binding.tvPanelDim.text  = "${result.panelWidth.toInt()}mm × ${result.panelHeight.toInt()}mm"
            binding.tvPanelCount.text = if (isSw) "Idadi: ${result.panelCount}×"
                                        else       "Count: ${result.panelCount}×"
        }

        // Glass card
        binding.tvGlassTitle.text = if (isSw) "GLASI" else "GLASS"
        binding.tvGlassDim.text   = "${result.glassWidth.toInt()}mm × ${result.glassHeight.toInt()}mm"
        binding.tvGlassCount.text = if (isSw) "Idadi: ${result.panelCount}×"
                                    else       "Count: ${result.panelCount}×"
    }

    private fun showSaveDialog() {
        val useSw = sharedVm.useSw.value ?: false
        val input = layoutInflater.inflate(R.layout.dialog_save_job, null)
        val etName = input.findViewById<TextInputEditText>(R.id.etJobName)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (useSw) "Hifadhi Kazi" else "Save Job")
            .setMessage(if (useSw) "Weka jina la kazi (hiari):" else "Enter a job name (optional):")
            .setView(input)
            .setPositiveButton(if (useSw) "Hifadhi" else "Save") { _, _ ->
                val result = sharedVm.result.value ?: return@setPositiveButton
                sharedVm.saveJob(result, etName.text.toString())
            }
            .setNegativeButton(if (useSw) "Ghairi" else "Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
