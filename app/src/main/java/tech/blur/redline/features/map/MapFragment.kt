package tech.blur.redline.features.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.android.synthetic.main.fragment_map.view.*
import tech.blur.redline.R
import tech.blur.redline.features.BaseFragment


class MapFragment : BaseFragment(), MapFragmentView, OnMapReadyCallback {

    lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap

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

        mMapView.getMapAsync(this)



        return rootView
    }

    override fun getLayoutID() = R.layout.fragment_map

    override fun onMapReady(mMap: GoogleMap?) {
        googleMap = mMap!!

        // For showing a move to my location button
        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
        } else
            googleMap.isMyLocationEnabled = true

        // For dropping a marker at a point on the Map
        val nsk = LatLng(55.025331, 82.913682)
        googleMap.addMarker(MarkerOptions().position(nsk).title("Marker Title").snippet("Marker Description"))

        // For zooming automatically to the location of the marker
        val cameraPosition = CameraPosition.Builder().target(nsk).zoom(12f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        val geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyDVsJx-Hyq6w4laps9vUcA1gbq-mWLtH78")
            .build()


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

        val options = PolylineOptions()
        options.color(Color.parseColor("#CC0000FF"))
        options.width(5f)
        options.visible(true)

        options.add(nsk)
        options.add(LatLng(55.031103, 82.921267))

       // googleMap.addPolyline(options)
    }

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap) {

        val mainHandler = Handler(context!!.mainLooper)

        mainHandler.post{
            val decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.encodedPath)
            mMap.addPolyline(PolylineOptions().addAll(decodedPath))
        }



    }


    companion object {
        fun newInstance() = MapFragment()
    }

}