package com.example.projet_de_stage.viewModel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.CustomerRepository
import com.example.projet_de_stage.repository.ShopRepository

class ClientViewModel : ViewModel() {
    private val clientRepository = CustomerRepository()
    private val shopRepository = ShopRepository()
    private val appointmentRepository = AppointmentRepository()



    suspend fun getShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
        onConflict: () -> Unit
    ) {
        appointmentRepository.addAppointment(
            appointment = appointment,
            onSuccess = { onSuccess() },
            onFailure = { e -> onFailure(e) },
            onConflict = { onConflict() }
        )
    }

    fun getCustomerById(
        id: String,
        onSuccess: (Customer?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        clientRepository.getCustomerById(
            id = id,
            onSuccess = { customer -> onSuccess(customer) },
            onFailure = { e -> onFailure(e) }
        )
    }


}