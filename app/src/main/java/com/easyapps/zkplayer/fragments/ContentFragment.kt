package com.easyapps.zkplayer.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.easyapps.zkplayer.databinding.FragmentContentBinding
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.ads.BannerAds

class ContentFragment : Fragment() {
    private var dp: Float = 6f
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: ""
        val content = arguments?.getString("content") ?: ""

        binding.detailTitleText.text = title
        binding.detailAboutText.text = content
        BannerAds(requireContext(), binding.forad, TempDB.adsBanner)

        binding.detailImageView.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.aPlus.setOnClickListener {
            if (binding.detailAboutText.textSize < 55f) {
                dp += 1f
                binding.detailAboutText.textSize = pxFromDp(dp, requireContext())
            }
        }

        binding.aMinus.setOnClickListener {
            if (binding.detailAboutText.textSize > 26f) {
                dp -= 1f
                binding.detailAboutText.textSize = pxFromDp(dp, requireContext())
            }
        }
    }

    private fun pxFromDp(dp: Float, mContext: Context): Float {
        return dp * mContext.resources.displayMetrics.density
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(title: String, content: String): ContentFragment {
            val fragment = ContentFragment()
            val args = Bundle().apply {
                putString("title", title)
                putString("content", content)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
