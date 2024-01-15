package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Movie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class DetailsViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Movies")
    private val moviesLiveData: MutableLiveData<List<Movie>?> = MutableLiveData()
    private val originalMoviesList: MutableList<Movie> = mutableListOf()

    fun likeMovie(movieId: Int, userId: String, title: String) {
        val db = FirebaseFirestore.getInstance()
        val likeCollection = db.collection("Likes")

        likeCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val existingLikes = task.result?.documents

                    if (existingLikes.isNullOrEmpty()) {

                        val likeData = hashMapOf(
                            "userId" to userId,
                            "movieId" to movieId,
                            "title" to title
                        )

                        likeCollection.add(likeData)
                    }
                }
            }
    }

    fun getMovie(movieId: Int, callback: (Movie?) -> Unit) {
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
                Log.d("DetailsViewModel", "Retrieved movies: $moviesList")

                val selectedMovie = moviesList.find { it.Id == movieId }

                callback(selectedMovie)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DetailsViewModel", "Error fetching movies: ${databaseError.message}")
                moviesLiveData.value = null
                callback(null)
            }
        })
    }
}
