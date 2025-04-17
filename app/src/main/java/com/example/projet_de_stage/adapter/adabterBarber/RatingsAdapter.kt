package com.example.projet_de_stage.adapter.adabterBarber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Rating

class RatingsAdapter(
    private val ratings: List<Rating>
) : RecyclerView.Adapter<RatingsAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvShopName = view.findViewById<TextView>(R.id.tvShopName)
        val tvCustomer = view.findViewById<TextView>(R.id.tvCustomerName)
        val tvRatingValue = view.findViewById<TextView>(R.id.tvRating)
        val tvReview = view.findViewById<TextView>(R.id.tvComment)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val item = ratings[position]
        holder.tvShopName.text = item.shopName
        holder.tvCustomer.text = item.customer
        holder.tvRatingValue.text = item.rating
        holder.tvReview.text = item.comment
        holder.tvDate.text = item.date
    }

    override fun getItemCount(): Int = ratings.size
}