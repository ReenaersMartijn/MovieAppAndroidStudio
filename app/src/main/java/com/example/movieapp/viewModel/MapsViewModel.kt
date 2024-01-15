// MapsViewModel.kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.pow
import kotlin.math.sqrt

class MapsViewModel : ViewModel() {

    private val cinemaLocationsLiveData: MutableLiveData<List<LatLng>> = MutableLiveData()
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Cinemas")

    // Make the method public
    fun fetchCinemaLocations() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cinemaLocations = mutableListOf<LatLng>()

                for (snapshot in dataSnapshot.children) {
                    val latitude = snapshot.child("Latitude").getValue(Double::class.java)
                    val longitude = snapshot.child("Longitude").getValue(Double::class.java)

                    latitude?.let { lat ->
                        longitude?.let { lon ->
                            cinemaLocations.add(LatLng(lat, lon))
                        }
                    }
                }

                cinemaLocationsLiveData.value = cinemaLocations
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                cinemaLocationsLiveData.value = emptyList() // or handle differently based on your use case
            }
        })
    }

    // Get live data of cinema locations
    fun getCinemaLocations(): LiveData<List<LatLng>> {
        return cinemaLocationsLiveData
    }

    // Get the closest cinema to a given location
    fun getClosestCinema(userLocation: LatLng): LiveData<LatLng?> {
        val closestCinemaLiveData = MutableLiveData<LatLng?>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var minDistance = Double.MAX_VALUE
                var closestCinema: LatLng? = null

                for (snapshot in dataSnapshot.children) {
                    val latitude = snapshot.child("Latitude").getValue(Double::class.java)
                    val longitude = snapshot.child("Longitude").getValue(Double::class.java)

                    latitude?.let { lat ->
                        longitude?.let { lon ->
                            val cinemaLocation = LatLng(lat, lon)
                            val distance = calculateDistance(userLocation, cinemaLocation)

                            if (distance < minDistance) {
                                minDistance = distance
                                closestCinema = cinemaLocation
                            }
                        }
                    }
                }

                closestCinemaLiveData.value = closestCinema
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                closestCinemaLiveData.value = null // or handle differently based on your use case
            }
        })

        return closestCinemaLiveData
    }

    // Calculate the distance between two LatLng points
    private fun calculateDistance(point1: LatLng, point2: LatLng): Double {
        val latDiff = point1.latitude - point2.latitude
        val lonDiff = point1.longitude - point2.longitude

        return sqrt(latDiff.pow(2) + lonDiff.pow(2))
    }
}
