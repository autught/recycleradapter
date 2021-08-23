package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * @description:
 * @author:  79120
 * @date :   2021/7/9 11:25
 */
interface IViewHolderProduce<VH : RecyclerView.ViewHolder> {
    fun getItemType():Int
    fun onProduce(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH
}