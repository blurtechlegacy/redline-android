package tech.blur.redline.features.signup.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tech.blur.redline.core.model.User
import tech.blur.redline.core.model.Wrapper
import tech.blur.redline.core.model.rUser

interface SignUpApi {

    @GET("tags/")
    fun getPrefs(): Call<Wrapper<ArrayList<String>>>

    @POST("users/reg")
    fun regUser(@Body user: rUser): Call<Wrapper<User>>

}