package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/30 10:16
 */
abstract class StateAdapter<VH : RecyclerView.ViewHolder> {
    @State
    var state: Int = STATE_NONE

    abstract fun onCreateStateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    abstract fun onBindStateViewHolder(helper: VH)

    abstract fun getStateItemType(): Int

    companion object {
        const val STATE_NONE = 0
        const val STATE_LOADING = 1
        const val STATE_FAILURE = 2
        const val STATE_SUCCESS = 3
        const val STATE_EMPTY = 4
    }

    @IntDef(
        STATE_NONE,
        STATE_LOADING,
        STATE_FAILURE,
        STATE_SUCCESS,
        STATE_EMPTY
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class State
}