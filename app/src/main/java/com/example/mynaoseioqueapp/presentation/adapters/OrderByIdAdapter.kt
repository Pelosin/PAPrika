package com.example.mynaoseioqueapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderRequest
import com.example.mynaoseioqueapp.data.remote.dto.LineOrderResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.databinding.RowOrderByIdBinding
import com.example.mynaoseioqueapp.domain.model.LineOrder

class OrderByIdAdapter (
    val context: Context,
    val lineOrderList: List<LineOrderResponse>
) : RecyclerView.Adapter<OrderByIdAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowOrderByIdBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderByIdAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_order_by_id, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderByIdAdapter.ViewHolder, position: Int) {
        val lineOrder = lineOrderList[position]

        holder.binding.foodName.text = lineOrder.food.name
        holder.binding.quantity.text = lineOrder.quantity.toString()
    }

    override fun getItemCount(): Int {
        return lineOrderList.size
    }
}