package tech.blur.redline.features.map

import com.arellomobile.mvp.MvpView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult

interface MapFragmentView: MvpView {
    fun addPolyline(result: DirectionsResult, latLng: LatLng)
}
