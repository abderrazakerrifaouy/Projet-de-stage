package com.example.projet_de_stage.repository

import android.util.Log
import com.example.projet_de_stage.data.Appointment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val firestoreCollection = firestore.collection("appointments")

    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val realtimeAppointments = realtimeDb.child("appointments")

    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // أولا نبحث إذا كان كاين موعد بنفس اليوم والساعة والحلاق
        firestoreCollection
            .whereEqualTo("barberId", appointment.barberId)
            .whereEqualTo("date", appointment.date.toString()) // LocalDate نخليه كـ String
            .whereEqualTo("time", appointment.time)
            .get()
            .addOnSuccessListener {
                    // ما كاين حتى تعارض ➔ نضيفه
                    firestoreCollection.document(appointment.id)
                        .set(appointment)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                    realtimeAppointments.child(appointment.id).setValue(appointment)

            }
            .addOnFailureListener { e -> onFailure(e) }
    }
    /**
     * تجلب قائمة المواعيد (Appointment) الخاصة بالعميل من Firestore
     *
     * @param customerId معرف العميل
     * @param onSuccess يعاد استدعاؤه مع اللائحة عند النجاح
     * @param onFailure يعاد استدعاؤه مع الاستثناء عند الفشل
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
                // نحول كل مستند لكائن Appointment إذا أمكن
                val appointments = querySnapshot.documents
                    .mapNotNull { it.toObject(Appointment::class.java) }
                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun getAllAppointmentsByBarberIdandStatus(
        status : String,
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


    fun listenToNewAppointmentsForBarber(
        barberId: String,
        onNewAppointment: (Appointment) -> Unit,
        onError: (DatabaseError) -> Unit
    ) {
        val seenAppointments = mutableSetOf<String>()

        realtimeAppointments.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val appointment = snapshot.getValue(Appointment::class.java)
                if (appointment != null && appointment.barberId == barberId && appointment.status == "pending") {
                    if (!seenAppointments.contains(appointment.id)) {
                        seenAppointments.add(appointment.id)
                        onNewAppointment(appointment)
                    }
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

    fun deleteAppointmentInRealtimeDatabase(
        appointmentId: String ,
        onSuccess: () -> Unit,
        onFailure: () -> Unit) {
        realtimeAppointments.child(appointmentId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }

    }


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
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


}
