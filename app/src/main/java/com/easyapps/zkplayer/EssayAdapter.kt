package com.easyapps.zkplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.easyapps.zkplayer.databinding.ItemLayoutBinding
import com.easyapps.zkplayer.utils.JsonUtils.string
import org.json.JSONArray
import org.json.JSONObject

class EssayAdapter(private val essayItems: JSONArray =JSONArray(), private var onItemClick:(Bundle) -> Unit ={}) : RecyclerView.Adapter<EssayAdapter.ViewHolder>() {

    private var filteredEssayItems: List<JSONObject> = (0 until essayItems.length()).map { essayItems.getJSONObject(it) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredEssayItems[position]
        holder.binding.titleTextView.text = item.getString("title")
        holder.binding.root.setOnClickListener {
            onItemClick.invoke(bundleOf("title" to item.string("title"), "content" to item.string("content")))
        }
    }

    override fun getItemCount()= filteredEssayItems.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun filterItems(query: String) {
        filteredEssayItems = if (query.isEmpty()) (0 until essayItems.length()).map { essayItems.getJSONObject(it) }
        else (0 until essayItems.length()).map { essayItems.getJSONObject(it) }.filter { it.getString("title").contains(query, ignoreCase = true) }
        notifyDataSetChanged()
    }

}
