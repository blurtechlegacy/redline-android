package tech.blur.redline.features.map

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import retrofit2.Retrofit
import javax.inject.Inject

@InjectViewState
class MapPresenter : MvpPresenter<MapFragmentView>() {

    var longitude: Double = 0.0
    var latitude: Double = 0.0

    lateinit var googleApiClient: GoogleApiClient
    lateinit var geoApiContext: GeoApiContext

    @Inject
    lateinit var retrofit: Retrofit

    fun buildRoute() {
        val pointArray: ArrayList<LatLng> = ArrayList()
        pointArray.add(LatLng(latitude, longitude))
        pointArray.add(LatLng(55.023690, 82.923720))
        pointArray.add(LatLng(55.026476, 82.920939))
        getRoute(pointArray, pointArray[0])
    }

    private fun getRoute(pointArray: ArrayList<LatLng>, origin: LatLng) {
        val apiRequest = DirectionsApi.newRequest(geoApiContext)

        apiRequest.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        apiRequest.destination(
            com.google.maps.model.LatLng(
                pointArray[pointArray.indexOf(origin) + 1].latitude,
                pointArray[pointArray.indexOf(origin) + 1].longitude
            )
        )
        apiRequest.mode(TravelMode.WALKING) //set travelling mode

        apiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onFailure(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onResult(result: DirectionsResult?) {
                viewState.addPolyline(result!!, pointArray[pointArray.indexOf(origin) + 1])
                if (pointArray.indexOf(origin) != pointArray.size - 2) getRoute(
                    pointArray,
                    pointArray[pointArray.indexOf(origin) + 1]
                )
            }

        })
    }


}