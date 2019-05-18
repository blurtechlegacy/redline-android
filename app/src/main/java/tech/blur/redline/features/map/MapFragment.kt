package tech.blur.redline.features.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import tech.blur.redline.R
import tech.blur.redline.features.BaseFragment


class MapFragment : BaseFragment(), MapFragmentView, OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener {

    lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var geoApiContext: GeoApiContext

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(getLayoutID(), container, false)

        mMapView = rootView.mapView
        mMapView.onCreate(savedInstanceState)

        mMapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyDVsJx-Hyq6w4laps9vUcA1gbq-mWLtH78")
            .build()

        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()

        mMapView.getMapAsync(this)



        return rootView
    }

    override fun getLayoutID() = R.layout.fragment_map

    override fun onMapReady(mMap: GoogleMap?) {
        googleMap = mMap!!

        // For showing a move to my location button

        val permList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(activity!!, permList, 1)

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "No Location Permissions", Toast.LENGTH_LONG).show()
        } else {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationButtonClickListener(this)
            googleMap.setOnMyLocationClickListener(this)
            googleMap.setOnMapClickListener(this)
            googleMap.setOnMapLongClickListener(this)
            googleMap.setOnMarkerClickListener(this)
            googleMap.setOnMarkerDragListener(this)
            getCurrentLocation()
        }

        // For dropping a marker at a point on the Map
        val nsk = LatLng(55.025331, 82.913682)


        val apiRequest = DirectionsApi.newRequest(geoApiContext)
        apiRequest.origin(com.google.maps.model.LatLng(nsk.latitude, nsk.longitude))
        apiRequest.destination(com.google.maps.model.LatLng(55.031103, 82.921267))
        apiRequest.mode(TravelMode.WALKING) //set travelling mode

        apiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onFailure(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onResult(result: DirectionsResult?) {
                addPolyline(result!!, googleMap)
            }

        })

        // googleMap.addPolyline(options)
    }

    private fun moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        val latLng = LatLng(latitude, longitude)

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
        googleMap.uiSettings.isZoomControlsEnabled = true


    }

    private fun getCurrentLocation() {
        googleMap.clear()
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.longitude
            latitude = location.latitude

            //moving the map to location
            moveMap()
        }
    }


    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        getCurrentLocation()
        return false

    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onConnected(bundle: Bundle?) {
        getCurrentLocation()
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onMapLongClick(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).draggable(true))
    }

    override fun onMapClick(p0: LatLng?) {
        googleMap.clear()
    }

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap) {

        val mainHandler = Handler(context!!.mainLooper)

        mainHandler.post {
            val decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.encodedPath)
            mMap.addPolyline(PolylineOptions().addAll(decodedPath))
        }
    }

    override fun onMarkerDragStart(marker: Marker) {
        Toast.makeText(context, "onMarkerDragStart", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDrag(marker: Marker) {
        Toast.makeText(context, "onMarkerDrag", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragEnd(marker: Marker) {
        // getting the Co-ordinates
        latitude = marker.position.latitude
        longitude = marker.position.longitude

        moveMap()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(context, "onMarkerClick", Toast.LENGTH_SHORT).show()
        return true
    }

    companion object {
        fun newInstance() = MapFragment()
    }

}