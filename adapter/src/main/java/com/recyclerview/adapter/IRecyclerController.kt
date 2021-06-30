package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @description:
 * @author:  79120
 * @date :   2021/6/23 17:50
 */
interface IRecyclerController<T : Any, VH : RecyclerView.ViewHolder> {
    fun onCreate(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder

    fun onBind(data: T, holder: VH)

    fun onBindPayloads(data: T, holder: VH): Unit?=null
}