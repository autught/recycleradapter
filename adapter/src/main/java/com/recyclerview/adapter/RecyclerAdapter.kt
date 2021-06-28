package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * @description:
 * @author:  79120
 * @date :   2021/6/21 15:49
 */
abstract class RecyclerAdapter<T : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private var itemType: ((T) -> Int)? = null
    private var weakInflater: WeakReference<LayoutInflater>? = null
    private val models = SparseArrayCompat<IRecyclerController<T, VH>>()
    private val updateCallback by lazy { AdapterListUpdateCallback(this) }
    private var recycler: IRecyclerModel<T> = CollectionsModel(updateCallback)

    fun <M : IRecyclerModel<T>> setRecyclerModel(block: ((ListUpdateCallback) -> M)) {
        this.recycler = block.invoke(updateCallback)
    }

    fun setControllerType(block: ((T) -> Int)) {
        this.itemType = block
    }

    fun <C : IRecyclerController<T, VH>> addControllers(type: Int, c: C) {
        models.takeIf { !it.containsKey(type) }?.put(type, c)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        require(!models.isEmpty)
        val inflater = weakInflater?.get() ?: LayoutInflater.from(parent.context)
        val model = models[viewType]
        return model!!.onCreate(parent, inflater)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val type = getItemViewType(position)
        require(models.containsKey(type))
        val data = requireNotNull(getItem(position))
        models[type]?.onBind(data, holder)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val type = getItemViewType(position)
            require(models.containsKey(type))
            val data = requireNotNull(getItem(position))
            models[type]?.onBindPayloads(data, holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getDataItemType(position) ?: super.getItemViewType(position)
    }

    private fun getDataItemType(position: Int): Int? {
        val data = requireNotNull(getItem(position))
        return itemType?.invoke(data)
    }

    override fun getItemCount(): Int = recycler.itemCount

    fun getItem(@IntRange(from = 0) index: Int): T? = recycler.getItem(index)

    fun submitData(data: MutableList<T>) {
        recycler.submitData(data)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        weakInflater = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        weakInflater = null
    }
}