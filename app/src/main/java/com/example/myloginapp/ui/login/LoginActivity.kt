package com.example.myloginapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myloginapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginState.observe(this@LoginActivity, {
            processLoginScreen(it)
        })
        username.afterTextChanged {
            loginViewModel.changeEmail(
                username.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.changePassword(
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login()
            }
        }
    }

    private fun processLoginScreen(loginScreen: LoginScreen?) {
        when (loginScreen) {
            is LoginScreen.InitState -> {
            }
            is LoginScreen.FormInput -> processUIFormInput(loginScreen)
            LoginScreen.Loading -> TODO()
            null -> TODO()
        }
    }

    private fun processUIFormInput(formInput: LoginScreen.FormInput) {
        binding.loading.visibility = View.INVISIBLE

        when (formInput.emailInput.second) {
            false -> binding.username.error = "error email"
            else -> {}
        }
        when (formInput.passwordInput.second) {
            false -> binding.password.error = "error password"
            else -> {}
        }

        loginViewModel.resetState()

        if(formInput.isValidForm){
            Toast.makeText(applicationContext,"Login successful",Toast.LENGTH_LONG).show()
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}