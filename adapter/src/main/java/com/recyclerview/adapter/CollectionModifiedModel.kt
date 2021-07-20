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
        getCollection().toMutableList().add(t)
        updateCallback.onInserted(lastCount, 1)
    }

    fun addItems(ts: List<T>) {
        val lastCount = itemCount
        getCollection().toMutableList().addAll(ts)
        updateCallback.onInserted(lastCount, ts.count())
    }

    fun removeItem(t: T) {
        val index = getCollection().toMutableList().indexOf(t)
        if (getCollection().toMutableList().remove(t)) {
            updateCallback.onRemoved(index, 1)
        }
    }

    fun setItem(index: Int, t: T) {
        getCollection().toMutableList()[index] = t
        updateCallback.onChanged(index, 1, null)
    }

    fun modifyItem(index: Int, block: (T) -> Unit) {
        block.invoke(getCollection().toMutableList()[index])
        updateCallback.onChanged(index, 1, null)
    }

    fun setItemPayloads(index: Int, t: T) {
        getCollection().toMutableList()[index] = t
        updateCallback.onChanged(index, 1, 1)
    }

    fun modifyItemPayloads(index: Int, block: (T) -> Unit) {
        block.invoke(getCollection().toMutableList()[index])
        updateCallback.onChanged(index, 1, 1)
    }
}