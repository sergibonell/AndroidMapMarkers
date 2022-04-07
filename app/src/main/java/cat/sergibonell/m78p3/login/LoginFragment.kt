package cat.sergibonell.m78p3.login

import android.content.Intent
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
        loginButton = binding.loginButton
        registerButton = binding.registerButton

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
                        val emailLogged = it.result?.user?.email
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
                        val emailLogged = it.result?.user?.email
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
        startActivity(intent)
    }
}