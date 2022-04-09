package com.example.mynaoseioqueapp.util

import android.util.Log
import com.example.mynaoseioqueapp.domain.model.LineOrder
import java.math.BigDecimal

class LineOrderSetter {
    companion object {
        private val lineOrderList: ArrayList<LineOrder> = ArrayList()

//        private var previousLineOrder: LineOrder = LineOrder("", 1, BigDecimal(0))

        private var isOnCart: Boolean = false

        private var totalPrice: BigDecimal = BigDecimal(0)

        private var lineOrderIndex: Int = 0

        fun setLineOrderList(lineOrderToBeSaved: LineOrder, priceToBeSaved: BigDecimal) {

            addOnCart(lineOrderToBeSaved)

//            if(lineOrderList.isEmpty()){
//                lineOrderList.add(lineOrderToBeSaved)
//            }else{
//                lineOrderList.forEach { lineOrder ->
//                    if(lineOrder.name == lineOrderToBeSaved.name){
//                        lineOrder.quantity += 1
//                        lineOrder.price.plus(lineOrderToBeSaved.price)
//                        return@forEach
//                    }else{
//                        lineOrderList.add(lineOrderToBeSaved)
//                        return@forEach
//                    }
//                }
//            }
//            lineOrderList.forEach { lineOrder ->
//                isOnCart = lineOrder.name == lineOrderToBeSaved.name
//            }

//            if (isOnCart)
//            {
////                val index:Int = lineOrderList.indexOf(lineOrderToBeSaved)
////                lineOrderList[index].quantity += 1
//////                lineOrderToBeSaved.quantity += 1
////                lineOrderList[index].price.plus(lineOrderToBeSaved.price)
////                totalPrice += priceToBeSaved
//            }
//            else
//            {
//                lineOrderList.add(lineOrderToBeSaved)
////                previousLineOrder = lineOrderToBeSaved
//                totalPrice += priceToBeSaved
//            }
        }

        fun isLineOrderListEmpty(): Boolean {
            return lineOrderList.isEmpty()
        }

        fun getLineOrderList(): ArrayList<LineOrder> {
            // mudar isso depois para um metodo separado
//            lineOrderList.forEach { lineOrder ->
//                lineOrder.price = lineOrder.price * (lineOrder.quantity.toBigDecimal())
//                totalPrice.plus(lineOrder.price)
//            }
            return lineOrderList
        }

        fun getTotalPrice(): BigDecimal {
            lineOrderList.forEach { lineOrder ->
                totalPrice += lineOrder.price
            }
            return totalPrice
        }

        fun addOnCart(lineOrderToBeSaved: LineOrder) {
            lineOrderList.forEachIndexed { index, lineOrder ->
                if (lineOrder.name == lineOrderToBeSaved.name) {
                    isOnCart = true
                    lineOrderIndex = index
                    return@forEachIndexed
                }
            }

            if (isOnCart) {
                lineOrderList[lineOrderIndex].quantity += 1
//                lineOrderList[lineOrderIndex].price =
//                    lineOrderList[lineOrderIndex].price.plus(
//                        lineOrderList[lineOrderIndex].price)
//                totalPrice = lineOrderList[lineOrderIndex].price
                isOnCart = false
            } else {
                lineOrderList.add(lineOrderToBeSaved)
            }
        }
    }
}