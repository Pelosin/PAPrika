package com.example.mynaoseioqueapp.util

import com.example.mynaoseioqueapp.domain.model.LineOrder

class LineOrderSetter {
    companion object{
        private val lineOrderList: ArrayList<LineOrder> = ArrayList()

        private var previousLineOrder: LineOrder = LineOrder("", 1)

        fun setLineOrderList(lineOrderToBeSaved: LineOrder){
            if (lineOrderToBeSaved.equals(previousLineOrder))
            {
                lineOrderList.get(lineOrderList.indexOf(lineOrderToBeSaved)).quantity += 1
            }
            else
            {
                lineOrderList.add(lineOrderToBeSaved)
                previousLineOrder = lineOrderToBeSaved
            }
        }

        fun getLineOrderList() : ArrayList<LineOrder>{
            return lineOrderList
        }
    }
}