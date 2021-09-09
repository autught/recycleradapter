package com.recyclerview.adapter

import android.annotation.SuppressLint
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
    private var state: State? = null

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
        return if (isNormal()) {
            onViewHolderCreated(inflater, parent).also { setItemClickEvent(it) }
        } else {
            requireNotNull(statusAdapter).onCreateViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isNormal()) {
            onBindViewHolder(holder, getItem(position), position)
        } else {
            requireNotNull(statusAdapter).onBindViewHolder(holder, state!!)
        }
    }

    override fun getItemCount(): Int {
        return when {
            isNormal() -> super.getItemCount()
            state != null -> 1
            else -> 0
        }
    }

    private fun isNormal(): Boolean {
        return state is State.Normal && !(state as State.Normal).isEmpty
    }

    fun setStatusAdapter(adapter: StatusAdapter) {
        this.statusAdapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setState(state: State) {
        if (state.javaClass.hashCode() == this.state?.javaClass.hashCode()) return
        this.state = state
        notifyDataSetChanged()
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
        setState(State.Normal(list.isNullOrEmpty()))
        super.submitList(list)
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        setState(State.Normal(list.isNullOrEmpty()))
        super.submitList(list, commitCallback)
    }
}