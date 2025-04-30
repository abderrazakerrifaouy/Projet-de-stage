package com.example.projet_de_stage.fragment.fragmentClient

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projet_de_stage.R
import com.example.projet_de_stage.adapter.adapterClient.AppointmentAdapter
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.viewModel.ClientViewModel

class HistoryFragmentClient : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: AppointmentAdapter
    private val viewModel: ClientViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_requests_client, container, false)

        recyclerView = view.findViewById(R.id.list_item_historyClient)
        progressBar = view.findViewById(R.id.progressBarHistory)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // استلام بيانات العميل من الـ arguments
        val customer: Customer? = arguments?.getParcelable("customer")
        val customerId = customer?.uid
        if (customerId == null) {
            Toast.makeText(requireContext(), "لم يتم العثور على بيانات العميل", Toast.LENGTH_SHORT).show()
            return
        }

        // إعداد RecyclerView و Adapter
        adapter = AppointmentAdapter( mutableListOf()) { appointment ->
            Toast.makeText(requireContext(), "تم حذف موعد بنجاح", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observeViewModel()

        // تحميل البيانات من ViewModel
        progressBar.visibility = View.VISIBLE
        viewModel.fetchAppointmentsByCustomerId(customerId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        viewModel.appointments.observe(viewLifecycleOwner) { list ->
            progressBar.visibility = View.GONE
            adapter.updateData(list)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            message?.let {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "خطأ: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

}
