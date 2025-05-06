package com.example.projet_de_stage.adapter.adapterAdmin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber

class BarberAdapterShop(
    private val barbers: List<Barber>,
    private val onRemoveClick: (Barber) -> Unit
) : RecyclerView.Adapter<BarberAdapterShop.BarberViewHolder>() {

    inner class BarberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.barberNameShop)
        val experience = itemView.findViewById<TextView>(R.id.barberExperience)
        val rating = itemView.findViewById<TextView>(R.id.barberRating)
        val image = itemView.findViewById<ImageView>(R.id.barberImage)
        val removeButton = itemView.findViewById<Button>(R.id.removeBarberButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barbers_shop, parent, false)
        return BarberViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        val barber = barbers[position]
        holder.name.text = barber.name.toString()
        holder.experience.text = barber.experience.toString()
        holder.rating.text = "⭐ ${barber.rating} (${barber.numberOfRatings})"
        holder.image.setImageResource(barber.imageRes)

        holder.removeButton.setOnClickListener {
            onRemoveClick(barber)
        }
    }

    override fun getItemCount() = barbers.size
}
