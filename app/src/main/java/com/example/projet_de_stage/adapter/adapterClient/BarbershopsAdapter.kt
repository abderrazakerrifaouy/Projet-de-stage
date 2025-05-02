package com.example.projet_de_stage.adapter.adapterClient

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Shop

/**
 * Adapter for displaying a list of shops in a RecyclerView.
 * It binds each shop item with a corresponding view.
 */
class ShopAdapter(
    private val shops: List<Shop>,
    private val onItemClick: (Shop) -> Unit // Callback when an item is clicked
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    /**
     * Creates a new ViewHolder instance for each shop item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barbershop, parent, false) // Ensure XML is named item_shop.xml
        return ShopViewHolder(view)
    }

    /**
     * Binds the data from the shop list to the ViewHolder for the given position.
     */
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.bind(shop)
        holder.itemView.setOnClickListener { onItemClick(shop) } // Sets click listener for each item
    }

    /**
     * Returns the total number of items in the shop list.
     */
    override fun getItemCount(): Int = shops.size

    /**
     * ViewHolder for binding shop data to each item view.
     */
    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBarbershop: ImageView = itemView.findViewById(R.id.ivBarbershop) // Image view for shop image
        private val tvBarbershopName: TextView = itemView.findViewById(R.id.tvBarbershopName) // Shop name
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation) // Shop address
        private val tvBarbersCount: TextView = itemView.findViewById(R.id.tvBarbersCount) // Number of barbers in the shop

        /**
         * Binds the shop data to the respective views.
         */
        @SuppressLint("SetTextI18n")
        fun bind(shop: Shop) {
            // Load the shop image using Glide if URL exists, otherwise set a default image
            if (shop.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(shop.imageUrl)
                    .into(ivBarbershop)
            } else {
                ivBarbershop.setImageResource(R.drawable.app_name) // Default image if no URL
            }

            // Set the shop name, address, and number of barbers in the shop
            tvBarbershopName.text = shop.name
            tvLocation.text = shop.address
            tvBarbersCount.text = "${shop.barbers.size} available barbers" // Change the text to English

            // Optionally, show ratings and reviews if required, by adding the corresponding TextViews and logic
            // Example: tvRating.text = "${shop.rating} (${shop.reviews})"
        }
    }
}
