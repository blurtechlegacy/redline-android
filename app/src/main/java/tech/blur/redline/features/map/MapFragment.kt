package tech.blur.redline.features.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.fragment_map.view.*
import tech.blur.redline.R
import tech.blur.redline.core.model.Route
import tech.blur.redline.core.model.Showplace
import tech.blur.redline.features.BaseFragment
import tech.blur.redline.features.MainFlowFragment
import java.util.*
import kotlin.collections.ArrayList


class MapFragment : BaseFragment(), MapFragmentView, OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerClickListener {

    @InjectPresenter
    lateinit var presenter: MapPresenter

    lateinit var mMapView: MapView
    lateinit var routeChips: ChipGroup
    private lateinit var googleMap: GoogleMap
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    val routes: ArrayList<Route> = ArrayList()
    private var init = false


    private var selectedChip: Int = -1

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var geoApiContext: GeoApiContext

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(getLayoutID(), container, false)

        mMapView = rootView.mapView
        mMapView.onCreate(savedInstanceState)

        mMapView.onResume() // needed to get the map to display immediately

        routeChips = rootView.chip_group

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyDVsJx-Hyq6w4laps9vUcA1gbq-mWLtH78")
            .build()
        presenter.geoApiContext = geoApiContext
        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()



        mMapView.getMapAsync(this)



        return rootView
    }

    private fun getCity() {
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses = gcd.getFromLocation(presenter.latitude, presenter.longitude, 1)
        if (addresses.size > 0) {
            presenter.city = addresses[0].locality
            if (!init) {
                init = true
                presenter.downloadRoutes()
            }
        }
    }

    override fun getLayoutID() = R.layout.fragment_map

    override fun onMapReady(mMap: GoogleMap?) {
        googleMap = mMap!!

        // For showing a move to my location button

        val permList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)

        ActivityCompat.requestPermissions(activity!!, permList, 1)

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "No Location Permissions", Toast.LENGTH_LONG).show()
        } else {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationButtonClickListener(this)
            //googleMap.setOnMyLocationClickListener(this)
            //googleMap.setOnMapClickListener(this)
            //googleMap.setOnMapLongClickListener(this)
            googleMap.setOnMarkerClickListener(this)
            //googleMap.setOnMarkerDragListener(this)
            getCurrentLocation()
        }


//        routeChips.setOnCheckedChangeListener { group, checkedId ->
//            run {
//
//                presenter.buildRoute(0)
//            }
//        }


        // googleMap.addPolyline(options)
    }


    private fun moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        val latLng = LatLng(presenter.latitude, presenter.longitude)

        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).build()

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        googleMap.uiSettings.isZoomControlsEnabled = true


    }

    private fun getCurrentLocation() {
        //googleMap.clear()
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
            presenter.longitude = location.longitude
            presenter.latitude = location.latitude

            getCity()

            //moving the map to location
            moveMap()
        }
    }

    override fun setRoutsChip(list: ArrayList<Route>) {

        list.forEach {
            val chip = Chip(routeChips.context)
            chip.text = it.name
            chip.isClickable = true
            chip.isCheckable = true
            //TODO Color state list for the chip
            routeChips.addView(chip)
        }

        routeChips.setOnCheckedChangeListener { _, checkedId ->
            run {
                if (checkedId > 0) {
                    selectedChip = checkedId
                    presenter.buildRoute(routeChips.findViewById<Chip>(checkedId).text.toString())
                } else {
                    googleMap.clear()
                }
            }
        }

    }

    override fun showMessage(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
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

    override fun addPolyline(results: DirectionsResult, showplace: Showplace) {

        val mainHandler = Handler(context!!.mainLooper)

        mainHandler.post {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            showplace.geo[0],
                            showplace.geo[1]
                        )
                    )
                    .draggable(false)
                    .title(showplace.name)
                    .snippet(showplace.description)
            )



            val decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.encodedPath)
            val p = PolylineOptions()
                .addAll(decodedPath)
                .color(Color.RED)

            googleMap.addPolyline(p)
        }
    }

    override fun sendRoute(route: Route) {
        (parentFragment as MainFlowFragment).route = route
    }

    override fun onMarkerDragStart(marker: Marker) {
        Toast.makeText(context, "onMarkerDragStart", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDrag(marker: Marker) {
        Toast.makeText(context, "onMarkerDrag", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragEnd(marker: Marker) {
        // getting the Co-ordinates
        presenter.latitude = marker.position.latitude
        presenter.longitude = marker.position.longitude

        moveMap()
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        return true
    }

    fun buildCustomRoute() {
        presenter.buildCustomRoute()
    }

    companion object {
        fun newInstance() = MapFragment()
    }

}