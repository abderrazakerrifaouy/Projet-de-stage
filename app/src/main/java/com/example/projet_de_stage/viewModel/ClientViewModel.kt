package com.example.projet_de_stage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // LiveData للمحلات
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    // LiveData بيانات العميل
    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> = _customer

    // LiveData للمواعيد
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    // LiveData للأخطاء
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * يجلب جميع المحلات من المخزن ويحدث LiveData
     */
    suspend fun getShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

    /**
     * يضيف موعد جديد ويستجيب لحالات النجاح أو الفشل
     */
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

    /**
     * يجلب بيانات العميل حسب المعرف ويحدث LiveData
     */
    fun loadCustomerById(
        id: String,
        onSuccess: (Customer?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        clientRepository.getCustomerById(
            id = id,
            onSuccess = { c ->
                _customer.postValue(c)
                onSuccess(c)
                _error.postValue(null)
            },
            onFailure = { e ->
                _error.postValue(e.message)
                onFailure(e)
            }
        )
    }

    /**
     * تحدّث بيانات العميل في المخزن
     */
    fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        clientRepository.updateCustomer(
            customer = customer,
            onSuccess = {
                onSuccess()
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
                onFailure(exception)
            }
        )
    }

    /**
     * يجلب المواعيد الخاصة بالعميل ويحدّث LiveData
     *
     * @param customerId معرف العميل
     */
    fun fetchAppointmentsByCustomerId(customerId: String) {
        appointmentRepository.getAppointmentsByCustomerId(
            customerId = customerId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
            }
        )
    }
}
