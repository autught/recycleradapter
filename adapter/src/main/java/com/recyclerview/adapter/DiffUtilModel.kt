package com.recyclerview.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @description:
 * @author:  79120
 * @date :   2021/6/23 15:05
 */
class DiffUtilModel<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    updateCallback: ListUpdateCallback,
) : IRecyclerModel<T> {
    private val differBase = AsyncListDiffer(
        updateCallback,
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    override val itemCount: Int
        get() = differBase.currentList.count()

    override fun submitData(data: Collection<T>) {
        differBase.submitList(data.toMutableList())
    }

    override fun getItem(index: Int): T? {
        require(index < itemCount)
        return differBase.currentList[index]
    }
}