package com.recyclerview.adapter

import androidx.annotation.IntDef

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/30 10:16
 */
@IntDef(
    State.STATE_LOADING,
    State.STATE_ERROR,
    State.STATE_NORMAL,
    State.STATE_EMPTY
)
@Retention(AnnotationRetention.SOURCE)
annotation class State {
    companion object {
        const val STATE_LOADING = 0
        const val STATE_ERROR = 1
        const val STATE_NORMAL = 2
        const val STATE_EMPTY = 3
    }
}