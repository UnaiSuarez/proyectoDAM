package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
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

class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var useremail: String
        lateinit var providerSession: String
    }

    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var mAuth: FirebaseAuth

    private var RESULT_CODE_GOOGLE_SIGN_IN = 100

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

            etEmail = findViewById(R.id.editTextEmail)
            etPassword = findViewById(R.id.editTextPassword)
            mAuth = FirebaseAuth.getInstance()

            manageButtonLogin()
            etEmail.doOnTextChanged { text, start, before, count -> manageButtonLogin() }
            etPassword.doOnTextChanged { text, start, before, count -> manageButtonLogin() }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            goHome(currentUser.email.toString(), currentUser.providerId)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    private fun manageButtonLogin(){
        var btnLogin = findViewById<View>(R.id.textViewLogin)
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        if (!ValidateEmail.isEmailValid(email) || TextUtils.isEmpty(password)){
            btnLogin.background = resources.getDrawable(R.drawable.button_background_disabled)
            btnLogin.isEnabled = false
        } else {
            btnLogin.background = resources.getDrawable(R.drawable.button_background)
            btnLogin.isEnabled = true
        }
    }

    fun login(view: View){
        loginUser()
    }

    fun register(view: View){
        registerUser()
    }

    private fun loginUser(){
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) goHome(email, "email")
                else {
                    showAlert()
                }
            }
    }

    private fun goHome(email: String, provider: String){
        useremail = email
        providerSession = provider
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    private fun showAlert(){
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }



    private fun registerUser(){
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    fun forgotPassword(view: View){
        resetPassword()
    }

    private fun resetPassword(){
        email = etEmail.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el email", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Se ha enviado un email para restablecer su contraseña", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar el email para restablecer su contraseña", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun callSignInGoogle(view: View){
        signInGoogle()
    }

    private fun signInGoogle(){
        // Configuración de Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Cliente de Google Sign In
        var googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        // Iniciar la actividad de autenticación con Google
        startActivityForResult(googleSignInClient.signInIntent, RESULT_CODE_GOOGLE_SIGN_IN)
    }

    // Resultado de la autenticación con Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Resultado de la autenticación con Google exitosa
        if (requestCode == RESULT_CODE_GOOGLE_SIGN_IN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                //Inicio de sesión con Google exitoso
                val account = task.getResult(ApiException::class.java)!!
                //Obtener credenciales de Google
                if (account != null){
                    email = account.email!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    // Inicio de sesión con Firebase
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
                            // Ir a la pantalla principal
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
