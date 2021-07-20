package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/14 15:44
 */
abstract class LayoutController<T : Any>(private val layoutResId: Int) :
    IRecyclerController<T, BaseViewHolder> {

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int
    ): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layoutResId, parent, false))
    }
}

abstract class BindingController<T : Any>(private val vbClazz: Class<out ViewBinding>) :
    IRecyclerController<T, ViewBindingHolder<*>> {

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int
    ): ViewBindingHolder<*> {
        return vbClazz.bind(inflater, parent)!!
    }
}

@Suppress("UNCHECKED_CAST")
private fun <VB : ViewBinding> Class<VB>.bind(
    inflater: LayoutInflater,
    parent: ViewGroup
): ViewBindingHolder<VB>? {
    try {
//        val binding = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val methodType = MethodType.methodType(
//                LayoutInflater::class.java,
//                ViewGroup::class.java,
//                Boolean::class.java, this
//            )
//            val method = MethodHandles.lookup()
//                .findStatic(this, "inflate", methodType)
//            method.invoke(inflater, parent, false) as VB
//        } else {
        val method = getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java,
        )
        val binding = method.invoke(null, inflater, parent, false) as VB
//        }
        return ViewBindingHolder(binding)
    } catch (e: Exception) {
        return null
    }
}