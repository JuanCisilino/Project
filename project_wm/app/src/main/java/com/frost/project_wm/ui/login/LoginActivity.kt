package com.frost.project_wm.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.frost.project_wm.*
import com.frost.project_wm.databinding.ActivityLoginBinding
import com.frost.project_wm.model.User
import com.frost.project_wm.ui.dialog.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var binding: ActivityLoginBinding
    private val GOOGLE_SIGN_IN = 100
    private var loadingDialog = LoadingDialog(R.string.loading_message)
    private var initializingDialog = LoadingDialog(R.string.server_initializing)
    private var firstRun = true

    companion object{
        fun start(activity: Activity){
            activity.startActivity(Intent(activity.baseContext, LoginActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initServer()
        setBinding()
        setBtns()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(this, Observer { handleUser(it) })
        viewModel.userListLiveData.observe(this, Observer { handleUserList(it) })
    }

    private fun handleUserList(userList: List<User>?) {
        userList?.let { checkSession() }
            ?:run { loadingServerDialog() }
    }

    private fun handleUser(user: User?) {
        user?.let {
            firstRun = false
            viewModel.save(it)
            MainActivity.start(this)
            finish()
        }
            ?: run {
                loadingDialog.dismiss()
                Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadingServerDialog() {
        initializingDialog.show(supportFragmentManager)
        firstRun = false
        Handler().postDelayed( { viewModel.initServer() }, 3000)
    }

    private fun checkSession() {
        if (!firstRun) initializingDialog.dismiss()
        val email = viewModel.getData(getString(R.string.shared_pref_email))
        email?.let { if (it.isNotBlank()) viewModel.sessionLogin(it) }
    }

    private fun setBtns() {
        binding.btn.setOnClickListener {
            loadingDialog.show(supportFragmentManager)
            viewModel.godLogin() }
        binding.googleButton.setOnClickListener { startGoogle() }
    }

    private fun startGoogle() {
        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1077617626551-97u78q84mv36j79a8ebjmng4e1d1peo3.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConfig)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    private fun setBinding(){
        viewModel.setUserPrefs(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() { loadingDialog.dismiss() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    signInWithCredential(GoogleAuthProvider.getCredential(it.idToken, null))
                        .addOnCompleteListener {
                            loadingDialog.show(supportFragmentManager)
                            account.email?.let { viewModel.getUserByEmail(it) }
                            Handler().postDelayed(Runnable {
                                if (it.isSuccessful){
                                    account.email?.let { validateAndContinue(account) }
                                }else {
                                    showAlert()
                                }
                            }, 2000)
                        }
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }

    private fun validateAndContinue(account: GoogleSignInAccount) {
        val newUser = User(
            email = account.email?:"",
            nombre = account.displayName?:"none",
            rol = "user",
            empresa = "WM"
        )
        viewModel.user?.let { handleUser(it) }
            ?:run { viewModel.saveUser(newUser) }

    }
}