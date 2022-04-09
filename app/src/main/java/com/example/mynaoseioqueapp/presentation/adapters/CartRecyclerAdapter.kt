package com.example.mynaoseioqueapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.RowCartRecyclerBinding
import com.example.mynaoseioqueapp.domain.model.LineOrder

class CartRecyclerAdapter (
    val context: Context,
    private val lineOrderList : List<LineOrder>
) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    class CartViewHolder  (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val _binding = RowCartRecyclerBinding.bind(itemView)

        val binding: RowCartRecyclerBinding

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
        holder.binding.priceCart.text = lineOrder.quantity.toString()
        holder.binding.priceCart2.text = lineOrder.partialPrice.toString()
    }

    override fun getItemCount(): Int {
        return lineOrderList.size
    }
}