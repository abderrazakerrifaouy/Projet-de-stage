package com.example.projet_de_stage.adapter.adapterAdmin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Shop

/**
 * Adapter for displaying a list of shops in the admin interface.
 * Allows the shop owner to manage individual shops via a button click.
 *
 * @param shops Initial list of shops to display.
 * @param onManageClick Callback triggered when the "Manage" button is clicked, passing the shop ID.
 */
class ShopsAdapter(
    private var shops: List<Shop>,
    private val onManageClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>() {

    /**
     * ViewHolder class that holds references to the UI components of a shop item.
     */
    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivShopImage: ImageView = view.findViewById(R.id.ivShopImage)
        val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val btnManageShop: Button = view.findViewById(R.id.btnManageShop)
    }

    /**
     * Inflates the layout for a single shop item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    /**
     * Binds the shop data to the corresponding views.
     */
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]

        // Load image from URL if available, otherwise use default resource.
        if (shop.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(shop.imageUrl)
                .into(holder.ivShopImage)
        } else {
            holder.ivShopImage.setImageResource(shop.imageRes)
        }

        holder.tvShopName.text = shop.name

        // Handle "Manage Shop" button click
        holder.btnManageShop.setOnClickListener {
            onManageClick(shop)
        }

        // Optional: you could populate tvRating and tvReviewsCount when data is available
    }

    /**
     * Returns the number of items in the list.
     */
    override fun getItemCount(): Int = shops.size

    /**
     * Updates the shop list with new data and refreshes the RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newShops: List<Shop>) {
        this.shops = newShops
        notifyDataSetChanged()
    }
}
