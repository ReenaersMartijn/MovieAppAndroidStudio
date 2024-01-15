package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.movieapp.databinding.FragmentMainBinding
import com.example.movieapp.databinding.FragmentMovieBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        binding.SignInButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.RegisterButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }
        return binding.root
    }
}
