package com.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import java.lang.reflect.ParameterizedType

/**
 * @description: 多布局
 * @author:  79120
 * @date :   2021/7/8 23:20
 */
open class ComplexRecyclerAdapter<in RA:RecyclerAdapter<in Any>>(
    vararg controllers: RA,
    block: ((Any, Int) -> Int?)? = null
) : RecyclerAdapter<Any>() {
    private val models = SparseArrayCompat<RecyclerAdapter<Any>>()
    private val createTypeFunc: ((Any, Int) -> Int?)

    /**
     * 将controllers数组中item的下标作为type
     */
    init {
        val typeMap = ArrayMap<Class<*>, Int>()
        controllers.forEachIndexed { index, item ->
            models.takeIf { !it.containsKey(index) }
                ?.put(index, item)
            val parameterizedType = item.javaClass.genericSuperclass as ParameterizedType
            typeMap[parameterizedType.actualTypeArguments[0] as Class<*>] = index
        }
        createTypeFunc = block ?: { t, _ -> typeMap[t.javaClass] }
    }

    override fun getItemViewType(position: Int): Int {
        return createTypeFunc.invoke(getItem(position), position)
            ?: super.getItemViewType(position)
    }

    override fun onViewHolderCreated(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return requireNotNull(models[viewType]).onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, data: Any, position: Int) {
        val type = getItemViewType(position)
        models[type]?.onBindViewHolder(holder, data, position)
    }

}