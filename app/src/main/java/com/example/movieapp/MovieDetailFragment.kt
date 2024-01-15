package com.example.movieapp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.viewModel.DetailsViewModel
import com.example.movieapp.viewModel.MovieViewModel
import com.squareup.picasso.Picasso


class MovieDetailFragment : Fragment() {


    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieDetailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        movieDetailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        val args = MovieDetailFragmentArgs.fromBundle(requireArguments())

        val movieId = args.movieId

        val userId = args.userId

        var title = ""

        movieDetailsViewModel.getMovie(movieId) { movie ->
            binding.movieDetailTitle.text = movie?.Title

            binding.categories.text = movie?.Categories

            title = movie?.Title.toString()
            // Load movie image using Picasso
            movie?.Image?.let { imageUrl ->
                Picasso.get().load(imageUrl).into(binding.movieDetailImage)

            }
        }

        binding.LikeButton.setOnClickListener {
            movieDetailsViewModel.likeMovie(movieId, userId, title)
        }

        binding.GoBackButton.setOnClickListener {
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieFragment(userId)
            Navigation.findNavController(it).navigate(action)
        }

        return view
    }


}