package com.recyclerview.adapter

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @description:可变数据操控类
 * @author:  79120
 * @date :   2021/6/23 14:26
 */
open class CollectionsModel<T : Any>(
    private val updateCallback: ListUpdateCallback,
) : IRecyclerModel<T> {
    protected val mData = mutableListOf<T>()
    protected lateinit var originState: State

    override val itemCount: Int
        get() = if (::originState.isInitialized && (originState !is State.Normal
                    || (originState as State.Normal).isEmpty)
        ) 1 else mData.size

    override fun submitData(data: List<T>) {
        if (::originState.isInitialized && (originState !is State.Normal
                    || (originState as State.Normal).isEmpty)
        ) {
            updateCallback.onRemoved(0, 1)
        }
        originState = State.Normal(false)
        val originSize = mData.size
        mData.clear()
        mData.addAll(data)
        // notifyDataChanged
        if (originSize > data.size) {
            val differ = originSize - data.size
            updateCallback.onRemoved(data.size, differ)
            if (data.isEmpty()) {
                originState = State.Normal(true)
                updateCallback.onInserted(0, 1)
            } else {
                updateCallback.onChanged(0, data.size, null)
            }
        } else if (originSize < data.size) {
            val differ = data.size - originSize
            updateCallback.onInserted(originSize, differ)
            if (originSize > 0) {
                updateCallback.onChanged(0, originSize, null)
            }
        } else {
            if (originSize > 0) {
                updateCallback.onChanged(0, originSize, null)
            } else {
                originState = State.Normal(true)
                updateCallback.onInserted(0, 1)
            }
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
        require(index in 0..itemCount) {
            "index 传值超域"
        }
        return mData[index]
    }

    override fun getCurrentList(): List<T> {
        return mData
    }

    override fun getCurrentState(): State {
        return originState
    }
}