package com.recyclerview.adapter

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/30 10:16
 */
sealed class State {
    object Loading : State()
    class Normal(val isEmpty: Boolean) : State()
    object Error : State()
}