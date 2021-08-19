package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference
import java.net.URI.create

/**
 * @description:
 * @author:  79120
 * @date :   2021/6/21 15:49
 */
abstract class RecyclerAdapter<T : Any, VH : RecyclerView.ViewHolder>(private val produce: IViewHolderProduce<VH>) : RecyclerView.Adapter<VH>(){
    private var weakInflater: WeakReference<LayoutInflater>? = null
    private val updateCallback by lazy { AdapterListUpdateCallback(this) }
    private var recycler: IRecyclerModel<T> = CollectionsModel(updateCallback)
    private var onItemClickCallback: OnItemClickCallback<T>? = null
    private var onItemLongClickCallback: OnItemLongClickCallback<T>? = null
    private var onItemChildClickCallbacks: SparseArrayCompat<OnItemChildClickCallback<T>>? = null

    fun <M : IRecyclerModel<T>> setRecyclerModel(block: ((ListUpdateCallback) -> M)) {
        this.recycler = block.invoke(updateCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = weakInflater?.get() ?: LayoutInflater.from(parent.context)
        return produce.onProduce(inflater,parent, viewType)
    }

    override fun getItemCount(): Int = recycler.itemCount

    fun getItem(index: Int): T = recycler.getItem(index)

    fun submitData(data: Collection<T>) {
        recycler.submitData(data)
    }

    fun getCollection(): List<T> {
        return recycler.getCollection()
    }

//    fun setOnItemClickCallback(callback: OnItemClickCallback<T>) {
//        this.onItemClickCallback = callback
//    }
//
//    fun setOnItemLongClickCallback(callback: OnItemLongClickCallback<T>) {
//        this.onItemLongClickCallback = callback
//    }
//
//    fun setOnItemChildClickCallback(@IdRes id: Int, callback: OnItemChildClickCallback<T>) {
//        if (onItemChildClickCallbacks == null) onItemChildClickCallbacks = SparseArrayCompat()
//        onItemChildClickCallbacks?.put(id, callback)
//    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.weakInflater = null
    }

//    private fun RecyclerView.ViewHolder.handle() {
//        onItemClickCallback?.let {
//            handleClickBound(this, this.itemView) { d, i -> it.handle(d, i) }
//        }
//        onItemChildClickCallbacks?.let { sac ->
//            for (index in 0 until sac.size()) {
//                val view = this.itemView.findViewById<View>(sac.keyAt(index))
//                handleClickBound(this, view) { d, i ->
//                    sac.valueAt(index).handle(view, d, i)
//                }
//            }
//        }
//        onItemLongClickCallback?.let {
//            handleLongClickBound(this, this.itemView) { d, i -> it.handle(d, i) }
//        }
//    }
//
//    private fun handleClickBound(
//        helper: RecyclerView.ViewHolder,
//        view: View?,
//        block: (T, Int) -> Unit
//    ) {
//        view?.setOnClickListener {
//            val position = helper.bindingAdapterPosition
//            if (position in 1 until itemCount) {
//                block.invoke(getItem(position), position)
//            }
//        }
//    }
//
//    private fun handleLongClickBound(
//        helper: RecyclerView.ViewHolder,
//        view: View?,
//        block: (T, Int) -> Unit
//    ) {
//        view?.setOnLongClickListener {
//            val position = helper.bindingAdapterPosition
//            if (position in 1 until itemCount) {
//                block.invoke(getItem(position), position)
//            }
//            return@setOnLongClickListener true
//        }
//    }
}