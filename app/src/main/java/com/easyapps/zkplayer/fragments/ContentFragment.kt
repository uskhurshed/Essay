package com.easyapps.zkplayer.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easyapps.zkplayer.databinding.FragmentContentBinding
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.ads.BannerAds

class ContentFragment : Fragment() {

    companion object {
        private const val MAX_TEXT_SIZE = 55f
        private const val MIN_TEXT_SIZE = 16f
        private const val DEFAULT_TEXT_SIZE = 16f
    }

    private lateinit var binding: FragmentContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        detailTitleText.text = arguments?.getString("title").orEmpty()
        detailAboutText.text = arguments?.getString("content").orEmpty()
        detailAboutText.textSize = DEFAULT_TEXT_SIZE

        BannerAds(requireContext(), forad, TempDB.adsBanner)
        aPlus.setOnClickListener {
            adjustTextSize(increment = true)
        }

        aMinus.setOnClickListener {
            adjustTextSize(increment = false)
        }

        toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun FragmentContentBinding.adjustTextSize(increment: Boolean) {
        val currentSize = detailAboutText.textSize
        val newSize = if (increment) (currentSize + 1f).coerceAtMost(MAX_TEXT_SIZE)
        else (currentSize - 1f).coerceAtLeast(MIN_TEXT_SIZE)
        detailAboutText.textSize = newSize
    }

    fun  Int.dp(context: Context) = (this * context.resources.displayMetrics.density).toInt()

}
