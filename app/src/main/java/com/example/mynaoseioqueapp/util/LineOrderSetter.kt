package com.example.mynaoseioqueapp.util

import android.util.Log
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderResponse
import com.example.mynaoseioqueapp.domain.model.LineOrder
import java.math.BigDecimal
import java.text.FieldPosition

class LineOrderSetter {
    companion object {
        private val lineOrderList: ArrayList<LineOrder> = ArrayList()

        private var isOnCart: Boolean = false

//        private var totalPrice: BigDecimal = BigDecimal(0)

        private var lineOrderIndex: Int = 0

        fun setLineOrderList(lineOrderToBeSaved: LineOrder) {
            addOnCart(lineOrderToBeSaved)
        }

        fun isLineOrderListEmpty(): Boolean {
            return lineOrderList.isEmpty()
        }

        fun getLineOrderList(): ArrayList<LineOrder> {
            return lineOrderList
        }

        fun getTotalPrice(): BigDecimal {
            var totalPrice: BigDecimal = BigDecimal(0)
            if(lineOrderList.isEmpty()){
                return BigDecimal(0)
            } else {
                lineOrderList.forEach { lineOrder ->
                    totalPrice += lineOrder.price.multiply(lineOrder.quantity.toBigDecimal())
                }
                return totalPrice
            }
        }
//
//        fun getPriceWhenSubIsPressed (): BigDecimal {
//            lineOrderList.forEach {
//                totalPrice = it.partialPrice
//                totalPrice -= it.price
//            }
//            return totalPrice
//        }

//        fun getTotalPrice(): BigDecimal {
//            var totalPriceTest = BigDecimal(0)
//            if (lineOrderList.isEmpty()) {
//                return BigDecimal(0)
//            } else {
//                lineOrderList.forEach {
//                    totalPriceTest = it.price.multiply(it.quantity.toBigDecimal())
//                }
//                return totalPriceTest
//            }
//        }

        fun getLineOrderEspecificPrice(lineOrder: LineOrder) : BigDecimal {
            val lineOrder = lineOrderList[lineOrderList.indexOf(lineOrder)]
            return lineOrder.price.multiply(lineOrder.quantity.toBigDecimal())
        }

        private fun addOnCart(lineOrderToBeSaved: LineOrder) {
            var quantity = 0
            lineOrderList.forEachIndexed { index, lineOrder ->
                if (lineOrder.name == lineOrderToBeSaved.name) {
                    isOnCart = true
                    lineOrderIndex = index
                    quantity = lineOrderToBeSaved.quantity
                    return@forEachIndexed
                }
            }

            if (isOnCart) {
                lineOrderList[lineOrderIndex].quantity += quantity
                isOnCart = false
            } else {
                lineOrderList.add(lineOrderToBeSaved)
            }
        }

        fun removeFromCart(position: Int) {
//            Log.d("MyLineOrder", lineOrderToBeRemoved.name)
//            lineOrderList.remove(lineOrderToBeRemoved)
            lineOrderList.removeAt(position)
            lineOrderList.forEach {
                Log.d("LineOrderList", it.name)
            }
        }

        fun cleanCart() {
            lineOrderList.clear()
        }

        fun subQuantity(lineOrder: LineOrder) {
            lineOrder.quantity -= 1
        }

        fun addQuantity(lineOrder: LineOrder) {
            lineOrder.quantity += 1
        }

        fun orderAgain(lineOrderListToSave: List<LineOrderResponse>) {
            lineOrderList.clear()
            lineOrderListToSave.forEach {
                lineOrderList.add(LineOrder(it.food.name, it.quantity, it.food.price, BigDecimal(0), it.food.url))
            }
        }
    }
}