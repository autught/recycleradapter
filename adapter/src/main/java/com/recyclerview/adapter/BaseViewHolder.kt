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
    private val views: SparseArray<View?> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewById(@IdRes viewId: Int): T {
        var view = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
                ?: throw NullPointerException("NullPointerException,can't find this id")
            views.put(viewId, view)
        }
        return view as T
    }

}

class ViewBindingHolder(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getBinding() = binding as VB
}