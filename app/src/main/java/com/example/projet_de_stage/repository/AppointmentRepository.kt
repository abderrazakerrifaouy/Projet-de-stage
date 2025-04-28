package com.example.projet_de_stage.repository

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


}
