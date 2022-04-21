package cat.sergibonell.m78p3.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import cat.sergibonell.m78p3.content.ContentActivity
import cat.sergibonell.m78p3.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var sharedPrefs: SharedPreferences
    private var emailUsed = "default"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)!!

        loginButton = binding.loginButton
        registerButton = binding.registerButton

        binding.emailField.setText(sharedPrefs.getString("lastEmail", ""))

        loginButton.setOnClickListener {
            loginUser()
        }

        registerButton.setOnClickListener {
            registerUser()
        }

        binding.googleButton.setOnClickListener {
            goToMap()
        }
    }

    fun loginUser(){
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if(email != "" && password != ""){
            FirebaseAuth.getInstance().
            signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        with (sharedPrefs.edit()) {
                            putString("lastEmail", email)
                            apply()
                        }
                        emailUsed = email
                        goToMap()
                    }
                    else{
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_LONG).show()
        }
    }

    fun registerUser(){
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()

        if(email != "" && password != ""){
            FirebaseAuth.getInstance().
            createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        emailUsed = email
                        goToMap()
                    }
                    else{
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_LONG).show()
        }
    }

    fun goToMap(){
        val intent = Intent(context, ContentActivity::class.java)
        intent.putExtra("email", emailUsed)
        startActivity(intent)
    }
}