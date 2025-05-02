package com.example.projet_de_stage.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet_de_stage.data.Barber
import com.example.projet_de_stage.data.Customer
import com.example.projet_de_stage.data.ShopOwner
import com.example.projet_de_stage.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class to handle authentication related tasks
 */
class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<String>?>(null)
    val authState: StateFlow<Result<String>?> = _authState

    /**
     * Registers a new user in Firebase Auth and Firestore.
     *
     * @param user The user to be registered, can be a Barber, Customer, or ShopOwner.
     */
    fun registerUser(user: Any) {
        viewModelScope.launch {
            try {
                // Extract the email and password based on the user type
                val email = when (user) {
                    is Barber -> user.email
                    is Customer -> user.email
                    is ShopOwner -> user.email
                    else -> throw IllegalArgumentException("Unknown user type")
                }
                val password = when (user) {
                    is Barber -> user.password
                    is Customer -> user.password
                    is ShopOwner -> user.password
                    else -> throw IllegalArgumentException("Unknown user type")
                }

                // Register the user in Firebase Auth
                val uid = repository.registerUser(email, password)

                // Save user data to Firestore based on the user type
                when (user) {
                    is Barber -> {
                        user.uid = uid
                        repository.saveUserToFirestore("barbers", uid, user)
                    }
                    is Customer -> {
                        user.uid = uid
                        repository.saveUserToFirestore("customers", uid, user)
                    }
                    is ShopOwner -> {
                        user.uid = uid
                        repository.saveUserToFirestore("shop_owners", uid, user)
                    }
                }

                _authState.value = Result.success(uid)

            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            }
        }
    }

    /**
     * Logs in an existing user.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     */
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // Login the user with the provided email and password
                val uid = repository.loginUser(email, password)
                _authState.value = Result.success(uid)
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            }
        }
    }

    /**
     * Fetches user data by UID.
     *
     * @param uid The UID of the user.
     * @return The user object, which can be of type Barber, Customer, or ShopOwner.
     */
    suspend fun getUserByUid(uid: String): Any? {
        return repository.getUserByUid(uid)
    }
}
