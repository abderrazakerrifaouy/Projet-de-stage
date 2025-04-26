package com.example.projet_de_stage.fragment.fragmentAdmin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adabterAdmin.ShopsAdapter
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.view.admin.CreateShopActivity
import com.example.projet_de_stage.viewModel.AdmineViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyShopsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var shopsAdapter: ShopsAdapter
    private lateinit var fabAddShop: FloatingActionButton
    private lateinit var headerTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    private val viewModel = AdmineViewModel()
    private var shopOwnerUid: String = ""

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refleche()
        swipeRefreshLayout.setOnRefreshListener {
            refleche()
        }

    }
    fun refleche() {
        swipeRefreshLayout.isRefreshing = true
        handleFabClick()

        shopOwnerUid = arguments?.getString("shopOwner") ?: return
        getShopsList()
    }



    private fun handleFabClick() {
        fabAddShop.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "فتح إضافة المحل",
                Toast.LENGTH_SHORT
            ).show()
            Intent(requireContext(), CreateShopActivity::class.java).also { intent ->
                intent.putExtra("shopOwner", shopOwnerUid)
                startActivity(intent)
            }
        }
    }

    fun getShopsList() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val shops = withContext(Dispatchers.IO) {
                viewModel.getShopsByOwnerIdSuspend(shopOwnerUid)
            }

            if (shops.isEmpty()) {
                headerTitle.text = "لا يوجد محلات"
                recyclerView.adapter = null
            } else {
                headerTitle.text = "قائمة المحلات"
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                shopsAdapter = ShopsAdapter(shops) { shop ->
                    Toast.makeText(requireContext(), "تم النقر على ${shop}", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = shopsAdapter
            }


            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
        }
    }





}
