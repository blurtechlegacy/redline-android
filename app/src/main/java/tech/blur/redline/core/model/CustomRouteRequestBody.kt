package tech.blur.redline.core.model

import com.google.android.gms.maps.model.LatLng

class CustomRouteRequestBody(
    val city: String,
    val location: LatLng,
    val id: String
)