package com.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
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
abstract class RecyclerAdapter<T : Any>(private val layout: Int? = null) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var weakInflater: WeakReference<LayoutInflater>? = null
    private val updateCallback by lazy { AdapterListUpdateCallback(this) }
    private var recycler: IRecyclerModel<T> = CollectionsModel(updateCallback)
    private var itemChildClickLists: SparseArrayCompat<(T, Int, Int) -> Unit>? = null
    private var itemClickCallback: ((T, Int) -> Unit)? = null
    private var itemLongClickCallback: ((T, Int) -> Unit)? = null
    private var statusAdapter: StatusAdapter? = null

    @State
    private var state: Int = State.STATE_NORMAL

    fun setOnItemClickCallback(clickEvent: (T, Int) -> Unit) {
        itemClickCallback = clickEvent
    }

    fun setOnItemLongClickCallback(clickEvent: (T, Int) -> Unit) {
        itemLongClickCallback = clickEvent
    }

    fun setOnItemChildClickCallback(@IdRes viewId: Int, clickEvent: (T, Int, Int) -> Unit) {
        if (itemChildClickLists == null) itemChildClickLists = SparseArrayCompat()
        itemChildClickLists!!.put(viewId, clickEvent)
    }

    fun <M : IRecyclerModel<T>> setRecyclerModel(block: ((ListUpdateCallback) -> M)) {
        this.recycler = block.invoke(updateCallback)
    }

    open fun onViewHolderCreated(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layout!!, parent, false))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = weakInflater?.get() ?: LayoutInflater.from(parent.context)
        return if (state != State.STATE_NORMAL) {
            requireNotNull(statusAdapter).onCreateViewHolder(inflater, parent)
        } else {
            onViewHolderCreated(inflater, parent, viewType).also { setItemClickEvent(it) }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (state != State.STATE_NORMAL) {
            requireNotNull(statusAdapter).onBindViewHolder(holder, state)
        } else {
            onBindViewHolder(holder, getItem(position), position)
        }
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, data: T, position: Int)

    override fun getItemCount(): Int = if (state != State.STATE_NORMAL) 1 else recycler.itemCount

    fun getItem(index: Int): T = recycler.getItem(index)

    fun submitData(data: List<T>) {
        if (data.isEmpty()) {
            setState(State.STATE_EMPTY)
        } else {
            setState(State.STATE_NORMAL)
            recycler.submitData(data)
        }
    }

    fun getCurrentList(): List<T> {
        return recycler.getCurrentList()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = null
    }

    fun notifyDataModify(modify: CollectionModifiedModel<T>.() -> Unit) {
        if (recycler is CollectionModifiedModel<T>) {
            modify.invoke(recycler as CollectionModifiedModel<T>)
        }
    }

    private fun setItemClickEvent(helper: BaseViewHolder) {
        itemClickCallback?.let { block ->
            helper.itemView.setOnClickListener {
                val position = helper.bindingAdapterPosition
                block(getItem(position), position)
            }
        }
        itemLongClickCallback?.let { block ->
            helper.itemView.setOnLongClickListener {
                val position = helper.bindingAdapterPosition
                block(getItem(position), position)
                return@setOnLongClickListener true
            }
        }
    }

    /**
     * 设置点击事件
     * 最好在onCreateViewHolder()或onViewCreated()中设置
     */
    fun setChildClickEvent(helper: BaseViewHolder, view: View) {
        itemChildClickLists?.get(view.id)?.let { block ->
            view.setOnClickListener {
                val position = helper.bindingAdapterPosition
                block(getItem(position), position, it.id)
            }
        }
    }

    fun setStatusAdapter(adapter: StatusAdapter) {
        this.statusAdapter = adapter
    }

    fun setState(@State state: Int) {
        if (this.state!=State.STATE_NORMAL&&this.state!=state){
            if (state!=State.STATE_NORMAL){
                notifyItemChanged(0)
            }else{
                notifyItemRemoved(0)
            }
            this.state = state
        }
    }

}