package com.example.projet_de_stage.repository

import android.util.Log
import com.example.projet_de_stage.data.Appointment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val firestoreCollection = firestore.collection("appointments")

    private val realtimeDb = FirebaseDatabase.getInstance().reference
    private val realtimeAppointments = realtimeDb.child("appointments")

    fun addAppointment(
        appointment: Appointment,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
        onConflict: () -> Unit // حالة وجود تعارض
    ) {
        // أولا نبحث إذا كان كاين موعد بنفس اليوم والساعة والحلاق
        firestoreCollection
            .whereEqualTo("barberId", appointment.barberId)
            .whereEqualTo("date", appointment.date.toString()) // LocalDate نخليه كـ String
            .whereEqualTo("time", appointment.time)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // ما كاين حتى تعارض ➔ نضيفه
                    firestoreCollection.document(appointment.id)
                        .set(appointment)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onFailure(e) }
                    realtimeAppointments.child(appointment.id).setValue(appointment)
                } else {
                    // كاين رونديڤو فـ نفس الوقت
                    onConflict()
                }
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
}
