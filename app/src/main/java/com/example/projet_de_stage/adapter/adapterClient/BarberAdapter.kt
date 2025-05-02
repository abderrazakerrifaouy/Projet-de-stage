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

/**
 * Adapter for displaying a list of barbers.
 * It binds the barber data to the RecyclerView.
 */
class BarberAdapter(
    private val barbers: List<Barber>, // List of barbers to display
    private val onBarberSelected: (Barber) -> Unit // Callback when a barber is selected
) : RecyclerView.Adapter<BarberAdapter.BarberViewHolder>() {

    /**
     * Creates a new view holder for each item in the list.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barber, parent, false) // Inflating the item layout
        return BarberViewHolder(view, onBarberSelected)
    }

    /**
     * Binds the barber data to the view holder.
     */
    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        holder.bind(barbers[position]) // Binding the data for the current position
    }

    /**
     * Returns the total count of barbers in the list.
     */
    override fun getItemCount() = barbers.size

    /**
     * ViewHolder class for holding references to the views in each barber item.
     */
    class BarberViewHolder(
        itemView: View, // The view for each item
        private val onBarberSelected: (Barber) -> Unit // Callback for barber selection
    ) : RecyclerView.ViewHolder(itemView) {
        private val barberNameTextView: TextView = itemView.findViewById(R.id.tvBarberName)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val barberImageView: ImageView = itemView.findViewById(R.id.ivBarber)
        private val selectButton: Button = itemView.findViewById(R.id.btnSelect)

        /**
         * Binds the barber data to the views in the item.
         */
        fun bind(barber: Barber) {
            barberNameTextView.text = barber.name // Set barber name
            ratingBar.rating = barber.rating // Set barber rating
            barberImageView.setImageResource(R.drawable.my_profile) // Set default image for barber

            // Set the click listener to notify when the barber is selected
            selectButton.setOnClickListener {
                onBarberSelected(barber) // Trigger the callback when selected
            }
        }
    }
}
