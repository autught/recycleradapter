package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * @description:
 * @author:  79120
 * @date :   2021/8/24 17:18
 */
abstract class StatusAdapter<VH : RecyclerView.ViewHolder>(
    private val factory: IViewHolderFactory<VH>
) : RecyclerView.Adapter<VH>() {
    @State
    var state: Int = State.STATE_NORMAL
    private var inflaterRef: WeakReference<LayoutInflater>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = inflaterRef?.get() ?: LayoutInflater.from(parent.context)
        return factory.onCreate(inflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBind(holder, state)
    }

    abstract fun onBind(helper: VH, @State state: Int)

    override fun getItemCount(): Int {
        return 1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = WeakReference(LayoutInflater.from(recyclerView.context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.inflaterRef = null
    }
}