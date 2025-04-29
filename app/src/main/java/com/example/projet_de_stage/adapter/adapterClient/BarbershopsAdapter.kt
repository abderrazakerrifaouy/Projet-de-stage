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


class ShopAdapter(
    private val shops: List<Shop>,
    private val onItemClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barbershop, parent, false) // Make sure your XML is named item_shop.xml
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.bind(shop)
        holder.itemView.setOnClickListener { onItemClick(shop) }
    }

    override fun getItemCount(): Int = shops.size

    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBarbershop: ImageView = itemView.findViewById(R.id.ivBarbershop)
        private val tvBarbershopName: TextView = itemView.findViewById(R.id.tvBarbershopName)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvBarbersCount: TextView = itemView.findViewById(R.id.tvBarbersCount)

        @SuppressLint("SetTextI18n")
        fun bind(shop: Shop) {
            if (shop.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(shop.imageUrl)
                    .into(ivBarbershop)
            } else {
                ivBarbershop.setImageResource(R.drawable.app_name)
            }

            tvBarbershopName.text = shop.name
            tvLocation.text = shop.address
            tvBarbersCount.text = "${shop.barbers.size} حلاقين متاحين"

            // If you want to show rating and reviews, you can add them to your layout
            // and set them here like:
            // tvRating.text = "${shop.rating} (${shop.reviews})"
        }
    }
}
