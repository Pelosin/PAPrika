package com.example.mynaoseioqueapp.domain.mapper

import com.example.mynaoseioqueapp.data.remote.dto.LineOrderRequest
import com.example.mynaoseioqueapp.domain.model.LineOrder

class LineOrderMapper {
    companion object{
        fun mapLineOrderListToLineOrderRequestList(lineOrderList: List<LineOrder>) : List<LineOrderRequest> {
            val lineOrderRequestList: MutableList<LineOrderRequest> = ArrayList()
            try {
                lineOrderList.forEach { lineOrder ->
                    lineOrderRequestList.add(LineOrderRequest(lineOrder.name, lineOrder.quantity))
                }
            } catch (e: Exception) {
                return emptyList()
            }
            return lineOrderRequestList
        }

    }
}