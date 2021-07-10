package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/8 23:20
 */
abstract class LayoutAdapter<T : Any>(private val res: Int) : RecyclerAdapter<T, BaseViewHolder>() {
    override fun create(inflater: LayoutInflater, parent: ViewGroup, type: Int): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(res, parent, false))
    }
}

abstract class ViewBindingAdapter<T : Any, VB : ViewBinding>(private val vb: ((LayoutInflater, ViewGroup) -> VB)) :
    RecyclerAdapter<T, ViewBindingHolder<VB>>() {

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int
    ): ViewBindingHolder<VB> {
        val binding = vb.invoke(inflater, parent)
        return ViewBindingHolder(binding)
    }
}

open class MultiTypeAdapter<T : Any>(private val block: ((T) -> Int)? = null) :
    RecyclerAdapter<T, RecyclerView.ViewHolder>() {
    private val models = SparseArrayCompat<BaseController<T, RecyclerView.ViewHolder>>()

    fun <VH : RecyclerView.ViewHolder> addControllers(vararg controllers: BaseController<T, VH>) {
        controllers.forEachIndexed { index, controller ->
            models.takeIf { !it.containsValue(controller) }
                ?.put(index, controller as IRecyclerController<T, VH>)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return if (block == null || data == null) {
            super.getItemViewType(position)
        } else {
            block.invoke(data)
        }
    }

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int
    ): RecyclerView.ViewHolder {
        return requireNotNull(models[type]).create(inflater, parent, type)
    }

    override fun bind(data: T, holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        require(models.containsKey(type))
        models[type]?.bind(data, holder, position)
    }

    override fun bindPayloads(data: T, holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        require(models.containsKey(type))
        models[type]?.bindPayloads(data, holder, position)
    }
}