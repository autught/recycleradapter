package com.recyclerview.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

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
    ): RecyclerView.ViewHolder {
        return when {
            layoutResId != null -> layoutResId!!.layout(inflater, parent)
            vbClazz != null -> vbClazz!!.bind(inflater, parent)
                ?: super.invoke(inflater, parent)
            else -> super.invoke(inflater, parent)
        }
    }

    private fun Int.layout(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(this, parent, false))
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VB : ViewBinding> Class<VB>.bind(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewBindingHolder<VB>? {
        try {
            val binding = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val methodType = MethodType.methodType(
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java, this
                )
                val method = MethodHandles.lookup()
                    .findStatic(this, "inflate", methodType)
                method.invoke(inflater, parent, false) as VB
            } else {
                val method = getDeclaredMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java,
                )
                method.invoke(null, inflater, parent, false) as VB
            }
            return ViewBindingHolder(binding)
        } catch (e: Exception) {
            return null
        }
    }
}