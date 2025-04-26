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

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<String>?>(null)
    val authState: StateFlow<Result<String>?> = _authState

    fun registerUser( user: Any) {
        viewModelScope.launch {
            try {
                // استخراج الإيميل حسب نوع المستخدم
                val email = when (user) {
                    is Barber -> user.email
                    is Customer -> user.email
                    is ShopOwner -> user.email
                    else -> throw IllegalArgumentException("نوع مستخدم غير معروف")
                }
                val password = when (user) {
                    is Barber -> user.password
                    is Customer -> user.password
                    is ShopOwner -> user.password
                    else -> throw IllegalArgumentException("نوع مستخدم غير معروف")
                }

                // تسجيل المستخدم في Firebase Auth
                val uid = repository.registerUser(email, password)

                // تعيين uid وكلمة السر للمستخدم
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

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val uid = repository.loginUser(email, password)
                _authState.value = Result.success(uid)
            } catch (e: Exception) {
                _authState.value = Result.failure(e)
            }
        }
    }
    suspend fun getUserByUid(uid: String) : Any? {
        val user = repository.getUserByUid(uid)

        return user
    }
}
