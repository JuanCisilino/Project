package com.frost.project_wm.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.frost.project_wm.MainActivity
import com.frost.project_wm.R
import com.frost.project_wm.UserPrefs
import com.frost.project_wm.databinding.ActivityLoginBinding
import com.frost.project_wm.model.User

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setBtns()
        checkSession()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(this, Observer { handleUser(it) })
    }

    private fun handleUser(user: User?) {
        user?.let {
            UserPrefs(this).save(getString(R.string.shared_pref_email), it.email)
            UserPrefs(this).save(getString(R.string.shared_pref_name), it.nombre)
            UserPrefs(this).save(getString(R.string.shared_pref_role), it.rol)
            UserPrefs(this).save(getString(R.string.shared_pref_company), it.empresa)
            MainActivity.start(this)
        }
            ?:run {  }
    }

    private fun checkSession() { }

    private fun setBtns() {
        binding.btn.setOnClickListener { viewModel.getUserByEmail("test@testing.com") }
    }

    private fun setBinding(){
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() { }

}