package com.example.projet_de_stage.adapter.adabterAdmin

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

class ShopsAdapter(
    private var shops: List<Shop>,
    private val onManageClick: (String) -> Unit
) : RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivShopImage: ImageView = view.findViewById(R.id.ivShopImage)
        val tvShopName: TextView = view.findViewById(R.id.tvShopName)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvReviewsCount: TextView = view.findViewById(R.id.tvReviewsCount)
        val btnManageShop: Button = view.findViewById(R.id.btnManageShop)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.ivShopImage.setImageResource(shop.imageRes)
        holder.tvShopName.text = shop.name
        holder.btnManageShop.setOnClickListener {
            onManageClick(shop.id)
        }
    }

    override fun getItemCount(): Int = shops.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newShops: List<Shop>) {
        this.shops = newShops
        notifyDataSetChanged()
    }
}