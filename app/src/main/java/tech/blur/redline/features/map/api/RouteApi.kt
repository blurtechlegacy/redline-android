package tech.blur.redline.features.map.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import tech.blur.redline.core.model.Route
import tech.blur.redline.core.model.Wrapper

interface RouteApi {
    @GET("routes/")
    fun getRoutes(@Query("city") city:String): Call<Wrapper<ArrayList<Route>>>
}