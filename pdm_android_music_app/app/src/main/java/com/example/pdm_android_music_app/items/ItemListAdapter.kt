package com.example.pdm_android_music_app.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pdm_android_music_app.R
import com.example.pdm_android_music_app.core.TAG
import com.example.pdm_android_music_app.data.Song
import com.example.pdm_android_music_app.item.ItemEditFragment
import kotlinx.android.synthetic.main.view_item.view.*


class ItemListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }

    var items = emptyList<Song>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Song
            fragment.findNavController().navigate(R.id.fragment_item_edit, Bundle().apply {
                putString(ItemEditFragment.ITEM_ID, item._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.textView.text = item.toString()
        holder.itemView.setOnClickListener(onItemClick)
    }
}
