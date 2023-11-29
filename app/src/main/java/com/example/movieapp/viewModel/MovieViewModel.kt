package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Movie
import com.google.firebase.database.*

class MovieViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Movies")
    private val moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    fun getMovies(): MutableLiveData<List<Movie>> {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val moviesList: MutableList<Movie> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val movie = snapshot.getValue(Movie::class.java)
                    movie?.let { moviesList.add(it) }
                }

                moviesLiveData.value = moviesList
                Log.d("MovieViewModel", "Retrieved movies: $moviesList") // Logging the retrieved movies
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MovieViewModel", "Error fetching movies: ${databaseError.message}")
                // Notify observers of an error or handle it as required
                null.also { moviesLiveData.value = it } // or handle errors using a custom error state
            }

        })

        return moviesLiveData
    }
}


