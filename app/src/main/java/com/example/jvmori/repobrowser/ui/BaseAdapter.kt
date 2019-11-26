package com.example.jvmori.repobrowser.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(private var items : List<T>?) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>>(){

    abstract override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindView(items?.let { it[position] })
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bindView(item : T?)
    }

    fun setItemsList(items : List<T>){
        this.items = items
        notifyDataSetChanged()
    }
}