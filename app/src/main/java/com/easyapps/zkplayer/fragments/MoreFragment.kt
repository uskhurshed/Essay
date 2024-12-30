package com.easyapps.zkplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.easyapps.zkplayer.BuildConfig
import com.easyapps.zkplayer.databinding.FragmentMoreBinding
import com.easyapps.zkplayer.utils.OtherFunction.goWithUrl
import com.easyapps.zkplayer.utils.OtherFunction.sendEmail
import com.easyapps.zkplayer.utils.OtherFunction.share
import com.easyapps.zkplayer.utils.WindowUtils.setupTopInsets
import com.google.android.material.snackbar.Snackbar

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.root.setupTopInsets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        navRate.setOnClickListener { goWithUrl(requireContext(),"https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}") }
        navApps.setOnClickListener { goWithUrl(requireContext(),"https://play.google.com/store/apps/dev?id=7468148183308310395") }
        navShare.setOnClickListener { share(requireContext(),"ММТ","Поделиться") }
        navOffer.setOnClickListener { sendEmail(requireContext()) }
        navAbout.setOnClickListener { Snackbar.make(requireView(), "Программу создал Khurshed Usmonov.", Snackbar.LENGTH_LONG).show()  }
    }


}