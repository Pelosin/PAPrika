package com.example.mynaoseioqueapp.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynaoseioqueapp.R
import com.example.mynaoseioqueapp.common.Constant
import com.example.mynaoseioqueapp.databinding.RowCategoryRecyclerBinding
import com.example.mynaoseioqueapp.domain.model.Category
import com.example.mynaoseioqueapp.domain.model.Food
import com.example.mynaoseioqueapp.presentation.food_details.FoodDetailsActivity

class ParentAdapter(
    val context: Context,
    private val categoryList: List<Category>,
) : HomeRecyclerAdapter.HomeViewHolder.OnClickHomeEvent,
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {
    class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = RowCategoryRecyclerBinding.bind(itemView)
        val recylcerView: RecyclerView
        val categoryText: TextView

        init {
            recylcerView = binding.homeRecyclerView
            categoryText = binding.descTextView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_category_recycler, parent, false)

        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val category = categoryList[position]

        holder.categoryText.text = category.name

        holder.recylcerView.setHasFixedSize(true)
        holder.recylcerView.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false)
        holder.recylcerView.adapter = HomeRecyclerAdapter(context, category.foodList, this)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onClickEvent(food: Food) {
        context.startActivity(
            Intent(
            context, FoodDetailsActivity::class.java
        ).putExtra(Constant.FOOD_SERIALIZABLE, food))
    }
}