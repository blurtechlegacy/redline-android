package tech.blur.redline.features.map

import com.arellomobile.mvp.MvpView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.DirectionsResult
import tech.blur.redline.core.model.Route
import tech.blur.redline.core.model.Showplace

interface MapFragmentView: MvpView {
    fun addPolyline(result: DirectionsResult, showplace: Showplace)
    fun setRoutsChip(list: ArrayList<Route>)
    fun showMessage(s: String)
    fun sendRoute(route: Route)
}
