package com.example.projet_de_stage.fragment.fragmentAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterAdmin.JoinRequestsAdapter
import com.example.projet_de_stage.data.JoinRequest

class JoinRequestsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_join_requests, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.joinRequestsRecyclerView)
        val requests = listOf(
            JoinRequest(
                "1",
                "خالد علي",
                "خبير حلاقة",
                "abdo@gmail.com",
                "0656561323",
                "القاهرة",
                "1234567"
            )
        )

        recyclerView.adapter = JoinRequestsAdapter(requests) { id, approved ->
            Toast.makeText(
                requireContext(),
                "الطلب $id ${if (approved) "مقبول" else "مرفوض"}",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}