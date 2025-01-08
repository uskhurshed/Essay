package com.easyapps.zkplayer.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.easyapps.zkplayer.EssayAdapter
import com.easyapps.zkplayer.R
import com.easyapps.zkplayer.databinding.FragmentFavoriteBinding
import com.easyapps.zkplayer.utils.PreferencesUtil.getFromPref
import com.easyapps.zkplayer.utils.WindowUtils.setupBottomInsets
import com.easyapps.zkplayer.utils.WindowUtils.setupTopInsets
import org.json.JSONArray

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private var adapter =  EssayAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        binding.root.setupTopInsets()
        binding.searchList.setupBottomInsets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)
        val bookmarks = getBookmarks()
        emptyBookmarks.isVisible = bookmarks.length() == 0
        searchList.isVisible = bookmarks.length() != 0

        adapter = EssayAdapter(bookmarks) { findNavController().navigate(R.id.contentFragment, bundleOf("json" to it.toString())) }
        searchList.adapter = adapter

    }
    private fun getBookmarks(): JSONArray {
        val bookmark = getFromPref(requireContext(),"bookmarks","[]")
        return JSONArray(bookmark)
    }

}