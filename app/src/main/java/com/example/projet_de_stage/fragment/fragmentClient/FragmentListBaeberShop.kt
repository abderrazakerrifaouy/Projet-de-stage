package com.example.projet_de_stage.fragment.fragmentClient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.ShopAdapter
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.view.client.BarbershopDetails
import com.example.projet_de_stage.viewModel.ClientViewModel
import kotlinx.coroutines.launch

class FragmentListBarberShop : Fragment() {

    private val viewModel: ClientViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private var client: Customer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_barber_shop, container, false)

        setupRecyclerView(view)
        loadShops()

        return view
    }

    /**
     * تقوم هذه الدالة بإعداد RecyclerView
     * - تحدد مكانه داخل الـ Layout
     * - تعين له LinearLayoutManager للعرض العمودي
     */
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvBarbershops)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * تقوم هذه الدالة بتحميل قائمة المحلات (Shops) من الـ ViewModel
     * - تتحقق أولاً من وجود بيانات العميل (Customer)
     * - في حالة نجاح التحميل، يتم تعبئة RecyclerView بالبيانات عن طريق ShopAdapter
     * - في حالة غياب العميل، تعرض رسالة Toast للمستخدم
     */
    private fun loadShops() {
        viewLifecycleOwner.lifecycleScope.launch {
            client = arguments?.getParcelable("customer")

            if (client == null) {
                Toast.makeText(requireContext(), "لم يتم العثور على العميل", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val shopsList = viewModel.getShops()

            val adapter = ShopAdapter(shopsList) { shop ->
                val intent = Intent(requireContext(), BarbershopDetails::class.java)
                intent.putExtra("SHOP_DATA", shop)
                intent.putExtra("CLIENT_DATA", client)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }





}
