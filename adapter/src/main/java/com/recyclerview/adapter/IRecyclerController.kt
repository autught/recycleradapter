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

    fun create(inflater: LayoutInflater, parent: ViewGroup, type: Int): VH

    fun bind(data: T, holder: VH, position: Int)

    fun bindPayloads(data: T, holder: VH, position: Int): Unit? = null

}