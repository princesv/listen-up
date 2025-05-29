package com.example.listenup.ui

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import com.example.listenup.MainActivity
import com.example.listenup.R
import com.example.listenup.common.TimeConverter
import com.example.listenup.databinding.FragmentAudioToTextBinding
import com.example.listenup.databinding.FragmentTranslationBinding
import com.example.listenup.helper.AudioPlaybackSpeed
import com.example.listenup.viewModels.VoiceViewModel
import kotlin.math.roundToLong

class TranslationFragment : Fragment() {
    lateinit var voiceVm: VoiceViewModel
    private var _binding: FragmentTranslationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)
        voiceVm = (activity as MainActivity).voiceViewModel
        binding.voiceVm=voiceVm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPlayPosButton()
        setUpSeekbar()
        setUpPlaybackSpeedRadioGroup()
        setupUpdateAudioDurationOnSpeedChange()
    }
    fun setUpPlayPosButton(){
        voiceVm.isAudioPlaying.observe(requireActivity(),{
            if(!it){
                binding.playAudioToggle.setImageResource(R.drawable.play_icon)
            }else{
                binding.playAudioToggle.setImageResource(R.drawable.pause_icon)
            }
        })
    }
    fun setUpSeekbar(){
        voiceVm._audioDurationMillis.observe(requireActivity()){
            binding.audioPlaySeekBar.max = it?.toInt()!!
        }
        voiceVm._audioCurrentStateMillis.observe(requireActivity()){
          //  binding.audioPlaySeekBar.progress= it?.toInt()!!
            animateSeekBarProgress(binding.audioPlaySeekBar,it?.toInt()!!)
        }
    }
    fun animateSeekBarProgress(seekBar: SeekBar, toProgress: Int, duration: Long = 1000) {
        var animDuration=duration
        if(toProgress==0){
            animDuration=5
        }
        val animator = ValueAnimator.ofInt(seekBar.progress, toProgress).apply {
            this.duration = animDuration
            interpolator = LinearInterpolator() // Uniform speed

            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                seekBar.progress = animatedValue
            }
        }
        animator.start()
    }
    fun setUpPlaybackSpeedRadioGroup(){
        binding.playbackSpeedRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_slow -> {
                    voiceVm.playBackSpeedLd.value = AudioPlaybackSpeed.Slow
                }
                R.id.radio_medium -> {
                    voiceVm.playBackSpeedLd.value = AudioPlaybackSpeed.Medium
                }
                R.id.radio_fast -> {
                    voiceVm.playBackSpeedLd.value = AudioPlaybackSpeed.Fast
                }
            }
        }
    }
    fun setupUpdateAudioDurationOnSpeedChange(){
        voiceVm.playBackSpeedLd.observe(requireActivity(),{
            voiceVm.convertedAudioDuration.value = TimeConverter.getFormattedTimeFromMillis( (voiceVm.convertedAudioDurationMillis.value!! / voiceVm.playBackSpeedLd.value!!.speed).roundToLong())
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}