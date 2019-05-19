package tech.blur.redline.features.map

import com.arellomobile.mvp.MvpView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import tech.blur.redline.core.model.Route

interface MapFragmentView: MvpView {
    fun addPolyline(result: DirectionsResult, latLng: LatLng)
    fun setRoutsChip(list: ArrayList<Route>)
    fun showMessage(s: String)
}
