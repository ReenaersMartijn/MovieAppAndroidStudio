package com.example.movieapp.viewModel

import android.icu.text.CaseMap.Title
import androidx.lifecycle.ViewModel
import com.example.movieapp.models.Liked
import com.example.movieapp.models.Movie
import com.example.movieapp.models.User
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    fun getUserData(userId: String, callback: (User?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("Users").document(userId)

        userDocument.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val email = documentSnapshot.getString("Email")
                    val userId = documentSnapshot.getString("UserId")
                    val user = User(email, userId)

                    callback(user)
                } else {
                    callback(null)
                }
            }

    }

    fun getLikedMovies(userId: String, callback: (List<Liked>?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Likes")
            .whereEqualTo("userId",userId)
            .get()
            .addOnCompleteListener{task ->

                if (task.isSuccessful)
                {
                    val movies = task.result?.mapNotNull {  document ->
                        val data = document.data

                        if (data != null)
                        {
                            Liked(

                                movieId = (data["movieId"] as? Long)?.toInt() ?: 0,
                                userId = data["userId"] as String,
                                title =  data["title"] as String
                            )
                        }
                        else
                        {
                            null
                        }

                    }
                    callback(movies)
                }
                else
                {
                    callback(null)
                }
            }
    }
}