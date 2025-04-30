package com.example.projet_de_stage.adapter.adapterClient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import kotlin.let

class BarberAdapter(
    private val barbers: List<Barber>,
    private val onBarberSelected: (Barber) -> Unit
) : RecyclerView.Adapter<BarberAdapter.BarberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barber, parent, false)
        return BarberViewHolder(view, onBarberSelected)
    }

    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        holder.bind(barbers[position])
    }

    override fun getItemCount() = barbers.size

    class BarberViewHolder(
        itemView: View,
        private val onBarberSelected: (Barber) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvBarberName: TextView = itemView.findViewById(R.id.tvBarberName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val ivBarber: ImageView = itemView.findViewById(R.id.ivBarber)
        private val btnSelect: Button = itemView.findViewById(R.id.btnSelect)

        fun bind(barber: Barber) {
            tvBarberName.text = barber.name
            ratingBar.rating = barber.rating
            ivBarber.setImageResource(R.drawable.my_profile)

            btnSelect.setOnClickListener {
                onBarberSelected(barber)
            }
        }
    }
}