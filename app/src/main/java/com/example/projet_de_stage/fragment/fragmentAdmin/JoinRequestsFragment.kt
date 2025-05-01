package com.example.projet_de_stage.fragment.fragmentAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterAdmin.JoinRequestsAdapter
import com.example.projet_de_stage.viewModel.AdmineViewModel

class JoinRequestsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AdmineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_join_requests, container, false)
        viewModel = ViewModelProvider(this).get(AdmineViewModel::class.java)

        recyclerView = view.findViewById(R.id.joinRequestsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        val adapter = JoinRequestsAdapter(
            emptyList(),
            { id, approved, barber, idShop ->
                if (approved) {
                    viewModel.updateJoinRequestStatus(
                        id,
                        "accepted",
                        onSuccess = { check ->
                            if (check) {
                                viewModel.addBarberToShop(idShop ?: "", barber!!)
                                refleshUi()
                            }
                            else {
                                Toast.makeText(
                                    requireContext(),
                                    "حدث خطأ أثناء تحديث حالة الطلب",
                                    Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }
                    )
                } else {
                    viewModel.updateJoinRequestStatus(
                        id,
                        "rejected",
                        onSuccess = { check ->
                            if (check) {
                                refleshUi()
                            }
                            else {
                                Toast.makeText(
                                    requireContext(),
                                    "حدث خطأ أثناء تحديث حالة الطلب",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            })
                }
            } ,
            viewModel
        )



        recyclerView.adapter = adapter


        val shopOwnerId = arguments?.getString("shopOwner")
        if (shopOwnerId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "خطأ: لا يوجد معرف صاحب المحل", Toast.LENGTH_SHORT).show()
            return view
        }

        viewModel.getJoinRequestsByShopOwnerId(shopOwnerId)

        viewModel.joinRequests.observe(viewLifecycleOwner) { requests ->
            adapter.updateList(requests) // خاصنا نضيف updateList فال Adapter
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        return view
    }
    private fun refleshUi() {
        val shopOwnerId = arguments?.getString("shopOwner")
        if (!shopOwnerId.isNullOrEmpty()) {
            viewModel.getJoinRequestsByShopOwnerId(shopOwnerId)
        }
    }

}
