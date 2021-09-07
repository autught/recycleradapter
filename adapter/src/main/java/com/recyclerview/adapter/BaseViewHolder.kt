package com.recyclerview.adapter

import android.view.View
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2020/11/10 16:40
 */
class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    constructor(vb: ViewBinding) : this(vb.root) {
        itemView.setTag(-1, vb)
    }

    /**
     * Views indexed with their IDs
     */
    private var views: SparseArrayCompat<View?>? = null

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewById(@IdRes viewId: Int): T {
        var view = views?.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
                ?: throw NullPointerException("NullPointerException,can't find this id")
            if (views == null) {
                views = SparseArrayCompat()
            }
            views!!.put(viewId, view)
        }
        return view as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getBinding() = itemView.getTag(-1) as VB
}