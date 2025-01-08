package com.easyapps.zkplayer.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easyapps.zkplayer.BuildConfig
import com.easyapps.zkplayer.R
import com.easyapps.zkplayer.databinding.FragmentContentBinding
import com.easyapps.zkplayer.utils.OtherFunction
import com.easyapps.zkplayer.utils.OtherFunction.share
import com.easyapps.zkplayer.utils.PreferencesUtil.getFromPref
import com.easyapps.zkplayer.utils.PreferencesUtil.setFromPref
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.WindowUtils.setupBottomInsets
import com.easyapps.zkplayer.utils.WindowUtils.setupTopInsets
import com.easyapps.zkplayer.utils.ads.BannerAds
import org.json.JSONArray
import org.json.JSONObject

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

        val jsonString = arguments?.getString("json") ?: "{}"
        val jsonObject = JSONObject(jsonString)

        detailTitleText.text = jsonObject.optString("title")
        detailAboutText.text = jsonObject.optString("content")

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

        share.setOnClickListener {
            share(requireContext(),getString(R.string.app_name),"Я читаю эссе на тему ${detailTitleText.text}, предлагаю скачать его по ссылке https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        }
        save.setOnClickListener {
            val bookmarks = getBookmarks()
            if (!bookmarks.containsBookmarkWithTitle(jsonObject)) addBookmark(jsonObject)
            else deleteBookmark(jsonObject)
            changeSaveText(jsonObject)
        }
        changeSaveText(jsonObject)

    }

    private fun changeSaveText(jsonObject: JSONObject) {
        val bookmarksIsHave = getBookmarks().containsBookmarkWithTitle(jsonObject)
        binding.save.text = if (bookmarksIsHave)  getString(R.string.bookmark_delete)
        else getString(R.string.bookmark_add)
    }

    private fun JSONArray.containsBookmarkWithTitle(jsonObject: JSONObject): Boolean {
        val targetTitle = jsonObject.optString("title") ?: return false
        for (i in 0 until length()) {
            val item = optJSONObject(i) ?: continue
            if (item.optString("title") == targetTitle) return true
        }
        return false
    }

    private fun getBookmarks(): JSONArray {
        val bookmark = getFromPref(requireContext(),"bookmarks","[]")
        return JSONArray(bookmark)
    }

    private fun addBookmark(jsonObject: JSONObject) {
        val bookmarks = getBookmarks()
        bookmarks.put(jsonObject)
        setFromPref(requireContext(),"bookmarks",bookmarks.toString())
        Toast.makeText(requireContext(), getString(R.string.toast_add_bookmark), Toast.LENGTH_SHORT).show()
    }

    private fun deleteBookmark(jsonObject: JSONObject) {
        val bookmarks = getBookmarks()
        val targetTitle = jsonObject.optString("title") ?: return
        val updatedBookmarks = JSONArray()
        for (i in 0 until bookmarks.length()) {
            val item = bookmarks.optJSONObject(i) ?: continue
            if (item.optString("title") != targetTitle) updatedBookmarks.put(item)
        }
        setFromPref(requireContext(),"bookmarks",updatedBookmarks.toString())
        Toast.makeText(requireContext(), getString(R.string.toast_delete_bookmark), Toast.LENGTH_SHORT).show()
    }

    private fun TextView.changeTextSize(increase: Boolean, minSize: Float = 12f, maxSize: Float = 30f) {
        val currentSize = textSize / (resources.displayMetrics.density * resources.configuration.fontScale)
        val newSize = if (increase) currentSize + 1 else currentSize - 1
        val clampedSize = newSize.coerceIn(minSize, maxSize)
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, clampedSize)
    }
}
