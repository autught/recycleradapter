package com.recyclerview.adapter

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/20 14:20
 */
class CollectionModifiedModel<T : Any>(
    private val updateCallback: ListUpdateCallback
) : CollectionsModel<T>(updateCallback) {

    fun addItem(t: T) {
        val lastCount = itemCount
        mData.add(t)
        updateCallback.onInserted(lastCount, 1)
    }

    fun addItems(ts: List<T>) {
        val lastCount = itemCount
        mData.addAll(ts)
        updateCallback.onInserted(lastCount, ts.count())
    }

    fun removeItem(t: T) {
        val index = mData.indexOf(t)
        if (mData.remove(t)) {
            updateCallback.onRemoved(index, 1)
            if (mData.isEmpty()){
                originState=State.Normal(true)
                updateCallback.onInserted(0, 1)
            }
        }
    }

    fun setItem(index: Int, t: T) {
        mData[index] = t
        updateCallback.onChanged(index, 1, null)
    }

    fun modifyItem(index: Int, block: (T) -> Unit) {
        block.invoke(mData[index])
        updateCallback.onChanged(index, 1, null)
    }

    fun setItemPayloads(index: Int, t: T) {
        mData[index] = t
        updateCallback.onChanged(index, 1, 1)
    }

    fun modifyItemPayloads(index: Int, block: (T) -> Unit) {
        block.invoke(mData[index])
        updateCallback.onChanged(index, 1, 1)
    }
}