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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tech.blur.redline.App
import tech.blur.redline.core.model.Route
import tech.blur.redline.core.model.Showplace
import tech.blur.redline.core.model.Wrapper
import tech.blur.redline.features.map.api.RouteApi
import javax.inject.Inject

@InjectViewState
class MapPresenter : MvpPresenter<MapFragmentView>() {

    var city = ""

    var longitude: Double = 0.0
    var latitude: Double = 0.0

    lateinit var pointArray: ArrayList<Route>

    lateinit var googleApiClient: GoogleApiClient
    lateinit var geoApiContext: GeoApiContext

    @Inject
    lateinit var retrofit: Retrofit

    private val routeApi: RouteApi

    init {
        App.INSTANCE.getAppComponent().inject(this)
        routeApi = retrofit.create(RouteApi::class.java)
    }

    fun downloadRoutes() {
        routeApi.getRoutes(city).enqueue(object : Callback<Wrapper<ArrayList<Route>>> {
            override fun onFailure(call: Call<Wrapper<ArrayList<Route>>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Wrapper<ArrayList<Route>>>,
                response: Response<Wrapper<ArrayList<Route>>>
            ) {
                if (response.body() != null) {
                    pointArray = response.body()!!.data
                    viewState.setRoutsChip(pointArray)
                }
            }

        })
    }

    fun buildRoute(id: Int) {
        getRoute(pointArray[id].geos, LatLng(latitude, longitude))
    }

    fun buildRoute(name: String) {
        val pos = searchRoute(name)
        if (pos != -1)
            getRoute(pointArray[pos].geos, LatLng(latitude, longitude))
        else
            viewState.showMessage("Ошибка маршрута")
    }

    private fun searchRoute(name: String): Int {
        pointArray.forEach {
            if (it.name == name) return pointArray.indexOf(it)
        }

        return -1
    }

    private fun getRoute(pointArray: ArrayList<Showplace>, origin: LatLng) {
        val apiRequest = DirectionsApi.newRequest(geoApiContext)

        apiRequest.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        apiRequest.destination(
            com.google.maps.model.LatLng(
                pointArray[0].geo[0],
                pointArray[0].geo[1]
            )
        )
        apiRequest.mode(TravelMode.WALKING)

        apiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onFailure(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onResult(result: DirectionsResult?) {
                val latLng = LatLng(
                    pointArray[0].geo[0],
                    pointArray[0].geo[1]
                )
                viewState.addPolyline(result!!, latLng)
//                if (pointArray.indexOf(origin) != pointArray.size - 2)
                getRoute(
                    pointArray,
                    pointArray[0]
                )
            }

        })
    }

    private fun getRoute(pointArray: ArrayList<Showplace>, origin: Showplace) {
        val apiRequest = DirectionsApi.newRequest(geoApiContext)

        apiRequest.origin(com.google.maps.model.LatLng(origin.geo[0], origin.geo[1]))
        apiRequest.destination(
            com.google.maps.model.LatLng(
                pointArray[pointArray.indexOf(origin) + 1].geo[0],
                pointArray[pointArray.indexOf(origin) + 1].geo[1]
            )
        )
        apiRequest.mode(TravelMode.WALKING) //set travelling mode

        apiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onFailure(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onResult(result: DirectionsResult?) {
                val latLng = LatLng(
                    pointArray[pointArray.indexOf(origin) + 1].geo[0],
                    pointArray[pointArray.indexOf(origin) + 1].geo[1]
                )
                viewState.addPolyline(result!!, latLng)
                if (pointArray.indexOf(origin) != pointArray.size - 2) getRoute(
                    pointArray,
                    pointArray[pointArray.indexOf(origin) + 1]
                )
            }

        })
    }


}