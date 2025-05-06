package com.example.projet_de_stage.adapter.adapterAdmin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.viewModel.AdminViewModel

/**
 * RecyclerView Adapter for displaying join requests from barbers to shop owners.
 *
 * @param requests A list of join requests.
 * @param onAction Callback for handling accept/reject actions.
 * @param admineViewModel Shared ViewModel instance to access barber data.
 */
class JoinRequestsAdapter(
    private var requests: List<JoinRequest>,
    private val onAction: (String, Boolean, Barber?, String?) -> Unit,
    private val admineViewModel: AdminViewModel
) : RecyclerView.Adapter<JoinRequestsAdapter.JoinRequestViewHolder>() {

    /**
     * ViewHolder for each join request item.
     */
    inner class JoinRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvExperience: TextView = view.findViewById(R.id.tvPosition)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvReviewsCount: TextView = view.findViewById(R.id.tvReviewsCount)
        val btnAcceptRequest: Button = view.findViewById(R.id.btnAcceptRequest)
        val btnRejectRequest: Button = view.findViewById(R.id.btnRejectRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinRequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_join_request, parent, false)
        return JoinRequestViewHolder(view)
    }

    /**
     * Binds join request data to the view holder.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JoinRequestViewHolder, position: Int) {
        val item = requests[position]

        // Set placeholder values while loading data
        holder.tvName.text = "Loading name..."
        holder.tvExperience.text = item.experience
        holder.tvRating.text = "Loading rating..."

        // Load barber details using ViewModel
        getBarberDetails(item.barberId) { barber ->
            holder.tvName.text = barber?.name ?: "Unknown"
            holder.tvRating.text = barber?.rating?.toString() ?: "N/A"
            holder.tvReviewsCount.text = "(${barber?.numberOfRatings ?: 0} )"

            holder.btnAcceptRequest.setOnClickListener {
                onAction(item.id, true, barber, item.shopId)
            }
            holder.btnRejectRequest.setOnClickListener {
                onAction(item.id, false, barber, item.shopId)
            }
        }
    }

    override fun getItemCount(): Int = requests.size

    /**
     * Filters and updates the list of join requests to only include pending ones.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<JoinRequest>) {
        requests = newList.filter { it.status == "pending" }
        notifyDataSetChanged()
    }

    /**
     * Loads barber data by ID and returns it via the onResult callback.
     * Observer is removed immediately after receiving the data to avoid memory leaks.
     */
    private fun getBarberDetails(id: String, onResult: (Barber?) -> Unit) {
        admineViewModel.getBarberById(id)
        admineViewModel.barber.observeForever(object : androidx.lifecycle.Observer<Barber?> {
            override fun onChanged(barber: Barber?) {
                onResult(barber)
                admineViewModel.barber.removeObserver(this)
            }
        })
    }
}
