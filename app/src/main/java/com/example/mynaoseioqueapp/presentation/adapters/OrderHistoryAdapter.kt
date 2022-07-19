package com.example.mynaoseioqueapp.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.data.remote.dto.OrderByIdResponse
import com.example.mynaoseioqueapp.data.remote.dto.OrderHistoryResponse
import com.example.mynaoseioqueapp.databinding.RowHistorySettingsBinding
import com.example.mynaoseioqueapp.domain.model.Order
import com.example.mynaoseioqueapp.presentation.dialog.OrderHistoryDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class OrderHistoryAdapter(
    val context: Context,
    val onClickOrderEvent: OnClickOrderEvent,
    private val orderList: List<OrderHistoryResponse>
) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){

    interface OnClickOrderEvent {
        fun onClickOrderEvent(order: OrderHistoryResponse)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val binding = RowHistorySettingsBinding.bind(view)

        val orderIdTextView : TextView = binding.orderIdTextView
        val orderDateTextView : TextView = binding.orderDateTextView
        val orderPriceTextView : TextView = binding.orderPriceTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_history_settings, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]

        holder.orderIdTextView.text = "Order Nº ${ order.id.toString() }"
        holder.orderPriceTextView.text = "Total Price: ${ order.price.toString() } €"
        holder.orderDateTextView.text = order.createdDate.split(" ")[0]

        holder.binding.seeOrderDetailsButton.setOnClickListener {
            onClickOrderEvent.onClickOrderEvent(order)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}