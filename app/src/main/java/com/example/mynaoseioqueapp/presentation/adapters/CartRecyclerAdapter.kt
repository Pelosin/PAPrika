package com.example.mynaoseioqueapp.presentation.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.RowCartRecyclerBinding
import com.example.mynaoseioqueapp.domain.model.LineOrder
import com.example.mynaoseioqueapp.util.LineOrderSetter

class CartRecyclerAdapter (
    val context: Context,
    private val lineOrderList : List<LineOrder>,
    val onClickCartEvent: CartViewHolder.OnClickCartEvent,
    val priceTextView: TextView
) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    class CartViewHolder  (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val _binding = RowCartRecyclerBinding.bind(itemView)

        val binding: RowCartRecyclerBinding

        interface OnClickCartEvent{
            fun onClickSubEvent(lineOrder: LineOrder, position: Int)
            fun onClickAddEvent(lineOrder: LineOrder, position: Int)
            fun cleanWholeCart()
        }

        init {
            binding = _binding
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartRecyclerAdapter.CartViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_cart_recycler, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.CartViewHolder, position: Int) {
        val lineOrder = lineOrderList[position]
        holder.binding.titleCart.text = lineOrder.name
        holder.binding.quantityCartTextView.text = "${lineOrder.quantity.toString()}"
        holder.binding.priceCart.text = lineOrder.partialPrice.toString()
        lineOrder.url.let {
            holder.binding.imageCard.load(it)
        }
        holder.binding.addButton.setOnClickListener {
            onClickCartEvent.onClickAddEvent(lineOrder, position)
            LineOrderSetter.addQuantity(lineOrder)
            holder.binding.quantityCartTextView.text = "${lineOrder.quantity.toString()}"
            holder.binding.priceCart.text = LineOrderSetter.getLineOrderEspecificPrice(lineOrder).toString()
            priceTextView.text = LineOrderSetter.getTotalPrice().toString()

        }
        holder.binding.subButton.setOnClickListener {
//            onClickCartEvent.onClickSubEvent(lineOrder, position)
            if(lineOrder.quantity.equals(1)){
                Log.d("MyLineOrder", lineOrder.name)
                LineOrderSetter.removeFromCart(position)
                Log.d("MyPosition", position.toString())
                notifyItemRemoved(position)
                notifyDataSetChanged()
                priceTextView.text = LineOrderSetter.getTotalPrice().toString()
            } else {
                LineOrderSetter.subQuantity(lineOrder)
                holder.binding.quantityCartTextView.text = "${lineOrder.quantity.toString()}"
                holder.binding.priceCart.text = LineOrderSetter.getLineOrderEspecificPrice(lineOrder).toString()
                priceTextView.text = LineOrderSetter.getTotalPrice().toString()
            }

        }
    }

    override fun getItemCount(): Int {
        return lineOrderList.size
    }
}