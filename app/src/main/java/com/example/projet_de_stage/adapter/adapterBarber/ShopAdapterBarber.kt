package com.example.projet_de_stage.adapter.adapterBarber

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Shop
import com.bumptech.glide.Glide

/**
 * Adapter to display a list of shops for the barber to join.
 */
class ShopAdapterBarber(
    private val onRequestJoinClick: (Shop) -> Unit // Callback when the "Request to Join" button is clicked
) : RecyclerView.Adapter<ShopAdapterBarber.ShopViewHolder>() {

    // List to hold shop data
    private val shopList = mutableListOf<Shop>()

    /**
     * Updates the adapter data with a new list of shops.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Shop>) {
        shopList.clear()
        shopList.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Creates a new view holder when needed.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_barber, parent, false) // Inflate the layout for each shop item
        return ShopViewHolder(view)
    }

    /**
     * Binds data to the view holder for each item.
     */
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(shopList[position])
    }

    /**
     * Returns the total number of items in the list.
     */
    override fun getItemCount(): Int = shopList.size

    /**
     * ViewHolder class to bind shop data to views.
     */
    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageViewShop)
        private val textViewShopName: TextView = itemView.findViewById(R.id.textViewShopName)
        private val textViewShopAddress: TextView = itemView.findViewById(R.id.textViewShopAddress)
        private val textViewBarberCount: TextView = itemView.findViewById(R.id.textViewBarberCount)
        private val buttonRequestJoin: Button = itemView.findViewById(R.id.buttonRequestJoin)

        /**
         * Binds the shop data to the UI components in the item view.
         */
        @SuppressLint("SetTextI18n")
        fun bind(shop: Shop) {
            textViewShopName.text = shop.name
            textViewShopAddress.text = shop.address
            textViewBarberCount.text = "${shop.barbers.size} Barbers"

            // If the shop has a valid image URL, load it using Glide
            if (shop.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(shop.imageUrl)
                    .into(imageViewShop)
            } else {
                imageViewShop.setImageResource(shop.imageRes) // Use a default image if no URL is provided
            }

            // Set an onClick listener for the "Request to Join" button
            buttonRequestJoin.setOnClickListener {
                onRequestJoinClick(shop) // Trigger the callback when the button is clicked
            }
        }
    }
}
