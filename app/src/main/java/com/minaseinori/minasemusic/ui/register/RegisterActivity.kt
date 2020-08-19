package com.minaseinori.minasemusic.ui.register

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.minaseinori.minasemusic.R
import com.minaseinori.minasemusic.logic.model.RegisterRequest
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val mTAG = "RegisterActivity"
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewModel.tokenLiveData.observe(this, Observer {
            val data = it.getOrNull()
            if (data != null) {
                Log.d(mTAG, data.toString())
            } else {
                Toast.makeText(this, R.string.register_succeeded, Toast.LENGTH_SHORT).show()
            }
        })
        registerBtn?.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister() {
        val username = usernameEdit?.text.toString().trim()
        val email = emailEdit?.text.toString().trim()
        val password = passwordEdit?.text.toString().trim()
        val passwordAgain = passwordAgainEdit?.text.toString().trim()
        val registerReq = RegisterRequest(username, email, password, passwordAgain)
        viewModel.register(registerReq)
    }
}