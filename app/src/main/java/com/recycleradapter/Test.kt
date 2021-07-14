package com.recycleradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.recycleradapter.databinding.ItemTestBinding
import com.recyclerview.adapter.BaseViewHolder
import com.recyclerview.adapter.CustomController
import com.recyclerview.adapter.IRecyclerController
import com.recyclerview.adapter.RecyclerAdapter

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/13 17:39
 */
class Test {
    fun test(){
RecyclerAdapter.create(object :IRecyclerController<Any,BaseViewHolder>{})
    .setHandleClickEvent { any, i ->  }
    }

    private inner class ChildController: CustomController<Any>(ItemTestBinding::class.java) {
        override fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            type: Int
        ): RecyclerView.ViewHolder {
            return super.create(inflater, parent, type)
        }
        override fun bind(data: Any, holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }
}