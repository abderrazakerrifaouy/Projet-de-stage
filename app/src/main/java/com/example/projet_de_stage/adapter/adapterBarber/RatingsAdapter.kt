package com.example.projet_de_stage.adapter.adapterBarber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Rating

/**
 * Adapter to display a list of ratings provided by customers to barbers.
 *
 * @param ratings List of [Rating] objects to be displayed.
 */
class RatingsAdapter(
    private val ratings: List<Rating>
) : RecyclerView.Adapter<RatingsAdapter.RatingViewHolder>() {

    /**
     * ViewHolder representing a single rating item layout.
     */
    inner class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // If you want to show shop name, ensure it's available in the Rating data class
        // val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val tvCustomer: TextView = view.findViewById(R.id.tvCustomerName)
        val tvRatingValue: TextView = view.findViewById(R.id.tvRating)
        val tvReview: TextView = view.findViewById(R.id.tvComment)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    /**
     * Called when RecyclerView needs a new [RatingViewHolder] of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val rating = ratings[position]

        // holder.tvShopName.text = rating.shopName // Uncomment if available
        holder.tvCustomer.text = rating.customerName
        holder.tvRatingValue.text = rating.ratingValue
        holder.tvReview.text = rating.comment
        holder.tvDate.text = rating.date
    }

    /**
     * Returns the total number of items in the data set.
     */
    override fun getItemCount(): Int = ratings.size
}
