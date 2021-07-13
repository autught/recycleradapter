package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/9 11:25
 */
interface IRecyclerController<T : Any, VH : RecyclerView.ViewHolder> {
    private var clickBlock: ((T, Int) -> Unit)? = null
    private var clickLongBlock: ((T, Int) -> Unit)? = null

    fun create(inflater: LayoutInflater, parent: ViewGroup, type: Int): VH

    fun bind(data: T, holder: VH, position: Int)

    fun bindPayloads(data: T, holder: VH, position: Int): Unit? = null

    protected fun handleClickEvent(holder: VH) {
        clickBlock?.let {
            holder.itemView.setOnClickListener {
                val position = holder.absoluteAdapterPosition
                val data = if (position > 0) getItem(position) else null
                data?.let { clickBlock!!.invoke(data, position) }
            }
        }
        clickLongBlock?.let {
            holder.itemView.setOnLongClickListener {
                val position = holder.absoluteAdapterPosition
                val data = if (position > 0) getItem(position) else null
                data?.let { clickLongBlock!!.invoke(data, position) }
                return@setOnLongClickListener true
            }
        }
    }

    fun setOnItemClickListener(click: ((T, Int) -> Unit)) {
        this.clickBlock = click
    }

    fun setOnItemLongClickListener(click: ((T, Int) -> Unit)) {
        this.clickLongBlock = click
    }

}