package com.example.movieapp
import com.example.movieapp.adapter.MovieAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.viewModel.MovieViewModel

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieAdapter = MovieAdapter()

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMovies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = movieAdapter

        movieViewModel.getMovies().observe(
            viewLifecycleOwner,
        ) { movies ->
            movieAdapter.submitList(movies)
        }

        val searchBar: EditText = view.findViewById(R.id.searchBar)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                movieViewModel.searchMovies(s.toString())
            }
        })

        val navController = findNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.movieDetailFragment) {
                // Handle any detail fragment-specific logic here
            }
        }

        return view
    }
}
