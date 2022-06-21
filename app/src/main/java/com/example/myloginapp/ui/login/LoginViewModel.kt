package com.example.myloginapp.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<LoginScreen>()
    val loginState: LiveData<LoginScreen> = _loginState


    fun login() {
        val state = loginState.value
        if (state is LoginScreen.FormInput) {
            val validatedEmail = state.emailInput.first
            val validatedPassword = state.passwordInput.first
            _loginState.value = state.copy(
                emailInput = Pair(validatedEmail, isUserNameValid(validatedEmail)),
                passwordInput = Pair(validatedPassword, isPasswordValid(validatedPassword))
            )
        }
    }

    fun changeEmail(email: String) {
        val state = loginState.value
        if (state is LoginScreen.FormInput) {
            _loginState.value = state.copy(emailInput = Pair(email, false))
        }
    }

    fun changePassword(password: String) {
        val state = loginState.value
        if (state is LoginScreen.FormInput) {
            _loginState.value = state.copy(passwordInput = Pair(password, false))
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }
}

sealed class LoginScreen {
    object Loading : LoginScreen()
    data class FormInput(
        val emailInput: Pair<String, Boolean>,
        val passwordInput: Pair<String, Boolean>
    ) : LoginScreen() {
        val isValidForm get() = emailInput.second && passwordInput.second
    }
}