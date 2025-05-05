package com.example.projet_de_stage.repository

import android.util.Log
import com.example.projet_de_stage.data.Appointment
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Repository that manages all appointment-related operations
 * using both Firestore and Firebase Realtime Database.
 */
class AppointmentRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val firestoreCollection = firestore.collection("appointments")

    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val realtimeAppointments = realtimeDb.child("appointments")

    /**
     * Adds a new appointment if no conflict exists (same barber, date, and time).
     */
    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", appointment.barberId)
            .whereEqualTo("date", appointment.date)
            .whereEqualTo("time", appointment.time)
            .get()
            .addOnSuccessListener {
                firestoreCollection.document(appointment.id)
                    .set(appointment)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }

                realtimeAppointments.child(appointment.id).setValue(appointment)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Retrieves all appointments made by a specific customer.
     */
    fun getAppointmentsByCustomerId(
        customerId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("clientId", customerId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    /**
     * Retrieves appointments by barber ID and status (e.g., pending, accepted).
     */
    fun getAllAppointmentsByBarberIdandStatus(
        status: String,
        barberId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", barberId)
            .whereEqualTo("status", status)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch appointments", exception)
                onFailure(exception)
            }
    }

    /**
     * Retrieves all appointments related to a specific barber regardless of status.
     */
    fun getAllAppointmentsByBarberId(
        barberId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("barberId", barberId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch appointments", exception)
                onFailure(exception)
            }
    }

    /**
     * Updates the status field of an appointment in both Firestore and Realtime Database.
     * @param appointmentId The ID of the appointment to update.
     * @param newStatus The new status to set.
     */
    fun updateAppointmentStatus(
        appointmentId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection.document(appointmentId)
            .update("status", newStatus)
            .addOnSuccessListener {
                realtimeAppointments.child(appointmentId).child("status").setValue(newStatus)
                onSuccess()
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Listens for new "pending" appointments for a specific barber in Realtime Database.
     * @param barberId The ID of the barber to listen for appointments.
     * @param onNewAppointment Callback to invoke when a new appointment is found.
     * @param onError Callback to handle errors during the listen operation.
     * @see Appointment
     * @see DataSnapshot
     * @see DatabaseError
     */
    fun listenToNewAppointmentsForBarber(
        barberId: String,
        onNewAppointment: (Appointment) -> Unit,
        onError: (DatabaseError) -> Unit
    ) {
        val seenAppointments = mutableSetOf<String>()

        realtimeAppointments.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val appointment = snapshot.getValue(Appointment::class.java)
                if (appointment != null &&
                    appointment.barberId == barberId &&
                    appointment.status == "pending" &&
                    !seenAppointments.contains(appointment.id)
                ) {
                    seenAppointments.add(appointment.id)
                    onNewAppointment(appointment)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    /**
     * Deletes an appointment from the Realtime Database.
     * @param appointmentId The ID of the appointment to delete.
     * @param onSuccess Callback to invoke on successful deletion.
     * @param onFailure Callback to handle errors during the deletion operation.
     */
    fun deleteAppointmentInRealtimeDatabase(
        appointmentId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        realtimeAppointments.child(appointmentId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }
    }

    /**
     * Retrieves all appointments for a specific shop.
     * @param shopId The ID of the shop.
     * @param onSuccess Callback to invoke with the list of appointments.
     * @param onFailure Callback to handle errors during the operation.
     */
    fun getAppointmentsByShopId(
        shopId: String,
        onSuccess: (List<Appointment>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection
            .whereEqualTo("shopId", shopId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }


    /**
     * Deletes a request from Firestore based on shop ID and barber ID.
     * @param shopId The ID of the shop.
     * @param barberId The ID of the barber.
     * @param onSuccess Callback to invoke on successful deletion.
     */
    fun deleteRequestByShopIdAndBarberId(
        shopId: String,
        barberId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreCollection.whereEqualTo("shopId", shopId)
            .whereEqualTo("barberId", barberId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                }
            }
            .addOnFailureListener { e -> onFailure(e) }

    }
}
