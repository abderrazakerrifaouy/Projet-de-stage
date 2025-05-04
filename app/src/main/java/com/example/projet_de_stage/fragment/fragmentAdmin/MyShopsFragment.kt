package com.example.projet_de_stage.fragment.fragmentAdmin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterAdmin.ShopsAdapter
import com.example.projet_de_stage.view.admin.BarberListActivity
import com.example.projet_de_stage.view.admin.CreateShopActivity
import com.example.projet_de_stage.viewModel.AdminViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

/**
 * Fragment displaying the list of shops owned by a shop owner.
 */
class MyShopsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsAdapter: ShopsAdapter
    private lateinit var fabAddShop: FloatingActionButton
    private lateinit var headerTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel = AdminViewModel()
    private var shopOwnerUid: String = ""

    /**
     * Inflates the fragment layout and initializes views.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_shops, container, false)

        recyclerView = view.findViewById(R.id.shopsRecyclerView)
        fabAddShop = view.findViewById(R.id.fabAddShop)
        headerTitle = view.findViewById(R.id.headerTitle)
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        return view
    }

    /**
     * Sets up swipe refresh and updates the shop list.
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshShopList()
        swipeRefreshLayout.setOnRefreshListener {
            refreshShopList()
        }
    }

    /**
     * Refreshes the list of shops.
     */
    fun refreshShopList() {
        swipeRefreshLayout.isRefreshing = true
        handleFabClick()

        shopOwnerUid = arguments?.getString("shopOwner") ?: return
        getShopsList()
    }

    /**
     * Handles the click event of the FloatingActionButton to add a new shop.
     */
    private fun handleFabClick() {
        fabAddShop.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Opening the Add Shop screen",
                Toast.LENGTH_SHORT
            ).show()
            Intent(requireContext(), CreateShopActivity::class.java).also { intent ->
                intent.putExtra("shopOwner", shopOwnerUid)
                startActivity(intent)
            }
        }
    }

    /**
     * Fetches the list of shops for the specified shop owner and updates the UI.
     */
    @SuppressLint("SetTextI18n")
    fun getShopsList() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val shops = withContext(Dispatchers.IO) {
                viewModel.getShopsByOwnerIdSuspend(shopOwnerUid)
            }

            if (shops.isEmpty()) {
                headerTitle.text = "No shops found"
                recyclerView.adapter = null
            } else {
                headerTitle.text = "Shop List"
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                shopsAdapter = ShopsAdapter(shops) { shop ->
                    Toast.makeText(requireContext(), "You clicked on ${shop.id}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), BarberListActivity::class.java)
                    intent.putExtra("shopId", shop)
                    startActivity(intent)
                }
                recyclerView.adapter = shopsAdapter
            }

            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
