package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
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
 * 当有多种Controller时必须设置block这个参数
 */
abstract class RecyclerAdapter<T : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    IRecyclerController<T, VH> {
    private var weakInflater: WeakReference<LayoutInflater>? = null
    private val updateCallback by lazy { AdapterListUpdateCallback(this) }
    private var recycler: IRecyclerModel<T> = CollectionsModel(updateCallback)
    private var handleClickEvent: ClickEvent<T>? = null
    private var handleLongClickEvent: ClickEvent<T>? = null
    private var handleChildClickPairs: SparseArrayCompat<ClickEvent<T>>? = null

    fun <M : IRecyclerModel<T>> setRecyclerModel(block: ((ListUpdateCallback) -> M)) {
        this.recycler = block.invoke(updateCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = weakInflater?.get() ?: LayoutInflater.from(parent.context)
        return create(inflater, parent, viewType).also {
            val position = it.bindingAdapterPosition
            val data = getItem(position)
            handleClickEvent?.handleClickEvent(it.itemView, data, position)
            handleLongClickEvent?.handleLongClickEvent(it.itemView, data, position)
            handleChildClickPairs?.let { sac ->
                for (index in 0 until sac.size()) {
                    sac.valueAt(index).handleClickEvent(
                        it.itemView.findViewById(sac.keyAt(index)), data, position
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bind(requireNotNull(getItem(position)), holder, position)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindPayloads(requireNotNull(getItem(position)), holder, position)
        }
    }

    override fun getItemCount(): Int = recycler.itemCount

    protected fun getItem(@IntRange(from = 0) index: Int): T? = recycler.getItem(index)

    fun submitData(data: MutableList<T>) {
        recycler.submitData(data)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = null
    }

     fun setHandleClickEvent(event: ClickEvent<T>) {
        this.handleClickEvent = event
    }

     fun setHandleLongClickEvent(event: ClickEvent<T>) {
        this.handleLongClickEvent = event
    }

     fun setHandleChildClickEvent(resId: Int, event: ClickEvent<T>) {
        if (handleChildClickPairs == null) {
            handleChildClickPairs = SparseArrayCompat()
        }
        handleChildClickPairs.takeIf { it!!.containsKey(resId) }?.put(resId, event)
    }

    private fun ClickEvent<T>.handleClickEvent(view: View, data: T?, position: Int) {
        view.setOnClickListener { invoke(data, position) }
    }

    private fun ClickEvent<T>.handleLongClickEvent(view: View, data: T?, position: Int) {
        view.setOnLongClickListener {
            invoke(data, position)
            return@setOnLongClickListener true
        }
    }

    companion object {
        fun <T : Any, VH : RecyclerView.ViewHolder> create(controller: IRecyclerController<T, VH>) =
            object : RecyclerAdapter<T, VH>() {
                override fun create(
                    inflater: LayoutInflater,
                    parent: ViewGroup,
                    type: Int
                ) = controller.create(inflater, parent, type)

                override fun bind(data: T, holder: VH, position: Int) {
                    controller.bind(data, holder, position)
                }

                override fun bindPayloads(data: T, holder: VH, position: Int) =
                    controller.bindPayloads(data, holder, position)
            }
    }
}