package com.recycleradapter

import androidx.recyclerview.widget.DiffUtil
import com.recyclerview.adapter.BaseViewHolder
import com.recyclerview.adapter.CustomListAdapter

/**
 * @description:
 * @author:  79120
 * @date :   2021/9/9 10:48
 */
class Test:CustomListAdapter<String>(StringDifferCallback()) {
    override fun onBindViewHolder(holder: BaseViewHolder, data: String, position: Int) {
        TODO("Not yet implemented")
    }

}

class StringDifferCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return true
    }
}