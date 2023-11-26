package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val action = LogInFragmentDirections.actionLogInFragmentToMainFragment()
                        Navigation.findNavController(requireView()).navigate(action)

                    } else {
                        Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val action = LogInFragmentDirections.actionLogInFragmentToMainFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
}
