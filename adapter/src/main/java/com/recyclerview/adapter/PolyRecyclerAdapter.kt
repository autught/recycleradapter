package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

/**
 * @description: 多布局
 * @author:  79120
 * @date :   2021/7/8 23:20
 */
open class PolyRecyclerAdapter(
    vararg controllers: IRecyclerController<out Any, out RecyclerView.ViewHolder>,
) : RecyclerAdapter<Any, RecyclerView.ViewHolder>() {
    private val models = SparseArrayCompat<IRecyclerController<Any, RecyclerView.ViewHolder>>()
    private var block: ((Any, Int) -> Int?)

    init {
        val typeMap = ArrayMap<Class<*>, Int>()
        @Suppress("UNCHECKED_CAST")
        controllers.forEachIndexed { index, item ->
            models.takeIf { !it.containsKey(index) }?.put(
                index,
                item as IRecyclerController<Any, RecyclerView.ViewHolder>
            )
            val parameterizedType = item.javaClass.genericSuperclass as ParameterizedType
            typeMap[parameterizedType.actualTypeArguments[0] as Class<*>] = index
        }
        block = { t, _ -> typeMap[t.javaClass] }
    }

    fun setupItemType(block: ((Any, Int) -> Int?)) {
        this.block = block
    }

    override fun getItemViewType(position: Int): Int {
        return block.invoke(requireNotNull(getItem(position)), position)
            ?: super.getItemViewType(position)
    }

    override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        type: Int,
    ): RecyclerView.ViewHolder {
        return requireNotNull(models[type]).create(inflater, parent, type)
    }

    override fun bind(data: Any, holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        require(models.containsKey(type))
        models[type]?.bind(data, holder, position)
    }

    override fun bindPayloads(data: Any, holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        require(models.containsKey(type))
        models[type]?.bindPayloads(data, holder, position)
    }
}