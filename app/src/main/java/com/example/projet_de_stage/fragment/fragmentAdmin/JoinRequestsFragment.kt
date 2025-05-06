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
import com.example.projet_de_stage.adapter.adapterAdmin.JoinRequestsAdapter
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.viewModel.AdminViewModel

/**
 * Fragment for displaying join requests to a shop.
 * Allows the shop owner to accept or reject requests.
 */
class JoinRequestsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AdminViewModel

    /**
     * Called to have the fragment instantiate its user interface view.
     * Here we set up the RecyclerView with an adapter and observe data from the ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_join_requests, container, false)


        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        recyclerView = view.findViewById(R.id.joinRequestsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = JoinRequestsAdapter(
            emptyList(),
            { id, approved, barber, idShop ->
                handleJoinRequestApproval(id, approved, barber, idShop)
            },
            viewModel
        )

        recyclerView.adapter = adapter

        // Get shop owner ID from arguments
        val shopOwnerId = arguments?.getString("shopOwner")
        if (shopOwnerId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.error_missing_shop_owner_id), Toast.LENGTH_SHORT).show()
            return view
        }

        // Fetch join requests by shop owner ID
        viewModel.getJoinRequestsByShopOwnerId(shopOwnerId)

        // Observe changes in join requests data and update the UI
        viewModel.joinRequests.observe(viewLifecycleOwner) { requests ->
            adapter.updateList(requests) // Update the adapter's data list
        }

        // Observe error messages and display them as toast messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        return view
    }

    /**
     * Handles the acceptance or rejection of a join request.
     * @param id The request ID.
     * @param approved Boolean indicating if the request is approved.
     * @param barber The barber data.
     * @param idShop The shop ID.
     */
    private fun handleJoinRequestApproval(
        id: String, approved: Boolean, barber: Barber?, idShop: String?
    ) {
        if (approved) {
            viewModel.updateJoinRequestStatus(
                id,
                "accepted",
                onSuccess = { isSuccess ->
                    if (isSuccess) {
                        viewModel.addBarberToShop(idShop ?: "", barber!!)
                        refreshUi()
                    } else {
                        showToast(getString(R.string.error_updating_request_status))
                    }
                }
            )
        } else {
            viewModel.updateJoinRequestStatus(
                id,
                "rejected",
                onSuccess = { isSuccess ->
                    if (isSuccess) {
                        refreshUi()
                    } else {
                        showToast(getString(R.string.error_updating_request_status))
                    }
                }
            )
        }
    }

    /**
     * Refreshes the UI by fetching the latest join requests for the shop owner.
     */
    private fun refreshUi() {
        val shopOwnerId = arguments?.getString("shopOwner")
        if (!shopOwnerId.isNullOrEmpty()) {
            viewModel.getJoinRequestsByShopOwnerId(shopOwnerId)
        }
    }

    /**
     * Helper method to show a toast message.
     * @param message The message to display in the toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
