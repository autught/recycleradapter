package com.recyclerview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import java.lang.ref.WeakReference

abstract class CustomListAdapter<T,VH:RecyclerView.ViewHolder>(
    private val produce: IViewHolderFactory<VH>,
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(callback) {
    private var inflaterRef: WeakReference<LayoutInflater>? = null
    private var itemClickPair: Pair<VH.() -> Int, (T, Int) -> Unit>? = null

    fun setOnItemClickCallback(
        positionGet: RecyclerView.ViewHolder.() -> Int,
        clickEvent: ((T, Int) -> Unit),
    ) {
        this.itemClickPair = positionGet to clickEvent
    }

    abstract fun onBindViewHolder(holder: VH, data: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = inflaterRef?.get() ?: LayoutInflater.from(parent.context)
         return   produce.onCreate(inflater, parent, viewType).also { handleClickEvent(it) }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
            onBindViewHolder(holder, getItem(position), position)
    }

    private fun handleClickEvent(vh: VH) {
        itemClickPair?.let { pair ->
            vh.itemView.setOnClickListener {
                val position = pair.first.invoke(vh)
                try {
                    pair.second.invoke(getItem(position), position)
                } catch (e: IndexOutOfBoundsException) {
                    if (BuildConfig.DEBUG)
                        Log.e(this.javaClass.simpleName, "点击事件数组越界")
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = null
    }
}