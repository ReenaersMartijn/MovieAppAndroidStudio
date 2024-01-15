package com.example.movieapp
import MapsViewModel
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.movieapp.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null

    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        mapsViewModel.getCinemaLocations().observe(viewLifecycleOwner) { cinemaLocations ->

            googleMap?.clear()

            // Add markers for cinema locations
            cinemaLocations.forEach { location ->
                googleMap?.addMarker(MarkerOptions().position(location).title("Cinema"))
            }

            // Move camera to the first cinema location (you can adjust this logic)
            cinemaLocations.firstOrNull()?.let {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
            }
        }


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getAndShowLastLocation()
        } else {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = MovieFragmentArgs.fromBundle(requireArguments())
        val userId = args.id


        // Initialize MapsViewModel
        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        // Fetch cinema locations from the ViewModel
        mapsViewModel.fetchCinemaLocations()

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Set up the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.MoviesButton.setOnClickListener {
            val action = MapsFragmentDirections.actionMapsFragmentToMovieFragment(userId)
            Navigation.findNavController(it).navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.FindClosestCinemaButton.setOnClickListener {
            getClosestCinema()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAndShowLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    // Move camera to the user's current location
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    googleMap?.addMarker(MarkerOptions().position(currentLocation).title("You"))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                }
            }
    }
    @SuppressLint("MissingPermission")
    private fun getClosestCinema() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mapsViewModel.getClosestCinema(userLocation).observe(viewLifecycleOwner) { closestCinema ->
                        closestCinema?.let {
                            // Move camera to the closest cinema location
                            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                        }
                    }
                }
            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, try to get the last known location again
                    getAndShowLastLocation()
                } else {
                    // Permission denied, handle accordingly (show a message, disable location-related functionality, etc.)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
