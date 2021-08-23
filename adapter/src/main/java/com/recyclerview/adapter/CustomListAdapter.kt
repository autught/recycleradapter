package com.recyclerview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.BuildConfig
import java.lang.ref.WeakReference

abstract class CustomListAdapter<T, VH : RecyclerView.ViewHolder>(
    private val produce: IViewHolderProduce<VH>,
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(callback) {
    private var inflaterRef: WeakReference<LayoutInflater>? = null
    private var itemClickPair: Pair<RecyclerView.ViewHolder.() -> Int, (T, Int) -> Unit>? = null
    private var stateAdapter: StateAdapter<VH>? = null

    fun setOnItemClickCallback(
        positionGet: RecyclerView.ViewHolder.() -> Int,
        clickEvent: ((T, Int) -> Unit),
    ) {
        this.itemClickPair = positionGet to clickEvent
    }

    abstract fun onBindViewHolder(holder: VH, data: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = inflaterRef?.get() ?: LayoutInflater.from(parent.context)
        return if (stateAdapter?.state != StateAdapter.STATE_SUCCESS) {
            stateAdapter!!.onCreateStateViewHolder(inflater, parent)
        } else {
            produce.onProduce(inflater, parent, viewType).also { handleClickEvent(it) }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (stateAdapter?.state != StateAdapter.STATE_SUCCESS) {
            stateAdapter!!.onBindStateViewHolder(holder)
        } else {
            onBindViewHolder(holder, getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (stateAdapter?.state != StateAdapter.STATE_SUCCESS) {
            stateAdapter!!.getStateItemType()
        } else {
            produce.getItemType()
        }
    }

    private fun handleClickEvent(vh: RecyclerView.ViewHolder) {
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

    /**仅仅当提交的数据为空时，state才有效*/
    override fun submitList(list: List<T>?) {
        if (list.isNullOrEmpty()) {
            stateAdapter?.state = StateAdapter.STATE_EMPTY
        }
        super.submitList(list)
    }

    /**仅仅当提交的数据为空时，state才有效*/
    override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        if (!list.isNullOrEmpty()) stateAdapter?.state = StateAdapter.STATE_EMPTY
        super.submitList(list, commitCallback)
    }

    /**仅仅当列表数据为空时，state才有效*/
//    override fun setNewState(vararg state: Int) {
//        if (currentList.isEmpty()) {
//            val lastSize = this.state?.count() ?: 0
//            val currentSize = state.count()
//            this.state = state
//            if (lastSize < currentSize) {
//                notifyItemRangeInserted(lastSize, currentSize - lastSize)
//            } else if (lastSize > currentSize) {
//                notifyItemRangeRemoved(currentSize, lastSize - currentSize)
//            }
//            if (lastSize != 0) {
//                notifyItemRangeChanged(0, lastSize)
//            }
//        }
//    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = null
    }
}