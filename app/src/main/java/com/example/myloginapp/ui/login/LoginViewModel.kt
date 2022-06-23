package com.example.myloginapp.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+-_=])(?=\\S+$).{4,}$"
class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<LoginScreen>(LoginScreen.InitState("", ""))
    val loginState: LiveData<LoginScreen> = _loginState


    fun login() {
        val state = loginState.value
        state?.let { state ->
            val validatedEmail = state.email
            val validatedPassword = state.password
            _loginState.value = LoginScreen.FormInput(
                emailInput = Pair(validatedEmail, isUserNameValid(validatedEmail)),
                passwordInput = Pair(validatedPassword, isPasswordValid(validatedPassword))
            )
        }
    }

    fun resetState() {
        val state = loginState.value
        state?.let {
            if (state is LoginScreen.FormInput) {
                _loginState.value = LoginScreen.InitState(state.email, state.password)
            }
        }
    }

    fun changeEmail(email: String) {
        val state = loginState.value
        if (state is LoginScreen.InitState) {
            _loginState.value = state.copy(email = email)
        }
    }

    fun changePassword(password: String) {
        val state = loginState.value
        if (state is LoginScreen.InitState) {
            _loginState.value = state.copy(password = password)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches() && username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        return pattern.matcher(password).matches() && password.isNotBlank()
    }
}

sealed class LoginScreen(open val email: String, open val password: String) {
    data class InitState(override val email: String, override val password: String) :
        LoginScreen(email, password)

    object Loading : LoginScreen("", "")
    data class FormInput(
        val emailInput: Pair<String, Boolean>,
        val passwordInput: Pair<String, Boolean>
    ) : LoginScreen(emailInput.first, passwordInput.first) {
        val isValidForm get() = emailInput.second && passwordInput.second
    }
}