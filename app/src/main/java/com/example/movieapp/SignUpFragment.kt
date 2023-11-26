package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.navigation.Navigation

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        } else {
                            Toast.makeText(activity, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(activity, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
