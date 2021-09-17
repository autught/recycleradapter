package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class CustomListAdapter<T> @JvmOverloads constructor(
    callback: DiffUtil.ItemCallback<T>,
    private val layout: Int? = null,
) : ListAdapter<T, BaseViewHolder>(callback) {
    private var inflaterRef: WeakReference<LayoutInflater>? = null
    private var itemChildClickLists: SparseArrayCompat<(T, Int, Int) -> Unit>? = null
    private var itemClickCallback: ((T, Int) -> Unit)? = null
    private var itemLongClickCallback: ((T, Int) -> Unit)? = null
    private var statusAdapter: StatusAdapter? = null

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

    open fun onViewHolderCreated(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder {
        return BaseViewHolder(inflater.inflate(layout!!, parent, false))
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, data: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = inflaterRef?.get() ?: LayoutInflater.from(parent.context)
        return   if (statusAdapter?.isAbnormal()==false) {
            onViewHolderCreated(inflater, parent).also { setItemClickEvent(it) }
        } else {
            requireNotNull(statusAdapter).onCreateViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (statusAdapter?.isAbnormal()==false) {
            onBindViewHolder(holder, getItem(position), position)
        } else {
            requireNotNull(statusAdapter).onBindViewHolder(holder)
        }
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if (count == 0 && statusAdapter != null) {
            statusAdapter!!.getItemCount()
        } else {
            count
        }
    }

    fun setStatusAdapter(adapter: StatusAdapter) {
        this.statusAdapter = adapter
    }

    fun setState( state: Int) {
        if (statusAdapter?.setState(state)!=null) {
            notifyDataSetChanged()
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        inflaterRef = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        inflaterRef = null
    }

    override fun submitList(list: MutableList<T>?) {
        if (loadState != State.STATE_NORMAL) {
            notifyItemRemoved(0)
        }
        super.submitList(list)
        if (list.isNullOrEmpty()) {
            setState(State.STATE_EMPTY)
        }
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
        if (list.isNullOrEmpty()) {
            setState()
        }
    }
}