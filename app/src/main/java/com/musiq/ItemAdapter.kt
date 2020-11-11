package com.musiq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*


class ItemAdapter(private var itemList: List<Item>, private val listener: OnItemClickListener): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView1: TextView = itemView.tv_text

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position, itemList[position].getText1())
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, key: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView1.text = currentItem.text
    }

    fun filterList(filteredList: ArrayList<Item>) {
        itemList = filteredList
        notifyDataSetChanged()
    }
}