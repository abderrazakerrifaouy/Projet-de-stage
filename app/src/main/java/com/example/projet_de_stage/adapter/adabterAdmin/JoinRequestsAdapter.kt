package com.example.projet_de_stage.adapter.adabterAdmin

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
import com.example.projet_de_stage.viewModel.AdmineViewModel

class JoinRequestsAdapter(
    private var requests: List<JoinRequest>,
    private val onAction: (String, Boolean , Barber? , String?) -> Unit,
    private val admineViewModel: AdmineViewModel // نمرروه من برا
) : RecyclerView.Adapter<JoinRequestsAdapter.JoinRequestViewHolder>() {

    inner class JoinRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvExperience: TextView = view.findViewById(R.id.tvPosition)
        val btnAcceptRequest: Button = view.findViewById(R.id.btnAcceptRequest)
        val btnRejectRequest: Button = view.findViewById(R.id.btnRejectRequest)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvReviewsCount = view.findViewById<TextView>(R.id.tvReviewsCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_join_request, parent, false)
        return JoinRequestViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JoinRequestViewHolder, position: Int) {
        val item = requests[position]

        // أول حاجة نفرغ الاسم والتقييم حتى نجيبو
        holder.tvName.text = "تحميل الاسم..."
        holder.tvExperience.text = item.experience
        holder.tvRating.text = "جاري تحميل التقييم..."

        // نجيب Barber بالـ id
        getBarberDetails(item.idBarber) { barber ->
            holder.tvName.text = barber?.name ?: "غير معروف"
            holder.tvRating.text = barber?.rating?.toString() ?: "غير متوفر"
            holder.tvReviewsCount.text = "(${barber?.Nrating} تقيي)"


        holder.btnAcceptRequest.setOnClickListener { onAction(item.id, true , barber , item.idShop ) }
        holder.btnRejectRequest.setOnClickListener { onAction(item.id, false , barber , item.idShop) }
        }
    }

    override fun getItemCount(): Int = requests.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<JoinRequest>) {
        requests = newList
        notifyDataSetChanged()
    }

    // دالة تجيب الإسم
    private fun getBarberDetails(id: String, onResult: (Barber?) -> Unit) {
        admineViewModel.getBarberById(id)

        admineViewModel.barber.observeForever(object : androidx.lifecycle.Observer<Barber?> {
            override fun onChanged(barber: Barber?) {
                onResult(barber) // نرجع كل البيانات الخاصة بالـ barber
                // مهم بزاف نزيل المراقبة باش مانخليش memory leak
                admineViewModel.barber.removeObserver(this)
            }
        })
    }
}
