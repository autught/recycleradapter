package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/14 15:44
 */
abstract class CustomController<T : Any> :
    IRecyclerController<T, RecyclerView.ViewHolder> {
    private var layoutResId: Int? = null
    private var vbClazz: Class<out ViewBinding>? = null

    constructor(layoutResId: Int? = null) {
        this.layoutResId = layoutResId
    }

    constructor(clazz: Class<out ViewBinding>) {
        this.vbClazz = clazz
    }

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int
    ): RecyclerView.ViewHolder {
        return when {
            layoutResId != null -> layoutResId!!.layout(inflater, parent)
            vbClazz != null -> vbClazz!!.bind(inflater, parent)
            else -> create(inflater, parent, type)
        }
    }

    private fun Int.layout(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(this, parent, false))
    }

    private fun <VB : ViewBinding> Class<VB>.bind(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBindingHolder<VB> {
        val method = getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java,
        )
        val binding = method.invoke(null, inflater, parent, false) as VB
        return ViewBindingHolder(binding)
    }
}