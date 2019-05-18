package tech.blur.redline

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mMapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        mMapView = rootView.findViewById(R.id.mapView)
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

        val options = PolylineOptions()
        options.color(Color.parseColor("#CC0000FF"))
        options.width(5f)
        options.visible(true)

        options.add(LatLng(55.031103, 82.921267))


        googleMap.addPolyline(options)
    }


    companion object {
        public fun newInstance() = MapFragment()
    }

}