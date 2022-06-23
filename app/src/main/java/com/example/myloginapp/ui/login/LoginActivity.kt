package com.example.myloginapp.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myloginapp.R
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
            loginViewModel.resetState()
            loginViewModel.changeEmail(
                username.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.resetState()
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
                resetErrorState()
            }
            is LoginScreen.FormInput -> processUIFormInput(loginScreen)
            LoginScreen.Loading -> TODO("Create loading state to handle it here")
            null -> TODO("Handle null state")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetErrorState(){
        binding.username.background = getDrawable(R.drawable.editext_base_background)
        binding.password.background = getDrawable(R.drawable.editext_base_background)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun processUIFormInput(formInput: LoginScreen.FormInput) {
        binding.loading.visibility = View.INVISIBLE

        when (formInput.emailInput.second) {
            false -> binding.username.background = getDrawable(R.drawable.editext_error_background)
            else ->{}
        }
        when (formInput.passwordInput.second) {
            false -> binding.password.background = getDrawable(R.drawable.editext_error_background)
            else ->{}
        }

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