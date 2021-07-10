package com.recycleradapter

import com.recycleradapter.databinding.ItemTestBinding
import com.recyclerview.adapter.ViewBindingAdapter
import com.recyclerview.adapter.ViewBindingHolder

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/9 10:42
 */
class Test : ViewBindingAdapter<Any,ItemTestBinding>({ inflater, group ->
    ItemTestBinding.inflate(
        inflater,
        group,
        false
    )
}) {
    override fun bind(data: Any, holder: ViewBindingHolder<ItemTestBinding>) {
        holder.binding.container
    }


}