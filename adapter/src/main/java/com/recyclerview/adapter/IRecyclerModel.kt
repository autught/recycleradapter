package com.recyclerview.adapter

import androidx.annotation.IntRange

/**
 * @description:
 * @author:  79120
 * @date :   2021/6/23 14:10
 */
interface IRecyclerModel<T> {
    val itemCount: Int

    fun submitData(data: Collection<T>)

    fun getItem(@IntRange(from = 0) index: Int): T?
}