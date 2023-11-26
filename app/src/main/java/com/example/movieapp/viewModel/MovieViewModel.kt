package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Movie
import com.google.firebase.database.*

class MovieViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("movies")
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
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })

        return moviesLiveData
    }
}


