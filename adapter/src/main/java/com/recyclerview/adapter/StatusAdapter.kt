package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @description:
 * @author:  79120
 * @date :   2021/8/24 17:18
 */
abstract class StatusAdapter {
    @State
    private var state: Int = State.STATE_NORMAL

    abstract fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View

    abstract fun onBind(@State state: Int)

    fun onViewRecycled() {}
}