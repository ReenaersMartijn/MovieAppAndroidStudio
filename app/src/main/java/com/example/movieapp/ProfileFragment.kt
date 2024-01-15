package com.example.movieapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.movieapp.databinding.FragmentMainBinding
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.viewModel.MovieViewModel
import com.example.movieapp.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val args = MovieFragmentArgs.fromBundle(requireArguments())
        val userId = args.id

        firebaseAuth = FirebaseAuth.getInstance()


        binding.MapButton.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToMapsFragment(userId)
            Navigation.findNavController(it).navigate(action)
        }
        binding.MoviesButton.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToMovieFragment(userId)
            Navigation.findNavController(it).navigate(action)
        }
        binding.LogOutButton.setOnClickListener {
            firebaseAuth.signOut()
            val action = ProfileFragmentDirections.actionProfileFragmentToMainFragment()
            Navigation.findNavController(it).navigate(action)
        }
        profileViewModel.getUserData(userId)
        {
            user ->
            binding.emailTextView.text = "Email: ${user?.email}"
            binding.userIdTextView.text = "ID: ${user?.userId}"
        }
        profileViewModel.getLikedMovies(userId) { movies ->
            var txt = ""

            if (movies != null) {
                for (movie in movies) {
                    txt += "${movie?.title} \n"
                }
            }

            binding.likedMoviesList.text = txt
        }

        return view
    }
}