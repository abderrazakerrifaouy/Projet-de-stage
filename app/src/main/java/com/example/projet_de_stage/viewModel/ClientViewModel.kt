package com.example.projet_de_stage.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.CustomerRepository
import com.example.projet_de_stage.repository.ShopRepository

class ClientViewModel : ViewModel() {
    private val clientRepository = CustomerRepository()
    private val shopRepository = ShopRepository()
    private val appointmentRepository = AppointmentRepository()
    private val barberRepository = BarberRepository()

    // LiveData للمحلات
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops



    private val _barber = MutableLiveData<Barber?>() // barberId -> barberName
    val barber: LiveData<Barber?> = _barber

    private val _shop = MutableLiveData<Shop?>()
    val shop: LiveData<Shop?> = _shop

    // LiveData للمواعيد
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    private val _appointmentsDate = MutableLiveData<List<Appointment>>()
    val appointmentsDate: LiveData<List<Appointment>> = _appointmentsDate

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

    fun getAppointmentsByIdBarebr(
        uid : String ,
    ){
        appointmentRepository.getAllAppointmentsByBarberIdandStatus(
            status = "accepted",
            barberId = uid ,
            onSuccess = { list ->
                _appointmentsDate.postValue(list)
                Log.e("Firestore", "Failed to fetch appointments ${list.size} ")
                _error.postValue(null)
            } ,
            onFailure = { exceptoin ->
                _error.postValue(exceptoin.message)
                Log.e("FirestoreError", "Failed to fetch appointments", exceptoin)

            }
        )
    }

    fun getBarberById(
         id : String,
    ){
        barberRepository.getBarberById(
            id = id,
            onSuccess = { barber ->
                _barber.postValue(barber)
            },
            onFailure = { e ->
                _error.postValue(e.message)
            })
    }

    fun getShopById(
        id : String,
    ){
        shopRepository.getShopById(
            id = id,
            onSuccess = { shop ->
                _shop.postValue(shop)
            },
            onFailure = { e ->
                _error.postValue(e.message)
            })
    }

}
