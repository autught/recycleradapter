package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/14 15:44
 */
class LayoutVHFactory(private val layoutResId: Int) : IViewHolderFactory<BaseViewHolder> {
    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layoutResId, parent, false))
    }
}

class ViewBindingVHFactory(private val vbClazz: Class<out ViewBinding>) :
    IViewHolderFactory<ViewBindingHolder> {
    override fun onCreate(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingHolder {
        return bind(vbClazz, inflater, parent)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VB : ViewBinding> bind(
        clazz: Class<VB>,
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBindingHolder {
        val method = clazz.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java,
        )
        val binding = method.invoke(null, inflater, parent, false) as VB
        return ViewBindingHolder(binding)
    }
}
