package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * @description:
 * @author:  79120
 * @date :   2021/7/9 11:25
 */
fun interface IViewCreatedFactory<VH : RecyclerView.ViewHolder> {
    fun onViewCreated(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH
}