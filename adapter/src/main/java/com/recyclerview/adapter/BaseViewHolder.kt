package com.recyclerview.adapter

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2020/11/10 16:40
 */
class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Views indexed with their IDs
     */
    private var views: SparseArray<View?>? = null

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewById(@IdRes viewId: Int): T {
        var view = views?.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
                ?: throw NullPointerException("NullPointerException,can't find this id")
            if (views == null) {
                views = SparseArray()
            }
            views!!.put(viewId, view)
        }
        return view as T
    }

}

/**
 * 如果使用viewBinding注意在RecyclerView.Adapter$onViewRecycled()中置空
 */
class ViewBindingHolder<VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)