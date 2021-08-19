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
    private val mData = mutableListOf<T>()

    override val itemCount: Int
        get() = mData.count()

    override fun submitData(data: Collection<T>) {
        val originSize = itemCount
        mData.clear()
        mData.addAll(data)
        // notifyDataChanged
        if (originSize > data.count()) {
            val differ = originSize - data.count()
            updateCallback.onRemoved(data.count(), differ)
            if (data.count() > 0) {
                updateCallback.onChanged(0, data.count(), null)
            }
        } else if (originSize < data.count()) {
            val differ = data.count() - originSize
            updateCallback.onInserted(originSize, differ)
            if (originSize > 0) {
                updateCallback.onChanged(0, originSize, null)
            }
        }
        if (originSize > 0) {
            updateCallback.onChanged(0, originSize, null)
        }
    }

    override fun getItem(index: Int): T {
        require(index in 0 until itemCount) {
            "index 传值超域"
        }
        return mData[index]
    }

    override fun getCollection(): List<T> {
        return mData
    }
}