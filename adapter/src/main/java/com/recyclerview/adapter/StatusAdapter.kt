package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * @description:
 * @author:  79120
 * @date :   2021/8/24 17:18
 */
abstract class StatusAdapter {
    private var loadState: Int = LOAD_NONE

    fun setState(state: Int) {
        if (this.loadState == state) return
        val temp = loadState
        this.loadState = state
        if (temp != LOAD_NONE) {
            if (state != LOAD_NONE) {
//                notifyItemChanged(0)
            } else {

            }
        } else {
//            notifyItemInserted(0)
        }
    }

    fun isAbnormal(): Boolean {
        return loadState == LOADING || loadState == LOAD_EMPTY || loadState == LOAD_ERROR
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder

    abstract fun onBindViewHolder(helper: BaseViewHolder)

    fun getItemCount() = 0

    companion object {
        const val LOAD_NONE = 0
        const val LOADING = 1
        const val LOAD_ERROR = 2
        const val LOAD_EMPTY = 3
    }

}