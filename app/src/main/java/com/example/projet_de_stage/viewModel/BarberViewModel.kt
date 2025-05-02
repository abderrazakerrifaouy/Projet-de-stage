package com.example.projet_de_stage.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projet_de_stage.data.Appointment
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.JoinRequest
import com.example.projet_de_stage.data.Shop
import com.example.projet_de_stage.repository.AppointmentRepository
import com.example.projet_de_stage.repository.BarberRepository
import com.example.projet_de_stage.repository.CustomerRepository
import com.example.projet_de_stage.repository.JoinRequestRepository
import com.example.projet_de_stage.repository.ShopRepository

/**
 * ViewModel for managing barber-related data and actions.
 */
class BarberViewModel : ViewModel() {

    // Repositories for accessing data
    private val shopRepository = ShopRepository()
    private val customerRepository = CustomerRepository()
    private val joinRequestRepository = JoinRequestRepository()
    private val appointmentRepository = AppointmentRepository()
    private val barberRepository = BarberRepository()

    // LiveData for observing appointments
    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    // LiveData for real-time appointments
    private val _realTimeAppointments = MutableLiveData<List<Appointment>>()
    val realTimeAppointments: LiveData<List<Appointment>> = _realTimeAppointments

    // LiveData for customer data
    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> = _customer

    // LiveData for errors
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Retrieves the shop associated with a barber.
     * @param barber Barber object to search for.
     * @return Shop associated with the barber or null if not found.
     */
    suspend fun getShopByBarber(barber: Barber): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull { shop -> barber in shop.barbers }
    }

    /**
     * Retrieves the shop by barber ID.
     * @param barberId The ID of the barber.
     * @return Shop associated with the barber or null if not found.
     */
    suspend fun getShopByBarberId(barberId: String): Shop? {
        return shopRepository.getAllShops()
            .firstOrNull {
                it.barbers.any { barber -> barber.uid == barberId }
            }
    }

    /**
     * Updates the shop details.
     * @param shop The shop object to update.
     * @param callback Callback with result of the operation.
     */
    fun updateShop(shop: Shop, callback: (Boolean) -> Unit) {
        shopRepository.updateShop(
            shop,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )
    }

    /**
     * Retrieves all shops.
     * @return List of all shops.
     */
    suspend fun getAllShops(): List<Shop> {
        return shopRepository.getAllShops()
    }

    /**
     * Creates a join request for a barber to join a shop.
     * @param request The join request data.
     * @param callback Callback with result of the operation.
     */
    fun createJoinRequests(request: JoinRequest, callback: (Boolean) -> Unit) {
        joinRequestRepository.addRequest(
            request,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )
    }

    /**
     * Retrieves appointments for a barber filtered by status.
     * @param status The status of appointments (e.g., pending, accepted).
     * @param barberId The ID of the barber.
     */
    fun getAppointmentByBarberIdAndStatus(
        status: String,
        barberId: String
    ) {
        appointmentRepository.getAllAppointmentsByBarberIdandStatus(
            status = status,
            barberId = barberId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
            }
        )
    }

    /**
     * Loads customer data by customer ID and updates the LiveData.
     * @param id The ID of the customer.
     */
    fun loadCustomerById(
        id: String,
    ) {
        customerRepository.getCustomerById(
            id = id,
            onSuccess = { customer ->
                _customer.postValue(customer)
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
            }
        )
    }

    /**
     * Retrieves all appointments for a barber by their ID.
     * @param barberId The ID of the barber.
     */
    fun getAllAppointmentsByBarberId(
        barberId: String
    ) {
        appointmentRepository.getAllAppointmentsByBarberId(
            barberId = barberId,
            onSuccess = { list ->
                _appointments.postValue(list)
                _error.postValue(null)
            },
            onFailure = { exception ->
                _error.postValue(exception.message)
            }
        )
    }

    /**
     * Modifies barber data.
     * @param barber The barber data to update.
     * @param callback Callback with result of the operation.
     */
    fun modifyBarber(barber: Barber, callback: (Boolean) -> Unit) {
        barberRepository.updateBarber(
            barber,
            onSuccess = {
                callback(true)
            },
            onFailure = {
                callback(false)
            }
        )
    }

    /**
     * Updates the status of an appointment.
     * @param appointmentId The ID of the appointment.
     * @param newStatus The new status of the appointment.
     * @param onSuccess Callback for success.
     * @param onFailure Callback for failure.
     */
    fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        appointmentRepository.updateAppointmentStatus(
            appointmentId = appointmentId,
            newStatus = newStatus,
            onSuccess = { onSuccess() },
            onFailure = { exception -> onFailure(exception) }
        )
    }

    /**
     * Listens for new appointments in real-time for a barber.
     * @param barberId The ID of the barber.
     * @param onNewAppointment Callback for new appointments.
     * @param onError Callback for errors.
     */
    fun listenToNewRealtimeAppointments(
        barberId: String,
        onNewAppointment: (Appointment) -> Unit,
        onError: (String) -> Unit
    ) {
        appointmentRepository.listenToNewAppointmentsForBarber(
            barberId = barberId,
            onNewAppointment = onNewAppointment,
            onError = { dbError -> onError(dbError.message) }
        )
    }

    /**
     * Deletes an appointment from the real-time database.
     * @param appointmentId The ID of the appointment to delete.
     * @param onSuccess Callback for success.
     */
    fun deleteAppointmentInRealtimeDatabase(
        appointmentId: String,
        onSuccess: (Boolean) -> Unit
    ) {
        appointmentRepository.deleteAppointmentInRealtimeDatabase(
            appointmentId = appointmentId,
            onSuccess = { onSuccess(true) },
            onFailure = { onSuccess(false) }
        )
    }
}
