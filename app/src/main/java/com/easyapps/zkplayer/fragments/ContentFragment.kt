package com.easyapps.zkplayer.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easyapps.zkplayer.databinding.FragmentContentBinding
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.WindowUtils.setupBottomInsets
import com.easyapps.zkplayer.utils.WindowUtils.setupTopInsets
import com.easyapps.zkplayer.utils.ads.BannerAds

class ContentFragment : Fragment() {

    private lateinit var binding: FragmentContentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        binding.toolbar.setupTopInsets()
        binding.detailAboutText.setupBottomInsets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        detailTitleText.text = arguments?.getString("title").orEmpty()
        detailAboutText.text = arguments?.getString("content").orEmpty()

        BannerAds(requireContext(), forad, TempDB.adsBanner)
        aPlus.setOnClickListener {
            detailAboutText.changeTextSize(increase = true)
        }

        aMinus.setOnClickListener {
            detailAboutText.changeTextSize(increase = false)
        }

        toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun TextView.changeTextSize(increase: Boolean, minSize: Float = 12f, maxSize: Float = 30f) {
        val currentSize = textSize / (resources.displayMetrics.density * resources.configuration.fontScale)
        val newSize = if (increase) currentSize + 1 else currentSize - 1
        val clampedSize = newSize.coerceIn(minSize, maxSize)
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, clampedSize)
    }
}
