package com.example.projet_de_stage.repository

import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository responsible for handling user authentication
 * and user data management using Firebase Auth and Firestore.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Registers a new user in Firebase Authentication using email and password.
     * @return UID of the newly created user.
     * @throws Exception if UID cannot be retrieved.
     */
    suspend fun registerUser(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Failed to retrieve UID")
    }

    /**
     * Logs in a user using Firebase Authentication with email and password.
     * @return UID of the logged-in user.
     * @throws Exception if UID cannot be retrieved.
     */
    suspend fun loginUser(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Failed to retrieve UID")
    }

    /**
     * Checks Firestore collections to determine the user type (ShopOwner, Barber, or Customer).
     * @return Result wrapping the user object.
     */
    suspend fun getUserType(uid: String): Result<Any> {
        val collections = listOf("shop_owners", "barbers", "customers")
        for (collection in collections) {
            val doc = firestore.collection(collection).document(uid).get().await()
            if (doc.exists()) {
                return when (collection) {
                    "shop_owners" -> Result.success(doc.toObject(ShopOwner::class.java)!!)
                    "barbers" -> Result.success(doc.toObject(Barber::class.java)!!)
                    "customers" -> Result.success(doc.toObject(Customer::class.java)!!)
                    else -> Result.failure(Exception("Unknown user type"))
                }
            }
        }
        return Result.failure(Exception("User not found in any collection"))
    }

    /**
     * Saves a user object to Firestore under the specified collection and UID.
     */
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

    /**
     * Retrieves a user object from Firestore based on UID by checking all known user collections.
     * @return The user object if found, null otherwise.
     */
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
