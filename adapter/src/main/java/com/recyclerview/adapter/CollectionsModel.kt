package com.recyclerview.adapter

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @description:可变数据操控类
 * @author:  79120
 * @date :   2021/6/23 14:26
 */
open class CollectionsModel<T : Any>(
    private val updateCallback: ListUpdateCallback,
) : IRecyclerModel<T> {
    protected val mData = mutableListOf<T>()

    override val itemCount: Int
        get() = mData.size

    override fun submitData(data: List<T>) {
        val originSize = mData.size
        mData.clear()
        mData.addAll(data)
        // notifyDataChanged
        if (originSize > data.size) {
            val differ = originSize - data.size
            updateCallback.onRemoved(data.size, differ)
            updateCallback.onChanged(0, data.size, null)
        } else if (originSize < data.size) {
            val differ = data.size - originSize
            updateCallback.onInserted(originSize, differ)
            if (originSize > 0) {
                updateCallback.onChanged(0, originSize, null)
            }
        } else {
            if (originSize > 0) {
                updateCallback.onChanged(0, originSize, null)
            }
        }
    }

    override fun getItem(index: Int): T {
        require(index in 0..itemCount) {
            "index 传值超域"
        }
        return mData[index]
    }

    override fun getCurrentList(): List<T> {
        return mData
    }
}