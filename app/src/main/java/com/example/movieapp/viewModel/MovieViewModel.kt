package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Movie
import com.google.firebase.database.*

class MovieViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Movies")
    private val moviesLiveData: MutableLiveData<List<Movie>?> = MutableLiveData()
    private val originalMoviesList: MutableList<Movie> = mutableListOf()

    init {
        fetchMovies()
    }

    fun getMovies(): MutableLiveData<List<Movie>?> {
        return moviesLiveData
    }

    private fun fetchMovies() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                originalMoviesList.clear()
                val moviesList: MutableList<Movie> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val movie = snapshot.getValue(Movie::class.java)
                    movie?.let {
                        moviesList.add(it)
                        originalMoviesList.add(it)
                    }
                }

                moviesLiveData.value = moviesList
                Log.d("MovieViewModel", "Retrieved movies: $moviesList")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MovieViewModel", "Error fetching movies: ${databaseError.message}")
                moviesLiveData.value = null
            }
        })
    }

    fun searchMovies(query: String) {
        val filteredList = originalMoviesList.filter { movie ->
            movie.Title?.contains(query, ignoreCase = true) ?: false
        }
        moviesLiveData.value = filteredList
    }
}



