package com.recyclerview.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @description:不可变数据变换相应类
 * @author:  79120
 * @date :   2021/6/23 15:05
 */
open class DiffUtilModel<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val updateCallback: ListUpdateCallback
) : IRecyclerModel<T> {
    private val differBase = AsyncListDiffer(
        updateCallback,
        AsyncDifferConfig.Builder(diffCallback).build()
    )
    protected lateinit var originState: State

    override val itemCount: Int
        get() = if (::originState.isInitialized && (originState !is State.Normal
                    || (originState as State.Normal).isEmpty)
        ) 1 else differBase.currentList.size

    override fun submitData(data: List<T>) {
        if (::originState.isInitialized && (originState !is State.Normal
                    || (originState as State.Normal).isEmpty)
        ) {
            updateCallback.onRemoved(0, 1)
        }
        originState = State.Normal(false)
        differBase.submitList(data)
        if (data.isEmpty()) {
            originState = State.Normal(true)
            updateCallback.onInserted(0, 1)
        }
    }

    override fun submitState(state: State) {
        when (state) {
            is State.Loading -> {
                if (!::originState.isInitialized) {
                    this.originState = state
                    updateCallback.onInserted(0, 1)
                } else if (originState is State.Error || (originState is State.Normal && (originState as State.Normal).isEmpty)) {
                    this.originState = state
                    updateCallback.onChanged(0, 1, null)
                }
            }
            is State.Error -> {
                if (!::originState.isInitialized) {
                    this.originState = state
                    updateCallback.onInserted(0, 1)
                } else if (originState is State.Loading || (originState is State.Normal && (originState as State.Normal).isEmpty)) {
                    this.originState = state
                    updateCallback.onChanged(0, 1, null)
                }
            }
            is State.Normal -> {
                require(state.isEmpty) { "please use [ submitData() ]  to notify data changed" }
                if (!::originState.isInitialized) {
                    this.originState = state
                    updateCallback.onInserted(0, 1)
                } else if (originState is State.Loading || originState is State.Error) {
                    this.originState = state
                    updateCallback.onChanged(0, 1, null)
                } else if (originState is State.Normal && !(originState as State.Normal).isEmpty) {
                    submitData(emptyList())
                }
            }
        }
    }

    override fun getItem(index: Int): T {
        require(index < itemCount)
        return differBase.currentList[index]
    }

    override fun getCurrentList(): List<T> {
        return differBase.currentList
    }

    override fun getCurrentState(): State {
        return originState
    }
}