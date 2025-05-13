package com.example.listenup.ui

import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.listenup.ListenUpApp
import com.example.listenup.MainActivity
import com.example.listenup.R
import com.example.listenup.databinding.FragmentAudioToTextBinding
import com.example.listenup.viewModels.VoiceViewModel


class AudioToTextFragment : Fragment() {

    private var _binding: FragmentAudioToTextBinding? = null
    private val binding get() = _binding!!
    lateinit var voiceVm: VoiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAudioToTextBinding.inflate(inflater, container, false)
        voiceVm = (activity as MainActivity).voiceViewModel
        binding.voiceVm = voiceVm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        voiceVm.isRunning.observe(requireActivity(),{
            if(it){
                binding.recordToggle.setImageResource(R.drawable.recording_progress_icon)
            }else{
                binding.recordToggle.setImageResource(R.drawable.start_recording_icon)
            }
        })

        setupTranslateButton(view)
    }
    fun setupTranslateButton(view: View){
        val button = view.findViewById<Button>(R.id.translate_button)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_audioToTextFragment_to_translationFragment)
            voiceVm.processText()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

}