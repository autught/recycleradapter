package com.recyclerview.adapter

import android.view.View
import androidx.collection.SparseArrayCompat

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/15 10:31
 */
typealias  ClickEvent<A> = (A?, Int) -> Unit

interface OnAdapterClickListener<T> {
    var handleClickEvent: (T?.(Int) -> Unit)?
    var handleLongClickEvent: ClickEvent<T>?
    var handleChildClickPairs: SparseArrayCompat<ClickEvent<T>>?

    fun setHandleChildClickEvent(resId: Int, event: ClickEvent<T>) {
        if (handleChildClickPairs == null) {
            handleChildClickPairs = SparseArrayCompat()
        }
        handleChildClickPairs?.takeIf { !it.containsKey(resId) }?.put(resId, event)
    }

    fun ClickEvent<T>.handleClickEvent(view: View, data: T?, position: Int) {
        view.setOnClickListener { invoke(data, position) }
    }

    fun ClickEvent<T>.handleLongClickEvent(view: View, data: T?, position: Int) {
        view.setOnLongClickListener {
            invoke(data, position)
            return@setOnLongClickListener true
        }
    }
}