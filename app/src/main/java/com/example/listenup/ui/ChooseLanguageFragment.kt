package com.example.listenup.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.listenup.MainActivity
import com.example.listenup.R
import com.example.listenup.languageModel.OkHttpModelDownloader
import com.example.listenup.languageModel.VoskModel
import com.example.listenup.viewModels.VoiceViewModel

class ChooseLanguageFragment : Fragment() {

    private lateinit var optionsContainer: LinearLayout
    private val radioButtons = mutableListOf<RadioButton>()
    private val progressBars = mutableListOf<ProgressBar>()
    private var selectedRadioButton: RadioButton? = null
    lateinit var voiceVm: VoiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        voiceVm = (activity as MainActivity).voiceViewModel
        return inflater.inflate(R.layout.fragment_chooselanguage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        optionsContainer = view.findViewById(R.id.optionsContainer)

        val options = listOf(VoskModel.Default, VoskModel.English, VoskModel.Hindi, VoskModel.Spanish)

        options.forEachIndexed { index, model ->
            val itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.language_radio_option_item, optionsContainer, false)

            val radioButton = itemView.findViewById<RadioButton>(R.id.radioButton)
            val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
            val labelView = itemView.findViewById<TextView>(R.id.optionLabel)

            radioButton.id = index + 1
            labelView.text = model.modelName

            radioButtons.add(radioButton)
            progressBars.add(progressBar)

            itemView.setOnClickListener {
                // Uncheck previously selected button
                selectedRadioButton?.isChecked = false
                // Check the new one
                radioButton.isChecked = true
                selectedRadioButton = radioButton

                getLanguageModel(progressBar,model)
            }

            optionsContainer.addView(itemView)
        }
    }
    private fun getLanguageModel(progressBar: ProgressBar,model:VoskModel) {
        if (OkHttpModelDownloader.isModelDownloaded(requireContext(), model)) {
            progressBar.progress = 100
            voiceVm.selectedLanguageModel.value = model
        } else {
            OkHttpModelDownloader.downloadModel(
                context = requireContext(),
                model = model,
                onProgress = { percent ->
                    requireActivity().runOnUiThread {
                        progressBar.progress = percent
                    }
                },
                onComplete = {
                    requireActivity().runOnUiThread {
                        voiceVm.selectedLanguageModel.value = model
                        Toast.makeText(requireContext(), "Download complete", Toast.LENGTH_SHORT).show()
                        //   initRecognizer(model)
                        // initializeSpeechService(model.dirName)
                    }
                },
                onError = { e ->
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}