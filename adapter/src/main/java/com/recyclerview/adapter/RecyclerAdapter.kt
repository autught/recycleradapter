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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = weakInflater?.get() ?: LayoutInflater.from(parent.context)
        return if (statusAdapter==null||statusAdapter!!.normal()) {
            onViewHolderCreated(inflater, parent, viewType).also { setItemClickEvent(it) }
        } else {
            requireNotNull(statusAdapter).onCreateViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (statusAdapter==null||statusAdapter!!.normal()) {
            onBindViewHolder(holder, getItem(position), position)
        } else {
            requireNotNull(statusAdapter).onBindViewHolder(holder)
        }
    }

    open fun onViewHolderCreated(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layout!!, parent, false))
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, data: T, position: Int)

    override fun getItemCount(): Int {
        val count = recycler.itemCount
        return if (count == 0 && statusAdapter?.normal()==false) {
            statusAdapter!!.getItemCount()
        } else {
            count
        }
    }

    fun getItem(index: Int): T = recycler.getItem(index)

    fun submitData(data: List<T>) {
        if (data.isEmpty())
            setState(StatusAdapter.LOAD_EMPTY)
        recycler.submitData(data)
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

    fun setStatusAdapter(adapter: StatusAdapter) {
        this.statusAdapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setState(state: Int) {
        if (statusAdapter?.setState(state) == true) {
            notifyDataSetChanged()
        }
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

}