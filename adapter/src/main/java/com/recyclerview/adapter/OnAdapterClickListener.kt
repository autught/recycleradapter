package com.recyclerview.adapter

import android.view.View

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/15 10:31
 */
fun interface OnItemClickCallback<T> {
    fun handle(t: T, position: Int)
}

fun interface OnItemLongClickCallback<T> {
    fun handle(t: T, position: Int)
}

fun interface OnItemChildClickCallback<T> {
    fun handle(view: View, t: T, position: Int)
}