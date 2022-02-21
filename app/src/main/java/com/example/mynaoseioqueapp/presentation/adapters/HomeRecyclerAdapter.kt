package com.example.mynaoseioqueapp.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.databinding.HomeRecyclerRowBinding
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.presentation.food_details.FoodDetailsActivity

class HomeRecyclerAdapter (
    val context: Context,
    val foodList: List<Food>
): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameTextView : TextView
        val foodPriceTextView : TextView
        val foodCardView : CardView
        private val binding = HomeRecyclerRowBinding.bind(itemView)

        init {
            foodNameTextView = binding.rowTitle
            foodPriceTextView = binding.rowPrice
            foodCardView = binding.homeCardRow
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRecyclerAdapter.HomeViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.home_recycler_row, parent, false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeRecyclerAdapter.HomeViewHolder, position: Int) {
        val selectedFood = foodList[position]

        holder.foodNameTextView.text = selectedFood.name
        holder.foodPriceTextView.text = selectedFood.price.toString()

        holder.foodCardView.setOnClickListener {
            context.startActivity(Intent(context, FoodDetailsActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}