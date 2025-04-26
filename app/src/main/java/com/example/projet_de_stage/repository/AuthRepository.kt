package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // تسجيل مستخدم جديد في Firebase Auth
    suspend fun registerUser(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("تعذر الحصول على UID")
    }

    suspend fun loginUser(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("تعذر الحصول على UID")
    }

    suspend fun getUserType(uid: String): Result<Any> {
        val collections = listOf("shop_owners", "barbers", "customers")
        for (collection in collections) {
            val doc = firestore.collection(collection).document(uid).get().await()
            if (doc.exists()) {
                return when (collection) {
                    "shop_owners" -> Result.success(doc.toObject(ShopOwner::class.java)!!)
                    "barbers" -> Result.success(doc.toObject(Barber::class.java)!!)
                    "customers" -> Result.success(doc.toObject(Customer::class.java)!!)
                    else -> Result.failure(Exception("Unknown type"))
                }
            }
        }
        return Result.failure(Exception("User not found in any collection"))
    }


    // حفظ معلومات المستخدم في Firestore
    suspend fun saveUserToFirestore(
        collection: String,
        uid: String,
        userObject: Any
    ) {
        firestore.collection(collection)
            .document(uid)
            .set(userObject)
            .await()
    }

    suspend fun getUserByUid(uid: String): Any? {
        val collections = listOf("shop_owners", "barbers", "customers")
        for (collection in collections) {
            val doc = firestore.collection(collection).document(uid).get().await()
            if (doc.exists()) {
                return when (collection) {
                    "shop_owners" -> doc.toObject(ShopOwner::class.java)
                    "barbers" -> doc.toObject(Barber::class.java)
                    "customers" -> doc.toObject(Customer::class.java)
                    else -> null
                }
            }
        }
        return null
    }
}
