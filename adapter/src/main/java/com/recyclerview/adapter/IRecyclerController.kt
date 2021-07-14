package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * @description:
 * @author:  79120
 * @date :   2021/7/9 11:25
 */

typealias  ClickEvent<A> = ((A?, Int) -> Unit)

interface IRecyclerController<T : Any, VH : RecyclerView.ViewHolder> {

    fun create(inflater: LayoutInflater, parent: ViewGroup, type: Int): VH

    fun bind(data: T, holder: VH, position: Int)

    fun bindPayloads(data: T, holder: VH, position: Int): Unit? = null

//    fun setHandleClickEvent(event: ClickEvent<T>): Unit? = null
//
//    fun setHandleLongClickEvent(event: ClickEvent<T>): Unit? = null
//
//    fun setHandleChildClickEvent(resId: Int, event: ClickEvent<T>): Unit? = null
}