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
import com.example.listenup.MainActivity
import com.example.listenup.R
import com.example.listenup.databinding.FragmentAudioToTextBinding
import com.example.listenup.databinding.FragmentTranslationBinding
import com.example.listenup.viewModels.VoiceViewModel

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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}