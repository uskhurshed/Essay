package com.easyapps.zkplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.easyapps.zkplayer.EssayAdapter
import com.easyapps.zkplayer.R
import com.easyapps.zkplayer.databinding.FragmentHomeBinding
import com.easyapps.zkplayer.utils.JsonUtils.jsonArray
import com.easyapps.zkplayer.utils.JsonUtils.jsonObject
import com.easyapps.zkplayer.utils.JsonUtils.string
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.WindowUtils.setupBottomInsets
import com.easyapps.zkplayer.utils.WindowUtils.setupTopInsets

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var adapter =  EssayAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.toolbar.setupTopInsets()
        binding.searchList.setupBottomInsets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        val jsonArray = TempDB.globalJsonObject.jsonArray("langs")
        for (i in 0 until jsonArray.length()) {
            val radioButton = createRadioButton()
            val jsonObject = jsonArray.jsonObject(i)
            radioButton.text = jsonObject.string("title")
            radioButton.setOnClickListener {
                adapter = EssayAdapter(jsonObject.jsonArray("content")) { findNavController().navigate(R.id.contentFragment, it) }
                binding.searchList.adapter = adapter
            }
            binding.radioGroup.addView(radioButton)
            if (i == 0) radioButton.performClick()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = adapter.filterItems(s.toString())
        })
    }


    private fun createRadioButton()= RadioButton(requireContext()).apply {
        layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT)
        textSize = 14f
        gravity = android.view.Gravity.CENTER
        text = text
        setPadding(30.dp, 0, 30.dp, 0)
        setTextColor(ContextCompat.getColorStateList(context, R.color.text_input_selector))
        setBackgroundResource(R.drawable.tab_selector)
        buttonDrawable = null
    }
    private val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()

}