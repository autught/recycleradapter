package com.recycleradapter

import com.recyclerview.adapter.*

/**
 * @description:
 * @author:  79120
 * @date :   2021/7/13 17:39
 */
class Test {
fun test(adapter:RecyclerAdapter<*,*>){
    val testAdapter=PolyRecyclerAdapter(C1(),C2())
}


    private inner class C1: LayoutController<Any>(R.layout.item_test) {
        override fun bind(data: Any, holder: BaseViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }

    private inner class C2: LayoutController<Any>(R.layout.item_test) {
        override fun bind(data: Any, holder: BaseViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
    }


}