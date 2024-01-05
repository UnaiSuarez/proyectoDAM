package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity() {

    companion object {
        lateinit var useremail: String
        lateinit var providerSession: String
    }

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private var password2 by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText

    private lateinit var mAuth: FirebaseAuth

    private var RESULT_CODE_GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        etEmail = findViewById(R.id.editTextEmail)
        etPassword = findViewById(R.id.editTextPassword)
        etPassword2 = findViewById(R.id.editTextPassword2)
        mAuth = FirebaseAuth.getInstance()

        manageButtonRegister()
        etEmail.doOnTextChanged { text, start, before, count -> manageButtonRegister() }
        etPassword.doOnTextChanged { text, start, before, count -> manageButtonRegister() }
    }


    private fun manageButtonRegister() {
        var buttonRegister = findViewById<View>(R.id.textViewregister)
        email = etEmail.text.toString()
        password = etPassword.text.toString()
        password2 = etPassword2.text.toString()
        if (!ValidateEmail.isEmailValid(email) || TextUtils.isEmpty(password)){
            buttonRegister.background = resources.getDrawable(R.drawable.button_background_disabled)
            buttonRegister.isEnabled = false
        } else {
            if (password != password2){
                buttonRegister.background = resources.getDrawable(R.drawable.button_background_disabled)
                buttonRegister.isEnabled = false
            }else{
                buttonRegister.background = resources.getDrawable(R.drawable.button_background)
                buttonRegister.isEnabled = true
            }
        }
    }

     fun setLogin(view: View){
        intentLogin()
    }

    private fun intentLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun register(view: View){
        registerUser()
    }

    private fun registerUser() {
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    val dbregister = FirebaseFirestore.getInstance()
                    dbregister.collection("users").document(email).set(
                        hashMapOf(
                            "user" to email,
                            "dateRegister" to dateRegister
                        )
                    )

                    goHome(email, "email")
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun callSignInGoogle(view: View){
        signInGoogle()
    }

    private fun signInGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()


        startActivityForResult(googleSignInClient.signInIntent, RESULT_CODE_GOOGLE_SIGN_IN)
    }

    private fun goHome(email: String, provider: String){
        LoginActivity.useremail = email
        LoginActivity.providerSession = provider
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_CODE_GOOGLE_SIGN_IN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!

                if (account != null){
                    email = account.email!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    mAuth.signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            val dateRegister = SimpleDateFormat("dd/MM/yyyy").format(Date())
                            val dbregister = FirebaseFirestore.getInstance()
                            dbregister.collection("users").document(email).set(
                                hashMapOf(
                                    "user" to email,
                                    "dateRegister" to dateRegister
                                )
                            )
                            goHome(email, "Google")
                        }
                        else Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT).show()

                    }
                }


            } catch (e: ApiException) {
                Toast.makeText(this, "Error en la conexión con Google", Toast.LENGTH_SHORT).show()
            }
        }

    }
}