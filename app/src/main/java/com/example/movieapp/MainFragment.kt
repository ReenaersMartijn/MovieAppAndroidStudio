package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.movieapp.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.LogOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val action = MainFragmentDirections.actionMainFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        }
        binding.MovieButton.setOnClickListener {

            val action = MainFragmentDirections.actionMainFragmentToMovieFragment()
            Navigation.findNavController(it).navigate(action)

        }


        return binding.root
    }
}
