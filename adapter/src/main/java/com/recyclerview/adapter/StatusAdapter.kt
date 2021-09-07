package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @description:
 * @author:  79120
 * @date :   2021/8/24 17:18
 */
abstract class StatusAdapter {
    @State
    private var state: Int = State.STATE_NORMAL

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder

    abstract fun onBindViewHolder(helper: BaseViewHolder, @State state: Int)

    fun onViewRecycled() {}
}