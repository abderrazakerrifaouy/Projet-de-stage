package com.example.projet_de_stage.adapter.adabterAdmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.data.JoinRequest

class JoinRequestsAdapter(
    private val requests: List<JoinRequest>,
    private val onAction: (String, Boolean) -> Unit
) : RecyclerView.Adapter<JoinRequestsAdapter.JoinRequestViewHolder>() {

    inner class JoinRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvExperience = view.findViewById<TextView>(R.id.tvPosition)
        val btnAcceptRequest = view.findViewById<Button>(R.id.btnAcceptRequest)
        val btnRejectRequest = view.findViewById<Button>(R.id.btnRejectRequest)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_join_request, parent, false)
        return JoinRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: JoinRequestViewHolder, position: Int) {
        val item = requests[position]
        holder.tvExperience.text = item.experience
        holder.btnAcceptRequest.setOnClickListener { onAction(item.id, true) }
        holder.btnRejectRequest.setOnClickListener { onAction(item.id, false) }
    }

    override fun getItemCount(): Int = requests.size
}