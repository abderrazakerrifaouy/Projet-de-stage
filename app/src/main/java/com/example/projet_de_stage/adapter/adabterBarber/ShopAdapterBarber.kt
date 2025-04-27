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
import kotlin.text.isNotEmpty
import com.bumptech.glide.Glide


class ShopAdapterBarber(
    private val onRequestJoinClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapterBarber.ShopViewHolder>() {

    private val shopList = mutableListOf<Shop>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Shop>) {
        shopList.clear()
        shopList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop_barber, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(shopList[position])
    }

    override fun getItemCount(): Int = shopList.size

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewShop: ImageView = itemView.findViewById(R.id.imageViewShop)
        private val textViewShopName: TextView = itemView.findViewById(R.id.textViewShopName)
        private val textViewShopAddress: TextView = itemView.findViewById(R.id.textViewShopAddress)
        private val textViewBarberCount: TextView = itemView.findViewById(R.id.textViewBarberCount)
        private val buttonRequestJoin: Button = itemView.findViewById(R.id.buttonRequestJoin)

        @SuppressLint("SetTextI18n")
        fun bind(shop: Shop) {
            textViewShopName.text = shop.name
            textViewShopAddress.text = shop.address
            textViewBarberCount.text = "${shop.barbers.size} حلاقين"
            if (shop.imageUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(shop.imageUrl)
                    .into(imageViewShop)
            } else {
                imageViewShop.setImageResource(shop.imageRes)
            }

            buttonRequestJoin.setOnClickListener {
                onRequestJoinClick(shop)
            }
        }
    }
}
